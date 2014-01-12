package jp.lychet.baucheryshed.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.lychet.enums.E_ActionType;
import jp.lychet.sql.SQLSkillAdapter;


public class SelectAction {
	protected BattleScene BS;
	protected int CastFlag;
	protected BattleModule User;
	protected Random rnd;
	protected AbilityList skill;//選択可能な行動部分。skill.Actionで発動ってか最初から分けておけばよくね
	static protected SQLSkillAdapter SkillMaker;
	public SelectAction(BattleScene BS,BattleModule User){
		this.BS = BS;
		this.User=User;
		skill = new AbilityList(User);
		SkillMaker = new SQLSkillAdapter(BS.getContext());
		rnd = new Random();
		CastFlag=0;
	}
	
	public void setSkill(int id){
		skill.add(SkillMaker.getAction(id, User));
	}
	
	public void setSkill(BattleAction Action){
		skill.add(Action);
	}
	
	public void releaseSkill(int CastId){
		skill.release(CastId);
	}
	
	public void SelectActionTypeFree() {
		List<E_ActionType> TypeList = new ArrayList<E_ActionType>();
		TypeList.add(E_ActionType.Snipe);//使えるものを追加していき
		TypeList.add(E_ActionType.Check);
		TypeList.add(E_ActionType.Cast);
		//TypeList.add(E_ActionType.Self);
		AbilityList TempList = skill.getList(TypeList);//使えるリストを作って
		TempList.Action(rnd.nextInt(TempList.size()),BS);//その中から選ぶ
		TypeList=null;
		TempList=null;
	}
	
	public void SelectActionTypeInfight() {
		List<E_ActionType> TypeList = new ArrayList<E_ActionType>();
		TypeList.add(E_ActionType.Attack);//使えるものを追加していき
		TypeList.add(E_ActionType.Leave);
		//TypeList.add(E_ActionType.Self);
		AbilityList TempList = skill.getList(TypeList);//使えるリストを作って
		TempList.Action(rnd.nextInt(TempList.size()),BS);//その中から選ぶ
		TypeList=null;
		TempList=null;
	}
	
	public void SelectDefenceAction() {
		List<E_ActionType> TypeList = new ArrayList<E_ActionType>();
		TypeList.add(E_ActionType.Defence);//使えるものを追加していき
		AbilityList TempList = skill.getList(TypeList);//使えるリストを作って
		TempList.Action(rnd.nextInt(TempList.size()),BS);//その中から選ぶ
		TypeList=null;
		TempList=null;
	}
	
}
