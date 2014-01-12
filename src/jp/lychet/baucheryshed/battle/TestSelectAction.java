package jp.lychet.baucheryshed.battle;

import java.util.ArrayList;

import jp.lychet.enums.E_ActionType;



public class TestSelectAction extends SelectAction{
	public TestSelectAction(BattleScene BS,BattleModule User){
		super(BS,User);
	}
	
	@Override
	public void SelectActionTypeFree() {
		// TODO 遠隔・接敵・魔法・自己スキルから選択orパス
		/*
		 * 最もランダムなNPCな方法
		 * List<E_ActionType> TypeList = new List<E_ActionType>();
		 * TypeList.add(E_ActionType.Snipe);//使えるものを追加していき
		 * TypeList.add(E_ActionType.Check);
		 * TypeList.add(E_ActionType.Cast);
		 * TypeList.add(E_ActionType.Self);
		 * AbilityList TempList = skill.getList(TypeList);//使えるリストを作って
		 * TempList.Action(rnd.nextInt(TempList.size()));//その中から選ぶ
		 * 
		 */
		//仮
		if(CastFlag>3){
			if(rnd.nextInt(2)==0)SelectCastCancelAction();
			else {
				BS.toast(User.getId(),"Attack");
				SelectAttackAction();
				CastFlag--;
			}
			return;
		}
		if(User.getId()==0)SelectSnipeAction();
		else SelectCastAction();
	}
	
	@Override
	public void SelectActionTypeInfight() {
		// TODO 攻撃・退避スキルから選択orパス
		if(User.getId()!=0)SelectAttackAction();
		else SelectLeaveAction();
	}
	
	@Override
	public void SelectDefenceAction() {
		// TODO 防御スキルから選択
		BS.toast(User.getId(),"Block");
		skill.Action(2,BS);
	}
	
	private void SelectAttackAction() {
		// TODO 攻撃スキルから選択
		//Type=Attackのもののリスト作ってうんぬん。
		BS.toast(User.getId(),"Attack");
		ArrayList<E_ActionType> ActionType = new ArrayList<E_ActionType>();
		ActionType.add(E_ActionType.Attack);
		AbilityList Ability = skill.getList(ActionType);
		Ability.Action(rnd.nextInt(Ability.size()-2)+2, BS);
		CastFlag--;
	}
	
	private void SelectSnipeAction() {
		// TODO 遠隔スキルから選択
		BS.toast(User.getId(),"Snipe");
		skill.Action(0,BS);
	}

	private void SelectCheckAction() {
		// TODO 接敵スキルから選択
		BS.toast(User.getId(),"Check");
		new CheckAction(1,User).Action(BS);
		
	}
	
	private void SelectLeaveAction() {
		// TODO 退避スキルから選択
		BS.toast(User.getId(),"Leave");
		new LeaveAction(1,User).Action(BS);
		
	}
	
	private void SelectCastAction() {
		// TODO 魔法スキルから選択
		BS.toast(User.getId(),"Cast");
		skill.Action(3,BS);
		CastFlag++;
	}
	
	private void SelectCastCancelAction() {
		// TODO 魔法スキルから選択
		BS.toast(User.getId(),"CastCancel");
		ArrayList<E_ActionType> ActionType = new ArrayList<E_ActionType>();
		ActionType.add(E_ActionType.CastCancel);
		AbilityList Ability = skill.getList(ActionType);
		Ability.Action(rnd.nextInt(Ability.size()-1)+1, BS);
		CastFlag--;
	}
	
	private void StandByPassiveAction() {
		// TODO ターン経過で自動発動するもの（BUFF）
		
	}
	
	private void CheckPassiveAction() {
		// TODO チェック時に自動発動するもの（BUFF)こんな感じでどんどんパッシブ増やそう！
		
	}
	
	
}
