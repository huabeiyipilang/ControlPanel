package cn.kli.controlpanel;

import cn.kli.controlpanel.framework.BaseFragment;

/**
 * Created by carl on 14-4-2.
 */
public class Navigation {
    public String title;
    public Class<? extends BaseFragment> fragment;

    public Navigation(String t, Class<? extends BaseFragment> f){
        title = t;
        fragment = f;
    }
}
