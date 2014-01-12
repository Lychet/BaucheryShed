package jp.lychet.baucheryshed.battle;

import jp.lychet.enums.E_ActionType;


public class LeaveAction extends BattleAction{
	private int UseAP;
	public LeaveAction(int UseAp,BattleModule User){
		super(User,E_ActionType.Leave);
		this.UseAP=UseAp;
		this.Type=E_ActionType.Leave;
	}
	@Override
	public void Action(BattleScene BS){
		User.useAp(UseAP);
		BS.BreakPair(User.getId());
	}
	@Override
	public void Feint(BattleScene BS){
		//TODO Leave妨害ができるようになったら
	}
}
