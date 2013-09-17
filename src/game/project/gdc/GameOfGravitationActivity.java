package game.project.gdc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameOfGravitationActivity extends Activity {
	//private static final String TAG = GameOfGravitationActivity.class.getSimpleName();
	
	MainGamePanel myPanel;
	Button play, release, reset, instructions, exit;
	TextView status;
	EditText level;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.main);
        
        myPanel = (MainGamePanel) findViewById(R.id.myPanel);
        play = (Button) findViewById(R.id.playButton);
        release = (Button) findViewById(R.id.releaseButton);
        reset = (Button) findViewById(R.id.resetButton);
        instructions = (Button) findViewById(R.id.instructionButton);
        level = (EditText) findViewById(R.id.levelText);
        exit = (Button) findViewById(R.id.exitButton);
                
        play.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		try {
        			int nlevel = Integer.parseInt(level.getText().toString());
            		if (nlevel < myPanel.getNoOfLevels()) {
            			myPanel.loadGame(nlevel);
            		} else {
            			Toast.makeText(GameOfGravitationActivity.this, "Enter a level between 0 and " 
            							+ (myPanel.getNoOfLevels()-1), Toast.LENGTH_SHORT).show();
            		}
        		} catch (Exception e) {
        			Toast.makeText(GameOfGravitationActivity.this, "Enter a valid number", Toast.LENGTH_SHORT).show();
        		}
        		InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        		input.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        	}
        });
        
        reset.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		myPanel.loadGame(myPanel.getCurrentLevel());
        		myPanel.setIsReleased(false);
        	}
        });
        
        release.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		if (!myPanel.getIsReleased()) {
        			myPanel.setIsReleased(true);
        		}
        	}
        });
        
        exit.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		finish();
        		System.exit(0);
        	}
        });
    }
   /*
    @Override
    public void onStop() {
    	try {
    		myPanel.getThread().join();
    		//this.finish();
    	} catch (InterruptedException e) {
    		
    	}
    }*/
}