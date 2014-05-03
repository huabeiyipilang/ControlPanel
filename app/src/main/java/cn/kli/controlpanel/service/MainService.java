package cn.kli.controlpanel.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import cn.kli.controlpanel.R;
import cn.kli.controlpanel.indicator.IndicatorView;
import cn.kli.controlpanel.framework.manager.NetworkManager;
import cn.kli.utils.Conversion;

/**
 * Created by carl on 14-4-17.
 */
public class MainService extends Service {

    public static final int MSG_SHOW_QUICK_PANEL = 1;
    public static final int MSG_UPDATE_NOTIFICATION = 2;

    private NetworkManager mNetworkManager;
    private Notification mNotification = new Notification();
    private IndicatorView mIndicator;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case MSG_SHOW_QUICK_PANEL:
                    mIndicator.show();
                    break;
                case MSG_UPDATE_NOTIFICATION:
                    updateNotification();
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
        if(mIndicator == null){
            mIndicator = IndicatorView.getsInstance(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateNotification();
        mHandler.sendEmptyMessage(MSG_SHOW_QUICK_PANEL);
        return START_STICKY;
    }

    private void initNotification(){
        mNotification = new Notification();
        mNotification.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, new Intent(this, MainService.class), PendingIntent.FLAG_CANCEL_CURRENT);
        mNotification.contentIntent = pendingIntent;
    }

    private void updateNotification(){

        if(mNetworkManager == null){
            mNetworkManager = new NetworkManager();
        }
        mNotification.icon = mNetworkManager.getIcon();
        if(Build.VERSION.SDK_INT >= 11){
            mNotification.largeIcon = Conversion.drawable2Bitmap(getResources().getDrawable(R.drawable.ic_launcher));
        }
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_main);
        contentView.setTextViewText(R.id.tv_title, getString(R.string.app_name));
        mNotification.contentView = contentView;
        startForeground(1001, mNotification);
        mHandler.removeMessages(MSG_UPDATE_NOTIFICATION);
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_NOTIFICATION, getNotificationUpdateDuring());

    }

    private long getNotificationUpdateDuring(){
        return 3000;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIndicator.hide();
        stopForeground(true);
    }

}
