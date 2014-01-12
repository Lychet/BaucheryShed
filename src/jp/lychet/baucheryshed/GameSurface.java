package jp.lychet.baucheryshed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{

	int Count;
	Bitmap theScreen;
	Canvas Screen;
	Thread DrawThread,UpDateThread;
	SurfaceHolder surfaceHolder;
	SceneBase BaseScene;
	Matrix screenMatrix;
	float scale,paddingX,paddingY;
	
	public GameSurface(Context context,AttributeSet as,int type) {//コンストラクタ
		super(context,as,type);
		constract(context);
	}
	
	public GameSurface(Context context,AttributeSet as) {//コンストラクタ
		super(context,as);
		constract(context);
	}
	
	public GameSurface(Context context) {//コンストラクタ
		super(context);
		constract(context);
	}
	
	private void constract(Context context){
		Count=0;
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		BaseScene=new TestScene(context,null);
		setFocusable(true);
		theScreen=Bitmap.createBitmap(480, 800,Bitmap.Config.ARGB_8888);
		Screen=new Canvas(theScreen);
		screenMatrix=new Matrix();
		WindowManager WM = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display disp=WM.getDefaultDisplay();
		screenAdjust(disp.getWidth(),disp.getHeight());
		
	}
	
	private void screenAdjust(float width,float height){
		paddingX=0.0f;
		paddingY=0.0f;
		screenMatrix.setTranslate(0, 0);
		float wScale=width/480,hScale=height/800;
		if(wScale>hScale){
			scale=hScale;
			paddingX=(width-480*scale)/2;
		} else {
			scale=wScale;
			paddingY=(height-800*scale)/2;
		}
		screenMatrix.postTranslate(paddingX,paddingY);
		screenMatrix.postScale(scale,scale);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
		//画面変更時
		screenAdjust(width,height);
	}
		
	@Override
	public void surfaceCreated(SurfaceHolder holder){//アクティヴィティ開始---
		DrawThread = new Thread(new Runnable(){
			
			class FPSAdjuster{
				private int Count;
				private float FPS;
				private long startTime;
				private final int FPSNum=60;
				
				protected FPSAdjuster(){
					Count=0;
					FPS=60f;
				}
				
				public void Update(){
					if(Count==0){
						startTime=System.currentTimeMillis();
					}
					if(Count==FPSNum){
						long t=System.currentTimeMillis();
						FPS=1000.f/((t-startTime)/FPSNum);
						Count=0;
						startTime=t;
					}
					Count++;
				}
				public Float getFPS(){
					return FPS;
				}
				public long WaitTime(){
					long tookTime=System.currentTimeMillis()-startTime;
					long waitTime=Count*1000/FPSNum-tookTime;
					if(waitTime>0)return waitTime;
					else return 0;
				}
				
			}
			
			@Override
			public void run(){
				//描画
				Canvas canvas;
				Paint paint=new Paint();
				paint.setColor(Color.WHITE);
				paint.setTextSize(35);
				FPSAdjuster FPS = new FPSAdjuster();
				while(DrawThread != null){//描画処理メインループ-------
					FPS.Update();
					Screen.drawColor(Color.BLACK,PorterDuff.Mode.CLEAR);
					BaseScene.onDraw(Screen);
					canvas = surfaceHolder.lockCanvas();
					canvas.drawColor(Color.WHITE,PorterDuff.Mode.CLEAR);
					canvas.drawBitmap(theScreen,screenMatrix,paint);
					canvas.drawText(FPS.getFPS().toString(), 450f, 30f,paint);
					surfaceHolder.unlockCanvasAndPost(canvas);
					try {
						Thread.sleep(FPS.WaitTime());
					} catch (InterruptedException e) {
					}
				}//----------------------------------------------------
				
			}
		});
		
		UpDateThread = new Thread (new Runnable(){//戦闘処理
			@Override
			public void run(){
				while(UpDateThread != null){//処理メインループ-------
					BaseScene.onUpdate();
				}//----------------------------------------------------
				
			}
		});
			
		UpDateThread.start();
		DrawThread.start();
	}//Ａｃｔｉｖｉｔｙ開始-----------------------------------------------
		
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//スレッド止める
		DrawThread = null;
		UpDateThread = null;
	}
	@Override
	public boolean onTouchEvent(MotionEvent e){
		MotionInformation MI = new MotionInformation(e, scale,paddingX,paddingY);
		BaseScene.onMotion(MI);
		return true;
	}
}

