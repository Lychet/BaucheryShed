package jp.lychet.baucheryshed.battle;

import jp.lychet.enums.E_Status;
import jp.lychet.object.Character;

public class BattleModule extends Character{
	private int PairFlag,CheckFlag;
	private int LP,MaxCP,CP,AP;
	final private int id;//自分のモジュール番号
	final private BattleScene BS;
	private SelectAction ActionDefine;//スキルもこの中に
	private boolean DidActionFlag;
	final private CastingList Cast;
	
	public BattleModule(BattleScene BS,int id){//コンストラクタ。
		this.id=id;
		this.BS=BS;
		ActionDefine =new SelectAction(BS,this);
		PairFlag=id;
		CheckFlag=id;
		LP=100;
		MaxCP=100;
		CP=0;
		AP=100;
		ActionDefine.setSkill(1);//NormalAttackをセット
		ActionDefine.setSkill(3);//NormalBlockをセット
		ActionDefine.setSkill(2);//NormalCastをセット
		ActionDefine.setSkill(new CheckAction(1,this));//NormalCheckをセット
		ActionDefine.setSkill(new LeaveAction(1,this));//NormalLeaveをセット
		Cast=new CastingList();
	}
	
	public Character getCharacter(){//パラメータのみ渡す。使ってるか…？
		return this;
	}
	
	public void SelectActionType() {//ペアがあるかないか判断して行動決定へ
		if(PairFlag==id){
			ActionDefine.SelectActionTypeFree();
		}
		else {
			ActionDefine.SelectActionTypeInfight();
		}
	}
	public void SelectActionType(int i){//防御行動を選択
		DidActionFlag=true;
		ActionDefine.SelectDefenceAction();
	}
	
	public void StandBy(){//ターン開始処理(全員）
		
		BS.toast(id, "");
		
		//TODO ここで最初のBUFF発動タイム
		
		if(DidActionFlag){
			DidActionFlag=false;
			return;
		}else{
			AP += this.getParameter(E_Status.DEXTEROUS);
			if(AP>100)AP=100;//Ap回復
			
			if(MaxCP<getParameter(E_Status.CAPACITY)*10){
				MaxCP+=getParameter(E_Status.CAPACITY);
				if(MaxCP>getParameter(E_Status.CAPACITY)*10)MaxCP=getParameter(E_Status.CAPACITY)*10;
			}//Cp回復
			
			return;
		}
	}
	
	public void PreAction(){//ターン開始処理（イニシアチブ）
		DidActionFlag=true;
		if(CheckFlag!=id){//チェック継続なら成功
			BS.MakePair(this.id,CheckFlag);//ペアを作る
		}
		//TODO ここで二番目のBUFFタイム
	}
	
	public int SelectTarget(){//後で攻撃用回復用など分けないとだ。
		if(PairFlag==id){
			return (id+1)%2;//Freeの時のターゲット選択。雑だなコレェ…
		}else{
			return PairFlag;//Pairの時その相手しか見えない。
		}
	}
	
	public void setLp(int point){//ダメージ・回復適用。和はよく使うし別メソッドで置いとくか。
		LP=point;
	}
	public void addCp(int point){//Cp回復…ってこれだけじゃ柔軟なダメージ適用はできないな！
		CP+=point;
	}
	public void useCp(int point){//こっから
		MaxCP-=point;
	}
	public void useAp(int point){
		AP-=point;//Apが減るすべてのタイミングで、それに比例してCpも減り得るとしよう
	}
	public int getLp(){
		return LP;
	}
	public int getAp(){
		return AP;
	}
	public int getCp(){//ここまでは名前の通り
		return CP;
	}
	public int getId(){return id;}//自分の位置を返すのみ
	
	public void setActionFlag(boolean e){DidActionFlag=e;}
	
	//--------------------------
	//CheckPair関連
	public void BeChecked(int id){//チェックされた時の処理
		if(CheckFlag!=this.id)CheckFlag=id;//チェック中はカットになる
		//戦闘狂：チェックされると自動でチェックし返すとか強そう。先手打てるとかっ
		//↑ペア中とか気を付けないと。
	}
	
	public void setCheckFlag(int id){//チェックフラグを建てる
		CheckFlag=id;
	}
	public int setPairFlag(int tar){//ペアを作る。元のペアは解消、チェックは消滅
		int result=-1;
		CheckFlag=id;//チェック消滅
		if(PairFlag!=id){//元のペアがあれば
			result=PairFlag;//BSに返して、相手のペア状態を解消
		}
		PairFlag=tar;//自分のペアをチェッカーに変更
		return result;
	}
	//------------------------------
	
	//------------------------------
	//戦闘関連
	
	public AttackParameter DefaultDef(){
		DidActionFlag=true;
		return new AttackParameter(0,0,0);
	}
	
	public void damageLp(int damage){
		LP=PInt.get(LP-damage);
		MaxCP=PInt.get(MaxCP-CP*damage/LP*5);
	}
	
	public void damageAp(int damage){
		//TODO 中断の確認
		double DamageRatio=(double)damage/AP;
		if(DamageRatio>=1/3){
			CheckFlag=this.id;
		}
		AP=PInt.get(AP-damage);
		MaxCP=PInt.get(MaxCP-CP*DamageRatio*5);
	}
	
	//------------------------------
	
	
	//----------------------
	//Cast関連
	public void CalcCp(){//計算用。
		int result=0;
		for(int i=0;i<Cast.size();i++){//リストのを全部足す。
			result+=Cast.getCp(i);
		}
		CP=result;
	}
	public void CastMagic(BattleAction Action,int Ap){//キャストメソッド。
		//自分のキャストリストの追加
		Cast.add(Action);
		//Cp再計算
		CalcCp();
		//キャンセル用アクションの追加
		BattleAction Cancel = new CastCancelAction(Action,Ap,this);//コピーして使いまわしたりしないように。
		ActionDefine.setSkill(Cancel);
	}
	
	public void ReleaseMagic(int CastId){//解放メソッド
		//自分のキャストリストの削除
		Cast.release(CastId);
		//Cpの再計算
		CalcCp();
		
		//キャンセル用行動の削除
		ActionDefine.releaseSkill(CastId);
		//注意！術者と対象が一緒だとキャンセル行動じゃなく術がここで解除される場合も
		//その後ReleaseEnchantでキャンセル行動が解除される。まぁ結果は同じ。
	}
	
	public void BeEnchant(BattleAction Action){//術をかけられる。
		//行動の追加
		ActionDefine.setSkill(Action);
	}
	public void ReleaseEnchant(int CastId){//術の削除
		ActionDefine.releaseSkill(CastId);
	}
	//------------------------
	
}
