package com.github.zzk.xposed.plugin;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Author: Ronan.zhuang
 * Email:  newzzack@gmail.com
 * Date:   2018.05.10 16:18
 */
public interface IPlugin {
    void hook(XC_LoadPackage.LoadPackageParam lpparam);
}
