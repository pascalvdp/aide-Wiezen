package com.mycompany.Wiezen;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.graphics.*;
import java.util.*;
import android.widget.AdapterView.*;

public class ChoiceList extends FrameLayout implements Choices
{
	Context context;
	Game game;
	int choice;
	MainActivity mA;
	ListView lv;
	View vie;
	Button but;
	Map<String, Integer> choicesMap = new HashMap<String, Integer>();
	final ArrayList<User> arrayOfUsers = new ArrayList<User>();
	int pos=-1;
	float frameWidth,frameHeight;
	final float schermbreedte=1280,schermhoogte=736;//recentste tablet breedte=1920 hoogte=1128

	public ChoiceList(Context context, Game game, MainActivity mA)
	{
		this(context, null, 0, game);	
		this.mA = mA;
		this.context = context;
	}
	public ChoiceList(final Context context, AttributeSet attrs, int defStyle, Game game)
	{
        super(context, attrs, defStyle);
		//this.context = context;
		this.game = game;
		//this.mA=mA;
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;
		params.width = (int) xVerh(500);//380
		params.height = (int) yVerh(300);
		setMeasuredDimension(params.width, params.height);//params.width
		setLayoutParams(params);//dit is voor de framelayout	

		//setBackgroundColor(Color.RED);
		lv = new ListView(context);

		//params terug new maken
		params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addViewInLayout(lv, -1, params);

		frameWidth = getMeasuredWidth();
		frameHeight = getMeasuredHeight();

		setChoicesMap();
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

	public void setList()
	{
		arrayOfUsers.clear();
		game.choice = SOLOSLIM;
		//if(alleen){choice=ALLEEN;maxChoice=PAS;}
		int m=0;
		if (game.maxChoice == OPENMISERE || game.maxChoice == MISERE)
		{m = 1;}
		while (game.choice > game.maxChoice - m)
		{	
			switch (game.choice)
			{
				case SOLOSLIM:
					arrayOfUsers.add(new User(soloslim, soloslimHelp, soloslimToolTip));
					break;
				case SOLO:
					arrayOfUsers.add(new User(solo, soloHelp, soloToolTip));
					break;
				case OPENMISERE:
					arrayOfUsers.add(new User(openmisere, openmisereHelp, openmisereToolTip));
					break;	
				case TROELMEE:
					if (game.troelMeePlayer == 1 && game.maxChoice == TROEL)
					{arrayOfUsers.add(new User(troelmee, troelmeeHelp, troelmeeToolTip));}
					break;	
				case TROEL:
					if (game.troelPlayer == 1)
					{
						game.maxChoice = TROEL;//dit is om geen lagere keuzes mogelijk te maken
						arrayOfUsers.add(new User(troel, troelHelp, troelToolTip));
					}
					break;	
				case DANSEN12INTROEF:
					arrayOfUsers.add(new User(dansen12introef, dansen12introefHelp, dansen12introefToolTip));
					break;		
				case DANSEN12:
					arrayOfUsers.add(new User(dansen12, dansen12Help, dansen12ToolTip));
					break;	
				case DANSEN11INTROEF:
					arrayOfUsers.add(new User(dansen11introef, dansen11introefHelp, dansen11introefToolTip));
					break;		
				case DANSEN11:
					arrayOfUsers.add(new User(dansen11, dansen11Help, dansen11ToolTip));
					break;		
				case DANSEN10INTROEF:
					arrayOfUsers.add(new User(dansen10introef, dansen10introefHelp, dansen10introefToolTip));
					break;		
				case DANSEN10:
					arrayOfUsers.add(new User(dansen10, dansen10Help, dansen10ToolTip));
					break;	
				case MISERE:
					arrayOfUsers.add(new User(misere, misereHelp, misereToolTip));
					break;
				case DANSEN9INTROEF:
					arrayOfUsers.add(new User(dansen9introef, dansen9introefHelp, dansen9introefToolTip));
					break;		
				case DANSEN9:
					arrayOfUsers.add(new User(dansen9, dansen9Help, dansen9ToolTip));
					break;	
				case MEEGAAN:
					if (game.maxChoice == game.VRAAG)
					{arrayOfUsers.add(new User(meegaan, meegaanHelp, meegaanToolTip));}
					break;
				case VRAAG:
					if (game.dealer != 1)
					{arrayOfUsers.add(new User(vraag, vraagHelp, vraagToolTip));}
					break;	
				case ALLEEN:
					if (game.dealer == 1)
						arrayOfUsers.add(new User(alleen, alleenHelp, alleenToolTip));
					break;
			}
			game.choice--;
		}
		if (game.troelPlayer != 1 && (game.troelMeePlayer != 1 || (game.troelMeePlayer == 1 && game.maxChoice != TROEL)) || game.maxChoice > TROELMEE)
		{arrayOfUsers.add(new User(pas, pasHelp, pasToolTip));}

		UsersAdapter adapter = new UsersAdapter(context, arrayOfUsers);

		lv.setAdapter(adapter);

		if (pos == -1)
		{
			lv.setStackFromBottom(true);//start lijst onderaan
		}
		else
		{
			lv.setSelection(pos);		
		}

		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{  
					pos = lv.getFirstVisiblePosition();	
					setChoice(choicesMap.get(arrayOfUsers.get(p3).getChoice()));
				}
			});
		lv.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					String str=arrayOfUsers.get(p3).getToolTip();
					if (str != null)
					{
						toast(str);
					}
					return true;
				}
			});

	}

	private void setChoice(int choice)
	{
		if (choice == TROELMEE)
		{
			for (int aa=1;aa <= 4;aa++)
			{
				if (game.playerChoice[aa] == TROEL)
				{game.playerTrumpCategory[game.player] = game.playerTrumpCategory[aa];break;}
			}
		}

		boolean goToNextList=false;
		if (choice == DANSEN9 || choice == DANSEN10 || choice == DANSEN11 || choice == DANSEN12 || choice == SOLO)
		{goToNextList = true;}
		if (goToNextList)
		{
			this.choice = choice;
			setListCategory();
		}
		else
		{
			setVisibility(View.INVISIBLE);
			game.playerChoice[game.player] = choice;
			if (choice != 0)
			{game.maxChoice = choice;}//indien geen pas dan maxchoice vastleggen
			if (choice == DANSEN9INTROEF || choice == DANSEN10INTROEF || choice == DANSEN11INTROEF || choice == DANSEN12INTROEF || choice == SOLOSLIM)
			{
				game.playerTrumpCategory[game.player] = game.trumpCategory;
			}
			mA.continueWithStartChoice();
		}
	}

	private void setListCategory()
	{
		arrayOfUsers.clear();
		if (game.trumpCategory != 1)arrayOfUsers.add(new User("1", "Harten (♡)"));
		if (game.trumpCategory != 2)arrayOfUsers.add(new User("2", "Klaveren (♧)"));
		if (game.trumpCategory != 3)arrayOfUsers.add(new User("3", "Koeken (◇)"));
		if (game.trumpCategory != 4)arrayOfUsers.add(new User("4", "Schuppen (♤)"));
		arrayOfUsers.add(new User("《TERUG", "Opnieuw kiezen"));
		UsersAdapter adapter = new UsersAdapter(context, arrayOfUsers);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					setChoiceAndCategory(choicesMap.get(arrayOfUsers.get(p3).getChoice()));
				}

			});
	}

	private void setChoiceAndCategory(int choice)
	{
		if (choice == TERUG)
		{
			setList();
		}
		else
		{
			setVisibility(View.INVISIBLE);
			game.playerChoice[game.player] = this.choice;
			if (this.choice != 0)
			{game.maxChoice = this.choice;}//indien geen pas dan maxchoice vastleggen
			game.playerTrumpCategory[game.player] = choice - 100;
			mA.continueWithStartChoice();
		}
	}

	private void setChoicesMap()
	{
		choicesMap.put("SOLOSLIM", Choices.SOLOSLIM);
		choicesMap.put("SOLO", Choices.SOLO);
		choicesMap.put("OPENMISERE", Choices.OPENMISERE);
		choicesMap.put("TROELMEE", Choices.TROELMEE);
		choicesMap.put("TROEL", Choices.TROEL);
		choicesMap.put("DANSEN12INTROEF", Choices.DANSEN12INTROEF);
		choicesMap.put("DANSEN12", Choices.DANSEN12);
		choicesMap.put("DANSEN11INTROEF", Choices.DANSEN11INTROEF);
		choicesMap.put("DANSEN11", Choices.DANSEN11);
		choicesMap.put("DANSEN10INTROEF", Choices.DANSEN10INTROEF);
		choicesMap.put("DANSEN10", Choices.DANSEN10);
		choicesMap.put("MISERE", Choices.MISERE);
		choicesMap.put("DANSEN9INTROEF", Choices.DANSEN9INTROEF);
		choicesMap.put("DANSEN9", Choices.DANSEN9);
		choicesMap.put("MEEGAAN", Choices.MEEGAAN);
		choicesMap.put("VRAAG", Choices.VRAAG);
		choicesMap.put("ALLEEN", Choices.ALLEEN);
		choicesMap.put("PAS", Choices.PAS);

		choicesMap.put("1", Choices.HARTEN);
		choicesMap.put("2", Choices.KLAVEREN);
		choicesMap.put("3", Choices.KOEKEN);
		choicesMap.put("4", Choices.SCHUPPEN);
		choicesMap.put("《TERUG", Choices.TERUG);
	}
}
