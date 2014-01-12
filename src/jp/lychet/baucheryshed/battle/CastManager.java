package jp.lychet.baucheryshed.battle;

import java.util.ArrayList;


public class CastManager{
	private static int CastCount = 0;
	private CastInfoList CastList;
	
	CastManager(){
		CastList = new CastInfoList();
	}
	
	private class CastInfoList extends ArrayList<CastInfomation>{
		
		public int indexOfById(int Id){
			for(int i=0;i<super.size();i++){
				if(super.get(i).CheckEquals(Id))return i;
			}
			return -1;
		}
		
		@Override
		public void clear(){
			CastCount = 0;
			super.clear();
		}
		
	}
	
	private class CastInfomation{
		protected int CastId,Caster,Target;
		CastInfomation(int Caster,int Target){
			CastCount++;
			CastId = CastCount;//術の通し番号、識別用ID
			this.Caster = Caster;//Cpを使用して術を保持しているモジュールのID
			this.Target = Target;//術の効果を受けているモジュールのID
		}
		
		public boolean CheckEquals(int id){
			return id==CastId;
		}
		
		public int getCaster(){return Caster;}
		public int getTarget(){return Target;}
		public int getId(){return CastId;}
		
	}
	
	public int add(int Caster,int Target){//追加する際に識別IDを得る
		CastList.add(new CastInfomation(Caster,Target));
		return CastCount;
	}
	
	public int getIndexById(int CastId){
		return CastList.indexOfById(CastId);
	}
	
	public int getCaster(int index){//-1なら見つからなかった
		try{
			CastInfomation Result = CastList.get(index);
			return Result.getCaster();
		}catch(IndexOutOfBoundsException e){
			return -1;
		}
	}
	
	public int getTarget(int index){
		try{
			CastInfomation Result = CastList.get(index);
			return Result.getTarget();
		}catch(IndexOutOfBoundsException e){
			return -1;
		}
	}
	
	public int remove(int index){
		return CastList.remove(index).getId();
	}
	
}
