package vn.framgia.phamhung.notificaitondemo2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Song> mSongs = new ArrayList<>();
    private TextView mName;
    private ImageView mImage;
    private ImageView mPlay;
    private ImageView mNext;
    private ImageView mPrevious;
    private SeekBar mSeekBar;
    private MyService mService;
    private boolean mBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initViews();
        mSongs.add(new Song(0,"Người theo đuổi ánh trăng",R.raw.nguoi_theo_duoi_anh_sang));
        mSongs.add(new Song(1,"Người theo đuổi ánh trăng",R.raw.nguoi_theo_duoi_anh_sang));
        mSongs.add(new Song(2,"Người theo đuổi ánh trăng",R.raw.nguoi_theo_duoi_anh_sang));
        Intent intent = new Intent(this, MyService.class);
        if (mService == null) {
            startService(intent);
        }
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }
    public void initViews(){
        mName = findViewById(R.id.text_name);
        mImage = findViewById(R.id.image_disk);
        mNext = findViewById(R.id.image_button_next);
        mPrevious = findViewById(R.id.image_button_back);
        mPlay = findViewById(R.id.image_button_play);
        mSeekBar =  findViewById(R.id.seek_bar_music);
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.e("a", "onServiceDisconnected");
            mBound = false;
        }
    };
}
