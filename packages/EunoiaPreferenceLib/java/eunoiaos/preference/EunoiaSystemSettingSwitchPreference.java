/*
 * Copyright (C) 2015 The CyanogenMod Project
 * Copyright (C) 2018 The LineageOS Project
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

package eunoiaos.preference;

import android.content.Context;
import android.util.AttributeSet;

import eunoiaos.providers.EunoiaSettings;

public class EunoiaSystemSettingSwitchPreference extends SelfRemovingSwitchPreference {

    public EunoiaSystemSettingSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EunoiaSystemSettingSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EunoiaSystemSettingSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected boolean isPersisted() {
        return EunoiaSettings.System.getString(getContext().getContentResolver(), getKey()) != null;
    }

    @Override
    protected void putBoolean(String key, boolean value) {
        EunoiaSettings.System.putInt(getContext().getContentResolver(), key, value ? 1 : 0);
    }

    @Override
    protected boolean getBoolean(String key, boolean defaultValue) {
        return EunoiaSettings.System.getInt(getContext().getContentResolver(),
                key, defaultValue ? 1 : 0) != 0;
    }
}