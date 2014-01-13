package cn.kli.controlpanel.about;

import android.webkit.WebView;
import cn.kli.controlpanel.base.BaseFloatWindow;

public class AboutWindow extends BaseFloatWindow {

    @Override
    protected void onCreate() {
        super.onCreate();
        WebView view = new WebView(getContext());
        view.loadUrl("file:///android_asset/html/about.html");
        setContentView(view);
    }


}
