package com.mycompany.Wiezen;
//tablet 10inch=mdpi layout-sw720dp
//tablet 7inch=hdpi  layout-sw600dp
//gsm=xhdpi en xxhdpi layout-sw320dp

//wat is uitgeschakeld indien toch nog iets testen
//regel 1341 game.playerDeck[a].setCardsEnabled(true);//uitgeschakeld
//regel 1345 mButtonStartChoice.setVisibility(View.VISIBLE);//uitgeschakeld
//regel 126 textViewPlayer[a].getTextView().setOnLongClickListener( is uitgeschakeld
//regel 982 game.deck.setCardsEnabled(false);is bijgezet

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;
import java.io.*;
import android.database.*;
import android.database.sqlite.*;
import android.content.*;
import android.widget.RadioGroup.*;
import android.graphics.*;


public class MainActivity extends Activity
{
	private static final int DIALOG_ALLEEN=1;
	
	Database mDb;
	Game game;
	Intent mServiceIntent;
	
	Button mButtonNewGame,mButtonGame;
	TextViewPlayer[] textViewPlayer =new TextViewPlayer[5];
	ButtonView mButtonStartChoice,mButtonStartPlay;
	CheckBoxView mCheckBoxAnimationDealingCards;
	ChoiceList mChList;
	Image2 mImage2Dealer,mImage2Trump;
	ImageBalloon mImageBalloon;

	final float schermbreedte=1280,schermhoogte=736;//recentste tablet breedte=1920 hoogte=1128
	View ll;
	TextView tv1;

	View vi,viStart,viText1,viTextViewPlayers;
	RelativeLayout linear;
	Person[] person=new Person[5];
	boolean mainMenu=true,chooseMenu,playMenu;
	boolean getOn;//doorgaan
	boolean animationCards;//animatie kaarten van speler1
	boolean yourTurn;//wordt niet opgeslagen
	ProgressDialog progressDialog;
	CountDownTimer cntDTimer1,cntDTimer2;
	CountDownTimer cntDTimerDealAgain;
	CountDownTimer cntDTimer3;
	CountDownTimer cntDTimerYourTurn;
	CountDownTimer cntDTimerImage2Dealer;

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState); 
		// remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);		 
        setContentView(R.layout.main);

		linear = findViewById(R.id.main); 
		viStart = getLayoutInflater().inflate(R.layout.startmain, null);
		linear.addView(viStart);

		viText1 = getLayoutInflater().inflate(R.layout.text1, null);
		linear.addView(viText1);
		viText1.setVisibility(View.INVISIBLE);

		vi = findViewById(R.id.main);
		vi.setBackgroundResource(R.drawable.background1);

		tv1 = findViewById(R.id.textView1);
		viText1.setY(yVerh(-230));//-130

		mDb = new Database(this, false);
		mDb.open();
		//mDb.setActiveName("save2");extra spel indien nodig

		game = new Game(this);
		mServiceIntent = new Intent(this, MyService.class);
		
		for (int a=1;a <= 4;a++)
		{
			person[a] = new Person(this);
			person[a].name = "speler" + String.valueOf(a);
		}
		//main 
		mButtonNewGame = findViewById(R.id.buttonnewgame);
		mButtonGame = findViewById(R.id.buttongame);
		//choosemain
		for (int a=1;a <= 4;a++)
		{
			textViewPlayer[a] = new TextViewPlayer(this);
			textViewPlayer[a].setVisibility(View.INVISIBLE);
			textViewPlayer[a].setText(person[a].name);
			linear.addView(textViewPlayer[a]);
		}

		textViewPlayer[1].setX(xVerh(schermbreedte / 2));//435
		textViewPlayer[1].setY(yVerh(570));//545
		textViewPlayer[2].setX(xVerh(150));//150
		textViewPlayer[2].setY(yVerh(schermhoogte / 2));//200
		textViewPlayer[3].setX(xVerh(schermbreedte / 2));//575
		textViewPlayer[3].setY(yVerh(160));//135
		textViewPlayer[4].setX(xVerh(1130));//1065
		textViewPlayer[4].setY(yVerh(schermhoogte / 2));//450

		textViewPlayer[2].setRotation(90);
		textViewPlayer[4].setRotation(-90);
		
		for (int c=1;c <= 4;c++)
		{
			final int a=c;
			textViewPlayer[a].getTextView().setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{//uitgeschakeld
//					boolean show=true;
//					if(a==1)
//					{
//						if(game.playerDeck[2].showCards || game.playerDeck[3].showCards || game.playerDeck[4].showCards)
//						{
//							show=false;
//						}
//						if(show)
//						{
//							game.playerDeck[2].moveCardsToPlayer(2,-1,0);
//							game.playerDeck[3].moveCardsToPlayer(3,-1,0);
//							game.playerDeck[4].moveCardsToPlayer(4,-1,0);
//						}
//						else
//						{
//							game.playerDeck[2].moveCardsOutsideScreen(2,-1,0);
//							game.playerDeck[3].moveCardsOutsideScreen(3,-1,0);
//							game.playerDeck[4].moveCardsOutsideScreen(4,-1,0);
//						}
//					}
//					else
//					{
//						if(game.playerDeck[a].showCards)
//						{
//							game.playerDeck[a].moveCardsOutsideScreen(a,-1,0);
//						}
//						else
//						{
//							game.playerDeck[a].moveCardsToPlayer(a,-1,0);
//						}
//					}
					return false;
				}
			});
		}

		mButtonStartChoice = new ButtonView(this);
		mButtonStartChoice.setVisibility(View.INVISIBLE);
		mButtonStartChoice.setText("Start keuze/Enable cards");
		linear.addView(mButtonStartChoice);

		mButtonStartChoice.setX(xVerh(920));//1085
		mButtonStartChoice.setY(yVerh(350));//368
		mButtonStartChoice.setRotation(90);//90

		mButtonStartPlay = new ButtonView(this);
		mButtonStartPlay.setVisibility(View.INVISIBLE);
		linear.addView(mButtonStartPlay);

		mButtonStartPlay.setX(xVerh(640));//640 op 368
		mButtonStartPlay.setY(yVerh(368));
		
		mCheckBoxAnimationDealingCards=new CheckBoxView(this);
		mCheckBoxAnimationDealingCards.setText("Animatie verdelen kaarten");
		mCheckBoxAnimationDealingCards.setTextColor(Color.WHITE);
		linear.addView(mCheckBoxAnimationDealingCards);
		
		mCheckBoxAnimationDealingCards.setX(xVerh(210));
		mCheckBoxAnimationDealingCards.setY(yVerh(650));

		MainActivity mA=this;
		mChList = new ChoiceList(this, game, mA);
		mChList.setVisibility(View.INVISIBLE);
		linear.addView(mChList);
		mChList.setX(xVerh(schermbreedte / 2));
		mChList.setY(yVerh(schermhoogte / 2 + yVerh(10)));
		mImage2Trump = new Image2(this, game);
		mImage2Trump.moveAndScaleTo(mImage2Trump.getX(), mImage2Trump.getY(), 0, 0, 0, 0);
		linear.addView(mImage2Trump);
		//zet op scherm
		mImage2Trump.setX(xVerh(190));//170
		mImage2Trump.setY(yVerh(190));//630

		mImage2Dealer = new Image2(this, R.drawable.dealer);
		linear.addView(mImage2Dealer);
		//zet buiten scherm
		mImage2Dealer.setX(xVerh(640));//640
		mImage2Dealer.setY(yVerh(-200));//-200
		
		mImageBalloon = new ImageBalloon(this);
		mImageBalloon.moveAndScaleTo(mImageBalloon.getX(), mImageBalloon.getY(), 0, 0, 0, 0);
		linear.addView(mImageBalloon);
		//zet op scherm
		mImageBalloon.setX(xVerh(640));//
		mImageBalloon.setY(yVerh(528));//528
		
		loadAll();//al wat hier achter komt kan eerder komen

		mButtonNewGame.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					progressDialog = ProgressDialog.show(MainActivity.this, "Eventjes wachten a.u.b.", "Laden gegevens...", true);
					game.gameEnd=false;
					game.doublePoints=1;
					mainMenu = false;
					chooseMenu = true;
					playMenu = false;
					yourTurn=false;
					mImageBalloon.set.pause();
					mImageBalloon.setScaleX(0);
					mImageBalloon.setScaleY(0);
					game.setDealer(random(4));
					game.setOldStartPlayer(0);
					game.restartChoice();
					mButtonStartPlay.setText("");//dit om performclick te bepalen

					vi.setBackgroundResource(R.drawable.green_back_3);
					linear.removeAllViews();
					linear.addView(viStart);
					viStart.setVisibility(View.GONE);//r;
					linear.addView(mCheckBoxAnimationDealingCards);//dit ook er in gezet, anders problemen met button doorgaan
					mCheckBoxAnimationDealingCards.setVisibility(View.INVISIBLE);
					linear.addView(viText1);
					viText1.setVisibility(View.INVISIBLE);
					linear.addView(mButtonStartChoice);	
					linear.addView(mButtonStartPlay);
					linear.addView(mChList);
					linear.addView(mImage2Trump);
					linear.addView(mImage2Dealer);
					linear.addView(mImageBalloon);
					for (int a=1;a <= 4;a++)
					{
						textViewPlayer[a].setText(person[a].name);//extra
						linear.addView(textViewPlayer[a]);
					}
					//clear decks en save
					for (int c=1;c <= 4;c++)game.playerDeck[c].clear();
					game.gameDeck.clear();
					for (int a=0;a <= 1;a++)game.waitingDeck[a].clear();
					game.last4CardDeck.clear();

					CardSet cardSet =new CardSet(MainActivity.this, MainActivity.this);
					game.deck.clear();
					game.deck.addAll(cardSet);
					game.deck.addViewsToLayout(linear);

					game.deck.scramble();
					game.deck.changeZ(false);
					game.deck.setCardsDealer(game.dealer);
					game.deck.setCardsStart(true);
					game.deck.setCardsNotTrumpCard(false);

					game.deck.setCardsOutsideScreen(3);
					game.deck.showImages(false);//true  test
					game.waitingDeck[0].addAll(game.deck);//waitingDeck[0] dient nu als startdeck
					//progressDialog bovenaan zorgt ervoor dat dit niet onderbroken kan worden
					//run zorgt ervoor dat alles in die volgorde gebeurd, deze kan wel onderbroken worden
					new Thread(new Runnable() {
							@Override
							public void run()
							{//geen toast gebruiken onder deze run
								for (int a=1;a <= 4;a++)game.playerPoints[a]=0;//punten op 0 zetten
								if(!mButtonGame.isClickable())
								{
									mDb.setParameters(game,MainActivity.this);
									for (int c=1;c<=4;c++)
									{
										mDb.setCards(game.playerDeck[c], "playerdeck" + String.valueOf(c));
									}
									mDb.setCards(game.gameDeck, "gamedeck");
									for (int a=0;a <=1;a++)
									{
										if(a==0)mDb.setCards(game.waitingDeck[0], "waitingdeck0");
										if(a==1)mDb.setCards(game.waitingDeck[1], "waitingdeck1");
									}
									mDb.setCards(game.last4CardDeck, "last4carddeck");
									mDb.setCards(game.deck, "deck");
									mDb.setPlayers(game, 0);//dit is om de playerPoints te saven
									mButtonGame.setClickable(true);
								}
								runOnUiThread(new Runnable() {
										@Override
										public void run()
										{		
											if(mCheckBoxAnimationDealingCards.getcheckBox().isChecked())
											{
												setTextTv1(true);
												startAnimation();
											}
											else
											{
												animationCards=true;
												startWithoutAnimation();
											}
											progressDialog.dismiss();
										}
									});
							}
						}).start();			
				}
			});

		mButtonGame.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if(getOn)
					{
						getOn=false;//indien performclick zet men getOn terug false, ook gebruikt bij startchoose
					}
					else
					{
						getOn=true;
					}
					mainMenu = false;
					vi.setBackgroundResource(R.drawable.green_back_3);
					viStart.setVisibility(View.GONE);//gone
					mCheckBoxAnimationDealingCards.setVisibility(View.INVISIBLE);
					game.deck.showImages(false);//dit is nodig na button "opnieuw delen"
					for (int a=2;a <= 4;a++)//2 tot 4
					{
						game.playerDeck[a].showCards=false;
					}	
					if (chooseMenu)
					{ 
						if (game.deck.size() == 52)
						{//bij allereerste keer starten spel en terug eruitgaan dan zal waitingDeck[0] leeg zijn
							if(game.waitingDeck[0].isEmpty())
							{//waitingdeck[0] wordt deck
								game.waitingDeck[0].addAll(game.deck);
							}
							else	
							{//deck wordt waitingdeck[0]
								game.deck.clear();
								game.deck.addAll(game.waitingDeck[0]);
							}
							game.deck.setCardsCheckPoint(false);
							game.deck.setCardsRestart(false);//dit is indien onderbreking als de kaarten bewegen
							if(mCheckBoxAnimationDealingCards.getcheckBox().isChecked())
							{
								game.deck.changeZ(false);//false
								game.deck.setCardsStart(true);
								game.deck.setCardsDivide(false);
								game.deck.setCardsBundle(false);
								game.deck.setCardsDeal(false);
								game.deck.setCardsNotTrumpCard(false);
								game.deck.setCardsDealer(game.dealer);
								tv1.setText("");
								startAnimation();
							}
							else
							{
								startWithoutAnimation();
							}
						}
						else
						{
							game.waitingDeck[0].clear();
							getOn=true;
							if(animationCards)getOn=false;
							animationCards=false;
							startChoose();//vanaf hier start het spel na het delen, kaarten van de andere spelers blijven staan
							onClickListenerplayerDeck();
							boolean found = false;
							for (int a=1;a <= 4;a++)
							{
								if (game.playerChoice[a] != -1)
								{
									textViewPlayer[a].setText(game.getChoice(a));
								}
								else
								{
									found = true;
								}
							}
							//moveImageDealer(true);
							
							if (!found)
							{
								mButtonStartPlay.setVisibility(View.VISIBLE);
								mButtonStartPlay.setText("Start spel");
								if (game.maxChoice == game.PAS)
								{//indien iedereen past
									//toast("iedereen past");
									Toast.makeText(MainActivity.this, "iedereen past", Toast.LENGTH_SHORT).show();
									mButtonStartPlay.setText("Opnieuw uitdelen");//indien je dit aanpast, ook aanpassen in onclicklistener
									setPlayerCardsVisible(0,false,0);
								}
								if (game.maxChoice == game.VRAAG && game.playerChoice[1] == game.VRAAG)
								{
									mButtonStartPlay.setVisibility(View.INVISIBLE);
									showDialog(DIALOG_ALLEEN);
								}
							}
							if (game.player == 1 && game.playerChoice[1] == -1)
							{
								mChList.pos = -1;
								mChList.setList();
								mChList.setVisibility(View.VISIBLE);
							}
						}
					}

					if (playMenu)
					{
						if(game.gameEnd)
						{
							mButtonStartPlay.setText("Opnieuw uitdelen");//indien je dit aanpast, ook aanpassen in onclicklistener
							mButtonStartPlay.setVisibility(View.VISIBLE);
							setPlayerCardsVisible(0,false,0);
							mImage2Trump.setBackground();
							mImage2Trump.moveAndScaleTo(mImage2Trump.getX(), mImage2Trump.getY(), 0, 2000, 0, 1);
							startPlay();
						}
						else
						{
							mImage2Trump.setBackground();
							mImage2Trump.moveAndScaleTo(mImage2Trump.getX(), mImage2Trump.getY(), 0, 2000, 0, 1);
							startPlay();
							onClickListenerplayerDeck();
							if (game.maxChoice == game.PAS)
							{//indien iedereen past
								//toast("iedereen past");
								Toast.makeText(MainActivity.this, "iedereen past", Toast.LENGTH_SHORT).show();
								mButtonStartPlay.setText("Opnieuw uitdelen");//indien je dit aanpast, ook aanpassen in onclicklistener
								mButtonStartPlay.setVisibility(View.VISIBLE);
								setPlayerCardsVisible(0,true,100);
							}
							if(game.gameDeck.size()==0)
							{
								playCard(true);
							}
							else
							{
								playCard(false);
							}
						}
					}
					//zet getOn terug op false
					getOn=false;
				}
			});

		mButtonStartChoice.getButton().setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					mButtonStartPlay.setVisibility(View.INVISIBLE);
					for (int a=1;a <= 4;a++)//dit moet later weg
					{
						textViewPlayer[a].setText(person[a].name);
					}
					moveImageDealer(true);
					mChList.setVisibility(View.INVISIBLE);//dit moet later weg
					for (int a=1;a <= 4;a++)//dit moet later weg
					{//dit is om de troefkaart terug in oorspronkelijke staat te brengen 
						for (int crd=1;crd <= 13;crd++)//dit moet later weg
						{
							if (game.playerDeck[a].card(crd).trumpCard)
							{
								game.setTrumpCategory(game.playerDeck[a].card(crd).category);
								break;
							}
						}
					}
					game.restartChoice();
					game.checkTroel();
					if (game.player == 1)
					{
						mChList.pos = -1;
						mChList.setList();
						mChList.setVisibility(View.VISIBLE);
					}
					else
					{
						game.startChoice();
						continueWithStartChoice();
					}
				}
			});

		mButtonStartChoice.getButton().setOnLongClickListener(new OnLongClickListener()
			{//extra

				@Override
				public boolean onLongClick(View p1)
				{
					for (int a=1;a <= 4;a++)
					{
						if (a != 1)
						{
							game.playerDeck[a].setCardsEnabled(true);
							toast("Cards are enabled");
						}
					}
					return true;
				}
			});

		mButtonStartPlay.getButton().setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view)
				{
					for (int a=1;a <= 4;a++)
					{
						game.playerDeck[a].setCardsChoose2(false);
					}
					game.playerDeck[1].setCardsEnabled(false);//erbijgezet
					game.playerDeck[2].setCardsEnabled(false);
					game.playerDeck[3].setCardsEnabled(false);
					game.playerDeck[4].setCardsEnabled(false);
					
					mButtonStartPlay.setVisibility(View.INVISIBLE);
					mButtonStartChoice.setVisibility(View.INVISIBLE);
					
					switch (view.getTag().toString())
					{
						case "Start spel":
							game.setHighestChoice();
							for (int a=1;a <= 4;a++)
							{
								textViewPlayer[a].setText(game.getPlay(a));
							}
							moveImageDealer(true);
							if (game.maxChoice == game.PAS)
							{//indien iedereen past
								//toast("iedereen past");
								Toast.makeText(MainActivity.this, "iedereen past", Toast.LENGTH_SHORT).show();
								mButtonStartPlay.setText("Opnieuw uitdelen");//indien je dit aanpast, ook aanpassen in onclicklistener
								mButtonStartPlay.setVisibility(View.VISIBLE);
								game.playerDeck[game.dealer].setCardsNotTrumpCard(false);
								setPlayerCardsVisible(0,true,100);
								break;
							}
							setTrumpCardToOpenMiserePlayer();
							setPlayerCardsVisibility(true,100);
							game.playerDeck[game.dealer].setCardsNotTrumpCard(true);
							if(game.playerDeck[game.dealer].showCards)
							{
								game.playerDeck[game.dealer].moveCardsToPlayer(game.dealer,500,100);
							}
							else
							{
								game.playerDeck[game.dealer].moveCardsOutsideScreen(game.dealer,500,100);
							}
							
							setValueToTrumpValue();
							mImage2Trump.setBackground();
							mImage2Trump.moveAndScaleTo(mImage2Trump.getX(), mImage2Trump.getY(), 0, 2000, 0, 1);
							chooseMenu = false;
							playMenu = true;
							game.setFriendsEnemies();
							game.restartGame();
							break;
						case "Opnieuw uitdelen":
							if (game.maxChoice == game.PAS)
							{//indien iedereen past
								game.doublePoints = game.doublePoints*2;
								String str="Puntentelling * " + String.valueOf(game.doublePoints);
								Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
							}
							else
							{
								game.doublePoints=1;
							}
							game.last4CardDeck.clear();//laatste 4 kaarten verwijderen
							//indien nog kaarten van de spelers overblijven => (open)misere of iedereen past
							for (int a=1;a <= 4;a++)
							{
								game.deck.addAll(game.playerDeck[a]);
								game.playerDeck[a].clear();
							}
							game.deck.addAll(game.waitingDeck[0]);
							game.waitingDeck[0].clear();
							game.deck.addAll(game.waitingDeck[1]);
							game.waitingDeck[1].clear();
							
							game.deck.restart();
							game.deck.scrambleLight();
							game.deck.changeZ(true);
							game.setDealer();
							game.deck.setCardsRestart(true);
						
							for(int a=0;a<game.deck.size();a++)game.deck.get(a).setValueToStartValue();
							//dit pas toepassen als het spel volledig is uitgespeeld want in gamedeck is dezelfde
							//kaart die dan ook al wordt aangepast
							game.deck.moveCardsOutsideScreen(3,-1,0);
							
							mButtonStartPlay.setVisibility(View.INVISIBLE);
							//zet buiten scherm
							mImage2Dealer.setX(xVerh(640));//640
							mImage2Dealer.setY(yVerh(-200));//-200

							if(chooseMenu)
							{
								mButtonStartChoice.setVisibility(View.INVISIBLE);
								mChList.setVisibility(View.INVISIBLE);
							}

							if(playMenu)
							{
								mImage2Trump.set.pause();
								mImage2Trump.setScaleX(0);
								mImage2Trump.setScaleY(0);
							}
							game.gameEnd=false;
							chooseMenu=true;
							playMenu=false;
							for (int a=1;a <= 4;a++)
							{
								textViewPlayer[a].setVisibility(View.INVISIBLE);
								textViewPlayer[a].setText(person[a].name);
							}
							game.setOldStartPlayer(0);
							game.restartChoice();
							animationCards=true;
							break;
						default:toast("fout in programma");
							break;
					}
				}
			});
			
		cntDTimerDealAgain = new CountDownTimer(1, 1){ //1, 1

			@Override
			public void onTick(long p1)
			{
				// TODO: Implement this method
			}

			@Override
			public void onFinish()
			{
				mButtonGame.performClick();
			}
		};

	}//oncreate 

	@Override
	protected Dialog onCreateDialog(int id)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id)
		{
			case DIALOG_ALLEEN:
				builder
					.setCancelable(false)
					.setTitle("Maak je keuze : Pas of alleen?")
					.setPositiveButton("ALLEEN", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id)
						{
							game.playerChoice[1] = game.ALLEEN;
							game.maxChoice = game.ALLEEN;
							textViewPlayer[1].setText(game.getChoice(1));
							moveImageDealer(true);
							mButtonStartPlay.setVisibility(View.VISIBLE);
							mButtonStartPlay.setText("Start spel");
						}
					})
					.setNegativeButton("PAS", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int id)
						{
							game.playerChoice[1] = game.PAS;
							game.maxChoice = game.PAS;
							textViewPlayer[1].setText(game.getChoice(1));
							moveImageDealer(true);
							mButtonStartPlay.setVisibility(View.VISIBLE);
							//toast("iedereen past");
							Toast.makeText(MainActivity.this, "iedereen past", Toast.LENGTH_SHORT).show();
							mButtonStartPlay.setText("Opnieuw uitdelen");//indien je dit aanpast, ook aanpassen in onclicklistener
							setPlayerCardsVisible(0,true,100);
						}
					});
				AlertDialog dialogDelete = builder.create();
				dialogDelete.show();
				break;
		}			
		return super.onCreateDialog(id);
	}

	public void loadAll()
	{   //progressDialog is hier niet nodig,kan niet bij opstarten onderbroken worden
		new Thread(new Runnable() {
				@Override
				public void run()
				{//geen toast gebruiken onder deze run
					if (!mDb.getParameters(game, MainActivity.this))
					{
						game.setDealer(random(4));
						chooseMenu = true;
						mCheckBoxAnimationDealingCards.getcheckBox().setChecked(true);
						mDb.setParameters(game, MainActivity.this);
						mButtonGame.setClickable(false);
					}
					if (!mDb.getPlayers(game))
					{
						game.restartChoice();//dit is om playerchoice en oldplayerchoice op -1 te zetten
						mDb.setPlayers(game, 0);
					}
					mDb.getPlay(game);

					game.deck.addAll(mDb.getCards(MainActivity.this, "deck", MainActivity.this));
					game.deck.changeZ(false);
					game.deck.setCardsDealer(game.dealer);
					for (int a = 1;a <= 4;a++)
					{
						game.playerDeck[a].addAll(mDb.getCards(MainActivity.this, "playerdeck" + String.valueOf(a),MainActivity.this));
					}
					game.gameDeck.addAll(mDb.getCards(MainActivity.this, "gamedeck",MainActivity.this));
					for (int b=0;b < game.gameDeck.size();b++)
					{
						game.gameDeck.get(b).setZ(b+1);
					}	
					game.waitingDeck[0].addAll(mDb.getCards(MainActivity.this, "waitingdeck0",MainActivity.this));
					game.waitingDeck[1].addAll(mDb.getCards(MainActivity.this, "waitingdeck1",MainActivity.this));
					game.last4CardDeck.addAll(mDb.getCards(MainActivity.this, "last4carddeck",MainActivity.this));
					//de echte views in last4CardDeck zetten
					if(!game.last4CardDeck.isEmpty())
					{
						boolean found=false;
						int cardRef = game.last4CardDeck.card(1).cardReference;
						int c=0;
						for(int b=0;b<=1;b++){
							if(c==4)break;
							for(int a=0;a<game.waitingDeck[b].size();a+=1){
								if(!found && game.waitingDeck[b].get(a).cardReference==cardRef){
									found=true;game.last4CardDeck.clear();
								}
								if(found)
								{
									game.last4CardDeck.add(game.waitingDeck[b].get(a));
									c++;
									if(c==4)break;
								}
							}
						}	
					}
					//indien bepaalde decks leeg zijn dan cardset vastleggen
					if (game.deck.isEmpty() && game.playerDeck[1].isEmpty() && game.waitingDeck[0].isEmpty() && game.waitingDeck[1].isEmpty())
					{
						CardSet cardSet =new CardSet(MainActivity.this, MainActivity.this);
						game.deck.addAll(cardSet);
						game.deck.scramble();
						game.deck.changeZ(false);
						game.deck.setCardsDealer(game.dealer);
						game.deck.setCardsStart(true);
						mDb.setCards(game.deck, "deck");
					}

					runOnUiThread(new Runnable() {
							@Override
							public void run()
							{			
								//plaats positie kaarten
								game.deck.setCardsOutsideScreen(3);
								for (int c=1;c <= 4;c++)game.playerDeck[c].setCardsOutsideScreen(c);
								game.gameDeck.setCardsOutsideScreen(3);
								game.waitingDeck[0].setCardsOutsideScreen(3);
								game.waitingDeck[1].setCardsOutsideScreen(3);
								game.last4CardDeck.setCardsOutsideScreen(3);
								//views toevoegen aan layout (eenmalig)
								game.deck.addViewsToLayout(linear);
								game.gameDeck.addViewsToLayout(linear);
								for (int c=1;c <= 4;c++)game.playerDeck[c].addViewsToLayout(linear);
								game.waitingDeck[0].addViewsToLayout(linear);
								game.waitingDeck[1].addViewsToLayout(linear);
								//game.last4CardDeck.addViewsToLayout(linear);
								//views van last4CardDeck = z = -1 tot -4
								for (int a=1;a <= game.last4CardDeck.size();a++)game.last4CardDeck.card(a).setZ(a-5);//z=-1 tot -4
							}
						});
				}
			}).start();	
	}

	public void restartAll() //r;
	{
		vi.setBackgroundResource(R.drawable.background1);
		viStart.setVisibility(View.VISIBLE);
		viText1.setVisibility(View.INVISIBLE);
		mCheckBoxAnimationDealingCards.setVisibility(View.VISIBLE);
		//plaats positie kaarten
		game.deck.setCardsOutsideScreen(3);
		game.deck.showImages(false);//true
		game.deck.setVisibilityShadow(true);//alle schaduws terug
		for (int c=1;c <= 4;c++)
		{
			game.playerDeck[c].setCardsOutsideScreen(c);
			game.playerDeck[c].setVisibilityShadow(true);//alle schaduws terug
		}
		game.gameDeck.setCardsOutsideScreen(3);
		game.waitingDeck[0].setCardsOutsideScreen(3);
		game.waitingDeck[1].setCardsOutsideScreen(3);
		game.last4CardDeck.setCardsOutsideScreen(3);
		for (int a=1;a <= 4;a++)textViewPlayer[a].setVisibility(View.INVISIBLE);
		mButtonStartPlay.setVisibility(View.INVISIBLE);
		//zet buiten scherm, maar eerst pauze
		mImage2Dealer.set.pause();
		mImage2Dealer.setX(xVerh(640));//640
		mImage2Dealer.setY(yVerh(-200));//-200
		
		if(chooseMenu)
		{
			mButtonStartChoice.setVisibility(View.INVISIBLE);
			mChList.setVisibility(View.INVISIBLE);
		}
		if(playMenu)
		{
			mImage2Trump.set.pause();
			mImage2Trump.setScaleX(0);
			mImage2Trump.setScaleY(0);
			mImageBalloon.set.pause();
			mImageBalloon.setScaleX(0);
			mImageBalloon.setScaleY(0);
		}
	}
	
	public void onClickListenerplayerDeck()
	{
		for (int c=1;c <= 4;c++)
		{
			for (int d=1;d <= game.playerDeck[c].size();d++)
			{
				final int a=c;
				final int b=d;
				game.playerDeck[a].card(b).getImageView().setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View p1)
						{   //choose2
							if (game.playerDeck[a].card(b).choose2)
							{
								boolean found=false;
								boolean deckIsToCenter=false;
								for (int g=1;g <= 4;g++)
								{
									if (game.playerDeck[g].card(1).checkPoint && game.playerDeck[g].card(2).checkPoint)
									{
										deckIsToCenter = true;
										game.playerDeck[g].setCardsCheckPoint(false);
										if (a == g)
										{
											game.playerDeck[a].moveCardsToPlayer(g,500,0);
											game.playerDeck[a].moveTrumpCardToPlayer(500,0);
										}
										else
										{//trumpcard wisselen van deck als er 1 is
											for (int crd=1;crd <= 13;crd++)
											{
												if (game.playerDeck[a].card(crd).trumpCard)
												{
													game.playerDeck[a].card(crd).trumpCard = false;
													game.playerDeck[g].card(crd).trumpCard = true;
													game.setTrumpCategory(game.playerDeck[g].card(crd).category);
													break;
												}
												if (game.playerDeck[g].card(crd).trumpCard)
												{
													game.playerDeck[g].card(crd).trumpCard = false;
													game.playerDeck[a].card(crd).trumpCard = true;
													game.setTrumpCategory(game.playerDeck[a].card(crd).category);
													break;
												}
											}
											game.swapDecks(game.playerDeck[a], game.playerDeck[g]);
											for (int i=1;i <= 4;i++)
											{
												game.playerDeck[i].changeZ(true);
												game.playerDeck[i].moveCardsToPlayer(i,500,0);
											}
											game.playerDeck[game.dealer].moveTrumpCardToPlayer(500,0);
											onClickListenerplayerDeck();
										}
									}
								}
								if (!deckIsToCenter)
								{
									for (int g=1;g <= 4;g++)
									{
										for (int h=1;h <= game.playerDeck[g].size();h++)
										{
											if (game.playerDeck[g].card(h).checkPoint)
											{
												if (a == g && !game.playerDeck[a].card(b).trumpCard && !game.playerDeck[g].card(h).trumpCard)
												{
													game.playerDeck[a].setCardsCheckPoint(true);
													game.playerDeck[a].moveCardsToCenter();
													deckIsToCenter = true;
													break;
												}
												else
												{
													found = true;
													game.playerDeck[g].card(h).checkPoint = false;
													game.swapCards(game.playerDeck[a], b, game.playerDeck[g], h);
													break;
												}
											}
										}
									}
								}
								if (found)
								{
									for (int i=1;i <= 4;i++)
									{
										game.playerDeck[i].sort();
										game.playerDeck[i].changeZ(true);
										game.playerDeck[i].moveCardsToPlayer(i,500,0);
									}
									game.playerDeck[game.dealer].moveTrumpCardToPlayer(500,0);
									onClickListenerplayerDeck();
								}
								else
								{
									if (!deckIsToCenter)
									{								
										game.playerDeck[a].card(b).moveAndScaleTo(xVerh(schermbreedte / 2), yVerh(schermhoogte / 2), 0, 500, 0, 1);
										game.playerDeck[a].card(b).checkPoint = true;
									}
								}

							}
							//choose
							if (game.playerDeck[a].card(b).choose)
							{
								startChoose();//vanaf hier start het spel na het delen, kaarten van de andere spelers blijven staan				
							}
							//selectable, enkel speler1
							if (game.playerDeck[a].card(b).selectable)
							{
								yourTurn=false;
								mImageBalloon.set.pause();
								mImageBalloon.setScaleX(0);
								mImageBalloon.setScaleY(0);
								if(cntDTimerYourTurn != null)cntDTimerYourTurn.cancel();
								game.playerDeck[a].stopRunningCards();//r;
								game.playerDeck[a].setCardsEnabled(false);
								game.playerDeck[a].setCardsSelectable(false);
								game.card = b-1;
								game.checkFollowed();//set playCategory for player 1
								playCardSave();
							}
						}
					});
			}
		}
	}

	public void onClickListenerDeck()
	{
		for (int a=1;a <= game.deck.size();a++)
		{
			final int b=a;
			game.deck.card(b).getImageView().setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						if (game.deck.card(b).start)
						{
							game.deck.setCardsEnabled(false);
							game.deck.card(b).checkPoint = true;
							game.deck.setCardsDivide(true);
							game.deck.setVisibleShadowAndMoveTo(1, b - 1, 1);
							game.deck.setVisibleShadowAndMoveTo(b, 52, 52);
							final long millisInFuture=30000;//30000
							
							cntDTimer1=new CountDownTimer(millisInFuture, 1000){//1000

								@Override
								public void onTick(long p1)
								{
									if (game.deck.card(b).bundle)
									{
										game.deck.divide();
										cntDTimer2=new CountDownTimer(millisInFuture, 1000){

											@Override
											public void onTick(long p1)
											{
												if (game.deck.card(b).deal)
												{												
													dealCardsToPlayers();
													onClickListenerplayerDeck();
													viText1.setVisibility(View.INVISIBLE);
													cancel();
												}
											}

											@Override
											public void onFinish()
											{
												//toast("timer2 finished");
											}
										}.start();
										cancel();				
									}
								}

								@Override
								public void onFinish()
								{
									//toast("fout in progamma");
								}

							}.start();
						}
					}
				});
		}
	}

	public void checkYourTurn()
	{
		if (game.player == 1)
		{
			if(yourTurn)
			{
				mImageBalloon.setScaleX(1);
				mImageBalloon.setScaleY(1);
			}
			else
			{
				cntDTimerYourTurn = new CountDownTimer(5000, 5000){

					@Override
					public void onTick(long p1)
					{
						// TODO: Implement this method
					}

					@Override
					public void onFinish()
					{
						mImageBalloon.moveAndScaleTo(mImageBalloon.getX(), mImageBalloon.getY(), 0, -1, 0, 1);
						yourTurn=true;
					}
				}.start();
			}
		}
	}
	
	public void playCard(boolean firstPlayer)
	{
		if (game.player != game.startPlayer || firstPlayer)
		{
			game.startGame();
			if (game.player == 1)
			{
				checkYourTurn();
				if (game.player == game.startPlayer)
				{
					if(game.playerChoice[game.player]==game.TROELMEE && game.round==1){
						if(game.getHighestValue(game.trumpCategory)!=0)
						{
							game.playerDeck[1].get(game.card).getImageView().setEnabled(true);
							game.playerDeck[1].get(game.card).selectable = true;
							game.playerDeck[1].moveSelectableCards();
						}
					}
					else
					{
						game.playerDeck[1].setCardsEnabled(true);
						game.playerDeck[1].setCardsSelectable(true);
					}	
				}
				else
				{
					if (game.getNumberOfCards(game.cat) == 0)
					{
						game.playerDeck[1].setCardsEnabled(true);
						game.playerDeck[1].setCardsSelectable(true);
					}
					else
					{
						for (int b=0;b < game.playerDeck[1].size();b++)
						{
							if (game.playerDeck[1].get(b).category == game.cat)
							{
								game.playerDeck[1].get(b).getImageView().setEnabled(true);
								game.playerDeck[1].get(b).selectable = true;
							}
						}	
						game.playerDeck[1].moveSelectableCards();
					}
				}
				if(game.playerDeck[1].size()==1)//==1
				{
					game.playerDeck[1].get(0).getImageView().performClick();
				}
			}
			else
			{
				playCardSave();
			}
		}
		else //de 4 kaarten zijn afgelegd
		{
			checkCardsPlayed();
			game.setOldStartPlayer();
			//hier bepalen wie ronde gewonnen heeft
			game.setStartPlayer(game.getWinner());
			game.setPlayer(game.startPlayer);
			game.oldLast4CardDeck.clear();
			game.oldLast4CardDeck.addAll(game.last4CardDeck);
			game.last4CardDeck.clear();
			game.last4CardDeck.addAll(game.gameDeck);
			if(game.playerChoice[game.player]==game.PAS || 
				 game.playerChoice[game.player]==game.MISERE || game.playerChoice[game.player]==game.OPENMISERE){
				game.waitingDeck[0].addAll(game.gameDeck);
			}else{
				game.waitingDeck[1].addAll(game.gameDeck);
			}
			game.checkGameEnd();
			//setRangeCategory, misere
			if(game.maxChoice==game.MISERE){game.setRangeCategory(0,false);}
			//setRangeCategory, openmisere
			if(game.maxChoice==game.OPENMISERE){game.setRangeCategory(0,true);}
			//zien bij openmisere de openmiserespelers er niet meer aan kunnen,einde spel dus
			if(game.gameEndOpenmisere()){
				game.gameEnd=true;
			}
			//settext
			for (int a=1;a <= 4;a++)
			{
				textViewPlayer[a].setText(game.getPlay(a));
			}
			//laatste kaart gamedeck roundPlayed=true als het niet de laatste ronde is
			game.gameDeck.card(game.gameDeck.size()).roundPlayed=true;
			//kaarten verschuiven naar de winnaar ..........
			game.gameDeck.changeZ(true);
			game.gameDeck.moveCardsOutsideScreen(game.player,-1,0);//-1 
			//indien einde spel = tellen punten
			if(game.gameEnd){
				game.setPoints();
				setPlayerCardsVisible(0,true,100);
				mButtonStartPlay.setVisibility(View.VISIBLE);
				mButtonStartPlay.setText("Opnieuw uitdelen");//indien je dit aanpast, ook aanpassen in onclicklistener	
				for (int a=1;a <= 4;a++)
				{
					textViewPlayer[a].setText(game.getPlay(a));
				}
			}
			game.gameDeck.clear();
			moveImageDealer(true);
		}
	}

	public void playCardSave()
	{
		game.playerDeck[game.player].get(game.card).selected = true;
		game.playerDeck[game.player].get(game.card).z=game.gameDeck.size()+1;//+1 om 1 tot 4 te krijgen, kaart wordt pas later aan gamedeck toegevoegd
		game.playerDeck[game.player].moveSelectedCard(game.player);
		game.gameDeck.add(game.playerDeck[game.player].get(game.card));
		game.playerDeck[game.player].remove(game.card);
		
		game.setRangeCategory2();
		game.setFollowAfterPlayingCard();//openmisere
		game.setOkayAfterPlayingCard();//openmisere
		
		onClickListenerplayerDeck();
		if(game.playerDeck[game.player].showCards)
		{
			game.playerDeck[game.player].moveCardsToPlayer(game.player,500,100);
		}
		game.setPlayer();
	}

	private void startAnimation()
	{
		if (game.dealer != 1)
		{
			int ee=random(52);
			game.deck.card(ee).checkPoint = true; //random(52)
		}
		onClickListenerDeck();
		viText1.setVisibility(View.VISIBLE);

		float deltaX=15;//15
		float x = xVerh(schermbreedte / 2 - (deltaX / 2 * (52 - 1)));
		float y = yVerh(schermhoogte / 2);
		for (int a=1;a <= game.deck.size();a++)
		{
			game.deck.card(a).moveAndScaleTo(x, y , 0, -1, 0, 1);
			x = x + xVerh(deltaX);
		}	

		new CountDownTimer(1000, 1000){//1000

			@Override
			public void onTick(long p1)
			{
				//2de getal bepaald interval
			}

			@Override
			public void onFinish()
			{
				setTextTv1(false);
			}
		}.start();
	}

	private void setTextTv1(boolean scramble)
	{
		if(scramble)
		{
			tv1.setText("Schudden kaarten......");
		}
		else
		{
			switch (game.dealer)
			{
				case 1:
					tv1.setText("Jij bent de deler, tik op een kaart......");//Jij bent de deler, tik op een kaart......
					break;
				case 2:
					tv1.setText("Speler 2 is de deler......");
					break;
				case 3:
					tv1.setText("Speler 3 is de deler......");
					break;
				case 4:
					tv1.setText("Speler 4 is de deler......");
					break;
			}
		}
	}
	
	private void startWithoutAnimation()
	{
		game.deck.setCardsStart(false);
		int ee=random(52);
		game.deck.card(ee).checkPoint = true; //random(52)
		game.deck.divide();
		//indien men meerdere keren divide wil toepassen dan moet men wel volgende lijn gebruiken
		//game.deck.setCardsCheckPoint(false);
		
		dealCardsToPlayersWithoutAnimation();
		getOn=true;
		mButtonGame.performClick();//na dit zijn de kaarten geen checkpoint meer
	}
	
	public void continueWithStartChoice()
	{
		textViewPlayer[game.player].setText(game.getChoice(game.player));//setTextChoicePlayer();
		if(game.player==game.dealer)moveImageDealer(true);
		game.setPlayer();
		if (game.startPlayer == game.player)
		{
			if (game.maxChoice == game.VRAAG && game.playerChoice[1] == game.VRAAG)
			{
				showDialog(DIALOG_ALLEEN);
			}
			else
			{
				mButtonStartPlay.setVisibility(View.VISIBLE);
				mButtonStartPlay.setText("Start spel");
				if (game.maxChoice == game.PAS)
				{//indien iedereen past
					//toast("iedereen past");
					Toast.makeText(MainActivity.this, "iedereen past", Toast.LENGTH_SHORT).show();
					mButtonStartPlay.setText("Opnieuw uitdelen");//indien je dit aanpast, ook aanpassen in onclicklistener
					setPlayerCardsVisible(0,true,100);
				}
			}
		}
		else
		{
			if (game.player == 1)
			{
				mChList.pos = -1;
				mChList.setList();
				mChList.setVisibility(View.VISIBLE);
				return;
			}
			else
			{
				game.startChoice();
				continueWithStartChoice();
				return;
			}
		}
	}

	private void startChoose()//vanaf hier start het spel na het delen, kaarten van de andere spelers blijven staan
	{
		game.playerDeck[1].setCardsDealer(game.dealer);//voor moveTrumpCardToPlayer,enkel bij mButtonGame
		game.playerDeck[1].setMaxZ(4);
		for (int a=1;a <= 4;a++)
		{
			game.playerDeck[a].changeZ(true);
			game.playerDeck[a].setCardsCheckPoint(false);
			game.playerDeck[a].setCardsChoose2(true);
			game.playerDeck[a].showImages(true);
			game.playerDeck[a].setCardsRotation(0);
			if (a == game.dealer)//enkel de deler kan de trumpcard hebben
			{
				for (int b=1;b <= 13;b++)
				{
					if (game.playerDeck[a].card(b).trumpCard)
					{
						game.playerDeck[a].card(b).showImage(true);
					}
				}
			}
			if (a == 1)
			{
				if(getOn)//doorgaan
				{
					setPlayerCardsVisible(1,false,0);
					game.playerDeck[game.dealer].setTrumpCardToPlayer();
				}
				else
				{
					setPlayerCardsVisible(1,true,100);
					game.playerDeck[game.dealer].moveTrumpCardToPlayer(-1,0);
				}
				//game.playerDeck[a].setCardsEnabled(true);//uitgeschakeld
			}
		}
		for (int a=1;a <= 4;a++)textViewPlayer[a].setVisibility(View.VISIBLE);
		//mButtonStartChoice.setVisibility(View.VISIBLE);//uitgeschakeld
		new CountDownTimer(1, 1){

			@Override
			public void onTick(long p1)
			{
				// TODO: Implement this method
			}

			@Override
			public void onFinish()
			{
				boolean found = false;
				for (int a=1;a <= 4;a++)
				{
					if (game.playerChoice[a] != -1)
					{
						found = true;
					}
				}
				if(!found)mButtonStartChoice.getButton().performClick();
			}
		}.start();
		moveImageDealer(false);
	}

	private void startPlay()
	{
		game.playerDeck[1].setCardsDealer(game.dealer);//voor moveTrumpCardToPlayer,enkel bij mButtonGame
		game.playerDeck[1].setMaxZ(4);
		for (int a=1;a <= 4;a++)
		{
			game.playerDeck[a].changeZ(true);
			game.playerDeck[a].setCardsCheckPoint(false);
			game.playerDeck[a].setCardsChoose2(false);// 10/11/2020
			game.playerDeck[a].showImages(true);
			if (a == 1)
			{
				if(getOn)//doorgaan
				{
					setPlayerCardsVisible(1,false,0);
				}
			}
		}
		setPlayerCardsVisibility(false,0);
		game.gameDeck.showImages(true);
		game.gameDeck.setCardsToGameDeck(game.startPlayer);
		game.last4CardDeck.setCardsTolast4CardDeck(game.oldStartPlayer,0.4f);
		for (int a=1;a <= game.last4CardDeck.size();a++)game.last4CardDeck.card(a).setZ(a-5);//z=-1 tot -4
		for (int a=1;a <= 4;a++)
		{
			textViewPlayer[a].setText(game.getPlay(a));
			textViewPlayer[a].setVisibility(View.VISIBLE);
		}
		moveImageDealer(false);
	}
	
	public void moveImageDealer(final boolean move)
	{//timer moeten zetten omdat textViewPlayer[1].getTextView().getWidth() niet direct aangepast is
		cntDTimerImage2Dealer=new CountDownTimer(1, 1){ //1, 1

			@Override
			public void onTick(long p1)
			{
				// TODO: Implement this method
			}

			@Override
			public void onFinish()
			{
				float x=0;
				float y=0;
				int width = textViewPlayer[game.dealer].getTextView().getWidth();//1 letter=25   353 147 206verschil speler1 dansen12introef1 7 16 
				float widthTwo=width/25; //25

				switch (game.dealer)
				{                  
					case 1:
						x = xVerh(597-width+(widthTwo*16));
						y = yVerh(570);
						break;
					case 2:
						x = xVerh(150);
						y = yVerh(340-width+(widthTwo*16));//327
						break;
					case 3:
						x = xVerh(597-width+(widthTwo*16));
						y = yVerh(165);//170
						break;
					case 4:
						x = xVerh(1130);
						y = yVerh(395+width-(widthTwo*16));//380
						break;
				}
				if(move)
				{
					mImage2Dealer.moveAndScaleTo(x, y, 0, 500, 0, 1);//500
				}
				else
				{
					mImage2Dealer.setX(x);
					mImage2Dealer.setY(y);
				}
			}
		}.start();
	}

	public int random(int i)
	{//bvb i=2 dan keuze=1 of 2, i=0=error
		Random generator=new Random();
		int rndm = generator.nextInt(i) + 1;
		return rndm;
	}

	private void dealCardsToPlayers()  //IOException  SQLException
	{
		int b=0;
		long delay = 0;
		float x,y;
		int duration=800;//-1=standaard  800
		game.deck.card(52).trumpCard = true;//laatste kaart=troefkaart
		game.setTrumpCategory(game.deck.card(52).category);
		for (int a=1;a < game.deck.size() ;a++)//laatste kaart blijft staan
		{
			switch (b)
			{
				case 4 :case 8 :case 12 :case  16 :case 20 :case 24 :
				case  28 :case 32 :case 37 :case  42 :case 47 :
					game.setPlayer();delay += 0;
					break;
			}
			b++;
			switch (game.player)
			{
				case 1:
					x = xVerh(schermbreedte / 2);
					y = yVerh(schermhoogte + 250);//+250
					game.deck.card(b).moveAndScaleTo(x, y, random(45), duration, delay, 1);
					break;
				case 2:
					x = xVerh(-250);
					y = yVerh(schermhoogte / 2);
					game.deck.card(b).moveAndScaleTo(x, y, random(45), duration, delay, 1);//random(45)
					break;
				case 3:
					x = xVerh(schermbreedte / 2);
					y = yVerh(-250);
					game.deck.card(b).moveAndScaleTo(x, y, random(45), duration , delay, 1);
					break;
				case 4:
					x = xVerh(schermbreedte + 250);
					y = yVerh(schermhoogte / 2);
					game.deck.card(b).moveAndScaleTo(x, y, random(45), duration, delay, 1);
					break;
			}
			game.playerDeck[game.player].add(game.deck.card(b));
			delay += 100;
		}
		game.playerDeck[game.player].add(game.deck.card(52));
		game.setPlayer();

		try
		{
			mDb.setParameters(game, this);//this
			game.deck.clear();
			mDb.setCards(game.deck, "deck");
			game.waitingDeck[0].clear();
			mDb.setCards(game.waitingDeck[0], "waitingdeck0");
			for (int a=1;a <= 4 ;a++)
			{//kaarten sorteren
				game.playerDeck[a].sort();
				mDb.setCards(game.playerDeck[a], "playerdeck" + String.valueOf(a));
			}
		}	
		catch (Exception e)
		{
			if (mDb.isOpen())
			{
				toast("fout in programma,database is toch open");
			}		
		}

	}

	private void dealCardsToPlayersWithoutAnimation()
	{
		int b=0;
		//long delay = 0;
		float x = 0,y = 0;
		//int duration=800;//-1=standaard  800
		game.deck.card(52).trumpCard = true;//laatste kaart=troefkaart
		game.setTrumpCategory(game.deck.card(52).category);
		for (int a=1;a < game.deck.size() ;a++)//laatste kaart blijft staan
		{
			switch (b)
			{
				case 4 :case 8 :case 12 :case  16 :case 20 :case 24 :
				case  28 :case 32 :case 37 :case  42 :case 47 :
					game.setPlayer();
					break;
			}
			b++;
			switch (game.player)
			{
				case 1:
					x = xVerh(schermbreedte / 2);
					y = yVerh(schermhoogte + 250);//+250
					break;
				case 2:
					x = xVerh(-250);
					y = yVerh(schermhoogte / 2);
					break;
				case 3:
					x = xVerh(schermbreedte / 2);
					y = yVerh(-250);
					break;
				case 4:
					x = xVerh(schermbreedte + 250);
					y = yVerh(schermhoogte / 2);
					break;
			}
			game.deck.card(b).setX(x);
			game.deck.card(b).setY(y);
			game.playerDeck[game.player].add(game.deck.card(b));
		}
		game.playerDeck[game.player].add(game.deck.card(52));
		game.setPlayer();

		try
		{
			mDb.setParameters(game, this);//this
			game.deck.clear();
			mDb.setCards(game.deck, "deck");
			for (int a=1;a <= 4 ;a++)
			{//kaarten sorteren
				game.playerDeck[a].sort();
				mDb.setCards(game.playerDeck[a], "playerdeck" + String.valueOf(a));
			}
		}	
		catch (Exception e)
		{
			if (mDb.isOpen())
			{
				toast("fout in programma,database is toch open");
			}		
		}
	}
	
	public void setTrumpCardToOpenMiserePlayer()
	{
		if (game.maxChoice == game.OPENMISERE)
		{//eerst zien of er nog een trumpcard is
			boolean found=false;
			int c = 0,d = 0;
			for (int a=1;a <= 4;a++)
			{
				for (int b=1;b <= game.playerDeck[a].size();b++)
				{
					if(game.playerDeck[a].card(b).trumpCard)
					{
						found=true;
						c=a;d=b;
						break;
					}
				}
			}
			//zoeken openmiserespeler waarvan kaarten nog niet zichtbaar zijn
			if(found)
			{
				for (int a=2;a <= 4;a++)//speler1 niet want deze zijn automatisch zichtbaar
				{
					if ((game.playerChoice[a] == game.OPENMISERE || game.oldPlayerChoice[a] == game.OPENMISERE) && !game.playerDeck[a].showCards)
					{	//zet laatste kaart=oldtrumpard
						game.playerDeck[a].card(13).oldTrumpCard=true;
						//zet echte trumpcard uit
						game.playerDeck[c].card(d).trumpCard=false;
						break;
					}
				}	
			}
			
		}	
	}
	
	public void setPlayerCardsVisibility(boolean move,long startDelay)
	{//al de openmiserespelers hun kaarten zichtbaar zetten
		if (game.maxChoice == game.OPENMISERE)
		{
			for (int a=2;a <= 4;a++)//speler1 niet want deze zijn automatisch zichtbaar
			{
				if ((game.playerChoice[a] == game.OPENMISERE || game.oldPlayerChoice[a] == game.OPENMISERE) && !game.playerDeck[a].showCards)
				{
					game.playerDeck[a].showImages(true);
					if(move)
					{
						game.playerDeck[a].moveCardsToPlayer(a,500,startDelay);
					}
					else
					{
						game.playerDeck[a].setCardsToPlayer(a);
					}
				}
			}	
		}
	}

	public void setPlayerCardsVisible(int player,boolean move,long startDelay)
	{
		if(player==0)
		{
			for (int a=1;a <= 4;a++)
			{
				if(move)
				{
					game.playerDeck[a].moveCardsToPlayer(a,500,startDelay);
				}
				else
				{
					game.playerDeck[a].setCardsToPlayer(a);
				}
			}	
		}
		else
		{
			if(move)
			{
				game.playerDeck[player].moveCardsToPlayer(player,500,startDelay);
			}
			else
			{
				game.playerDeck[player].setCardsToPlayer(player);
			}
		}
	}
	
	private void setValueToTrumpValue()
	{
		for (int a=1;a <= 4;a++)
		{
			for (int b=0;b < game.playerDeck[a].size();b++)
			{
				if (game.playerDeck[a].get(b).category == game.getTrumpCategory())
				{
					game.playerDeck[a].get(b).value = game.playerDeck[a].get(b).value + 20;
				}
			}
		}	
	}

	private void checkCardsPlayed(){//gebeurd pas als de 4 kaarten zijn afgelegd
		//alle categorien controleren bij de andere spelers om te zien of jij van deze categorie er enkel
		//nog hebt(ook bij troef dus) of bij een openmiserespeler de andere 2 er geen meer van hebben bijvb.
		if(game.maxChoice==game.OPENMISERE){//ok
			for(int categor=1;categor<=4;categor++){
				for(int c=1;c<=4;c++){
					int total=game.getNumberOfPlayedCards(categor)+game.getNumberOfPlayedCardsGameDeck(categor);
					for(int b=1;b<=4;b++){
						if(c==b || game.playerChoice[b]==game.OPENMISERE){total+=game.getNumberOfCards(b,categor);}
					}
					if(total==13){
						for(int b=1;b<=4;b++){
							if(c!=b && game.playerChoice[b]!=game.OPENMISERE){game.setFollow(c, b, categor);}
						}	
					}
				}
			}	
		}else{//ok
			for(int categor=1;categor<=4;categor++){
				for(int c=1;c<=4;c++){
					int total=0;
					for(int b=1;b<=4;b++){
						if(c!=b){total+=game.getNumberOfCards(b,categor);}
					}
					if(total==0){
						for(int b=1;b<=4;b++){
							if(c!=b){game.setFollow(c, b, categor);}
						}	
					}
				}
			}	
		}	
		game.setCheckDeck();//iets lager kan checkdeck nog aangepast worden
		if(game.maxChoice==game.OPENMISERE){
			//checkdeck aanpassen door openmisere
			for(int a=0;a<game.checkDeck.size();a++){
				for(int z=1;z<=4;z++){//player
					if(game.playerChoice[z]==game.OPENMISERE && game.checkDeck.get(a).player==z){
						game.checkDeck.get(a).player=0;
					}	
				}	
			}
			//voorwaarden om speler te controleren:
			//als men niet kan volgen
			//achter de allerhoogste kaart
			//achter de 1ste openmiserespeler, behalve als er nog een openmiserespeler komt die kan volgen
			//   						       behalve als de passer lager geraakt(indien passer lager moest)
			int categoryStart=game.gameDeck.get(0).category;
			int b=game.startPlayer;b--;if(b==0){b=4;}
			//eerst zoeken of er een allerhoogste kaart is
			int firstValue=0;
			int lowestValueInDesc=0;//wordt pas later gebruikt
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				if(game.gameDeck.get(a).category==categoryStart && 
				   game.hasHighestValue(game.gameDeck.get(a).value,game.gameDeck.get(a).category)){
					firstValue=game.gameDeck.get(a).value;
					lowestValueInDesc=game.getLowestValueInDescendingOrder(0,0,game.gameDeck.get(a).category);
					break;
				}	
			}
			//indien allerhoogste niet gevonden dan gaan zoeken bij de openmiserespelers of oldopenmiserespelers
			if(lowestValueInDesc==0){
				for(b=1;b<=4;b++){
					if((game.playerChoice[b]==game.OPENMISERE || game.oldPlayerChoice[b]==game.OPENMISERE) && 
					   game.hasHighestValue(game.getValueHighestValue(game.playerDeck[b],categoryStart),categoryStart)){
					 	lowestValueInDesc=game.getLowestValueInDescendingOrder(b,b,categoryStart);
					 	break;
					}	
				}	
				//firstValue nog vastleggen
				if(lowestValueInDesc!=0){
					b=game.startPlayer;b--;if(b==0){b=4;}
					for(int a=0;a<game.gameDeck.size();a++){
						b++;if(b==5){b=1;}
						if(game.gameDeck.get(a).category==categoryStart && game.gameDeck.get(a).value>=lowestValueInDesc){
							firstValue=game.gameDeck.get(a).value;break;
						}
					}
				}
			}
			//nu zoeken wie niet kan volgen of tot de allerhoogste kaart en dan vastleggen
			ArrayList<Integer> checkPlayers = new ArrayList<Integer>();
			b=game.startPlayer;b--;if(b==0){b=4;}	
			boolean found=false;
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				//openmiserespelers moeten niet hier gecontroleerd worden(ook oldopenmiserespelers)
				if(game.playerChoice[b]!=game.OPENMISERE && game.oldPlayerChoice[b]!=game.OPENMISERE && (found || game.gameDeck.get(a).category!=categoryStart)){
					checkPlayers.add(b);
				}
				if(game.gameDeck.get(a).category==categoryStart){
					if(game.gameDeck.get(a).value==firstValue){found=true;}   
				}
			}
			//nu zoeken achter de 1ste openmiserespeler
			//behalve als er nog een openmiserespeler komt die kan volgen
			//behalve als de passer lager geraakt
			b=game.startPlayer;b--;if(b==0){b=4;}
			boolean checkValue=false;
			int value=0;
			found=false;
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				if(game.gameDeck.get(a).category==categoryStart){
					if(game.gameDeck.get(a).value>value){value=game.gameDeck.get(a).value;}
				}
				if(found){
					if(game.playerChoice[b]==game.PAS){
						boolean ok=true;
						if(checkValue){
							ok=false;
							if(game.gameDeck.get(a).category==categoryStart){
								if(game.gameDeck.get(a).value==value){
									checkValue=false;ok=true;
								}
							}	
						}
						//zien of de (old)openmiserespelers achter je niet kunnen volgen
						int z=b;
						z++;if(z==5){z=1;}
						while(z!=game.startPlayer){
							if((game.playerChoice[z]==game.OPENMISERE || game.oldPlayerChoice[z]==game.OPENMISERE) && game.followed[b][z][categoryStart]){
								ok=false;
							}
							z++;if(z==5){z=1;}
						}	
						if(ok && game.playerChoice[b]!=game.OPENMISERE && game.oldPlayerChoice[b]!=game.OPENMISERE){
							checkPlayers.add(b);
						}
					}
				}
				if(game.playerChoice[b]==game.OPENMISERE){
					found=true;
					if(game.gameDeck.get(a).category==categoryStart){
						if(game.gameDeck.get(a).value==value){//value openmisere is de hoogste op dit moment
							checkValue=true;
						}
					}	
				}
			}	
			//nu zoeken achter de 2de,3de... hoogste kaart voor de allerhoogste kaart
			//bvb k2a8,de 2 is ook de laatste kaart
			if(lowestValueInDesc!=0){
				b=game.startPlayer;b--;if(b==0){b=4;}	
				found=false;
				for(int a=0;a<game.gameDeck.size();a++){
					b++;if(b==5){b=1;}
					if(game.gameDeck.get(a).value==firstValue){break;}
					if(game.playerChoice[b]!=game.OPENMISERE &&  game.oldPlayerChoice[b]!=game.OPENMISERE  && found && game.gameDeck.get(a).category==categoryStart){
						checkPlayers.add(b);
					}
					if(game.gameDeck.get(a).category==categoryStart){
						if(game.gameDeck.get(a).value>=lowestValueInDesc){found=true;}   
					}
				}
			}	
			//indien dezelfde in checkplayers dan deze wissen zodat er max. maar 1 in staat
			for(int a=0;a<checkPlayers.size()-1;a++){
				for(int c=a+1;c<checkPlayers.size();c++){
					if((int)checkPlayers.get(a)==(int)checkPlayers.get(c)){
						checkPlayers.remove(c);c--;
					}
				}	
			}
			//toast gebruiken
			String str="controle op speler(s)...";
			for(int a=0;a<checkPlayers.size();a++){
				str=str + " " + String.valueOf(checkPlayers.get(a));
			}	
			//controle allerlaatste kaart
			b=game.startPlayer;b--;if(b==0){b=4;}
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				for(int e=0;e<checkPlayers.size();e++){
					if(b==checkPlayers.get(e)){
						//controle missingcards
						for(int deck=0;deck<=4;deck++){
							for(int c=0;c<=4;c++){
								if(c!=b){
									int missingCards=game.getMissingCardsInAscendingOrder(deck,c,game.gameDeck.get(a));
									if(missingCards==0){
										game.setFollow(c, b, game.gameDeck.get(a).category);
										if(c==0){break;}//indien c=0 dan geen controle players
									}
								}
							}
						}
					}
				}
			}	
			//nu gaan we pas al de spelers die niet gevolgd hebben(enkel de passers) op niet gevolgd zetten
			//reden:hogerop wordt er controle gedaan op de spelers                          
			categoryStart=game.gameDeck.get(0).category;
			b=game.startPlayer;b--;if(b==0){b=4;}
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				if(game.gameDeck.get(a).category!=categoryStart && game.playerChoice[b]!=game.OPENMISERE){
					game.setFollow(0,b,categoryStart);
				}
			}
		}	

		//voorwaarden om speler te controleren:
		//als men niet kan volgen
		//achter de allerhoogste kaart
		//achter de 1ste miserespeler, behalve als er nog een miserespeler komt die kan volgen
		//   						   behalve als de passer lager geraakt(indien passer lager moest)
		if(game.maxChoice==game.MISERE){
			int categoryStart=game.gameDeck.get(0).category;
			int b=game.startPlayer;b--;if(b==0){b=4;}
			//eerst zoeken of er een allerhoogste kaart is
			int highestValue=0;
			int lowestValueInDesc=0;//wordt pas later gebruikt
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				if(game.gameDeck.get(a).category==categoryStart && 
				   game.hasHighestValue(game.gameDeck.get(a).value,game.gameDeck.get(a).category)){
					highestValue=game.gameDeck.get(a).value;
					lowestValueInDesc=game.getLowestValueInDescendingOrder(0,0,game.gameDeck.get(a).category);
					break;
				}	
			}
			//nu zoeken wie niet kan volgen of tot de allerhoogste kaart en dan vastleggen na de allerhoogste kaart
			ArrayList<Integer> checkPlayers = new ArrayList<Integer>();
			b=game.startPlayer;b--;if(b==0){b=4;}	
			boolean found=false;
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				if(found || game.gameDeck.get(a).category!=categoryStart){
					checkPlayers.add(b);
				}
				if(game.gameDeck.get(a).category==categoryStart){
					if(game.gameDeck.get(a).value==highestValue){found=true;}   
				}
			}
			//nu zoeken achter de 1ste miserespeler
			//behalve als er nog een miserespeler komt die kan volgen
			//behalve als de passer lager geraakt
			b=game.startPlayer;b--;if(b==0){b=4;}
			boolean checkValue=false;
			int value=0;
			found=false;
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				if(game.gameDeck.get(a).category==categoryStart){
					if(game.gameDeck.get(a).value>value){value=game.gameDeck.get(a).value;}
				}
				if(found){
					if(game.playerChoice[b]==game.PAS){
						boolean ok=true;
						if(checkValue){
							ok=false;
							if(game.gameDeck.get(a).category==categoryStart){
								if(game.gameDeck.get(a).value==value){
									checkValue=false;ok=true;
								}
							}	
						}
						//zien of de miserespelers achter je niet kunnen volgen
						int z=b;
						z++;if(z==5){z=1;}
						while(z!=game.startPlayer){
							if(game.playerChoice[z]==game.MISERE && game.followed[b][z][categoryStart]){
								ok=false;
							}
							z++;if(z==5){z=1;}
						}	
						if(ok){
							checkPlayers.add(b);
						}
					}
				}
				if(game.playerChoice[b]==game.MISERE){
					found=true;
					if(game.gameDeck.get(a).category==categoryStart){
						if(game.gameDeck.get(a).value==value){//value misere is de hoogste op dit moment
							checkValue=true;
						}
					}	
				}
			}	
			//nu zoeken achter de 2de,3de... hoogste kaart voor de allerhoogste kaart
			//bvb k2a8,de 2 is ook de laatste kaart
			if(lowestValueInDesc!=0){
				b=game.startPlayer;b--;if(b==0){b=4;}	
				found=false;
				for(int a=0;a<game.gameDeck.size();a++){
					b++;if(b==5){b=1;}
					if(game.gameDeck.get(a).value==highestValue){break;}
					if(found && game.gameDeck.get(a).category==categoryStart){
						checkPlayers.add(b);
					}
					if(game.gameDeck.get(a).category==categoryStart){
						if(game.gameDeck.get(a).value>=lowestValueInDesc){found=true;}   
					}
				}
			}	
			//indien dezelfde in checkplayers dan deze wissen zodat er max. maar 1 in staat
			for(int a=0;a<checkPlayers.size()-1;a++){
				for(int c=a+1;c<checkPlayers.size();c++){
					if((int)checkPlayers.get(a)==(int)checkPlayers.get(c)){
						checkPlayers.remove(c);c--;
					}
				}	
			}
			//toast gebruiken
			String str="controle op speler(s)...";
			for(int a=0;a<checkPlayers.size();a++){
				str=str + " " + String.valueOf(checkPlayers.get(a));
			}	
			//controle allerlaatste kaart
			b=game.startPlayer;b--;if(b==0){b=4;}
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				for(int e=0;e<checkPlayers.size();e++){
					if(b==checkPlayers.get(e)){
						//controle missingcards
						for(int deck=0;deck<=4;deck++){
							for(int c=0;c<=4;c++){
								if(c!=b){
									int missingCards=game.getMissingCardsInAscendingOrder(deck,c,game.gameDeck.get(a));
									if(missingCards==0){
										game.setFollow(c, b, game.gameDeck.get(a).category);
										if(c==0){
											//indien miserespeler dan in playcategory category verwijderen als die er in zit
											if(game.playerChoice[b]==game.MISERE){
												for(int d=0;d<game.playCategory[b].size();d++){
													if(game.playCategory[b].get(d)==game.gameDeck.get(a).category){game.playCategory[b].remove(d);}
												}
											}
										}
										if(c==0){break;}//indien c=0 dan geen controle players
									}
								}
							}
						}
					}
				}
			}	
			//nu gaan we pas al de spelers die niet gevolgd hebben op niet gevolgd zetten
			//reden:hogerop wordt er controle gedaan op de spelers                        
			categoryStart=game.gameDeck.get(0).category;
			b=game.startPlayer;b--;if(b==0){b=4;}
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				if(game.gameDeck.get(a).category!=categoryStart){
					game.setFollow(0,b,categoryStart);
				}
			}
		}	

		if(game.maxChoice!=game.OPENMISERE && game.maxChoice!=game.MISERE){//al de andere keuzes
			//zien naar alle kaarten die niet gevolgd hebben en geen troef zijn
			//zien of er net nog 1 kaart lager is achter de allerhoogste kaart of achter de eerstgekochte kaart
			//ook zoeken achter de 2de,3de... hoogste kaart voor de allerhoogste kaart
			//en eens zien naar de kaarten van de speler zelf...
			int categoryStart=game.gameDeck.get(0).category;
			int b=game.startPlayer;b--;if(b==0){b=4;}
			//eerst zoeken of er een allerhoogste kaart is
			int highestValue=0;
			int lowestValueInDesc=0;//wordt pas later gebruikt
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				if(game.gameDeck.get(a).category==categoryStart && 
				   game.hasHighestValue(game.gameDeck.get(a).value,game.gameDeck.get(a).category)){
					highestValue=game.gameDeck.get(a).value;
					lowestValueInDesc=game.getLowestValueInDescendingOrder(0,0,game.gameDeck.get(a).category);
					break;
				}	
			}
			//nu zoeken wie niet kan volgen tot de allerhoogste kaart(of tot de eerste troef) en dan vastleggen na deze kaart
			ArrayList<Integer> checkPlayers = new ArrayList<Integer>();
			b=game.startPlayer;b--;if(b==0){b=4;}	
			boolean found=false;
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				if(!found && game.gameDeck.get(a).category!=categoryStart && game.gameDeck.get(a).category!=game.trumpCategory ||
				   (found && (game.gameDeck.get(a).category!=game.trumpCategory || categoryStart==game.trumpCategory))){
					checkPlayers.add(b);
				}
				if(game.gameDeck.get(a).category==categoryStart){
					if(game.gameDeck.get(a).value==highestValue){found=true;}   
				}else{
					if(game.gameDeck.get(a).category==game.trumpCategory){found=true;}
				}
			}
			//nu zoeken achter de 2de,3de... hoogste kaart voor de allerhoogste kaart
			//bvb kva8,de v is ook de laatste kaart
			if(lowestValueInDesc!=0){
				b=game.startPlayer;b--;if(b==0){b=4;}	
				found=false;
				int highestValueAtTheMoment=0;
				for(int a=0;a<game.gameDeck.size();a++){
					b++;if(b==5){b=1;}
					if(game.gameDeck.get(a).value==highestValue){break;}
					if(game.gameDeck.get(a).category==categoryStart && game.gameDeck.get(a).value>highestValueAtTheMoment){
						highestValueAtTheMoment=game.gameDeck.get(a).value;
						if(found){continue;}
					}
					if(found && game.gameDeck.get(a).category==categoryStart){
						checkPlayers.add(b);
					}
					if(game.gameDeck.get(a).category==categoryStart){
						if(game.gameDeck.get(a).value>=lowestValueInDesc){found=true;}   
					}
				}
			}	
			//indien dezelfde in checkplayers dan deze wissen zodat er max. maar 1 in staat
			for(int a=0;a<checkPlayers.size()-1;a++){
				for(int c=a+1;c<checkPlayers.size();c++){
					if((int)checkPlayers.get(a)==(int)checkPlayers.get(c)){
						checkPlayers.remove(c);c--;
					}
				}	
			}
			//toast gebruiken
			String str="controle op speler(s)...";
			for(int a=0;a<checkPlayers.size();a++){
				str=str + " " + String.valueOf(checkPlayers.get(a));
			}	
			//controle allerlaatste kaart
			b=game.startPlayer;b--;if(b==0){b=4;}
			for(int a=0;a<game.gameDeck.size();a++){
				b++;if(b==5){b=1;}
				for(int e=0;e<checkPlayers.size();e++){
					if(b==checkPlayers.get(e)){
						//controle missingcards
						for(int deck=0;deck<=4;deck++){
							for(int c=0;c<=4;c++){
								if(c!=b){
									int missingCards=game.getMissingCardsInDescendingOrder(deck,c,game.gameDeck.get(a));
									if(missingCards==0){
										game.setFollow(c, b, game.gameDeck.get(a).category);
										if(c==0){break;}//indien c=0 dan geen controle players
									}
									//indien een andere speler maximum nog 1 kaart er van kan hebben dan 50% kans op niet gevolgd zetten
									//indien er ook minimum 1 van de andere 2 spelers nog gevolgd heeft
									if(missingCards==1 && c!=0){//bij 50% moet men c=0 niet toepassen
										int followers=0;
										for(int z=1;z<=4;z++){
											if(z!=c && z!=b && game.followed[c][z][game.gameDeck.get(a).category] ){
												followers++;
											}
										}
										if(game.random(2)==1 && followers!=0){//indien min. 1 van beide gevolgd heeft en 50%
											game.setFollow(c, b, game.gameDeck.get(a).category);
											if(c==0){break;}//indien c=0 dan geen controle players
										}
									}
								}
							}
						}
					}
				}
			}	
		}		
	}
	
	private void sleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{}
	}

	private float xVerh(float x)
	{//tablet10inch=1920  tablet7inch=1280 gsm=1920
		float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		float xVerh=screenWidth / schermbreedte;
		float xV =x * xVerh;
		return xV;
	}

	private float yVerh(float y)
	{//tablet10inch=1128   tablet7inch=736  gsm=1080
		float screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		float yVerh=screenHeight / schermhoogte;
		float yV =y * yVerh;
		return yV;
	}

	@Override
	public void onBackPressed()
	{
		if (mainMenu)super.onBackPressed();
		if (!mainMenu && (chooseMenu || playMenu))
		{
			final MainActivity mA=this;
			boolean notNow = false;
			mainMenu = true;
			//dit moet je op pauze zetten, de animaties blijven anders doorgaan en
			//geven daarna problemen als je te vlug heropstart
			//+check notNow
			for (int a=1;a <= game.deck.size();a++)
			{
				game.deck.card(a).set.pause();
				//game.deck.card(a).set.removeAllListeners();
				//game.deck.card(a).set.reverse();//error
				if (game.deck.card(a).deal)notNow = true;
			}
			//isRunning() misschien gebruiken??????
			for (int c=1;c <= 4;c++)
			{
				for (int d=1;d <= game.playerDeck[c].size();d++)
				{
					game.playerDeck[c].card(d).set.pause();
				}
			}
			for (int a=1;a <= game.gameDeck.size();a++)game.gameDeck.card(a).set.pause();
			for (int a=0;a <= 1;a++)
			{
				for (int b=1;b <= game.waitingDeck[a].size();b++)
				{
					game.waitingDeck[a].card(b).set.pause();
				}
			}
			for (int a=1;a <= game.last4CardDeck.size();a++)game.last4CardDeck.card(a).set.pause();
			
			//oldLast4CardDeck leegmaken
			game.oldLast4CardDeck.setCardsOutsideScreen(game.oldStartPlayer);//oude laatste 4 kaarten wegdoen
			game.oldLast4CardDeck.clear();
			
			if (notNow)
			{
				toast("Druk nogmaals!");
				mainMenu = false;
				mDb.setParameters(game, mA);
			}
			else
			{
				if(cntDTimer1 != null)cntDTimer1.cancel();
				if(cntDTimer2 != null)cntDTimer2.cancel();
				if(cntDTimerDealAgain != null)cntDTimerDealAgain.cancel();
				if(cntDTimer3 != null)cntDTimer3.cancel();
				if(cntDTimerYourTurn != null)cntDTimerYourTurn.cancel();
				if(cntDTimerImage2Dealer != null)cntDTimerImage2Dealer.cancel();
				restartAll();
			}
		}
	}

	@Override
	protected void onPause()
	{
		//toast("onpause");
		//saveAll();
		saveByExit();
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		//toast("onstop");
		super.onStop();
	}
	
	@Override
	protected void onDestroy()
	{
		//toast("ondestroy");
		super.onDestroy();
		if (mDb != null)
		{
			mDb.close();//dit maakt geen verschil
		}
	}

	public void saveByExit()
	{
		mServiceIntent.putExtra("dealer",game.dealer);
		mServiceIntent.putExtra("doublePoints",game.doublePoints);
		mServiceIntent.putExtra("startPlayer",game.startPlayer);
		mServiceIntent.putExtra("oldStartPlayer",game.oldStartPlayer);
		mServiceIntent.putExtra("player",game.player);
		mServiceIntent.putExtra("trumpCategory",game.trumpCategory);
		mServiceIntent.putExtra("mainMenu",mainMenu);
		mServiceIntent.putExtra("chooseMenu",chooseMenu);
		mServiceIntent.putExtra("playMenu",playMenu);
		mServiceIntent.putExtra("maxChoice",game.maxChoice);
		mServiceIntent.putExtra("troelPlayer",game.troelPlayer);
		mServiceIntent.putExtra("troelMeePlayer",game.troelMeePlayer);
		mServiceIntent.putExtra("alleen",game.alleen);
		mServiceIntent.putExtra("gameEnd",game.gameEnd);
		mServiceIntent.putExtra("mCheckBoxAnimationDealingCards",mCheckBoxAnimationDealingCards.getcheckBox().isChecked());
		
		for (int a=1;a <= 4;a++)
		{
			for (int b=1;b <= 4;b++)
			{
				for (int c=1;c <= 4;c++)
				{
					if (a != b)
					{
						mServiceIntent.putExtra(String.valueOf(a) + String.valueOf(b) + String.valueOf(c) + "followed",game.followed[a][b][c]);
					}
				}
			}
		}
		mServiceIntent.putExtra("getCategorySize",game.getCategory.size());	
		for (int b=0;b < game.getCategory.size();b++)
		{
			mServiceIntent.putExtra("getCategory" + String.valueOf(b) + "player",game.getCategory.get(b).player);
			mServiceIntent.putExtra("getCategory" + String.valueOf(b) + "category",game.getCategory.get(b).category);
			mServiceIntent.putExtra("getCategory" + String.valueOf(b) + "average",game.getCategory.get(b).average);
			mServiceIntent.putExtra("getCategory" + String.valueOf(b) + "remainingCards",game.getCategory.get(b).remainingCards);
			mServiceIntent.putExtra("getCategory" + String.valueOf(b) + "checkedCards",game.getCategory.get(b).checkedCards);
			mServiceIntent.putExtra("getCategory" + String.valueOf(b) + "underPlayer",game.getCategory.get(b).underPlayer);
		}	
		for (int a=1;a <= 4;a++)
		{
			int z=0;
			mServiceIntent.putExtra(String.valueOf(a) + "playCategorySize",game.playCategory[a].size());	
			for (int b=0;b < game.playCategory[a].size();b++)
			{
				z++;
				mServiceIntent.putExtra(String.valueOf(a) + String.valueOf(b) + "playCategory",game.playCategory[a].get(b));
				mServiceIntent.putExtra(String.valueOf(a) + String.valueOf(b) + "okay",game.okay[a][b]);
			}	
			while (z < 4)
			{
				z++;
				mServiceIntent.putExtra(String.valueOf(a) + String.valueOf(z) + "okay",game.okay[a][z]);
			}
		}	
		
		for (int a=1;a <= 4;a++)
		{
			mServiceIntent.putExtra("playerTrumpCategory" + String.valueOf(a),game.playerTrumpCategory[a]);
			mServiceIntent.putExtra("playerPoints" + String.valueOf(a),game.playerPoints[a]);
			mServiceIntent.putExtra("playerChoice" + String.valueOf(a),game.playerChoice[a]);
			mServiceIntent.putExtra("oldPlayerChoice" + String.valueOf(a),game.oldPlayerChoice[a]);
		}

		mServiceIntent.putExtra("decksize",game.deck.size());
		for(int b=0;b<game.deck.size();b++){
			mServiceIntent.putExtra("deck" + String.valueOf(b) + "value",game.deck.get(b).value);
			mServiceIntent.putExtra("deck" + String.valueOf(b) + "cardReference",game.deck.get(b).cardReference);
			mServiceIntent.putExtra("deck" + String.valueOf(b) + "category",game.deck.get(b).category);
		}
		
		mServiceIntent.putExtra("gameDecksize",game.gameDeck.size());
		for(int b=0;b<game.gameDeck.size();b++){
			mServiceIntent.putExtra("gameDeck" + String.valueOf(b) + "value",game.gameDeck.get(b).value);
			mServiceIntent.putExtra("gameDeck" + String.valueOf(b) + "cardReference",game.gameDeck.get(b).cardReference);
			mServiceIntent.putExtra("gameDeck" + String.valueOf(b) + "category",game.gameDeck.get(b).category);
		}
		
		for(int a=0;a<=1;a++){
			mServiceIntent.putExtra(String.valueOf(a) + "waitingDecksize",game.waitingDeck[a].size());
			for(int b=0;b<game.waitingDeck[a].size();b++){
				mServiceIntent.putExtra("waitingDeck" + String.valueOf(a) + String.valueOf(b) + "value",game.waitingDeck[a].get(b).value);
				mServiceIntent.putExtra("waitingDeck" + String.valueOf(a) + String.valueOf(b) + "cardReference",game.waitingDeck[a].get(b).cardReference);
				mServiceIntent.putExtra("waitingDeck" + String.valueOf(a) + String.valueOf(b) + "category",game.waitingDeck[a].get(b).category);
			}
		}
				
		mServiceIntent.putExtra("last4CardDecksize",game.last4CardDeck.size());
		for(int b=0;b<game.last4CardDeck.size();b++){
			mServiceIntent.putExtra("last4CardDeck" + String.valueOf(b) + "value",game.last4CardDeck.get(b).value);
			mServiceIntent.putExtra("last4CardDeck" + String.valueOf(b) + "cardReference",game.last4CardDeck.get(b).cardReference);
			mServiceIntent.putExtra("last4CardDeck" + String.valueOf(b) + "category",game.last4CardDeck.get(b).category);
		}
		
		for(int a=1;a<=4;a++){
			mServiceIntent.putExtra(String.valueOf(a) + "size",game.playerDeck[a].size());
			for(int b=0;b<game.playerDeck[a].size();b++){
				mServiceIntent.putExtra(String.valueOf(a) + String.valueOf(b) + "value",game.playerDeck[a].get(b).value);
				mServiceIntent.putExtra(String.valueOf(a) + String.valueOf(b) + "cardReference",game.playerDeck[a].get(b).cardReference);
				mServiceIntent.putExtra(String.valueOf(a) + String.valueOf(b) + "category",game.playerDeck[a].get(b).category);
				mServiceIntent.putExtra(String.valueOf(a) + String.valueOf(b) + "trumpCard",game.playerDeck[a].get(b).trumpCard);
			}
		}
		startService(mServiceIntent);
	}
	
	public void saveAll()//niet in gebruik
	{
		progressDialog = ProgressDialog.show(MainActivity.this, "Eventjes wachten a.u.b.", "Opslaan gegevens...", true);
		new Thread(new Runnable() {

				@Override
				public void run()//toast werkt niet in run
				{
					
					runOnUiThread(new Runnable() {
							@Override
							public void run()
							{		
								mDb.setParameters(game, MainActivity.this);//ok
								if(playMenu)mDb.setPlay(game);//ok
								mDb.setPlayers(game,0);//ok
								mDb.setCards(game.deck, "deck");//ok
								mDb.setCards(game.gameDeck, "gamedeck");//ok
								mDb.setCards(game.waitingDeck[0], "waitingdeck0");//ok
								mDb.setCards(game.waitingDeck[1], "waitingdeck1");//ok
								mDb.setCards(game.last4CardDeck, "last4carddeck");//ok
								for (int c=1;c <= 4;c++)
								{
									mDb.setCards(game.playerDeck[c], "playerdeck" + String.valueOf(c));//ok
								}

								toast("alles opgeslagen....");
								progressDialog.dismiss();
							}
					});
				}
			}).start();	
	}
	
	public void toast(String pp)
	{
		//Toast.makeText(this, pp, Toast.LENGTH_SHORT).show();
	}
}
