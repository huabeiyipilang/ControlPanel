package cn.kli.controlpanel.indicator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import cn.kli.controlpanel.R;
import cn.kli.controlpanel.controlwindow.ControlPanelWindow;
import cn.kli.controlpanel.framework.FloatView;
import cn.kli.controlpanel.framework.manager.DeviceInfoManager;
import cn.kli.controlpanel.utils.FloatWindowManager;
import cn.kli.controlpanel.utils.VibrateUtils;

/**
 * Created by carl on 14-4-19.
 */
public class IndicatorView extends FloatView {

    private static IndicatorView sInstance;
    private DeviceInfoManager mDeviceInfo;
    private FloatWindowManager mWindowManager;

    private IndicatorView(Context context) {
        super(context);
        mDeviceInfo = DeviceInfoManager.getInstance(context);
        mWindowManager = FloatWindowManager.getInstance(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.view_indicator, null);
        setContentView(contentView);
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                VibrateUtils.getInstance(getContext()).vibrateShortly();
                IndicatorView.this.hide();
                return true;
            }
        });
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindowManager.openWindow(ControlPanelWindow.class);
            }
        });

    }

    public static IndicatorView getsInstance(Context context){
        if(sInstance == null){
            sInstance = new IndicatorView(context);
        }
        return sInstance;
    }

    @Override
    public void setLocation(float x, float y) {
        x = x > 0 ? mDeviceInfo.getScreenWidth() / 2 : - mDeviceInfo.getScreenWidth() / 2;
        super.setLocation(x, y);
    }


}
