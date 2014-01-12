package jp.lychet.baucheryshed;


public class SelectIcon{
	private String Title,Info;
	private int Iconid;
	public SelectIcon(String Title,String Info,int Iconid){
		this.Title=Title;
		this.Iconid=Iconid;
		this.Info=Info;
	}
	public String getTitle(){
		return Title;
	}
	public int getIcon(){
		return Iconid;
	}
}