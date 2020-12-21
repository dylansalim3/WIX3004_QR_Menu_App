package com.dylansalim.qrmenuapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.models.dto.FcmDto;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.NotificationNetworkInterface;
import com.dylansalim.qrmenuapp.ui.main.MainActivity;
import com.dylansalim.qrmenuapp.ui.merchant.MerchantActivity;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.dylansalim.qrmenuapp.utils.SharedPrefUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Random;

import io.reactivex.schedulers.Schedulers;


public class NotificationService extends FirebaseMessagingService {

    final String TAG = "NOTIFICATION_SERVICE";

    /**
     * Called if FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve
     * the token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        updateFCMToken(token);
    }

    /**
     * Send fcm token to server. Fcm token is unique to every device.
     */
    private void updateFCMToken(String fcm_token) {
        Log.d(TAG, "Updating fcm token");
        SharedPreferences sharedPreferences = getSharedPreferences("QRMenuApp", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");

        assert token != null;
        if (token.equals("")) {
            Log.e(TAG, "Jwt token is empty");
            return;
        }

        String dataString = null;
        try {
            dataString = JWTUtils.getDataString(token);
        } catch (Exception e) {
            Log.d(TAG, "Jwt decode failed");
            e.printStackTrace();
        }
        UserDetailDao userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);
        FcmDto fcmDto = new FcmDto(userDetailDao.getId(), fcm_token);

        NetworkClient.getNetworkClient().create(NotificationNetworkInterface.class)
                .updateFCMToken(fcmDto)
                .subscribeOn(Schedulers.io())
                .subscribe();

        Log.d(TAG, "Fcm token updated");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String body = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");

        if (SharedPrefUtil.getNotificationPref(this)) {
            sendNotification(body, title, getPendingIntent(remoteMessage));
        }
    }

    private PendingIntent getPendingIntent(RemoteMessage remoteMessage) {
        String activity = remoteMessage.getData().get("activity");
        String data = remoteMessage.getData().get("data");
        Intent intent;

        if (activity != null && data != null) {
            intent = new Intent(this, MerchantActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.store_id), Integer.parseInt(data));
            intent.putExtras(bundle);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody  FCM message body received.
     * @param messageTitle FCM message title received.
     */
    private void sendNotification(String messageBody, String messageTitle, PendingIntent pendingIntent) {
        String channelId = "QrMenuApp_Channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.qrmenu_logo)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        if (SharedPrefUtil.getNotificationSoundPref(this)) {
            notificationBuilder.setSound(defaultSoundUri);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Qr Menu App notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((new Random()).nextInt(), notificationBuilder.build());
    }
}
