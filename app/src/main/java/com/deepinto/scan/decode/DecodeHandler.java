
package com.deepinto.scan.decode;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.deepinto.R;
import com.deepinto.ui.ScanQrCodeAct;
import com.dtr.zbar.build.ZBarDecoder;


final class DecodeHandler extends Handler {

    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final ScanQrCodeAct activity;
    private ZBarDecoder zBarDecoder;

    private boolean running = true;

    DecodeHandler(ScanQrCodeAct activity) {
        zBarDecoder = new ZBarDecoder();
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        if (message.what == R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);

        } else if (message.what == R.id.quit) {
            running = false;
            Looper.myLooper().quit();
        }
    }


    // 解码
    private void decode(byte[] data, int width, int height) {
        // long start = System.currentTimeMillis();
        // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }

        // 宽高也要调整
        int tmp = width;
        width = height;
        height = tmp;
        Rect mCropRect = activity.initCrop();
        String result = null;
        if (zBarDecoder != null) {
            try {
                result = zBarDecoder.decodeCrop(rotatedData, width, height, mCropRect.left, mCropRect.top, mCropRect.width(), mCropRect.height());
            } catch (Exception ex) {
                ex.printStackTrace();
                zBarDecoder = null;
            }
            Handler handler = activity.getHandler();
            if (result != null) {
                // long end = System.currentTimeMillis();
                if (handler != null) {
                    Message message = Message.obtain(handler,
                            R.id.decode_succeeded, result);
                    message.sendToTarget();
                }
            } else {
                if (handler != null) {
                    Message message = Message.obtain(handler, R.id.decode_failed);
                    message.sendToTarget();
                }
            }
        }
    }


}
