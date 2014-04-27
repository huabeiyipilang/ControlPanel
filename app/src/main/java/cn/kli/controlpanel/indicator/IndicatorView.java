package cn.kli.controlpanel.indicator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import cn.kli.controlpanel.R;
import cn.kli.controlpanel.framework.FloatView;

/**
 * Created by carl on 14-4-19.
 */
public class IndicatorView extends FloatView {

    private static IndicatorView sInstance;

    private IndicatorView(Context context) {
        super(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.view_indicator, null);
        setContentView(contentView);
    }

    public static IndicatorView getsInstance(Context context){
        if(sInstance == null){
            sInstance = new IndicatorView(context);
        }
        return sInstance;
    }
}
