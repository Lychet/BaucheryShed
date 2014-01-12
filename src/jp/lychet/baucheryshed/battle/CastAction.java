package jp.lychet.baucheryshed.battle;

import jp.lychet.enums.E_ActionType;
import jp.lychet.enums.E_CastTargetType;
import jp.lychet.object.Skill;
import jp.lychet.sql.SQLSkillAdapter;


public class CastAction extends BattleAction{
	private int UseAP,UseCP;
	E_CastTargetType TargetType;
	E_ActionType CastedActionType;
	private int CastedActionId;
	public CastAction(BattleModule User,int Typeid,E_ActionType CastedType,int Ap,int Cp,E_CastTargetType Tar){
		super(User,E_ActionType.Cast);
		UseCP=Cp;
		UseAP=Ap;
		TargetType=Tar;
		CastedActionType = CastedType;
		CastedActionId = Typeid;
	}
	@Override
	public void Action(BattleScene BS){
		User.useAp(UseAP);
		
		//if(Target==User.getId())Target=User.SelectTarget();これはマルチアクション用だぬ。
		//TODO 対象決定処理(SELF以外)
		if(TargetType!=E_CastTargetType.SELF&&Target==User.getId())Target=User.SelectTarget();
		BattleAction Effect = SQLSkillAdapter.getAction(new Skill(0,CastedActionId,CastedActionType),BS.getModule(Target));
		//ここでnew Actionすることが大切。別のものはちゃんと別に生成しないといけないのだ。
		//SQLアダプタはちゃんと内部でnewしてくれる。
		Effect.setCp(Cp);
		switch(TargetType){
		case SELF:
			BS.setCast(Effect, User.getId(), User.getId(),UseAP);
			break;
		case SUPPORT:
			BS.setCast(Effect, Target, User.getId(),UseAP);
			break;
		case ENCHANT:
			BS.setCast(Effect, Target, Target,UseAP);
			break;
		case DRAIN:
			BS.setCast(Effect, User.getId(), Target,UseAP);
			break;
		}
	}
	
	@Override
	public void Feint(BattleScene BS) {
		User.useAp(1);
	}
	
}
