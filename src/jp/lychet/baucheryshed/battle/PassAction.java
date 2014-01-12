package jp.lychet.baucheryshed.battle;

import jp.lychet.enums.E_ActionType;


public class PassAction extends BattleAction{
	public PassAction(BattleModule User){
		super(User,E_ActionType.Pass);
	}
	@Override
	public void Action(BattleScene BS){
		BS.showMessage("Pass");
		BS.WaitForTap();
		User.setActionFlag(false);
	}
	@Override
	public void Feint(BattleScene BS){
	}
}
