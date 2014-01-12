package jp.lychet.object;

import jp.lychet.enums.E_ActionType;

public class Skill {
	protected E_ActionType Type;
	protected int id,Type_id;

	public Skill(){
		
	}
	public Skill(int id,int Type_id,E_ActionType Type){
		this.id=id;
		this.Type_id=Type_id;
		this.Type=Type;
	}
	
	public E_ActionType getType(){
		return Type;
	}
	
	public int getTypeId(){
		return Type_id;
	}
}
