package game.project.gdc;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class NotificationActivity extends Activity {
	ImageView image;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Resources r = getResources();
		
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics()));
		params.height = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics()));
		getWindow().setAttributes(params);
		
		setContentView(R.layout.notification);
		
		image = (ImageView) findViewById(R.id.messageImage);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//Intent mIntent = getIntent();	
		//Log.v("Notification","Start");
		image.setScaleType(ImageView.ScaleType.FIT_CENTER);
		image.setImageResource(R.drawable.win);
	}
}
