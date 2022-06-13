package com.mycompany.Wiezen;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.animation.*;
import android.view.ViewTreeObserver.*;
import java.util.*;
import android.os.*;

public class Card extends FrameLayout
{
	ImageView iv;
	ImageView ivShadow;
	static ImageView ivTemp;
	boolean visibleShadow=true;
 	Context context;//is enkel voor toast
	ObjectAnimator rotate;
	final float schermbreedte=1280,schermhoogte=736;//recentste tablet breedte=1920 hoogte=1128
	float frameWidth,frameHeight;
	int z;
	static int maxz=4;//,refz=maxz;
	boolean start,restart;
	boolean checkPoint,divide,bundle,deal;
	boolean choose,choose2;
 	boolean roundPlayed;
	boolean trumpCard,oldTrumpCard ,selected,selectable;
	float x=-1000,y=-1000;//richting waar men henen wil of waar men vertrekt
	int value, cardReference, category,sortingValue;
	static int dealer;
	static int counter=0;
	int testcounter;
	int player=-1;
	MainActivity mA;
	AnimatorSet set = new AnimatorSet();

	public Card(Context context, int value, int cardReference, int category, MainActivity mA)
	{
		this(context, null, 0);
		this.value = value;
		this.cardReference = cardReference;
		this.mA = mA;
		iv.setBackgroundResource(R.drawable.card_back);
		ivShadow.setBackgroundResource(R.drawable.shadow);
		this.category = category;

		int i=1;int z=category;
		while (z > 1)
		{i *= 8;z -= 1;}
		if (value > 20)
		{value -= 20;}
		sortingValue = value * category * i;
	}

    public Card(final Context context, AttributeSet attrs, int defStyle)
	{
        super(context, attrs, defStyle);
		this.context = context;
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		setLayoutParams(params);//dit is voor de framelayout		

		ivShadow = new ImageView(context);
		//params terug new maken
		params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		params.width = (int) xVerh(227);//260
		params.height = (int) yVerh(304);//348
		addViewInLayout(ivShadow, -1, params);
		setMeasuredDimension(params.width, params.height);

		frameWidth = getMeasuredWidth();
		frameHeight = getMeasuredHeight();

		iv = new ImageView(context);
		params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		params.width = (int) xVerh(180);//205
		params.height = (int) yVerh(257);//293
		addViewInLayout(iv, -1, params);

		iv.setEnabled(false);//=view kan niet aangeklikt worden
		//setClickable werkt anders=> als men een onclicklistener toepast dan wordt dit automatisch clickable=true
		setZ(5);//kaarten in gamedeck = 1 tot 4

		iv.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{		
					//toast("z=" + String.valueOf(getZ()));//maxz
					return true;//heeft enkel te maken met onclicklistener
				}
			});
    }

	public void setValueToStartValue()
	{
		if (value > 20)value -= 20;
	}

	public void showImage(boolean show)
	{
		if (show)
		{
			iv.setBackgroundResource(cardReference);
		}
		else
		{
			iv.setBackgroundResource(R.drawable.card_back);
		}
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


	public int random(int i)
	{//bvb i=2 dan keuze=1 of 2, i=0=error
		Random generator=new Random();
		int rndm = generator.nextInt(i) + 1;
		return rndm;
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
		if (duration == -1)duration = 1300;//1300
		set.setDuration(duration);//snel=500  2000 traag=5000 
		set.setStartDelay(startDelay);

		set.addPauseListener(new AnimatorSet.AnimatorPauseListener(){

				@Override
				public void onAnimationPause(Animator p1)
				{
					if (selected)
					{
						selected = false;
						setZ(z);
					}
				}

				@Override
				public void onAnimationResume(Animator p1)
				{
					//toast("resume...");
				}
		});
		
		animX.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener(){

				@Override
				public void onAnimationUpdate(ValueAnimator p1)
				{
					float anX=animX.getAnimatedValue();
					float anY=animY.getAnimatedValue();

					if (divide)
					{
						if (!visibleShadow)
						{
							if (anX > x &&  anX < x + 10 || anX == x)
							{
								ivShadow.setVisibility(View.INVISIBLE); 
							}
							if (anX < x &&  anX > x - 10)
							{
								ivShadow.setVisibility(View.INVISIBLE); 
							}
						}
						else
						{
							if (anX > x + 10 || anX < x - 10)
							{
								ivShadow.setVisibility(View.VISIBLE);
							}
						}
					}

					if (deal)
					{
						if (anX > x + 10 || anX < x - 10 || anY > y + 10 || anY < y - 10)
						{
							ivShadow.setVisibility(View.VISIBLE);
						}
					}
				}
			});

		set.addListener(new AnimatorSet.AnimatorListener(){

				@Override
				public void onAnimationStart(Animator anim)
				{
					counter = 0;
					if(start) restart=false;
					if (divide)
					{
						start = false;
					}
				}

				@Override
				public void onAnimationEnd(Animator p1)
				{
					if (roundPlayed)//voor gamedeck
					{
						roundPlayed = false;
						//toast("oldsttartplayer=" + String.valueOf( mA.game.oldStartPlayer));
						mA.game.oldLast4CardDeck.setCardsOutsideScreen(mA.game.oldStartPlayer);//oude laatste 4 kaarten wegdoen
						mA.game.last4CardDeck.setCardsTolast4CardDeck(mA.game.oldStartPlayer,0.4f);//0
						for (int a=1;a <= mA.game.last4CardDeck.size();a++)mA.game.last4CardDeck.card(a).setZ(a-5);//z=-1 tot -4
						if(!mA.game.gameEnd)mA.playCard(true);
					}

					if (oldTrumpCard)
					{
						oldTrumpCard = false;
						mA.playCard(true);
					}
					if (selected)
					{
						selected = false;
						setZ(z);
						mA.playCard(false);
					}
					
					if (deal)
					{
						deal = false;
						choose = true;
					}
		
					if(restart)
					{
						restart=false;
						counter++;
						testcounter = counter;//dit is enkel om te testen
						if (counter == 52)//52
						{//op deze manier moeten programmeren
							mA.cntDTimerDealAgain.start();
						}
					}

					if (choose)
					{
						counter++;
						testcounter = counter;
						if (counter == 51)//51
						{
							iv.performClick();
						}
						choose = false;
					}

					if (bundle)
					{
						bundle = false;
						deal = true;
					}
					if (divide)
					{
						x = xVerh(schermbreedte / 2);
						y = getY(); // getYY();
						moveAndScaleTo(x, y , 0, -1, 0, 1);
						divide = false;
						bundle = true;			
					}
					if (start)
					{
						counter++;
						testcounter = counter;
						//if ((counter > 48 && counter < 53) || counter > 96) toast(String.valueOf(counter));
						//if(counter>30)  toast(String.valueOf(counter));//
						if (dealer == 1)
						{
							iv.setEnabled(true);
						}
						else
						{
							if (checkPoint)
							{
								ivTemp = iv;
							}
							if (counter == 52)
							{
								ivTemp.performClick();//ga naar onClickListenerDeck()
							}
						}

					}

				}

				@Override
				public void onAnimationCancel(Animator p1)
				{
					//toast("cancel moveto");
				}

				@Override
				public void onAnimationRepeat(Animator p1)
				{
					// TODO: Implement this method
				}
			});
		set.start();//start hier zetten, anders werkt onAnimationStart niet
	}	

	public void setAndScaleTo(float Xpos, float Ypos, float rotation, float scaleFactor)
	{//bij scale wijzigd de hoogte en de breedte niet!!!
		setX(Xpos);
		setY(Ypos);
		setRotation(rotation);
		setScaleX(scaleFactor);
		setScaleY(scaleFactor);
	}
	
	public ImageView getImageView()
	{
		return iv;
	}

	public ImageView getShadowView()
	{
		return ivShadow;
	}

	public String getStringValue()
	{
		switch (value)
		{
			case 10:
				return "t";
			case 11:
				return "b";
			case 12:
				return "v";	
			case 13:
				return "k";	
			case 14:
				return "a";	
		}
		return String.valueOf(value);
	}

	public void toast(String pp)
	{
		Toast.makeText(context, pp, Toast.LENGTH_SHORT).show();
	}
}
