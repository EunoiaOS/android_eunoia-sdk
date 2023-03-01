/**
 * Copyright (C) 2015, The CyanogenMod Project
 *               2017-2023 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eunoiaos.app;

import android.annotation.SdkConstant;

/**
 * @hide
 * TODO: We need to somehow make these managers accessible via getSystemService
 */
public final class EunoiaContextConstants {

    /**
     * @hide
     */
    private EunoiaContextConstants() {
        // Empty constructor
    }

    /**
     * Use with {@link android.content.Context#getSystemService} to retrieve a
     * {@link eunoiaos.app.ProfileManager} for informing the user of
     * background events.
     *
     * @see android.content.Context#getSystemService
     * @see eunoiaos.app.ProfileManager
     *
     * @hide
     */
    public static final String EUNOIA_PROFILE_SERVICE = "profile";

    /**
     * Use with {@link android.content.Context#getSystemService} to retrieve a
     * {@link eunoiaos.hardware.EunoiaHardwareManager} to manage the extended
     * hardware features of the device.
     *
     * @see android.content.Context#getSystemService
     * @see eunoiaos.hardware.EunoiaHardwareManager
     *
     * @hide
     */
    public static final String EUNOIA_HARDWARE_SERVICE = "eunoiahardware";

    /**
     * Control device power profile and characteristics.
     *
     * @hide
     */
    public static final String EUNOIA_PERFORMANCE_SERVICE = "eunoiaperformance";

    /**
     * Manages display color adjustments
     *
     * @hide
     */
    public static final String EUNOIA_LIVEDISPLAY_SERVICE = "eunoialivedisplay";

    /**
     * Use with {@link android.content.Context#getSystemService} to retrieve a
     * {@link eunoiaos.trust.TrustInterface} to access the Trust interface.
     *
     * @see android.content.Context#getSystemService
     * @see eunoiaos.trust.TrustInterface
     *
     * @hide
     */
    public static final String EUNOIA_TRUST_INTERFACE = "eunoiatrust";

    /**
     * Use with {@link android.content.Context#getSystemService} to retrieve a
     * {@link eunoiaos.health.HealthInterface} to access the Health interface.
     *
     * @see android.content.Context#getSystemService
     * @see eunoiaos.health.HealthInterface
     *
     * @hide
     */
    public static final String EUNOIA_HEALTH_INTERFACE = "eunoiahealth";

    /**
     * Update power menu (GlobalActions)
     *
     * @hide
     */
    public static final String EUNOIA_GLOBAL_ACTIONS_SERVICE = "eunoiaglobalactions";

    /**
     * Features supported by the Eunoia SDK.
     */
    public static class Features {
        /**
         * Feature for {@link PackageManager#getSystemAvailableFeatures} and
         * {@link PackageManager#hasSystemFeature}: The device includes the hardware abstraction
         * framework service utilized by the eunoiaosdk.
         */
        @SdkConstant(SdkConstant.SdkConstantType.FEATURE)
        public static final String HARDWARE_ABSTRACTION = "com.eunoiaos.hardware";

        /**
         * Feature for {@link PackageManager#getSystemAvailableFeatures} and
         * {@link PackageManager#hasSystemFeature}: The device includes the eunoia profiles service
         * utilized by the eunoiaosdk.
         */
        @SdkConstant(SdkConstant.SdkConstantType.FEATURE)
        public static final String PROFILES = "com.eunoiaos.profiles";

        /**
         * Feature for {@link PackageManager#getSystemAvailableFeatures} and
         * {@link PackageManager#hasSystemFeature}: The device includes the eunoia performance service
         * utilized by the eunoiaosdk.
         */
        @SdkConstant(SdkConstant.SdkConstantType.FEATURE)
        public static final String PERFORMANCE = "com.eunoiaos.performance";

        /**
         * Feature for {@link PackageManager#getSystemAvailableFeatures} and
         * {@link PackageManager#hasSystemFeature}: The device includes the LiveDisplay service
         * utilized by the eunoiaosdk.
         */
        @SdkConstant(SdkConstant.SdkConstantType.FEATURE)
        public static final String LIVEDISPLAY = "com.eunoiaos.livedisplay";

        /**
         * Feature for {@link PackageManager#getSystemAvailableFeatures} and
         * {@link PackageManager#hasSystemFeature}: The device includes the Eunoia audio extensions
         * utilized by the eunoiaosdk.
         */
        @SdkConstant(SdkConstant.SdkConstantType.FEATURE)
        public static final String AUDIO = "com.eunoiaos.audio";

        /**
         * Feature for {@link PackageManager#getSystemAvailableFeatures} and
         * {@link PackageManager#hasSystemFeature}: The device includes the eunoia trust service
         * utilized by the eunoiaosdk.
         */
        @SdkConstant(SdkConstant.SdkConstantType.FEATURE)
        public static final String TRUST = "com.eunoiaos.trust";

        /**
         * Feature for {@link PackageManager#getSystemAvailableFeatures} and
         * {@link PackageManager#hasSystemFeature}: The device includes the eunoiaosettings service
         * utilized by the eunoiaosdk.
         */
        @SdkConstant(SdkConstant.SdkConstantType.FEATURE)
        public static final String SETTINGS = "com.eunoiaos.settings";

        /**
         * Feature for {@link PackageManager#getSystemAvailableFeatures} and
         * {@link PackageManager#hasSystemFeature}: The device includes the eunoia globalactions
         * service utilized by the eunoiaosdk and EunoiaParts.
         */
        @SdkConstant(SdkConstant.SdkConstantType.FEATURE)
        public static final String GLOBAL_ACTIONS = "com.eunoiaos.globalactions";

        /**
         * Feature for {@link PackageManager#getSystemAvailableFeatures} and
         * {@link PackageManager#hasSystemFeature}: The device includes the eunoia health
         * service utilized by the eunoia sdk and EunoiaParts.
         */
        @SdkConstant(SdkConstant.SdkConstantType.FEATURE)
        public static final String HEALTH = "com.eunoiaos.health";
    }
}
