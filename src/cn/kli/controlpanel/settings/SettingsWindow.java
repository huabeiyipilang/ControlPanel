package cn.kli.controlpanel.settings;

import android.content.Intent;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.base.BaseFloatWindow;
import cn.kli.controlpanel.module.quickpanel.QuickPanelService;
import cn.kli.controlpanel.settings.SettingItemToggle.OnCheckedChangeListener;

public class SettingsWindow extends BaseFloatWindow implements OnCheckedChangeListener {
    public static final String SETTING_NOTIFI_NETWORK_SPEED = "setting_notifi_network_speed";
    public static final String SETTING_MENU_VIBRATE = "setting_menu_vibrate";

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
        mNetworkSpeed.setKey(SETTING_NOTIFI_NETWORK_SPEED);
        mNetworkSpeed.setOnChangeListener(this);
        
        mMenuVibrate = (SettingItemToggle)findViewById(R.id.sit_menu_vibrate);
        mMenuVibrate.setKey(SETTING_MENU_VIBRATE);
    }

    @Override
    public void onCheckedChanged(String key, boolean enable) {
        if(SETTING_NOTIFI_NETWORK_SPEED.equals(key)){
            getContext().startService(new Intent(getContext(), QuickPanelService.class));
        }
    }
}
