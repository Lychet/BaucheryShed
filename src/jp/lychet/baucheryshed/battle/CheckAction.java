package jp.lychet.baucheryshed.battle;

import jp.lychet.enums.E_ActionType;


public class CheckAction extends BattleAction{
	private int UseAP;
	public CheckAction(int UseAp,BattleModule User){
		super(User,E_ActionType.Check);
		this.UseAP=UseAp;
	}
	@Override
	public void Action(BattleScene BS){
		User.useAp(UseAP);
		if(Target==User.getId())Target=User.SelectTarget();
		BS.CheckAction(User.getId(),Target);
	}
	@Override
	public void Feint(BattleScene BS){
		if(Target==User.getId())Target=User.SelectTarget();
		BS.CheckAction(User.getId(),User.getId());//実際はチェックしてない状態を作る。
	}
}
