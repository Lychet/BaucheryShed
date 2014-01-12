package jp.lychet.baucheryshed.battle;

import jp.lychet.enums.E_ActionType;


public class CastCancelAction extends BattleAction{
	private int UseAP;
	private BattleAction TargetAction;
	public CastCancelAction(BattleAction Action,int AP, BattleModule User){
		super(User,E_ActionType.CastCancel);
		TargetAction = Action;
		UseAP=AP/2;
		this.CastId=Action.getCastId();
	}
	@Override
	public void Action(BattleScene BS){
		User.useAp(UseAP);
		//BS.deleteCast(CastId);これはいらない。CastID持つ時点で自動で削除するから、ここで呼ぶと多重になる
	}
	@Override
	public void Feint(BattleScene BS){
		//TODO いるか？強すぎじゃないか？キャンセルした。→してませんでしたうぇｗｗｗｗとか……
		//Cpめっちゃ使うとかならありかなぁ。占拠量増大とか！それで隠す、なんかかっけぇ
	}
}
