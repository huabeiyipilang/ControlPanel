package cn.kli.controlpanel.device;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.base.BaseFloatWindow;
import cn.kli.utils.NetworkManager;

public class ControlPanelWindow extends BaseFloatWindow {

    //utils
    private AudioManager mAudioManager;
    private NetworkManager mNetworkManager;
    
    //views
    private RadioGroup mRingerGroup;
    private RadioButton mNormalRinger;
    private RadioButton mSilentRinger;
    private RadioButton mVibrateRinger;
    private CheckBox mSilentAll;
    private CheckBox mBluetooth;
    private CheckBox mWifi;
    private CheckBox mMobile;
    private CheckBox mAirPlane;
    private CheckBox mDisplayRotation;

    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.window_control_panel);
        setTitle(getString(R.string.setting));
        setType(TYPE_WRAP_CONTENT);
        
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mNetworkManager = new NetworkManager(getContext());
        
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initViews(){
        mRingerGroup = (RadioGroup)findViewById(R.id.rg_ringer_group);
        mNormalRinger = (RadioButton)findViewById(R.id.rb_ringer_normal);
        mSilentRinger = (RadioButton)findViewById(R.id.rb_ringer_silent);
        mVibrateRinger = (RadioButton)findViewById(R.id.rb_ringer_vibrate);
        mSilentAll = (CheckBox)findViewById(R.id.cb_silent_all);
        mBluetooth = (CheckBox)findViewById(R.id.cb_bluetooth);
        mWifi = (CheckBox)findViewById(R.id.cb_wifi);
        mMobile = (CheckBox)findViewById(R.id.cb_mobile);
        mAirPlane = (CheckBox)findViewById(R.id.cb_airplane);
        mDisplayRotation = (CheckBox)findViewById(R.id.cb_display_rotation);
        
        switch(mAudioManager.getRingerMode()){
        case AudioManager.RINGER_MODE_NORMAL:
            mNormalRinger.setChecked(true);
            break;
        case AudioManager.RINGER_MODE_SILENT:
            mSilentRinger.setChecked(true);
            break;
        case AudioManager.RINGER_MODE_VIBRATE:
            mVibrateRinger.setChecked(true);
            break;
        }
        mRingerGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup arg0, int checkedId) {
                if(checkedId == -1){
                    return;
                }
                int mode = AudioManager.RINGER_MODE_NORMAL;
                switch(checkedId){
                case R.id.rb_ringer_silent:
                    mode = AudioManager.RINGER_MODE_SILENT;
                    break;
                case R.id.rb_ringer_vibrate:
                    mode = AudioManager.RINGER_MODE_VIBRATE;
                    break;
                }
                mAudioManager.setRingerMode(mode);
            }
            
        });
        
        mBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                
            }
        });
        
        mWifi.setChecked(mNetworkManager.getWifiEnabled());
        mWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNetworkManager.toggleWiFi(isChecked);
            }
        });
        
        try {
            mMobile.setChecked(mNetworkManager.getMobileDataEnabled());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    mNetworkManager.toggleGprs(isChecked);
                } catch (Exception e) {
                    e.printStackTrace();
                    mMobile.toggle();
                }
            }
        });
        
        mAirPlane.setChecked(mNetworkManager.isAirplaneModeOn());
        mAirPlane.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNetworkManager.toggleAirplaneMode(isChecked);
            }
        });

        int flag = Settings.System.getInt(getContext().getContentResolver(),Settings.System.ACCELEROMETER_ROTATION,0);
        mDisplayRotation.setChecked(flag == 1);
        mDisplayRotation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.System.putInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 
                        isChecked ? 1 : 0);
            }
        });
    }
    
    private void updateSilentAll(){
        mSilentAll.setChecked(mAudioManager.getMode() == AudioManager.RINGER_MODE_SILENT
                && mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0);
    }
}
