package jp.lychet.baucheryshed;

import android.view.MotionEvent;

public class MotionInformation {
	private int Type;
	private float x,y;
	
	public MotionInformation(MotionEvent e,float scale,float paddingX,float paddingY){
		Type=e.getAction();
		x=e.getX()/scale-paddingX;
		y=e.getY()/scale-paddingY;
	}
	
	public float getX(){return x;}
	public float getY(){return y;}
	public int getAction(){return Type;}
}
