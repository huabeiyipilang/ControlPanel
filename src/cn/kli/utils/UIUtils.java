package cn.kli.utils;

import android.content.Context;
import android.content.Intent;

public class UIUtils {
    
	public static void addShortcut(Context context, Intent intent, int name, int icon) {
		 
        String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        // ��ݷ�ʽҪ�����İ�

        // ���ÿ�ݷ�ʽ�Ĳ���
        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        // ��������
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
                        .getString(name)); // �������� Intent
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // ����ͼ��
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(context,
                        		icon));
        // ֻ����һ�ο�ݷ�ʽ
        shortcutIntent.putExtra("duplicate", false);
        // ����
        context.sendBroadcast(shortcutIntent);

	}
}
