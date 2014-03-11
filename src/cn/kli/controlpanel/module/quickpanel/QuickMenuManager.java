package cn.kli.controlpanel.module.quickpanel;

import android.content.Context;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.about.AboutWindow;
import cn.kli.controlpanel.device.ControlPanelWindow;
import cn.kli.controlpanel.device.SoundWindow;
import cn.kli.controlpanel.settings.SettingsWindow;

public class QuickMenuManager {
    private Context mContext;
    
    QuickMenuManager(Context context){
        mContext = context;
    }
    
    QuickMenuItem getMenuFromConfig(){
        MenuItemFactory factory = new MenuItemFactory(mContext);

        // 锁屏
        QuickMenuItem lockMenu = factory.getLockScreenItem();
        QuickMenuItem controlPanelMenu = factory.getWindowItem(R.drawable.ic_audio_alarm, getString(R.string.setting), ControlPanelWindow.class);

        QuickMenuItem aboutMenu = factory.getWindowItem(R.drawable.ic_audio_alarm, getString(R.string.module_about), AboutWindow.class);
        QuickMenuItem preferenceMenu = factory.getWindowItem(R.drawable.ic_audio_alarm, getString(R.string.setting_preference), SettingsWindow.class);
        QuickMenuItem helpMenu = factory.getFolderItem(R.drawable.ic_audio_alarm, getString(R.string.help));
        helpMenu.addChild(preferenceMenu);
        helpMenu.addChild(aboutMenu);

        //root menu
        QuickMenuItem root = new QuickMenuItem();
        root.addChild(controlPanelMenu);
        root.addChild(lockMenu);
        root.addChild(helpMenu);
        root.level = -1;
        return root;

    }

    private String getString(int res){
        return mContext.getString(res);
    }
}
