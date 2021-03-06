/*
 * Copyright 2015 Takagi Katsuyuki
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

package jp.tkgktyk.xposed.forcetouchdetector;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by tkgktyk on 2015/06/03.
 */
public class Mod implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    private XSharedPreferences mPrefs;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        mPrefs = new XSharedPreferences(FTD.PACKAGE_NAME);
        mPrefs.makeWorldReadable();

        FTD.Settings settings = new FTD.Settings(null, mPrefs);
        ModInternal.initZygote(mPrefs);
        if (settings.forceTouchScreenEnable) {
            ModForceTouchScreen.initZygote(mPrefs);
        } else {
            ModForceTouch.initZygote(mPrefs);
            ModLongPress.initZygote(mPrefs);
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("android") &&
                loadPackageParam.processName.equals("android")) {
            ModInternal.handleLoadPackage(loadPackageParam.classLoader);
        }
    }
}
