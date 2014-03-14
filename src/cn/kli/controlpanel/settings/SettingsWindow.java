package cn.kli.controlpanel.settings;

import android.content.Intent;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.base.BaseFloatWindow;
import cn.kli.controlpanel.module.quickpanel.QuickPanelService;
import cn.kli.controlpanel.settings.SettingItemToggle.OnCheckedChangeListener;

public class SettingsWindow extends BaseFloatWindow implements OnCheckedChangeListener {

    private SettingItemToggle mNetworkSpeed;
    private SettingItemToggle mMenuVibrate;
    
    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.settings_main);
        setTitle(getString(R.string.setting));
        initViews();
    }

    private void initViews(){
        mNetworkSpeed = (SettingItemToggle)findViewById(R.id.sit_notif_network_speed);
        mNetworkSpeed.setKey(Prefs.SETTING_NOTIFI_NETWORK_SPEED);
        mNetworkSpeed.setOnChangeListener(this);
        
        mMenuVibrate = (SettingItemToggle)findViewById(R.id.sit_menu_vibrate);
        mMenuVibrate.setKey(Prefs.SETTING_MENU_VIBRATE);
    }

    @Override
    public void onCheckedChanged(String key, boolean enable) {
        if(Prefs.SETTING_NOTIFI_NETWORK_SPEED.equals(key)){
            getContext().startService(new Intent(getContext(), QuickPanelService.class));
        }
    }
    
    public void init(){
        
    }
}
