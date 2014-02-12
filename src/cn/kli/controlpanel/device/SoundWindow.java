package cn.kli.controlpanel.device;

import cn.kli.controlpanel.R;
import cn.kli.controlpanel.base.BaseFloatWindow;

public class SoundWindow extends BaseFloatWindow {

    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.window_sound);
        setTitle(getString(R.string.module_sound));
        setType(TYPE_WRAP_CONTENT);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
