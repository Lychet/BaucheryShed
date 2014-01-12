package jp.lychet.baucheryshed;

import android.content.Context;
import android.graphics.Canvas;

public class TestScene extends SceneBase{

	public TestScene(Context context,SceneBase superScene){
		super(context,superScene);
		childScene = new TestStarScene(context,this);
	}
	
	@Override
	public void Update(){
	}
	
	@Override
	public void Draw(Canvas canvas){
	}
	
	@Override
	public void onMotionListener(MotionInformation e){
	}
}
