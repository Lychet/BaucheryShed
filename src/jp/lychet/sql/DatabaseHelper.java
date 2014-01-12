package jp.lychet.sql;

import jp.lychet.enums.E_ActionType;
import jp.lychet.enums.E_CastTargetType;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	final static String FileName = "Temp.db";
	final static int VERSION = 3;
	
	public DatabaseHelper(Context context) {
		super(context, FileName , null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//データが存在しないときの処理
		db.execSQL("create Table SkillTable("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
				"name TEXT NOT NULL,"+
				"type TEXT NOT NULL,"+
				"type_id INTEGER NOT NULL"+
				");"
				);
		db.execSQL("create Table AttackSkillTable("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
				"use_ap INTEGER NOT NULL,"+
				"ipower REAL NOT NULL,"+
				"idamage REAL NOT NULL,"+
				"mpower REAL NOT NULL,"+
				"mdamage REAL NOT NULL"+
				");"
				);
		db.execSQL("create Table DefenceSkillTable("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
				"use_ap INTEGER NOT NULL,"+
				"ipower REAL NOT NULL,"+
				"idamage REAL NOT NULL,"+
				"mpower REAL NOT NULL,"+
				"mdamage REAL NOT NULL"+
				");"
				);
		db.execSQL("create Table CastSkillTable("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
				"use_ap INTEGER NOT NULL,"+
				"use_cp INTEGER NOT NULL,"+
				"type TEXT NOT NULL,"+
				"targettype TEXT NOT NULL,"+
				"type_id INTEGER NOT NULL"+
				");"
				);
		//NormalAttack追加
		ContentValues val = new ContentValues();
		val.put("use_ap", 1);
		val.put("ipower", 1);
		val.put("idamage", 1);
		val.put("mpower", 0);
		val.put("mdamage", 0);
		db.insert("AttackSkillTable",null,val);
		
		val = new ContentValues();
		val.put("name", "NormalAttack");
		val.put("type", E_ActionType.Attack.toString());
		Cursor cursor = null;
		int Type_id = 0;
		try{
			cursor = db.rawQuery("select max(id) as id from AttackSkillTable", null);
			cursor.moveToFirst();
			Type_id=cursor.getInt(cursor.getColumnIndex("id"));	
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		val.put("id", 1);
		val.put("type_id", Type_id);
		db.insert("SkillTable",null,val);
		
		//NormalCast追加
		val = new ContentValues();
		val.put("use_ap", 1);
		val.put("use_cp", 1);
		val.put("type", E_ActionType.Attack.toString());
		val.put("targettype", E_CastTargetType.SELF.toString());
		val.put("type_id", Type_id);
		db.insert("CastSkillTable",null,val);
		
		val = new ContentValues();
		val.put("name", "NormalCast");
		val.put("type", E_ActionType.Cast.toString());
		try{
			cursor = db.rawQuery("select max(id) as id from CastSkillTable", null);
			cursor.moveToFirst();
			Type_id=cursor.getInt(cursor.getColumnIndex("id"));	
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		val.put("type_id", Type_id);
		db.insert("SkillTable",null,val);		
		
		//NormalBlock追加
		val = new ContentValues();
		val.put("use_ap", 1);
		val.put("ipower", 1);
		val.put("idamage", 1);
		val.put("mpower", 0);
		val.put("mdamage", 0);
		db.insert("DefenceSkillTable",null,val);
		
		val = new ContentValues();
		val.put("name", "NormalBlock");
		val.put("type", E_ActionType.Defence.toString());
		try{
			cursor = db.rawQuery("select max(id) as id from DefenceSkillTable", null);
			cursor.moveToFirst();
			Type_id=cursor.getInt(cursor.getColumnIndex("id"));	
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
		val.put("type_id", Type_id);
		db.insert("SkillTable",null,val);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//バージョン変更の処理
		db.execSQL("drop table if exists SkillTable;");
		db.execSQL("drop table if exists AttackSkillTable;");
		db.execSQL("drop table if exists DefenceSkillTable;");
		db.execSQL("drop table if exists CastSkillTable;");
		onCreate(db);
		
	}
	
}
