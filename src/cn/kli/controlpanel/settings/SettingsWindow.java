package cn.kli.controlpanel.settings;

import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import cn.kli.controlpanel.Prefs;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.base.BaseFloatWindow;
import cn.kli.controlpanel.module.quickpanel.IndicatorManager;
import cn.kli.controlpanel.module.quickpanel.QuickPanelService;
import cn.kli.controlpanel.settings.SettingItemToggle.OnCheckedChangeListener;

public class SettingsWindow extends BaseFloatWindow implements OnCheckedChangeListener, android.widget.CompoundButton.OnCheckedChangeListener {

    private SettingItemToggle mNetworkSpeed;
    private SettingItemToggle mMenuVibrate;
    private RadioButton mMemoRadio;
    private RadioButton mBatteryRadio;
    private RadioButton mCpuRadio;
    
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

        mMemoRadio = (RadioButton)findViewById(R.id.rb_memory_mode);
        mMemoRadio.setOnCheckedChangeListener(this);
        mBatteryRadio = (RadioButton)findViewById(R.id.rb_battery_mode);
        mBatteryRadio.setOnCheckedChangeListener(this);
        mCpuRadio = (RadioButton)findViewById(R.id.rb_cpu_mode);
        mCpuRadio.setOnCheckedChangeListener(this);
        
        switch(Prefs.getPrefs().getInt(Prefs.SETTING_INDICATOR_MODE, IndicatorManager.MODE_MEMORY)){
        case IndicatorManager.MODE_BATTERY:
            mBatteryRadio.setChecked(true);
            break;
        case IndicatorManager.MODE_MEMORY:
            mMemoRadio.setChecked(true);
            break;
        case IndicatorManager.MODE_CPU:
            mCpuRadio.setChecked(true);
            break;
        }
    }

    @Override
    public void onCheckedChanged(String key, boolean enable) {
        if(Prefs.SETTING_NOTIFI_NETWORK_SPEED.equals(key)){
            getContext().startService(new Intent(getContext(), QuickPanelService.class));
        }
    }
    
    public void init(){
        
    }

    @Override
    public void onCheckedChanged(CompoundButton bt, boolean checked) {
        if(!checked){
            return;
        }
        int mode = IndicatorManager.MODE_MEMORY;
        if(bt == mMemoRadio){
            mode = IndicatorManager.MODE_MEMORY;
        }else if(bt == mBatteryRadio){
            mode = IndicatorManager.MODE_BATTERY;
        }else if(bt == mCpuRadio){
            mode = IndicatorManager.MODE_CPU;
        }
        IndicatorManager.getInstance(getContext()).setMode(mode);
        Prefs.getPrefs().edit().putInt(Prefs.SETTING_INDICATOR_MODE, mode).commit();
    }
}
