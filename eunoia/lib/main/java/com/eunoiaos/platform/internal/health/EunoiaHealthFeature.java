/*
 * Copyright (C) 2023 The LineageOS Project
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

package com.eunoiaos.platform.internal.health;

import android.content.Context;
import android.os.Handler;

import com.eunoiaos.platform.internal.EunoiaBaseFeature;

public abstract class EunoiaHealthFeature extends EunoiaBaseFeature {
    protected static final String TAG = "EunoiaHealth";

    public EunoiaHealthFeature(Context context, Handler handler) {
        super(context, handler);
    }

    public abstract boolean isSupported();
}