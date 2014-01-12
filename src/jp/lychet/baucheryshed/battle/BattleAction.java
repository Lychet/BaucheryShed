package jp.lychet.baucheryshed.battle;

import java.util.List;

import jp.lychet.enums.E_ActionType;

public abstract class BattleAction {
	protected E_ActionType Type;
	protected int CastId,Cp;
	protected BattleModule User;
	protected int Target;
	//各効果ごとパラメータは、継承してそこで設定
	public BattleAction(BattleModule User,E_ActionType Type){
		CastId=-1;
		Cp=0;
		this.Type=Type;
		if(User!=null)Target=User.getId();
		this.User=User;
	}
	
	public abstract void Action(BattleScene BS);
	public void CastedAction(BattleScene BS){
		Action(BS);
		if(CastId>=0){
			//TODO キャステドアクションを使用したときの処理
			BS.deleteCast(CastId);
		}
		
	}
	//public void Feint(BS) 行動値消費3分の一ただし最低1減る、相手には普通の発動に見える。が何も起きない。
	//これをすべての行動で発生させられるようにしないか？
	//Castedはフェイントじゃ消えないけどCp最大値を必要値分消費する、つまり集中力使う。
	
	public abstract void Feint(BattleScene BS);
	public void CastedFeint(BattleScene BS){
		Feint(BS);
		if(CastId>=0){
			//TODO フェイント時処理
		}
		
	}
	public int getCastId(){return CastId;}
	public void setCastId(int CastId){this.CastId=CastId;}
	public int getCp(){return Cp;}
	public void setCp(int Cp){this.Cp=Cp;}
	public boolean TypeCheck(List<E_ActionType> Type){
		for( E_ActionType i : Type ){
			if(i==this.Type)return true;
		}
		return false;
	}
	public void setUser(BattleModule User){
		Target=User.getId();
		this.User=User;
	}
}
