package jp.lychet.baucheryshed;

import android.content.Context;
import android.graphics.Canvas;

public abstract class SceneBase {
	protected SceneBase superScene=null,childScene=null;
	protected Context context;
	protected int defaultCallback;
	protected SceneBase(Context context,SceneBase superScene){
		this.context=context;
		this.superScene=superScene;
	}
	
	public void onUpdate(){
		if(childScene==null)Update();
		else childScene.onUpdate();
	}
	
	public void onDraw(Canvas canvas){
		if(childScene==null)Draw(canvas);
		else childScene.onDraw(canvas);
	}
	
	public void onMotion(MotionInformation e){
		if(childScene==null)onMotionListener(e);
		else childScene.onMotion(e);
	}
	
	protected abstract void Update();
	protected abstract void Draw(Canvas canvas);
	protected abstract void onMotionListener(MotionInformation e);
	
	public Context getContext(){
		return context;
	}
	public void callback(int callback){
		defaultCallback=callback;
	}
}
