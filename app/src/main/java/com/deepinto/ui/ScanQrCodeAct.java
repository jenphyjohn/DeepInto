package com.deepinto.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.deepinto.R;
import com.deepinto.scan.camera.CameraManager;
import com.deepinto.scan.decode.MainHandler;
import com.deepinto.scan.utils.BeepManager;
import com.mincat.sample.manager.BasePostFragmentAct;
import com.mincat.sample.utils.HiddenStatusBar;
import com.mincat.sample.utils.StatusBarUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Ming
 * @desc 二维码扫描
 */
public class ScanQrCodeAct extends BasePostFragmentAct implements SurfaceHolder.Callback {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 26;

    public static final String EXTRA_STRING = "extra_string";

    // 定义全局常量
    private static final String TAG = ScanQrCodeAct.class.getSimpleName();
    private MainHandler mainHandler;
    private SurfaceHolder mHolder;

    private CameraManager mCameraManager;
    private BeepManager beepManager;

    private SurfaceView scanPreview;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Rect mCropRect = null;

    private boolean isHasSurface = false;
    private boolean isOpenCamera = false;

    public Handler getHandler() {
        return mainHandler;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HiddenStatusBar.hiddenStatusBarAct(this);

//        // 设置StatusBar背景为透明色
//        if (Build.VERSION.SDK_INT > 22) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.parseColor("#00000000"));
//        } else {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
//
//        // 将整个界面上移22个dp值
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            findViewById(android.R.id.content).setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
//        // 设置Status字体颜色值
////        StatusBarUtil.setStatusTextColor(false, this);
        setContentView(R.layout.act_sacn_qr_code);

        initView();
        checkPermissionCamera();
    }

    /**
     * 初始化参数
     */
    public void initView() {
        scanPreview = getId(R.id.capture_preview);
        scanContainer = getId(R.id.capture_container);
        scanCropView = getId(R.id.capture_crop_view);
        scanLine = getId(R.id.capture_scan_line);
        findViewById(R.id.capture_imageview_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isHasSurface = false;
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(3000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

    }

    @Override
    public void onNetRequest() {

    }

    @Override
    public void onClick(View view) {

    }


    //region 初始化和回收相关资源
    private void initCamera(SurfaceHolder surfaceHolder) {
        mainHandler = null;
        try {
            mCameraManager.openDriver(surfaceHolder);
            if (mainHandler == null) {
                mainHandler = new MainHandler(this, mCameraManager);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "相机被占用", ioe);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Log.e(TAG, "Unexpected error initializing camera");
            displayFrameworkBugMessageAndExit();
        }

    }

    private void releaseCamera() {
        if (null != mainHandler) {
            //关闭聚焦,停止预览,清空预览回调,quit子线程looper
            mainHandler.quitSynchronously();
            mainHandler = null;
        }
        //关闭声音
        if (null != beepManager) {
            Log.e(TAG, "releaseCamera: beepManager release");
            beepManager.releaseRing();
            beepManager = null;
        }
        //关闭相机
        if (mCameraManager != null) {
            mCameraManager.closeDriver();
            mCameraManager = null;
        }
    }
    //endregion

    //region 检查权限
    private void checkPermissionCamera() {
        int checkPermission = 0;
        if (Build.VERSION.SDK_INT >= 23) {
            // checkPermission =ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
            checkPermission = PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                isOpenCamera = true;
            }

        } else {
            checkPermission = checkPermission(26);
            if (checkPermission == AppOpsManager.MODE_ALLOWED) {
                isOpenCamera = true;
            } else if (checkPermission == AppOpsManager.MODE_IGNORED) {
                isOpenCamera = false;
                displayFrameworkBugMessageAndExit();
            }
        }
    }

    /**
     * 反射调用系统权限,判断权限是否打开
     *
     * @param permissionCode 相应的权限所对应的code
     * @see {@link AppOpsManager }
     */
    private int checkPermission(int permissionCode) {
        int checkPermission = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager _manager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class<?>[] types = new Class[]{int.class, int.class, String.class};
                Object[] args = new Object[]{permissionCode, Binder.getCallingUid(), getPackageName()};
                Method method = _manager.getClass().getDeclaredMethod("noteOp", types);
                method.setAccessible(true);
                Object _o = method.invoke(_manager, args);
                if ((_o instanceof Integer)) {
                    checkPermission = (Integer) _o;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            checkPermission = 0;
        }
        return checkPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finish();

                } else {
                    finish();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void displayFrameworkBugMessageAndExit() {
        String per = String.format(getString(R.string.permission), getString(R.string.camera), getString(R.string.camera));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.qr_name));
        builder.setMessage(per);
        builder.setPositiveButton(getString(R.string.i_know), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ScanQrCodeAct.this.finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                ScanQrCodeAct.this.finish();
            }
        });
        builder.show();
    }
    //endregion

    //region 扫描结果
    public void checkResult(final String result) {
        if (beepManager != null) {
            beepManager.startRing();
        }
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activityResult(result.trim());
            }
        }, beepManager.getTimeDuration());
    }

    private void activityResult(String result) {
        if (!isFinishing()) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("width", mCropRect.width());
            bundle.putInt("height", mCropRect.height());
            bundle.putString(EXTRA_STRING, result);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            ScanQrCodeAct.this.finish();
        }
    }

    //endregion

    //region  初始化截取的矩形区域
    public Rect initCrop() {
        int cameraWidth = 0;
        int cameraHeight = 0;
        if (null != mCameraManager) {
            cameraWidth = mCameraManager.getCameraResolution().y;
            cameraHeight = mCameraManager.getCameraResolution().x;
        }

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
        return new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    //endregion

    //region SurfaceHolder Callback 回调方法
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** 没有添加SurfaceHolder的Callback");
        }
        if (isOpenCamera) {
            if (!isHasSurface) {
                isHasSurface = true;
                initCamera(holder);
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged: ");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;

    }

    @Override // 当界面获取到焦点时回调此方法
    protected void onResume() {
        super.onResume();
        if (isOpenCamera) {
            mHolder = scanPreview.getHolder();
            mCameraManager = new CameraManager(getApplication());
            if (isHasSurface) {
                initCamera(mHolder);
            } else {
                mHolder.addCallback(this);
                mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
        }
    }

    @Override // 当界面失去焦点时回调此方法
    public void onPause() {
        releaseCamera();
        super.onPause();
        if (scanLine != null) {
            scanLine.clearAnimation();
            scanLine.setVisibility(View.GONE);
        }
    }

    @Override // 当页面销毁的时候回调此方法
    protected void onDestroy() {
        super.onDestroy();
        //remove SurfaceCallback
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
    }

    @Override
    public void onHandleResponsePost(String response, String sign) {

    }

    @Override
    public void errorListener(VolleyError error, String sign) {

    }
}
