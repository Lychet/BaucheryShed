package jp.lychet.baucheryshed.battle;

import jp.lychet.enums.E_ActionType;
import jp.lychet.enums.E_Element;
import jp.lychet.enums.E_Status;

public class DefenceAction extends BattleAction{
	private int UseAP;
	private double IPower,IDamage,MPower,MDamage;
	public DefenceAction(BattleModule User,int UseAp,double IPower,double IDamage,double MPower,double MDamage,E_ActionType Type){
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
		BS.setBlock(Calc());//ガードの情報を保持させる
	}
	@Override
	public void Feint(BattleScene BS){//使わない
		User.useAp(UseAP/2);
		BS.setBlock(new AttackParameter(0,0,0));//ガードの情報を保持させる
		BS.DamageCalc(User.getId());//ダメージ計算
	}
	public AttackParameter Calc(){
		int POW,DAM,MPOW,MDAM;
		POW = (int)(IPower * User.getParameter(E_Status.VITAL));//スキル倍率でうまく調整するしか
		DAM = (int)(IDamage * User.getParameter(E_Status.VITAL));
		MPOW = (int)(MPower * User.getParameter(E_Status.CAPACITY));//スキル倍率でうまく調整するしか
		MDAM = (int)(MDamage * User.getParameter(E_Status.CAPACITY));
		return new AttackParameter(POW,DAM,MPOW,MDAM,0,E_Element.Neutral);
	}

}
