package cn.kli.utils;

import android.content.Context;
import android.content.Intent;

public class UIUtils {
    
	public static void addShortcut(Context context, Intent intent, int name, int icon) {
		 
        String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        // 快捷方式要启动的包

        // 设置快捷方式的参数
        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        // 设置名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
                        .getString(name)); // 设置启动 Intent
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 设置图标
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(context,
                        		icon));
        // 只创建一次快捷方式
        shortcutIntent.putExtra("duplicate", false);
        // 创建
        context.sendBroadcast(shortcutIntent);

	}
}
