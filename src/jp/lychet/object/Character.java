package jp.lychet.object;

import jp.lychet.enums.E_Status;

public class Character {
	//パラメータ部分
	protected int POW,VIT,MAG,CAP,QUI,DEX;//プレイヤーの見えるステータス
	//これとスキルごとで計算してスキルの能力を決定…スキルが複雑なのか！
	
	public Character(){
		POW=10;
		VIT=10;
		MAG=10;
		CAP=10;
		QUI=10;
		DEX=10;
	}
	
	public int getParameter(E_Status status){
		switch(status){
		case POWER:
			return POW;
		case VITAL:
			return VIT;
		case MAGIC:
			return MAG;
		case CAPACITY:
			return CAP;
		case QUICK:
			return QUI;
		case DEXTEROUS:
			return DEX;
		}
		return 0;
	}
	
	public void setParameter(E_Status status,int point){
		switch(status){
		case POWER:
			POW = point;
			return;
		case VITAL:
			VIT = point;
			return;
		case MAGIC:
			MAG = point;
			return;
		case CAPACITY:
			CAP = point;
			return;
		case QUICK:
			QUI = point;
			return;
		case DEXTEROUS:
			DEX = point;
			return;
		}
		return;
	}

}
