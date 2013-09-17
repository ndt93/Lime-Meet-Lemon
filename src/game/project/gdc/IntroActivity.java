package game.project.gdc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class IntroActivity extends Activity {
	VideoView myVideo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.intro);
		
		myVideo = (VideoView) findViewById(R.id.myVideo);
		
		Uri videoUri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.intro_animation);
		myVideo.setVideoURI(videoUri);
		//myVideo.setMediaController(null);
		
		myVideo.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				if (e.getAction()==MotionEvent.ACTION_DOWN) {
					((VideoView) v).stopPlayback();
					startGame();
				}
				return true;
			}
		});
		
		myVideo.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer m) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				startGame();
			}
		});
		
		myVideo.start();
	}

	public synchronized void startGame() {
		Intent intent = new Intent(this,GameOfGravitationActivity.class);
		this.startActivity(intent);
		finish();
	}
}
