package com.github.zzk.xposed.plugin;

import com.github.zzk.xposed.HookParams;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Author: Ronan.zhuang
 * Email:  newzzack@gmail.com
 * Date:   2018.05.11 16:27
 */
public class ADBlock implements IPlugin {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(HookParams.getInstance().XMLParserClassName, lpparam.classLoader,
                HookParams.getInstance().XMLParserMethod, String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        try {
//                            if (!PreferencesUtils.isADBlock())
//                                return;

                            if (param.args[1].equals("ADInfo"))
                                param.setResult(null);
                        } catch (Error | Exception e) {

                        }
                    }
                });
    }
}
