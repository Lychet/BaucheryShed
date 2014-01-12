package jp.lychet.baucheryshed.battle;

import java.util.ArrayList;
import java.util.Random;

import jp.lychet.baucheryshed.MotionInformation;
import jp.lychet.baucheryshed.SceneBase;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public abstract class BattleScene extends SceneBase{
	ArrayList<BattleModule> Module;
	CastManager CastManager;
	AttackParameter ATK,DEF;
	Random rnd;
	int Count;
	Thread DrawThread,UpDateThread;
	SurfaceHolder surfaceHolder;
	String[] DrawStr;
	String Message;
	boolean TouchFlag;
	Matrix matrix;
	float rotate=1.0f;
	
	public BattleScene(Context context,SceneBase superScene){
		super(context,superScene);
		Module=new ArrayList<BattleModule>();
		TouchFlag=false;
		Count=0;
		Message="";
		rnd = new Random();
		matrix = new Matrix();
		matrix.postScale(1.0f, 1.0f);
	}
	
	@Override
	public void Update(){
		CheckInitiative();
	}
	
	
	
	
	//-------------------------------
	//初期化処理
	public void Initialize(){//初期化
		CastManager = new CastManager();
		Encount();
	}
	public abstract void Encount();//戦闘の初期設定。モジュール数の設定もあるので、確実になるよう抽象化
	
	
	//----------------------------
	//メインループ
	public void CheckInitiative(){
		int nextAction;
		nextAction=0;//行動者決定
		for(int i=1;i<Module.size();i++){
			int j=1;
			//TODO イニシアチブチェック用メソッドの追加
			if(Module.get(nextAction).getAp()<Module.get(i).getAp())nextAction=i;
			else if(Module.get(nextAction).getAp()==Module.get(i).getAp()&&rnd.nextInt(j+1)==0){
				j++;
				nextAction=i;//これで同じ確立になる
			}
		}
		
		showMessage("CheckInitiatve");
		toast(nextAction,"SelectAction");
		WaitForTap();
		
		
		StandBy(nextAction);
		
	}
	
	public void StandBy(int nextAction){
		//Ap回復など
		for(BattleModule i:Module){
			i.StandBy();//もじーる内部に書いておく
		}
		
		
		showMessage("StandBy");
		WaitForTap();
		
		SelectAction(nextAction);
	}
	
	public void SelectAction(int id){
		Module.get(id).PreAction();
		Module.get(id).SelectActionType();//ペアがあるかないか判断しておく
		//とんだ先であれこれ進行していく、でここに戻ってくる
		Result();//結果を呼ぶ
	}
	
	public void Result(){
		
		//TODO CheckやCastの中断・Breakなど状態確認
		
		//戦闘終了判断
		
		
		showMessage("Result");
		WaitForTap();
		
		
		//ループが続けられるか判断して、再度CheckInitiativeを呼び出す
		if(UpDateThread!=null)CheckInitiative();
	}
	
	//----------------------------
	
	
	//----------------------------
	//攻撃行動時
	public void setAttack(AttackParameter result){//AttackActionに呼ばれる
		//攻撃のパラメータを保持しておく。スキルのActionメソッドで計算済み。
		ATK=result;
		showMessage("Attack");
		WaitForTap();
	}
	
	public void SelectAction(int id,int DefenceFlag){//AttackActionに呼ばれる
		Module.get(id).SelectActionType(DefenceFlag);//防御行動。DefenceActionを呼び出す
	}
	
	public void setBlock(AttackParameter result){//DefenceActionに呼ばれる
		//防御のパラメータを保持しておく。スキルのActionメソッドで計算済み。
		DEF=result;
		showMessage("Block");
		WaitForTap();
	}
	
	public void DamageCalc(int TargetId){//DefenceActionに呼ばれる
		//TODO ダメージ計算 ATK-DEFとかなんとか
		//Module[TargetId].setLp(10);とかuseAp()とか
		//忘れずATK,DEFの情報をnullにすること！
		
		if(DEF==null)DEF=Module.get(TargetId).DefaultDef();
		DamageResult result=ATK.Calc(DEF);
		Module.get(TargetId).damageLp(result.Lp);
		Module.get(TargetId).damageAp(result.Ap);
		ATK=null;
		DEF=null;
		
		showMessage("Calc");
		WaitForTap();
		
	}
	
	//---------------------------
	
	//---------------------------
	//チェック・リーブ・ペア関連
	
	public void CheckAction(int User,int tar){//チェック開始。CheckActionに呼ばれる
		Module.get(User).setCheckFlag(tar);//チェックする
		Module.get(tar).BeChecked(User);//チェック中チェック→カットされる、などチェックされた時の処理
		showMessage("Check");
		WaitForTap();
	}
	
	public void MakePair(int User,int tar){
		Module.get(User).setPairFlag(tar);//チェッカーがターゲットをペアに
		int PairCheck=Module.get(tar).setPairFlag(User);//ターゲットがチェッカーをペアに
		if(PairCheck>=0){//ターゲットに元のペアがあれば
			Module.get(PairCheck).setPairFlag(PairCheck);//元のペアの相手のペアを解除し
			CheckAction(PairCheck,tar);//ターゲットにチェック中の状態に変更。
			//TODO ここの処理は自動リーブ(スイッチ)とか、色々選べたりすると楽しそう。
		}
		showMessage("MakePair");
		WaitForTap();
	}
	
	public void BreakPair(int User){//ペア同士のペアフラグを解除。
		int tar=Module.get(User).setPairFlag(User);
		Module.get(tar).setPairFlag(tar);
		showMessage("Leave");
		WaitForTap();
	}
	
	//----------------------------
	
	
	//----------------------------
	//Cast関連
	public void setCast(BattleAction Action,int Target,int Master,int Ap){//新しい情報の登録
		//キャストアクションすると最初に呼ばれる。Apのみ消費済み。
		//この時点でCpはActionに設定済み
		int CastId;
		CastId=CastManager.add(Master,Target);//新規登録
		Action.setCastId(CastId);
		Action.setUser(Module.get(Target));
		Module.get(Master).CastMagic(Action,Ap);//術者に情報を追加
		Module.get(Target).BeEnchant(Action);//対象に行動を追加
		showMessage("setCast");
		WaitForTap();
	}
	
	public void deleteCast(int CastId){//登録情報の削除
		int CastIndex = CastManager.getIndexById(CastId);
		if(CastIndex<0){
			showMessage("deleteCastError");
			WaitForTap();
		}
		int Caster = CastManager.getCaster(CastIndex);
		int Target = CastManager.getTarget(CastIndex);
		if(CastId!=CastManager.remove(CastIndex))showMessage("キャスト解放のエラー");
		Module.get(Caster).ReleaseMagic(CastId);
		Module.get(Target).ReleaseEnchant(CastId);
		showMessage("deleteCast");
		WaitForTap();
	}
	//---------------------------
	
	
	//---------------------------
	//デバッグ・確認用メソッド
	public void toast(int id,String str){
		DrawStr[id]=str;
	}
	public void showMessage(String str){
		Message=str;
	}
	
    public boolean onTouchEvent(MotionEvent event) {
        if(Count<=0){//長おし＝連打防止用
        	TouchFlag=true;
        	Count=10;
        }
        return true;
    }
	
	public void WaitForTap(){
		while(!TouchFlag){//タップするまで待つ
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		TouchFlag = false;
	}
	
	@Override
	public void onMotionListener(MotionInformation e){
		if((e.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN)TouchFlag=true;
	}
	
	//----------------------------
	
	public BattleModule getModule(int id){
		return Module.get(id);
	}
	
	
	//-----------------------------
	//描画処理
	@Override
	public void Draw(Canvas canvas){
		final Bitmap img;
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setTextSize(35);
		rotate+=0.5;
		matrix.setTranslate(-235,-235);
		matrix.postRotate(rotate);
		matrix.postTranslate(245,600);
		canvas = surfaceHolder.lockCanvas();
		canvas.drawColor(Color.BLACK,PorterDuff.Mode.CLEAR);
		canvas.drawText("User1:"+DrawStr[0],100,20,paint);
		canvas.drawText("User2:"+DrawStr[1],100,60,paint);
		canvas.drawText(Message,100,300,paint);
	}
	
	
	
}
