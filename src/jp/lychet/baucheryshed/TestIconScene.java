package jp.lychet.baucheryshed;

import java.util.ArrayList;

import android.content.Context;

public class TestIconScene extends IconSelectScene{

	public TestIconScene(Context context,SceneBase superScene){
		super(context,superScene);
		
		//-------------------
		//選択肢作成の必須手順
		IconInfo = new ArrayList<SelectIcon>();
		for(int i=0;i<70;i++){
			IconInfo.add(new SelectIcon(""+i,"",0));
		}
		BlockNum=(IconInfo.size()-1)/(rowNum*columnNum)+1;
		block = new Block[BlockNum];
		for(int i=0;i<BlockNum;i++){
			block[i]=new Block(i);
		}
		//--------------------
	}

	@Override
	protected void Action() {
		superScene.callback(nowIcon);
	}


}
