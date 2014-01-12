package jp.lychet.baucheryshed.battle;

import java.util.ArrayList;


public class CastingList extends ArrayList<BattleAction>{
	@Override
	public boolean add(BattleAction Action){
		return super.add(Action);
	}
	
	public int getCp(int index){
		return super.get(index).getCp();
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
