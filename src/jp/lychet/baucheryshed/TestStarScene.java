package jp.lychet.baucheryshed;

import android.content.Context;

public class TestStarScene extends StarSelectScene{
	public TestStarScene(Context context,SceneBase superScene){
		super(context,superScene);
	}
	
	@Override
	protected void Action() {
		childScene=new TestIconScene(context,this);
		switch(MenuNum.get(0)){//0指定で現在の選択ナンバーを取得
		default:
		}
		
	}
	
	@Override
	public void callback(int i){
		childScene=null;
		str=""+i;
	}
}
