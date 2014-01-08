package cn.kli.controlpanel.module.quickpanel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;
import cn.kli.controlpanel.BlackActivity;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.base.BaseFloatWindow;
import cn.kli.controlpanel.base.BaseFragment;
import cn.kli.controlpanel.base.FloatWindowManager;
import cn.kli.controlpanel.module.floatpanel.FloatManager;
import cn.kli.controlpanel.modules.FlashLightManager;
import cn.kli.controlpanel.modules.OneKeyLockScreen;
import cn.kli.utils.NetworkManager;

public class MenuItemFactory {
    private Context mContext;
    private AudioManager mAudioManager;
    private NetworkManager mNetworkManager;
    
    public MenuItemFactory(Context context){
        mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mNetworkManager = new NetworkManager(mContext);
    }
    
    //folder Item
    public QuickMenuItem getFolderItem(int icon, String title){
        QuickMenuItem item = new QuickMenuItem(icon, title);
        return item;
    }
    
    //folder Item
    public QuickMenuItem getFolderItem(int icon, int title){
        QuickMenuItem item = new QuickMenuItem(icon, mContext.getString(title));
        return item;
    }
    
    //activity Item
    public QuickMenuItem getActivityItem(int icon, String title, final Class<? extends Activity> cls){
        QuickMenuItem item = new QuickMenuItem(icon, title);
        item.setOnSelectRunnable(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(mContext, cls);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return item;
    }
    
    //fragment Item
    public QuickMenuItem getFragmentItem(int icon, String title, final Class<? extends BaseFragment> cls){
        QuickMenuItem item = new QuickMenuItem(icon, title);
        item.setOnSelectRunnable(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(mContext, BlackActivity.class);
                intent.putExtra("fragment", cls);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return item;
    }

    //float window Item
    public QuickMenuItem getWindowItem(int icon, String title, final Class<? extends BaseFloatWindow> cls){
        QuickMenuItem item = new QuickMenuItem(icon, title);
        item.setOnSelectRunnable(new Runnable(){
            @Override
            public void run() {
                FloatWindowManager.startWindow(mContext, cls);
            }
        });
        return item;
    }
    
    //锁屏
    public QuickMenuItem getLockScreenItem(){
        return getActivityItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.module_lock_screen), OneKeyLockScreen.class);
    }
    
    //音量
    public QuickMenuItem getSoundItem(){
        QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.module_sound));
        item.setOnSelectRunnable(new Runnable() {
            
            @Override
            public void run() {
                FloatManager.getInstance(mContext).showPanel();
            }
        });
        return item;
    }

    //音量 标准模式
    public QuickMenuItem getRingerModeNormalItem(){
        final QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.ringer_mode_normal));
        item.setToggle(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL);
        item.setOnSelectRunnable(new Runnable() {
            
            @Override
            public void run() {
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Toast.makeText(mContext, mContext.getString(R.string.ringer_mode_change)
                        + mContext.getString(R.string.ringer_mode_normal), Toast.LENGTH_SHORT).show();
            }
        });
        item.setUpdateRunnable(new Runnable(){

            @Override
            public void run() {
                item.title = mContext.getString(R.string.ringer_mode_normal);
                item.setToggle(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL);
            }
            
        });
        return item;
    }

    //音量 振动模式
    public QuickMenuItem getRingerModeVibrateItem(){
        final QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.ringer_mode_vibrate));
        item.setToggle(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE);
        item.setOnSelectRunnable(new Runnable() {
            
            @Override
            public void run() {
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                Toast.makeText(mContext, mContext.getString(R.string.ringer_mode_change)
                        + mContext.getString(R.string.ringer_mode_vibrate), Toast.LENGTH_SHORT).show();
            }
        });
        item.setUpdateRunnable(new Runnable(){

            @Override
            public void run() {
                item.title = mContext.getString(R.string.ringer_mode_vibrate);
                item.setToggle(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE);
            }
            
        });
        return item;
    }

    //音量 静音模式
    public QuickMenuItem getRingerModeSilentItem(){
        final QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.ringer_mode_silent));
        item.setToggle(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT);
        item.setOnSelectRunnable(new Runnable() {
            
            @Override
            public void run() {
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Toast.makeText(mContext, mContext.getString(R.string.ringer_mode_change)
                        + mContext.getString(R.string.ringer_mode_silent), Toast.LENGTH_SHORT).show();
            }
        });
        item.setUpdateRunnable(new Runnable(){

            @Override
            public void run() {
                item.title = mContext.getString(R.string.ringer_mode_silent);
                item.setToggle(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT);
            }
            
        });
        return item;
    }
    
    //手电筒
    public QuickMenuItem getFlashLightItem(){
        final QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm,
                mContext.getString(FlashLightManager.getInstance().isOn() ? R.string.flash_light_on : R.string.flash_light_off));
        item.setOnSelectRunnable(new Runnable() {
            
            @Override
            public void run() {
                if(FlashLightManager.getInstance().isOn()){
                    FlashLightManager.getInstance().close();
                }else{
                    FlashLightManager.getInstance().open();
                }
            }
        });
        item.setUpdateRunnable(new Runnable(){

            @Override
            public void run() {
                item.title = mContext.getString(FlashLightManager.getInstance().isOn() ? R.string.flash_light_on : R.string.flash_light_off);
            }
            
        });
        return item;
    }
    
    //移动网络开关
    public QuickMenuItem getMobileToggleItem(){
        final QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.menu_mobile));
        try {
            item.setToggle(mNetworkManager.getMobileDataEnabled());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setOnSelectRunnable(new Runnable() {
            
            @Override
            public void run() {
                try {
                    mNetworkManager.toggleGprs(!mNetworkManager.getMobileDataEnabled());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, R.string.setting_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        item.setUpdateRunnable(new Runnable(){

            @Override
            public void run() {
                try {
                    item.setToggle(mNetworkManager.getMobileDataEnabled());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        });
        return item;
    }
    
    //WIFI开关
    public QuickMenuItem getWifiToggleItem(){
        final QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.menu_wifi));
        item.setToggle(mNetworkManager.getWifiEnabled());
        item.setOnSelectRunnable(new Runnable() {
            
            @Override
            public void run() {
                try {
                    mNetworkManager.toggleWiFi(!mNetworkManager.getWifiEnabled());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, R.string.setting_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        item.setUpdateRunnable(new Runnable(){

            @Override
            public void run() {
                item.setToggle(mNetworkManager.getWifiEnabled());
            }
            
        });
        return item;
    }
}
