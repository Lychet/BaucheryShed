package jp.lychet.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLBaseAdapter {
	
	
	protected String TableName;
	protected static SQLiteDatabase DB;
	protected static DatabaseHelper Helper;
	
	public SQLBaseAdapter(Context context){
		Helper = new DatabaseHelper(context);
	}
	
	public static void DBOpen(){
		DB = Helper.getReadableDatabase();//ここまででデータベースオープン
	}
	
	public static void DBClose(){
		DB.close();
	}
}
