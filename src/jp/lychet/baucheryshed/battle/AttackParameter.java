package jp.lychet.baucheryshed.battle;

import jp.lychet.enums.E_Element;

public class AttackParameter {
	protected int IPower,IDamage,MPower,MDamage,Speed;
	protected E_Element Element;
	
	public AttackParameter(int IPower,int IDamage,int Speed){
		this.IPower=IPower;
		this.IDamage=IDamage;
		this.MPower=0;
		this.MDamage=0;
		this.Speed=Speed;
		this.Element=E_Element.Neutral;
	}
	public AttackParameter(int IPower,int IDamage,int MPower,int MDamage,int Speed,E_Element Element){
		this.IPower=IPower;
		this.IDamage=IDamage;
		this.MPower=MPower;
		this.MDamage=MDamage;
		this.Speed=Speed;
		this.Element=Element;
	}
	public DamageResult Calc(AttackParameter Def){
		int Lp,Ap;
		double HitPoint=1-(Def.Speed/this.Speed);
		double ElementPoint=ElementAffinity(Def.Element);
		if(HitPoint<0){
			Lp=0;
			Ap=0;
			return new DamageResult(Lp,Ap);
		}
		Ap=PInt.get(((double)this.IPower/Def.IPower+this.MPower*ElementPoint/Def.MPower)*HitPoint);
		HitPoint=1-(Def.Speed*1.2/this.Speed);
		Lp=PInt.get(this.IDamage*HitPoint-Def.IDamage)+PInt.get(this.MDamage*HitPoint*ElementPoint-Def.MDamage);
		return new DamageResult(Lp,Ap);
	}
	
	private double ElementAffinity(E_Element Def){
		switch(this.Element){
		case Jupiter:
			switch(Def){
			case Mars:
				return 0.5;
			case Saturn:
				return 2.0;
			default:
				return 1.0;
			}
		case Mars:
			switch(Def){
			case Saturn:
				return 0.5;
			case Venus:
				return 2.0;
			default:
				return 1.0;
			}
		case Mercury:
			switch(Def){
			case Jupiter:
				return 0.5;
			case Mars:
				return 2.0;
			default:
				return 1.0;
			}
		case Neutral:
			return 0;
		case Saturn:
			switch(Def){
			case Mercury:
				return 2.0;
			case Venus:
				return 0.5;
			default:
				return 1.0;
			}
		case Venus:
			switch(Def){
			case Jupiter:
				return 2.0;
			case Mercury:
				return 0.5;
			default:
				return 1.0;
			}
		default:
			return 1.0;
		
		}
	}
}
