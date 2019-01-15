package vn.framgia.phamhung.notificaitondemo2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    private static final String REQUEST_CODE ="REQUEST" ;
    private static final int VALUE_NEXT_SONG = 0;
    private static final int VALUE_PAUSE_SONG = 1;
    private static final int VALUE_PREVIOUS_SONG =2 ;
    private static final int VALUE_PLAY_SONG =3;
    private int position=0;
    private final IBinder mIBinder = new LocalBinder();
    private MediaPlayer mMediaPlayer;
    private NotificationCompat.Builder mBuilder;
    private String CHANNEL_ID ="myID";
    private NotificationManager mManager;
    private PendingIntent mNextPendingIntent, mPausePendingIntent, mPlayPendingIntent, mBackPendingIntent;
    private boolean isPlay = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int request = intent.getIntExtra(REQUEST_CODE, 0);
            switch (request) {
                case VALUE_NEXT_SONG:
                    next();
                    break;
                case VALUE_PREVIOUS_SONG:
                    back();
                    break;
                case VALUE_PAUSE_SONG:
                    pause();
                    break;
                case VALUE_PLAY_SONG:
                    play();
                    break;
            }
            createBackPendingIntent();
            createNextPendingIntent();
            createPausePendingIntent();
            createPlayPendingIntent();
            showNotification();
        }
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    public void createMedia(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(MyService.this,
                MainActivity.mSongs.get(position).getFile());
    }
    public void play(){
        isPlay = true;
        mMediaPlayer.start();
        isPlay = false;
    }
    public void pause(){
        mMediaPlayer.pause();
        isPlay = false;
    }
    public void next(){
        position++;
        if(position > MainActivity.mSongs.size()-1)
            position =0;
        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        createMedia();
        mMediaPlayer.start();
    }
    public void back(){
        position--;
        if(position <0)
            position =0;
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        createMedia();
        mMediaPlayer.start();
    }
    public void showNotification(){
        createNotificationChannel();
        Bitmap album = BitmapFactory.decodeResource(getResources(), R.drawable.disk);
        mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.disk)
                .setLargeIcon(album)
                .addAction(R.drawable.a, "NEXT", createNextPendingIntent())
                .addAction(R.drawable.a, "PAUSE", createPausePendingIntent())
                .addAction(R.drawable.a, "PLAY", createPlayPendingIntent())
                .addAction(R.drawable.a, "BACK", createBackPendingIntent())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle())
                .setContentTitle("Music")
                .setContentText("Tên bài hát")
                .setContentIntent(getPendingIntent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOnlyAlertOnce(true)
                .setAutoCancel(false);
        startForeground(1, mBuilder.build());
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Hello";
            String description = "Zello";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            mManager = getSystemService(NotificationManager.class);
            mManager.createNotificationChannel(channel);
        }
    }
    private PendingIntent getPendingIntent(){
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPenddingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        return resultPenddingIntent;
    }
    private PendingIntent createNextPendingIntent() {
        Intent nextIntent = new Intent(getApplicationContext(), MyService.class);
        nextIntent.putExtra(REQUEST_CODE, VALUE_NEXT_SONG);
        return mNextPendingIntent = PendingIntent.getService(getApplicationContext(),
                VALUE_NEXT_SONG, nextIntent, 0);
    }

    private PendingIntent createPausePendingIntent() {
        Intent nextIntent = new Intent(getApplicationContext(), MyService.class);
        nextIntent.putExtra(REQUEST_CODE, VALUE_PAUSE_SONG);
        return mPausePendingIntent = PendingIntent.getService(getApplicationContext(),
                VALUE_PAUSE_SONG, nextIntent, 0);
    }

    private PendingIntent createBackPendingIntent() {
        Intent nextIntent = new Intent(getApplicationContext(), MyService.class);
        nextIntent.putExtra(REQUEST_CODE, VALUE_PREVIOUS_SONG);
        return mBackPendingIntent = PendingIntent.getService(getApplicationContext(),
                VALUE_PREVIOUS_SONG, nextIntent, 0);
    }

    private PendingIntent createPlayPendingIntent() {
        Intent nextIntent = new Intent(getApplicationContext(), MyService.class);
        nextIntent.putExtra(REQUEST_CODE, VALUE_PLAY_SONG);
        return mPlayPendingIntent = PendingIntent.getService(getApplicationContext(),
                VALUE_PLAY_SONG, nextIntent, 0);
    }

    public class LocalBinder extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }
}
