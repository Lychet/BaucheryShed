package jp.lychet.baucheryshed;

import java.util.ArrayList;

import jp.lychet.baucheryshed.battle.PInt;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.MotionEvent;

public abstract class IconSelectScene extends SceneBase{
	protected Paint paint;
	
	//画像関連
	protected Bitmap buttonImg,vectorImg;
	protected Matrix Left,Right;
	protected final float iconSize=48f;
	protected Block [] block;
	
	//ブロック関連
	protected final float BlockPosiX=40f,BlockPosiY=20f;
	protected int nowBlock,BlockNum;
	protected final int rowNum=4,columnNum=4;
	
	//アイコン関連
	protected ArrayList<SelectIcon> IconInfo;
	protected int nowIcon;
	protected InformationWindow Info;
	protected final float infoPosiX=40f,infoPosiY=440f;
	protected final float buttonPosiX=200f,buttonPosiY=200f;
	protected final float titlePosiX=20f,titlePosiY=20f;
	
	//タッチイベント関連
	protected float DownX,MoveX,OldMoveX;
	protected final float FlingLevel = 50f;
	protected float nowPosi,toPosi;
	protected final float moveBlockSpeed=60f;
	
	protected class Block{
		Bitmap screen;
		int id;
		public Block(int i){
			screen=Bitmap.createBitmap(400,400,Bitmap.Config.ARGB_8888);
			id=i;
		}
		public Bitmap Draw(){
			Canvas canvas = new Canvas(screen);
			canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
			for(int i=0;i<rowNum;i++){
				for(int j=0;j<columnNum;j++){
					if(i+j*rowNum+id*rowNum*columnNum<IconInfo.size())canvas.drawBitmap(BitmapManager.getInstance(context).getIcon(IconInfo.get(i+j*rowNum+id*rowNum*columnNum).getIcon()), 18+i*100, 18+j*100, null);
				}
			}
			return screen;
		}
	}
	
	protected class InformationWindow{
		Bitmap screen;
		public InformationWindow(){
			screen=Bitmap.createBitmap(400,300,Bitmap.Config.ARGB_8888);
		}
		public Bitmap Draw(){
			Canvas canvas = new Canvas(screen);
			Paint paint = new Paint(Color.WHITE);
			paint.setTextSize(35);
			canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
			canvas.drawARGB(50,255,255,255);
			canvas.drawText(IconInfo.get(nowIcon).getTitle(), titlePosiX,titlePosiY, paint);
			canvas.drawBitmap(buttonImg, buttonPosiX,buttonPosiY, paint);
			return screen;
		}
	}
	
	
	public IconSelectScene(Context context,SceneBase superScene){
		super(context,superScene);
		IconInfo = new ArrayList<SelectIcon>();
		BlockNum=(IconInfo.size()-1)/(rowNum*columnNum)+1;
		block = new Block[BlockNum];
		for(int i=0;i<BlockNum;i++){
			block[i]=new Block(i);
		}
		nowBlock=0;
		Info=new InformationWindow();
		buttonImg= BitmapFactory.decodeResource(context.getResources(),R.drawable.buttonimg);
		vectorImg= BitmapFactory.decodeResource(context.getResources(),R.drawable.canflingimg);
		Left=new Matrix();
		Left.setScale(-1.0f,1.0f);
		Left.postTranslate(200,150);
		Right=new Matrix();
		Right.setScale(1.0f,1.0f);
		Right.postTranslate(280,150);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(35);
		nowPosi=0f;
		toPosi=0f;
	}
	
	@Override
	public void Update(){
		
	}
	
	@Override
	public void Draw(Canvas canvas){
		
		if(nowBlock>0)canvas.drawBitmap(vectorImg,Left,paint);
		if(nowBlock<BlockNum-1)canvas.drawBitmap(vectorImg,Right, paint);

		if(toPosi-nowPosi!=0.0){
			if(nowPosi-toPosi>moveBlockSpeed)nowPosi-=moveBlockSpeed;
			else if(nowPosi-toPosi<-moveBlockSpeed)nowPosi+=moveBlockSpeed;
			else nowPosi=toPosi;
			if(nowPosi==480f||nowPosi==-480f){
				if(nowPosi>0)nowBlock--;
				else nowBlock++;
				nowPosi=0f;
				toPosi=0f;
			}
		}
		for(int i=-1;i<=1;i++){
			if(nowBlock+i>=0&&nowBlock+i<BlockNum)canvas.drawBitmap(block[nowBlock+i].Draw(),BlockPosiX+nowPosi+480f*i,BlockPosiY,paint);
		}
		canvas.drawBitmap(Info.Draw(), infoPosiX,infoPosiY, paint);
	}
	
	@Override
	public void onMotionListener(MotionInformation e){
		switch(e.getAction()&MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			DownX=e.getX();
			SelectIcon(e.getX(),e.getY());
			break;
		case MotionEvent.ACTION_UP:
			
			if(DownX-e.getX()>5||DownX-e.getX()<-5){
				if((MoveX-OldMoveX)>FlingLevel){
					Fling(1);
				} else if((OldMoveX-MoveX)>FlingLevel){
					Fling(-1);
				}
			}else{
				Tap(e.getX(),e.getY());
			}
			MoveX=e.getX();
			OldMoveX=MoveX;
			break;
		case MotionEvent.ACTION_MOVE:
			SelectIcon(e.getX(),e.getY());
			OldMoveX=MoveX;
			MoveX=e.getX();
			break;
		default:
		}
	}
	
	protected void Fling(int direction){
		if(direction>0){
			if(nowBlock>0)toPosi=480f;
			else toPosi=0f;
		}
		else {
			if(nowBlock<BlockNum-1)toPosi=-480f;
			else toPosi=0f;
		}
	}
	protected void SelectIcon(float x,float y){
		if(x>=BlockPosiX&&x<BlockPosiX+400f&&y>=BlockPosiY&&y<BlockPosiY+400f){
			int result=rowNum*(PInt.get(y-BlockPosiY)/(400/columnNum))+PInt.get(x-BlockPosiX)/(400/rowNum)+nowBlock*rowNum*columnNum;
			if(result<IconInfo.size())nowIcon=result;
		}
	}
	protected void Tap(float x,float y){
		if(x>infoPosiX+buttonPosiX&&x<infoPosiX+buttonPosiX+160f&&y>infoPosiY+buttonPosiY&&y<infoPosiY+buttonPosiY+80f){//Actionボタン上なら
			Action();
		}
	}
	
	protected abstract void Action();
	
	public void MakeList(ArrayList<SelectIcon> list){
		IconInfo=list;
		BlockNum=(IconInfo.size()-1)/(rowNum*columnNum)+1;
	}
	
}
