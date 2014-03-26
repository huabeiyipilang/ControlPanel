package cn.kli.controlpanel.device;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.Statistic;
import cn.kli.controlpanel.base.BaseFloatWindow;
import cn.kli.controlpanel.devicesmanager.KliBatteryManager;
import cn.kli.utils.DeviceUtils;
import cn.kli.utils.NetworkManager;
import cn.kli.utils.view.RoundProgressBar;

public class ControlPanelWindow extends BaseFloatWindow {

    //utils
    private AudioManager mAudioManager;
    private NetworkManager mNetworkManager;
    private BluetoothAdapter mBluetoothManager;
    
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
    private CheckBox mScreenAutoBrightness;
    private View mBrightnessWidgets;
    private RoundProgressBar mBatteryMonitorView;
    private RoundProgressBar mMemoryMonitorView;

    private Handler mFreshHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
            case 1:
                freshView();
                mFreshHandler.sendEmptyMessageDelayed(1, 3000);
                break;
            }
        }
        
    };
    
    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.window_control_panel);
        setTitle(getString(R.string.setting));
        setType(TYPE_WRAP_CONTENT);
        
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mNetworkManager = new NetworkManager(getContext());
        mBluetoothManager = BluetoothAdapter.getDefaultAdapter();
        
        initViews();
        
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFreshHandler.removeMessages(1);
        mFreshHandler.sendEmptyMessage(1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFreshHandler.removeMessages(1);
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
        mScreenAutoBrightness = (CheckBox)findViewById(R.id.cb_auto_brightness);
        mBrightnessWidgets = findViewById(R.id.cw_screen_brightness);
        mBatteryMonitorView = (RoundProgressBar) findViewById(R.id.rpb_battery);
        mMemoryMonitorView = (RoundProgressBar) findViewById(R.id.rpb_memory);
        
        mMemoryMonitorView.setMax((int) DeviceUtils.getTotalMemory());
        mBatteryMonitorView.setMax(100);
        
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
                Statistic.onEvent("contril panel window", "ringer setting");
            }
            
        });
        
        mBluetooth.setChecked(mBluetoothManager.isEnabled());
        mBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mBluetoothManager.enable();
                }else{
                    mBluetoothManager.disable();
                }
                Statistic.onEvent("contril panel window", "bluetooth setting");
            }
        });
        
        mWifi.setChecked(mNetworkManager.getWifiEnabled());
        mWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNetworkManager.toggleWiFi(isChecked);
                Statistic.onEvent("contril panel window", "wifi setting");
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
                Statistic.onEvent("contril panel window", "mobile network setting");
            }
        });
        
        mAirPlane.setChecked(mNetworkManager.isAirplaneModeOn());
        mAirPlane.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNetworkManager.toggleAirplaneMode(isChecked);
                Statistic.onEvent("contril panel window", "airplane setting");
            }
        });

        int rotation = Settings.System.getInt(getContext().getContentResolver(),Settings.System.ACCELEROMETER_ROTATION,0);
        mDisplayRotation.setChecked(rotation == 1);
        mDisplayRotation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.System.putInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 
                        isChecked ? 1 : 0);
                Statistic.onEvent("contril panel window", "display rotation setting");
            }
        });

        int autoBrightness = Settings.System.getInt(getContext().getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE,0);
        mScreenAutoBrightness.setChecked(autoBrightness == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        mScreenAutoBrightness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 
                        isChecked ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                                : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                if(isChecked){
                    Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
                    anim.setAnimationListener(new AnimationListener() {
                        
                        @Override
                        public void onAnimationStart(Animation animation) {
                            
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mBrightnessWidgets.setVisibility(View.GONE);
                        }
                    });
                    mBrightnessWidgets.startAnimation(anim);
                }else{
                    mBrightnessWidgets.setVisibility(View.VISIBLE);
                    mBrightnessWidgets.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left));
                }

                Statistic.onEvent("contril panel window", "auto screen brightness setting");
            }
        });
        
        mBrightnessWidgets.setVisibility(autoBrightness == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                ? View.GONE : View.VISIBLE);
    }
    
    private void updateSilentAll(){
        mSilentAll.setChecked(mAudioManager.getMode() == AudioManager.RINGER_MODE_SILENT
                && mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0);
    }
    
    private void freshView(){
        int battery = (int) (KliBatteryManager.getInstance(getContext()).getValue() * 100f);
        mBatteryMonitorView.setProgress(battery, true);
        
        mMemoryMonitorView.setProgress((int) DeviceUtils.getAvailMemory(getContext()), true);
    }
}
