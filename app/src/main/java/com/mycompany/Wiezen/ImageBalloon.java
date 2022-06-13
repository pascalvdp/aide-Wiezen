package com.mycompany.Wiezen;
import android.widget.*;
import android.content.*;
import android.animation.*;
import android.util.*;
import android.view.*;
import android.graphics.*;

public class ImageBalloon extends FrameLayout
{
	ImageView iv;
	TextView tv;
 	Context context;
	ObjectAnimator rotate;
	final float schermbreedte=1280,schermhoogte=736;//recentste tablet breedte=1920 hoogte=1128
	float frameWidth,frameHeight;

	AnimatorSet set = new AnimatorSet();

	public ImageBalloon(Context context)
	{
		this(context, null, 0);
		iv.setBackgroundResource(R.drawable.textballoon);
	}

    public ImageBalloon(final Context context, AttributeSet attrs, int defStyle)
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
		params.width = (int) xVerh(265);//265
		params.height = (int) yVerh(55);//55
		addViewInLayout(iv, -1, params);
		setMeasuredDimension(params.width, params.height);

		tv = new TextView(context);
		tv.setText("Jouw beurt");
		//tv.setBackgroundColor(Color.RED);//test
		//tv.setTextAppearance(android.R.style.TextAppearance_Large); //style past kleur en size aan en ...,niet nodig
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX  , yVerh(30));
		tv.setTextColor(Color.BLACK);
		//params terug new maken
		params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;//CENTER
		addViewInLayout(tv, -1, params);
		
		frameWidth = getMeasuredWidth();
		frameHeight = getMeasuredHeight();

		iv.setEnabled(false);//=view kan niet aangeklikt worden
		//setClickable werkt anders=> als men een onclicklistener toepast dan wordt dit automatisch clickable=true
		setZ(10000);//???
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
