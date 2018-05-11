package com.github.zzk.xposed;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.github.zzk.xposed.plugin.ADBlock;
import com.github.zzk.xposed.plugin.AntiRevoke;
import com.github.zzk.xposed.plugin.IPlugin;
import com.github.zzk.xposed.plugin.LuckMoney;
import com.github.zzk.xposed.utils.SearchClasses;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * Author: Ronan.zhuang
 * Email:  newzzack@gmail.com
 * Date:   2018.05.10 15:49
 */
public class Main implements IXposedHookLoadPackage {

    private static IPlugin[] plugins = {
            new AntiRevoke(),
            new ADBlock(),
            new LuckMoney()
    };

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(HookParams.WECHAT_PACKAGE_NAME)) {
            try {
                XposedHelpers.findAndHookMethod(ContextWrapper.class, "attachBaseContext", Context.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Context context = (Context) param.args[0];
                        String processName = lpparam.processName;
                        if (!processName.equals(HookParams.WECHAT_PACKAGE_NAME) &&
                                !processName.equals(HookParams.WECHAT_PACKAGE_NAME + ":tools")) {
                            return;
                        }
                        String versionName = getVersionName(context, HookParams.WECHAT_PACKAGE_NAME);
                        XposedBridge.log("found wechat version" + versionName);
                        if (!HookParams.hasInstance()) {
                            SearchClasses.init(context, lpparam, versionName);
                            loadPlugin(lpparam);
                        }
                    }
                });
            } catch (Error | Exception e) {
                XposedBridge.log(e);
            }
        }
    }

    private String getVersionName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            XposedBridge.log(e.getMessage());
        }
        return "";
    }

    private void loadPlugin(XC_LoadPackage.LoadPackageParam lpparam) {
        for (IPlugin plugin : plugins) {
            try {
                plugin.hook(lpparam);
            } catch (Error | Exception e) {
                XposedBridge.log(e);
            }
        }
    }
}
