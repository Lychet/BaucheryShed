package jp.lychet.baucheryshed;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

public abstract class StarSelectScene extends SceneBase{
	protected Paint paint;
	protected String str;
	
	//星関連
	protected Matrix starmatrix;
	protected float nowRangle=0.0f,toRangle=0.0f;
	protected Bitmap starImg;
	protected Bitmap [] menuImg;
	protected final float StarRotateSpeed = 3.0f;
	protected final float StarFlingLevel = 50f;
	protected final float StarPosiX=10f,StarPosiY=580f;
	protected final int starRadius=235;
	
	//メニューデータ
	protected NowMenuNum MenuNum;
	protected final float menuRadius=96;
	
	//タッチイベント関連
	protected float DownX,MoveX,OldMoveX;
	
	class NowMenuNum{
		int num,max;
		public NowMenuNum(int max){
			this.max=max;
			this.num=0;
		}
		public void set(int i){
			num+=i;
			if(num<0)num=max+num%max;
			if(num>=max)num%=max;
			
		}
		public int get(int i){
			int result;
			set(i);
			result=num;
			set(-i);
			return result;
		}
		public int getmax(){
			return max;
		}
	}
	
	public StarSelectScene(Context context,SceneBase superScene){
		super(context,superScene);
		MenuNum=new NowMenuNum(5);
		starImg= BitmapFactory.decodeResource(context.getResources(),R.drawable.star);
		menuImg = new  Bitmap[MenuNum.getmax()];
		
		menuImg[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.menubase);
		menuImg[1]= BitmapFactory.decodeResource(context.getResources(),R.drawable.menubase2);
		menuImg[2]= BitmapFactory.decodeResource(context.getResources(),R.drawable.menubase3);
		menuImg[3]= BitmapFactory.decodeResource(context.getResources(),R.drawable.menubase);
		menuImg[4]= BitmapFactory.decodeResource(context.getResources(),R.drawable.menubase3);
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(35);
		str="NONE";
		starmatrix=new Matrix();
		starmatrix.setTranslate(StarPosiX,StarPosiY);
	}
	
	@Override
	public void Update(){
		
	}
	
	@Override
	public void Draw(Canvas canvas){
		canvas.drawText(str, 100, 110, paint);
		canvas.drawText(""+MenuNum.get(0), 100, 150, paint);
		if(toRangle-nowRangle!=0.0){
			if(nowRangle-toRangle>StarRotateSpeed)nowRangle-=StarRotateSpeed;
			else if(nowRangle-toRangle<-StarRotateSpeed) nowRangle+=StarRotateSpeed;
			else nowRangle=toRangle;
			if(nowRangle==72f||nowRangle==-72f){
				if(nowRangle>0)MenuNum.set(1);
				else MenuNum.set(-1);
				nowRangle=0f;
				toRangle=0f;
			}
			starmatrix.setRotate(nowRangle, starRadius,starRadius);
			starmatrix.postTranslate(StarPosiX, StarPosiY);
		}
		canvas.drawBitmap(starImg, starmatrix, paint);
		canvas.drawBitmap(menuImg[MenuNum.get(0)], starRadius+StarPosiX-menuRadius+starRadius*(float)Math.cos((90-nowRangle)/180*Math.PI),starRadius+StarPosiY-menuRadius-starRadius*(float)Math.sin((90-nowRangle)/180*Math.PI), paint);
		canvas.drawBitmap(menuImg[MenuNum.get(1)], starRadius+StarPosiX-menuRadius+starRadius*(float)Math.cos((162-nowRangle)/180*Math.PI),starRadius+StarPosiY-menuRadius-starRadius*(float)Math.sin((162-nowRangle)/180*Math.PI), paint);
		canvas.drawBitmap(menuImg[MenuNum.get(2)], starRadius+StarPosiX-menuRadius+starRadius*(float)Math.cos((234-nowRangle)/180*Math.PI),starRadius+StarPosiY-menuRadius-starRadius*(float)Math.sin((234-nowRangle)/180*Math.PI), paint);
		canvas.drawBitmap(menuImg[MenuNum.get(3)], starRadius+StarPosiX-menuRadius+starRadius*(float)Math.cos((306-nowRangle)/180*Math.PI),starRadius+StarPosiY-menuRadius-starRadius*(float)Math.sin((306-nowRangle)/180*Math.PI), paint);
		canvas.drawBitmap(menuImg[MenuNum.get(4)], starRadius+StarPosiX-menuRadius+starRadius*(float)Math.cos((378-nowRangle)/180*Math.PI),starRadius+StarPosiY-menuRadius-starRadius*(float)Math.sin((378-nowRangle)/180*Math.PI), paint);
	}
	
	@Override
	public void onMotionListener(MotionInformation e){
		switch(e.getAction()&MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			DownX=e.getX();
			toRangle=nowRangle;
			str="Down";
			break;
		case MotionEvent.ACTION_UP:
			
			if(DownX-e.getX()>5||DownX-e.getX()<-5){
				if((MoveX-OldMoveX)>StarFlingLevel){
					Fling(1);
				} else if((OldMoveX-MoveX)>StarFlingLevel){
					Fling(-1);
				} else {
					toRangle=0f;
				}
			}else{
				Tap(e.getX(),e.getY());
			}
			MoveX=e.getX();
			OldMoveX=MoveX;
			break;
		case MotionEvent.ACTION_MOVE:
			OldMoveX=MoveX;
			MoveX=e.getX();
			str="move:"+MoveX;
			if((MoveX-DownX)/5>30)DownX=MoveX-150;
			else if((MoveX-DownX)/5<-30)DownX=MoveX+150;
			toRangle=(MoveX-DownX)/5;
			break;
		default:
			str="Error";
		}
	}
	
	protected void Fling(int direction){
		str="Fling";
		if(direction>0){
			toRangle=72f;
		}
		else toRangle=-72f;
	}
	protected void Tap(float x,float y){
		toRangle=0f;
		str="Tap";
		if(starRadius+StarPosiX-menuRadius<x&&starRadius+StarPosiX+menuRadius>x
				&&StarPosiY-menuRadius<y&&StarPosiY+menuRadius>y){
			//選択中のメソッドの実行
			Action();
			str="Event!"+MenuNum.get(0);
		}
		else if(starRadius*(1+Math.cos(0.9*Math.PI))+StarPosiX-menuRadius<x&&starRadius*(1+Math.cos(0.9*Math.PI))+StarPosiX+menuRadius>x
				&&StarPosiY+starRadius*(1-Math.sin(0.9*Math.PI))-menuRadius<y&&StarPosiY+starRadius*(1-Math.sin(0.9*Math.PI))+menuRadius>y){
			toRangle=72f;
		}
		else if(starRadius*(1+Math.cos(0.1*Math.PI))+StarPosiX-menuRadius<x&&starRadius*(1+Math.cos(0.1*Math.PI))+StarPosiX+menuRadius>x
				&&StarPosiY+starRadius*(1-Math.sin(0.1*Math.PI))-menuRadius<y&&StarPosiY+starRadius*(1-Math.sin(0.1*Math.PI))+menuRadius>y){
			toRangle=-72f;
		}
	}
	protected abstract void Action();
}
