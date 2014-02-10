package cn.kli.controlpanel.module.quickpanel;

import android.content.Context;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.about.AboutWindow;
import cn.kli.controlpanel.settings.SettingsWindow;

public class QuickMenuManager {
    private Context mContext;
    
    QuickMenuManager(Context context){
        mContext = context;
    }
    
    QuickMenuItem getMenuFromConfig(){
        MenuItemFactory factory = new MenuItemFactory(mContext);

        // 音量
        QuickMenuItem soundMenu = factory.getSoundItem();

        // 铃声
        QuickMenuItem ringerMenu = factory.getFolderItem(R.drawable.ic_audio_ring_notif, R.string.module_ringer_mode);
        ringerMenu.addChild(factory.getRingerModeNormalItem());
        ringerMenu.addChild(factory.getRingerModeSilentItem());
        ringerMenu.addChild(factory.getRingerModeVibrateItem());

        // 网络
        QuickMenuItem networkMenu = factory.getFolderItem(R.drawable.ic_settings_display, R.string.module_network);
        networkMenu.addChild(factory.getMobileToggleItem());
        networkMenu.addChild(factory.getWifiToggleItem());

        // 锁屏
        QuickMenuItem lockMenu = factory.getLockScreenItem();

        QuickMenuItem aboutMenu = factory.getWindowItem(R.drawable.ic_audio_alarm, getString(R.string.module_about), AboutWindow.class);
        QuickMenuItem settingsMenu = factory.getWindowItem(R.drawable.ic_audio_alarm, getString(R.string.group_settings), SettingsWindow.class);
        settingsMenu.addChild(aboutMenu);

        //root menu
        QuickMenuItem root = new QuickMenuItem();
        root.addChild(ringerMenu);
        root.addChild(networkMenu);
        root.addChild(soundMenu);
        root.addChild(lockMenu);
        root.addChild(settingsMenu);
        root.level = -1;
        return root;

    }

    private String getString(int res){
        return mContext.getString(res);
    }
}
