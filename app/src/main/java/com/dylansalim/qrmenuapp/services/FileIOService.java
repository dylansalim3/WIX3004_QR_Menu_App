package com.dylansalim.qrmenuapp.services;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import com.squareup.picasso.Picasso;

import androidx.core.app.ActivityCompat;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class FileIOService {
    public static String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static int PICK_IMAGE = 101;
    private static String TAG = "FileIO";

    public static boolean requestReadIOPermission(Activity thisActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(thisActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    public static Single<Bitmap> getBitmapSingle(Picasso picasso, String url) {
        return Single.create(emitter -> {
            try {
                if(!emitter.isDisposed()){
                    Bitmap bitmap = picasso.load(url).get();
                    emitter.onSuccess(bitmap);
                }
            } catch (Throwable err) {
                emitter.onError(err);
            }
        });
    }
//    Usage::
//    FileIOService.getBitmapSingle(Picasso.get(), {url})
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(new SingleObserver<Bitmap>() {
//        @Override
//        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
//
//        }
//
//        @Override
//        public void onSuccess(@io.reactivex.annotations.NonNull Bitmap bitmap) {
//            itemImg = bitmap;
//        }
//
//        @Override
//        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//            Log.d(TAG, e.toString());
//        }
//    });
}
