package jp.lychet.baucheryshed;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapManager{
	private static BitmapManager instance = null;
	private Resources r;
	Bitmap [] icon;
	private BitmapManager(Context context){
		r=context.getResources();
		icon = new Bitmap[1];
		icon[0]= BitmapFactory.decodeResource(r,R.drawable.iconbase);
	}
	
	public static BitmapManager getInstance(Context context){
		if(instance == null){
			instance = new BitmapManager(context);
		}
		return instance;
	}
	
	public Bitmap getIcon(int i){
		try{
			return icon[i];
		}catch(ArrayIndexOutOfBoundsException e){
			return icon[0];
		}
	}
}
