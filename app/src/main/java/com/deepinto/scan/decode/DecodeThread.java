
package com.deepinto.scan.decode;

import android.os.Handler;
import android.os.Looper;

import com.deepinto.ui.ScanQrCodeAct;

import java.util.concurrent.CountDownLatch;


/**
 * @Desc This thread does all the heavy lifting of decoding the images.
 */
final class DecodeThread extends Thread {


    private final ScanQrCodeAct activity;


    private Handler handler;

    private final CountDownLatch handlerInitLatch;

    DecodeThread(ScanQrCodeAct activity) {
        this.activity = activity;
        handlerInitLatch = new CountDownLatch(1);


    }

    Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new DecodeHandler(activity);
        handlerInitLatch.countDown();
        Looper.loop();
    }

}
