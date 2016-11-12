package com.dwacommerce.pos.viewControllers;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.utility.Constants;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.byteclues.lib.utils.Util;

/**
 * Created by user on 06/09/16.
 */
public class BarcodeActivity extends AppCompatActivity implements DialogInterface.OnClickListener {
    private SurfaceView cameraView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_layout);
        if (ContextCompat.checkSelfPermission(BarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }

    }

    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, Constants.REQUEST_CODE_FOR_CAMERA);
            return;
        }
        Util.showAlertDialog(this, "Access to the camera is needed for detection");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != Constants.REQUEST_CODE_FOR_CAMERA) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        createCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScanning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            cameraSource.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCameraSource() {
        barcodeDetector = new BarcodeDetector.Builder(BarcodeActivity.this).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedFps(15.0f).setAutoFocusEnabled(true).build();
        MultiProcessor.Factory multiProcessorFactory = new MultiProcessor.Factory<Barcode>() {
            @Override
            public Tracker create(Barcode barcode) {
                getBarcode(barcode);
                return null;
            }
        };
        barcodeDetector.setProcessor(new MultiProcessor.Builder(multiProcessorFactory).build());
        init();
    }

    private void init() {
        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        SurfaceHolder surfaceHolder = cameraView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                startScanning(surfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                stopScanning();
            }
        });
    }

    private void startScanning(SurfaceHolder surfaceHolder) {
        try {
            cameraSource.start(surfaceHolder);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void stopScanning() {
        try {
            cameraSource.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void getBarcode(Barcode barcode) {
        try {
            String barcodeValue = barcode.displayValue;
            Intent intent = new Intent();
            intent.putExtra(Constants.BARCODE_VALUE, barcodeValue);
            setResult(RESULT_OK, intent);
            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ActivityCompat.requestPermissions(BarcodeActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CODE_FOR_CAMERA);
    }
}
