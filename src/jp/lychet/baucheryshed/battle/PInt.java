package jp.lychet.baucheryshed.battle;

public class PInt{
	private int num;
	public PInt(int a){
		num=a;
	}
	static public int get(int a){
		if(a<0)a=0;
		return a;
	}
	static public int get(double a){
		if(a<0)a=0;
		return (int)a;
	}
	static public int get(float a){
		if(a<0)a=0;
		return (int)a;
	}
}
