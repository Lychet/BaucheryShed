package jp.lychet.sql;

import jp.lychet.baucheryshed.battle.AttackAction;
import jp.lychet.baucheryshed.battle.BattleAction;
import jp.lychet.baucheryshed.battle.BattleModule;
import jp.lychet.baucheryshed.battle.CastAction;
import jp.lychet.baucheryshed.battle.DefenceAction;
import jp.lychet.enums.E_ActionType;
import jp.lychet.enums.E_CastTargetType;
import jp.lychet.object.Skill;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;

public class SQLSkillAdapter extends SQLBaseAdapter{
	public SQLSkillAdapter(Context context){
		super(context);
	}
	
	public Skill getSkill(int id){
		DBOpen();
		SQLiteCursor cursor = null;
		int Type_id = 0;
		E_ActionType Type = null;
		try{
			cursor = (SQLiteCursor)DB.query("SkillTable", null,"id=?",new String[]{""+id}, null,null,null);
			cursor.moveToFirst();
			Type_id=cursor.getInt(cursor.getColumnIndex("type_id"));
			Type = E_ActionType.valueOf(cursor.getString(cursor.getColumnIndex("type")));	
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		DBClose();
		return new Skill(id,Type_id,Type);
	}
	
	public BattleAction getAction(int id,BattleModule User){
		return getAction(getSkill(id),User);
	}
	
	public static BattleAction getAction(Skill skill,BattleModule User){
		DBOpen();
		SQLiteCursor cursor = null;
		switch(skill.getType()){
		case Attack:
		{
			double ipower,idamage,mpower,mdamage;
			int useAp;
			try{
				cursor = (SQLiteCursor)DB.query("AttackSkillTable", null,"id=?",new String[]{""+skill.getTypeId()}, null,null,null);
				cursor.moveToFirst();
				ipower = cursor.getDouble(cursor.getColumnIndex("ipower"));
				mpower = cursor.getDouble(cursor.getColumnIndex("mpower"));
				idamage = cursor.getDouble(cursor.getColumnIndex("idamage"));
				mdamage = cursor.getDouble(cursor.getColumnIndex("mdamage"));
				useAp = cursor.getInt(cursor.getColumnIndex("use_ap"));
			}finally{
				if(cursor!=null){
					cursor.close();
				}
			}
			DBClose();
			return new AttackAction(User,useAp,ipower,idamage,mpower,mdamage,E_ActionType.Attack);
		}
		case Defence:
		{
			double ipower,idamage,mpower,mdamage;
			int useAp;
			try{
				cursor = (SQLiteCursor)DB.query("DefenceSkillTable", null,"id=?",new String[]{""+skill.getTypeId()}, null,null,null);
				cursor.moveToFirst();
				ipower = cursor.getDouble(cursor.getColumnIndex("ipower"));
				mpower = cursor.getDouble(cursor.getColumnIndex("mpower"));
				idamage = cursor.getDouble(cursor.getColumnIndex("idamage"));
				mdamage = cursor.getDouble(cursor.getColumnIndex("mdamage"));
				useAp = cursor.getInt(cursor.getColumnIndex("use_ap"));
			}finally{
				if(cursor!=null){
					cursor.close();
				}
			}
			DBClose();
			return new DefenceAction(User,useAp,ipower,idamage,mpower,mdamage,E_ActionType.Defence);
		}
		case Cast:
		{
			int useAp,useCp,Typeid;
			E_ActionType Type;
			E_CastTargetType TargetType;
			try{
				cursor = (SQLiteCursor)DB.query("CastSkillTable", null,"id=?",new String[]{""+skill.getTypeId()}, null,null,null);
				cursor.moveToFirst();
				useAp = cursor.getInt(cursor.getColumnIndex("use_ap"));
				useCp = cursor.getInt(cursor.getColumnIndex("use_cp"));
				Typeid = cursor.getInt(cursor.getColumnIndex("type_id"));
				Type = E_ActionType.valueOf(cursor.getString(cursor.getColumnIndex("type")));
				TargetType = E_CastTargetType.valueOf(cursor.getString(cursor.getColumnIndex("targettype")));
			}finally{
				if(cursor!=null){
					cursor.close();
				}
			}
			DBClose();
			
			return new CastAction(User,Typeid,Type,useAp,useCp,TargetType);
		}
		default:
			DBClose();
			return null;
		}
		
		
	}
}
