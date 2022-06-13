package com.mycompany.Wiezen;
import android.widget.*;
import android.content.*;
import android.animation.*;
import android.util.*;
import android.view.*;
import android.preference.*;

public class CheckBoxView extends FrameLayout
{
	Context context;
	CheckBox checkBox;
	float frameWidth,frameHeight;
	final float schermbreedte=1280,schermhoogte=736;//recentste tablet breedte=1920 hoogte=1128
	AnimatorSet set;
	public CheckBoxView(Context context)
	{
		this(context, null, 0);

	}
	public CheckBoxView(final Context context, AttributeSet attrs, int defStyle)
	{
        super(context, attrs, defStyle);
		this.context = context;
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.width = (int) xVerh(680);//680
		params.height = (int) yVerh(100);//100
		setMeasuredDimension(params.width, params.height);
		setLayoutParams(params);//dit is voor de framelayout	

		//setBackgroundColor(Color.RED);
		checkBox = new CheckBox(context);
		checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, yVerh(22));

		//params terug new maken
		params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addViewInLayout(checkBox, -1, params);

		frameWidth = getMeasuredWidth();
		frameHeight = getMeasuredHeight();
	}

	public void setText(String str)
	{
		checkBox.setText(str);
	}

	public void setTextColor(int color)
	{
		checkBox.setTextColor(color);
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
		return super.getX() + frameWidth / 2;
	}

	@Override
	public float getY()
	{
		return super.getY() + frameHeight / 2;
	}

	public void toast(String pp)
	{
		Toast.makeText(context, pp, Toast.LENGTH_SHORT).show();
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

	public void moveTo(float Xpos, float Ypos, float rotation, long duration, long startDelay)
	{//niet in gebruik
		set = new AnimatorSet();
		final ObjectAnimator animX=ObjectAnimator.ofFloat(this, View.X, Xpos);
		final ObjectAnimator animY=ObjectAnimator.ofFloat(this, View.Y, Ypos);
		ObjectAnimator rotate = ObjectAnimator.ofFloat(this, View.ROTATION, rotation);//90
		set.play(animY).with(animX).with(rotate);
		if (duration == -1)duration = 1300;//2000
		set.setDuration(duration);//snel=500  2000 traag=5000 
		set.setStartDelay(startDelay);
		set.start();
	}	

	public CheckBox getcheckBox()
	{
		return checkBox;
	}

}
