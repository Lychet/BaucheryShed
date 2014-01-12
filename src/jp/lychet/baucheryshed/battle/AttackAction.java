package jp.lychet.baucheryshed.battle;

import jp.lychet.enums.E_ActionType;
import jp.lychet.enums.E_Element;
import jp.lychet.enums.E_Status;

public class AttackAction extends BattleAction{
	private int UseAP;
	private double IPower,IDamage,MPower,MDamage;
	public AttackAction(BattleModule User,int UseAp,double IPower,double IDamage,double MPower,double MDamage,E_ActionType Type){
		super(User,Type);
		this.IPower=IPower;
		this.IDamage=IDamage;
		this.MPower=MPower;
		this.MDamage=MDamage;
		this.UseAP=UseAp;
	}
	@Override
	public void Action(BattleScene BS){
		User.useAp(UseAP);
		BS.setAttack(Calc());//攻撃の情報を保持させる
		if(Target==User.getId())Target=User.SelectTarget();
		BS.SelectAction(Target,0);//二つ目の引数ある＝防御行動。防御させる
		BS.DamageCalc(Target);//ダメージ計算
	}
	
	@Override
	public void Feint(BattleScene BS) {
		User.useAp(UseAP/2);
		BS.setAttack(new AttackParameter(0,0,0));
		if(Target==User.getId())Target=User.SelectTarget();
		BS.SelectAction(Target,0);//二つ目の引数ある＝防御行動。防御させる
	}
	
	public AttackParameter Calc(){
		int POW,DAM,MPOW,MDAM;
		POW = (int)(IPower * User.getParameter(E_Status.POWER));
		MPOW=(int)(MPower * User.getParameter(E_Status.MAGIC));//スキル倍率でうまく調整するしか
		DAM = (int)(IDamage * User.getParameter(E_Status.POWER));
		MDAM = (int)(MDamage * User.getParameter(E_Status.MAGIC));
		return new AttackParameter(POW,DAM,MPOW,MDAM,0,E_Element.Neutral);
	}
	
}
