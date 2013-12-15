package cn.kli.controlpanel.module.quickpanel;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;
import cn.kli.controlpanel.R;
import cn.kli.controlpanel.module.floatpanel.FloatManager;
import cn.kli.controlpanel.modules.FlashLightManager;
import cn.kli.controlpanel.modules.OneKeyLockScreen;

public class MenuItemFactory {
    private Context mContext;
    AudioManager mAudioManager;
    
    public MenuItemFactory(Context context){
        mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }
    
    //锁屏
    public QuickMenuItem getLockScreenItem(){
        QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.module_lock_screen));        
        item.setOnSelectRunnable(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(mContext, OneKeyLockScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return item;
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
        if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
            item.title += mContext.getString(R.string.current);
        }
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
                if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
                    item.title += mContext.getString(R.string.current);
                }
            }
            
        });
        return item;
    }

    //音量 振动模式
    public QuickMenuItem getRingerModeVibrateItem(){
        final QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.ringer_mode_vibrate));
        if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
            item.title += mContext.getString(R.string.current);
        }
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
                if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
                    item.title += mContext.getString(R.string.current);
                }
            }
            
        });
        return item;
    }

    //音量 静音模式
    public QuickMenuItem getRingerModeSilentItem(){
        final QuickMenuItem item = new QuickMenuItem(R.drawable.ic_audio_alarm, mContext.getString(R.string.ringer_mode_silent));
        if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
            item.title += mContext.getString(R.string.current);
        }
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
                if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
                    item.title += mContext.getString(R.string.current);
                }
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
}
