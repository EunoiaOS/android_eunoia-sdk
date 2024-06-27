/*
 * Copyright (C) 2015-2016 The CyanogenMod Project
 *               2017-2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eunoiaos.platform.internal;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.UserHandle;
import android.util.Log;

import com.android.server.display.color.DisplayTransformManager;
import com.android.server.LocalServices;

import eunoiaos.app.EunoiaContextConstants;
import eunoiaos.hardware.IEunoiaHardwareService;
import eunoiaos.hardware.EunoiaHardwareManager;

import static com.android.server.display.color.DisplayTransformManager.LEVEL_COLOR_MATRIX_NIGHT_DISPLAY;
import static com.android.server.display.color.DisplayTransformManager.LEVEL_COLOR_MATRIX_GRAYSCALE;

/** @hide */
public class EunoiaHardwareService extends EunoiaSystemService {

    private static final boolean DEBUG = true;
    private static final String TAG = EunoiaHardwareService.class.getSimpleName();

    private final Context mContext;
    private final EunoiaHardwareInterface mEunoiaHwImpl;

    private interface EunoiaHardwareInterface {
        public int getSupportedFeatures();
        public boolean get(int feature);
        public boolean set(int feature, boolean enable);

        public int[] getDisplayColorCalibration();
        public boolean setDisplayColorCalibration(int[] rgb);
    }

    private class LegacyEunoiaHardware implements EunoiaHardwareInterface {

        private final int MIN = 0;
        private final int MAX = 255;

        /**
         * Matrix and offset used for converting color to grayscale.
         * Copied from com.android.server.accessibility.DisplayAdjustmentUtils.MATRIX_GRAYSCALE
         */
        private final float[] MATRIX_GRAYSCALE = {
            .2126f, .2126f, .2126f, 0,
            .7152f, .7152f, .7152f, 0,
            .0722f, .0722f, .0722f, 0,
                 0,      0,      0, 1
        };

        /** Full color matrix and offset */
        private final float[] MATRIX_NORMAL = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        };

        private final int LEVEL_COLOR_MATRIX_CALIB = LEVEL_COLOR_MATRIX_NIGHT_DISPLAY + 1;
        private final int LEVEL_COLOR_MATRIX_READING = LEVEL_COLOR_MATRIX_GRAYSCALE + 1;

        private boolean mAcceleratedTransform;
        private DisplayTransformManager mDTMService;

        private int[] mCurColors = { MAX, MAX, MAX };
        private boolean mReadingEnhancementEnabled;

        private int mSupportedFeatures = 0;

        public LegacyEunoiaHardware() {
            mAcceleratedTransform = mContext.getResources().getBoolean(
                    com.android.internal.R.bool.config_setColorTransformAccelerated);
            if (mAcceleratedTransform) {
                mDTMService = LocalServices.getService(DisplayTransformManager.class);
                mSupportedFeatures |= EunoiaHardwareManager.FEATURE_DISPLAY_COLOR_CALIBRATION;
                mSupportedFeatures |= EunoiaHardwareManager.FEATURE_READING_ENHANCEMENT;
            }
        }

        public int getSupportedFeatures() {
            return mSupportedFeatures;
        }

        public boolean get(int feature) {
            switch(feature) {
                case EunoiaHardwareManager.FEATURE_READING_ENHANCEMENT:
                    if (mAcceleratedTransform)
                        return mReadingEnhancementEnabled;
                default:
                    Log.e(TAG, "feature " + feature + " is not a boolean feature");
                    return false;
            }
        }

        public boolean set(int feature, boolean enable) {
            switch(feature) {
                case EunoiaHardwareManager.FEATURE_READING_ENHANCEMENT:
                    if (mAcceleratedTransform) {
                        mReadingEnhancementEnabled = enable;
                        mDTMService.setColorMatrix(LEVEL_COLOR_MATRIX_READING,
                                enable ? MATRIX_GRAYSCALE : MATRIX_NORMAL);
                        return true;
                    }
                default:
                    Log.e(TAG, "feature " + feature + " is not a boolean feature");
                    return false;
            }
        }

        private float[] rgbToMatrix(int[] rgb) {
            float[] mat = new float[16];

            for (int i = 0; i < 3; i++) {
                // Sanity check
                if (rgb[i] > MAX)
                    rgb[i] = MAX;
                else if (rgb[i] < MIN)
                    rgb[i] = MIN;

                mat[i * 5] = (float)rgb[i] / (float)MAX;
            }

            mat[15] = 1.0f;
            return mat;
        }

        public int[] getDisplayColorCalibration() {
            int[] rgb = mAcceleratedTransform ? mCurColors : null;
            if (rgb == null || rgb.length != 3) {
                Log.e(TAG, "Invalid color calibration string");
                return null;
            }
            int[] currentCalibration = new int[5];
            currentCalibration[EunoiaHardwareManager.COLOR_CALIBRATION_RED_INDEX] = rgb[0];
            currentCalibration[EunoiaHardwareManager.COLOR_CALIBRATION_GREEN_INDEX] = rgb[1];
            currentCalibration[EunoiaHardwareManager.COLOR_CALIBRATION_BLUE_INDEX] = rgb[2];
            currentCalibration[EunoiaHardwareManager.COLOR_CALIBRATION_MIN_INDEX] = MIN;
            currentCalibration[EunoiaHardwareManager.COLOR_CALIBRATION_MAX_INDEX] = MAX;
            return currentCalibration;
        }

        public boolean setDisplayColorCalibration(int[] rgb) {
            if (mAcceleratedTransform) {
                mCurColors = rgb;
                mDTMService.setColorMatrix(LEVEL_COLOR_MATRIX_CALIB, rgbToMatrix(rgb));
                return true;
            }
            return false;
        }

    }

    private EunoiaHardwareInterface getImpl(Context context) {
        return new LegacyEunoiaHardware();
    }

    public EunoiaHardwareService(Context context) {
        super(context);
        mContext = context;
        mEunoiaHwImpl = getImpl(context);
    }

    @Override
    public String getFeatureDeclaration() {
        return EunoiaContextConstants.Features.HARDWARE_ABSTRACTION;
    }

    @Override
    public void onBootPhase(int phase) {
        if (phase == PHASE_BOOT_COMPLETED) {
            Intent intent = new Intent(eunoiaos.content.Intent.ACTION_INITIALIZE_EUNOIA_HARDWARE);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            mContext.sendBroadcastAsUser(intent, UserHandle.ALL,
                    eunoiaos.platform.Manifest.permission.HARDWARE_ABSTRACTION_ACCESS);
        }
    }

    @Override
    public void onStart() {
        publishBinderService(EunoiaContextConstants.EUNOIA_HARDWARE_SERVICE, mService);
    }

    private final IBinder mService = new IEunoiaHardwareService.Stub() {

        private boolean isSupported(int feature) {
            return (getSupportedFeatures() & feature) == feature;
        }

        @Override
        public int getSupportedFeatures() {
            mContext.enforceCallingOrSelfPermission(
                    eunoiaos.platform.Manifest.permission.HARDWARE_ABSTRACTION_ACCESS, null);
            return mEunoiaHwImpl.getSupportedFeatures();
        }

        @Override
        public boolean get(int feature) {
            mContext.enforceCallingOrSelfPermission(
                    eunoiaos.platform.Manifest.permission.HARDWARE_ABSTRACTION_ACCESS, null);
            if (!isSupported(feature)) {
                Log.e(TAG, "feature " + feature + " is not supported");
                return false;
            }
            return mEunoiaHwImpl.get(feature);
        }

        @Override
        public boolean set(int feature, boolean enable) {
            mContext.enforceCallingOrSelfPermission(
                    eunoiaos.platform.Manifest.permission.HARDWARE_ABSTRACTION_ACCESS, null);
            if (!isSupported(feature)) {
                Log.e(TAG, "feature " + feature + " is not supported");
                return false;
            }
            return mEunoiaHwImpl.set(feature, enable);
        }

        @Override
        public int[] getDisplayColorCalibration() {
            mContext.enforceCallingOrSelfPermission(
                    eunoiaos.platform.Manifest.permission.HARDWARE_ABSTRACTION_ACCESS, null);
            if (!isSupported(EunoiaHardwareManager.FEATURE_DISPLAY_COLOR_CALIBRATION)) {
                Log.e(TAG, "Display color calibration is not supported");
                return null;
            }
            return mEunoiaHwImpl.getDisplayColorCalibration();
        }

        @Override
        public boolean setDisplayColorCalibration(int[] rgb) {
            mContext.enforceCallingOrSelfPermission(
                    eunoiaos.platform.Manifest.permission.HARDWARE_ABSTRACTION_ACCESS, null);
            if (!isSupported(EunoiaHardwareManager.FEATURE_DISPLAY_COLOR_CALIBRATION)) {
                Log.e(TAG, "Display color calibration is not supported");
                return false;
            }
            if (rgb.length < 3) {
                Log.e(TAG, "Invalid color calibration");
                return false;
            }
            return mEunoiaHwImpl.setDisplayColorCalibration(rgb);
        }
    };

}
