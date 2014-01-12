package jp.lychet.baucheryshed;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;

public class LootActivity extends Activity{
	private SurfaceView SV;
	private GameSurface GS;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loot);
		//GS = (GameSurface)findViewById(R.id.mainsurface);
	}
}
