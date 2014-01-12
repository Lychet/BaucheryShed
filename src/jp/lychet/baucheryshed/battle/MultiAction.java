package jp.lychet.baucheryshed.battle;

import java.util.List;

import jp.lychet.enums.E_ActionType;

public class MultiAction extends BattleAction{
	BattleAction Action1,Action2;
	
	public MultiAction(BattleAction Act1,BattleAction Act2){
		super(Act1.User,E_ActionType.Multi);
		Action1=Act1;
		Action2=Act2;
	}
	
	@Override
	public void Action(BattleScene BS){
		Action1.Action(BS);
		Action2.Target=Action1.Target;
		Action2.Action(BS);
	}
	@Override
	public void Feint(BattleScene BS){
		Action1.Feint(BS);
		Action2.Target=Action1.Target;
		Action2.Feint(BS);
	}
	@Override
	public boolean TypeCheck(List<E_ActionType> Type){
		for( E_ActionType i : Type ){
			if(i==Action1.Type||i==Action2.Type)return true;
		}
		return false;
	}
}
