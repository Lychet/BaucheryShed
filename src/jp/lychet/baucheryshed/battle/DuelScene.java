package jp.lychet.baucheryshed.battle;

import jp.lychet.baucheryshed.SceneBase;
import android.content.Context;

public class DuelScene extends BattleScene{
	public DuelScene(Context context,SceneBase superScene){
		super(context,superScene);
		Module.add(new BattleModule(this,0));
		Module.add(new BattleModule(this,1));
		DrawStr = new String[Module.size()];
		for(int i=0;i<Module.size();i++){
			DrawStr[i]="";
		}
	}
	
	@Override
	public void Encount(){
	}
}
