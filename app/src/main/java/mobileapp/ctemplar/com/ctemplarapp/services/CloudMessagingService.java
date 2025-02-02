package mobileapp.ctemplar.com.ctemplarapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import mobileapp.ctemplar.com.ctemplarapp.CTemplarApp;
import mobileapp.ctemplar.com.ctemplarapp.R;
import mobileapp.ctemplar.com.ctemplarapp.main.InboxFragment;
import mobileapp.ctemplar.com.ctemplarapp.main.MainActivityViewModel;
import mobileapp.ctemplar.com.ctemplarapp.message.ViewMessagesActivity;
import mobileapp.ctemplar.com.ctemplarapp.net.entity.RemoteMessageAction;
import mobileapp.ctemplar.com.ctemplarapp.net.entity.NotificationMessageEntity;
import mobileapp.ctemplar.com.ctemplarapp.repository.UserRepository;
import retrofit2.Response;
import timber.log.Timber;

import static mobileapp.ctemplar.com.ctemplarapp.message.ViewMessagesActivity.PARENT_ID;
import static mobileapp.ctemplar.com.ctemplarapp.repository.constant.MainFolderNames.FOLDER_NAME;

public class CloudMessagingService extends FirebaseMessagingService {
    public static final String FROM_NOTIFICATION = "from_notification";
    private static final String NOTIFICATION_CHANNEL_ID = "com.ctemplar.mails";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Timber.d("Message data payload: %s", remoteMessage.getData());
            Map<String, String> remoteMessageMap = remoteMessage.getData();
            NotificationMessageEntity notificationMessageEntity = NotificationMessageEntity
                    .getFromMap(remoteMessageMap);

            if (RemoteMessageAction.CHANGE_PASSWORD.toString()
                    .equals(notificationMessageEntity.getAction())
            ) {
                onPasswordChanged();
                return;
            }
            boolean isNotificationsEnabled = CTemplarApp.getUserStore().isNotificationsEnabled();
            if (isNotificationsEnabled) {
                showNotification(
                        notificationMessageEntity.getSender(),
                        notificationMessageEntity.getSubject(),
                        notificationMessageEntity.getFolder(),
                        notificationMessageEntity.getMessageID(),
                        notificationMessageEntity.getParentID(),
                        notificationMessageEntity.isSubjectEncrypted()
                );
            }
            WeakReference<InboxFragment> inboxFragmentReference = InboxFragment.instanceReference;
            if (inboxFragmentReference != null) {
                InboxFragment inboxFragment = inboxFragmentReference.get();
                if (inboxFragment != null && !inboxFragment.isRemoving()) {
                    inboxFragment.onNewMessage(notificationMessageEntity.getMessageID(),
                            notificationMessageEntity.getFolder());
                }
            }
        }
    }

    private void showNotification(
            final String sender,
            final String subject,
            final String folder,
            final long messageId,
            final long parentId,
            final boolean isSubjectEncrypted
    ) {
        long id = (parentId == -1) ? messageId : parentId;
        int notificationID = (messageId == -1) ? new Random().nextInt(1000) : (int) messageId;
        String content = (isSubjectEncrypted) ? getString(R.string.txt_new_message) : subject;

        Intent intent = new Intent(this, ViewMessagesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(PARENT_ID, id);
        intent.putExtra(FOLDER_NAME, folder);
        intent.putExtra(FROM_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationID, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setGroup(String.valueOf(parentId))
                .setContentTitle(sender)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setContentInfo(sender)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher_small);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            Timber.e("showNotification NotificationManager is null");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription(getString(R.string.notification_channel_description));
            notificationChannel.setShowBadge(false);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    private void onPasswordChanged() {
        Timber.d("onPasswordChanged");
        UserRepository userRepository = CTemplarApp.getUserRepository();
        String token = userRepository.getFirebaseToken();
        userRepository.signOut(MainActivityViewModel.ANDROID, token)
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull Response<Void> voidResponse) {
                        postExit();
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        postExit();
                        Timber.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void postExit() {
        Intent intent = new Intent(MainActivityViewModel.EXIT_BROADCAST_ACTION);
        sendBroadcast(intent);
    }
}
