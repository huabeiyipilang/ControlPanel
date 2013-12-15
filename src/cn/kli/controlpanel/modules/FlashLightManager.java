package cn.kli.controlpanel.modules;

import cn.kli.utils.klilog;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class FlashLightManager {
    private Camera mCamera;
    private static FlashLightManager sInstance;

    public static FlashLightManager getInstance() {
        if (sInstance == null) {
            sInstance = new FlashLightManager();
        }
        return sInstance;
    }

    public void open() {
        mCamera = Camera.open();
        Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
//        mCamera.startPreview();
        mCamera.setParameters(parameter);
        klilog.info("open");
    }

    public void close() {
        klilog.info("close");
        if (mCamera != null){
            Parameters parameter = mCamera.getParameters();
            parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameter);
            mCamera.release();
            mCamera = null;
        }
    }

    public boolean isOn() {
        boolean res = mCamera != null;
        klilog.info("res = "+res);
        return res;
    }
}
