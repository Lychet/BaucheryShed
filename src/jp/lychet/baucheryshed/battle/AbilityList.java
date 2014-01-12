package jp.lychet.baucheryshed.battle;

import java.util.ArrayList;
import java.util.List;

import jp.lychet.enums.E_ActionType;

public class AbilityList extends ArrayList<BattleAction>{
	
	AbilityList(BattleModule User){
		this.add(new PassAction(User));
	}
	
	private AbilityList(){
	}
	
	@Override
	public boolean add(BattleAction Action){
		return super.add(Action);
	}
	
	public boolean add(BattleAction Action,int CastId){
		Action.setCastId(CastId);
		return super.add(Action);
	}
	
	public void Action(int i,BattleScene BS){
		BattleAction BA=super.get(i);
		BA.CastedAction(BS);
	}
	
	public AbilityList getList(List<E_ActionType> Type){//指定タイプを持つアビリティーのみのリストを返す
		AbilityList Result = new AbilityList();
		BattleAction temp = null;
		Result.add(super.get(0));
		for(int i=1;i<size();i++){
			temp=super.get(i);
			if(temp.TypeCheck(Type))Result.add(temp);
		}
		return Result;
	}
	
	public void release(int CastId){
		for(int i=0;i<super.size();i++){
			if(super.get(i).getCastId()==CastId){
				super.remove(i);
				return;
			}
		}
	}
	
}
