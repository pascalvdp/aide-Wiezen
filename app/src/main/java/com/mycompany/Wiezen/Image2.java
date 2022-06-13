package com.mycompany.Wiezen;
import android.widget.*;
import android.content.*;
import android.animation.*;
import android.util.*;
import android.view.*;

public class Image2 extends FrameLayout
{
	ImageView iv;
	Game game;
 	Context context;
	ObjectAnimator rotate;
	final float schermbreedte=1280,schermhoogte=736;//recentste tablet breedte=1920 hoogte=1128
	float frameWidth,frameHeight;

	AnimatorSet set = new AnimatorSet();

	public Image2(Context context, int drawableReference)
	{
		this(context, null, 0);
		iv.setBackgroundResource(drawableReference);//R.drawable.dealer
		switch (drawableReference)
		{
			case R.drawable.dealer://andere drawables worden in "setBackground" gemaakt
				setTooltipText("Dit is de deler!");
				break;
			default:
				setTooltipText("Fout in programma!");
				break;
		}
	}

	public Image2(Context context, Game game)
	{
		this(context, null, 0);
		this.game = game;
	}

    public Image2(final Context context, AttributeSet attrs, int defStyle)
	{
        super(context, attrs, defStyle);
		this.context = context;
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		setLayoutParams(params);//dit is voor de framelayout		

		//params terug new maken
		iv = new ImageView(context);
		params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		params.width = (int) xVerh(65);//65
		params.height = (int) yVerh(55);//55
		addViewInLayout(iv, -1, params);
		setMeasuredDimension(params.width, params.height);

		frameWidth = getMeasuredWidth();
		frameHeight = getMeasuredHeight();

		iv.setEnabled(false);//=view kan niet aangeklikt worden
		//setClickable werkt anders=> als men een onclicklistener toepast dan wordt dit automatisch clickable=true
		setZ(-1);//kaarten in gamedeck = 1 tot 4
    }

	@Override
	public void setX(float x)
	{
		x = x - frameWidth / 2;
		super.setX(x);
	}

	@Override
	public void setY(float y)
	{
		y = y - frameHeight / 2;
		super.setY(y);
	}

	@Override
	public float getX()
	{
		// TODO: Implement this method
		return super.getX() + frameWidth / 2;
	}

	@Override
	public float getY()
	{
		// TODO: Implement this method
		return super.getY() + frameHeight / 2;
	}

	private float xVerh(float x)
	{
		float screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		float xVerh=screenWidth / schermbreedte;
		float xV =x * xVerh;
		return xV;
	}

	private float yVerh(float y)
	{
		float screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
		float yVerh=screenHeight / schermhoogte;
		float yV =y * yVerh;
		return yV;
	}

	public void setBackground()
	{
		switch (game.trumpCategory)
		{
			case 0:
				iv.setBackgroundResource(R.drawable.symbolnotrump);
				setTooltipText("Er is geen troef!");
				break;
			case 1:
				iv.setBackgroundResource(R.drawable.symbolhearts);
				setTooltipText("Troef is harten!");
				break;
			case 2:
				iv.setBackgroundResource(R.drawable.symbolclubs);
				setTooltipText("Troef is klaveren!");
				break;
			case 3:
				iv.setBackgroundResource(R.drawable.symboldiamonds);
				setTooltipText("Troef is koeken!");
				break;
			case 4:
				iv.setBackgroundResource(R.drawable.symbolspades);
				setTooltipText("Troef is schuppen!");
				break;
			default:
				setTooltipText("Fout in programma!");
				break;
		}
	}

	public void moveAndScaleTo(float Xpos, float Ypos, float rotation, long duration, long startDelay, float scaleFactor)
	{//bij scale wijzigd de hoogte en de breedte niet!!!
		set = new AnimatorSet();//final
		final ObjectAnimator animX=ObjectAnimator.ofFloat(this, View.X, Xpos);
		final ObjectAnimator animY=ObjectAnimator.ofFloat(this, View.Y, Ypos);
		rotate = ObjectAnimator.ofFloat(this, View.ROTATION, rotation);//90
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, View.SCALE_X, scaleFactor);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y, scaleFactor);
		set.play(animY).with(animX).with(rotate).with(scaleX).with(scaleY);
		if (duration == -1)duration = 1300;//2000
		set.setDuration(duration);//snel=500  2000 traag=5000 
		set.setStartDelay(startDelay);
		set.start();//start hier zetten, anders werkt onAnimationStart niet
	}	

	public ImageView getImageView()
	{
		return iv;
	}

	public void toast(String pp)
	{
		Toast.makeText(context, pp, Toast.LENGTH_SHORT).show();
	}
}
