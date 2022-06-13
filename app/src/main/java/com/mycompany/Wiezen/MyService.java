package com.mycompany.Wiezen;
import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;
import android.app.job.*;
import java.util.*;

public class MyService extends JobService  //IntentService
{
	Database mDb;
	Game game;
	MainActivity mA;
	int value, cardReference, category;
	boolean trumpCard;
	int size;

	@Override
	public boolean onStartJob(JobParameters p1)
	{
		// TODO: Implement this method
		return false;
	}

	@Override
	public boolean onStopJob(JobParameters p1)
	{
		// TODO: Implement this method
		return false;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Let it continue running until it is stopped.
		//Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		
		game=new Game(this);
		mDb=new Database(this, false);
		mDb.open();
		mA=new MainActivity();
		mA.mCheckBoxAnimationDealingCards=new CheckBoxView(this);
		
		game.dealer=intent.getIntExtra("dealer",-1);
		game.doublePoints=intent.getIntExtra("doublePoints",0);
		game.startPlayer=intent.getIntExtra("startPlayer",-1);
		game.oldStartPlayer=intent.getIntExtra("oldStartPlayer",-1);
		game.player=intent.getIntExtra("player",-1);
		game.trumpCategory=intent.getIntExtra("trumpCategory",-1);
		mA.mainMenu=intent.getBooleanExtra("mainMenu",false);
		mA.chooseMenu=intent.getBooleanExtra("chooseMenu",false);
		mA.playMenu=intent.getBooleanExtra("playMenu",false);
		game.maxChoice=intent.getIntExtra("maxChoice",-1);
		game.troelPlayer=intent.getIntExtra("troelPlayer",-1);
		game.troelMeePlayer=intent.getIntExtra("troelMeePlayer",-1);
		game.alleen=intent.getBooleanExtra("alleen",false);
		game.gameEnd=intent.getBooleanExtra("gameEnd",false);
		mA.mCheckBoxAnimationDealingCards.getcheckBox().setChecked(intent.getBooleanExtra("mCheckBoxAnimationDealingCards",false));
		mDb.setParameters(game,mA);
		
		for (int a=1;a <= 4;a++)
		{
			for (int b=1;b <= 4;b++)
			{
				for (int c=1;c <= 4;c++)
				{
					if (a != b)
					{
						game.followed[a][b][c]=intent.getBooleanExtra(String.valueOf(a) + String.valueOf(b) + String.valueOf(c) + "followed",false);
						
					}
				}
			}
		}
		size=intent.getIntExtra("getCategorySize",0);
		for(int b=0;b<size;b++){
			int player=intent.getIntExtra("getCategory" + String.valueOf(b) + "player",-1);
			int category=intent.getIntExtra("getCategory" + String.valueOf(b) + "category",-1);
			float average=intent.getFloatExtra("getCategory" + String.valueOf(b) + "average",-1);
			int remainingCards=intent.getIntExtra("getCategory" + String.valueOf(b) + "remainingCards",-1);
			int checkedCards=intent.getIntExtra("getCategory" + String.valueOf(b) + "checkedCards",-1);
			int underPlayer=intent.getIntExtra("getCategory" + String.valueOf(b) + "underPlayer",-1);
			ObjPlayer ObjP=new ObjPlayer(player,category,average,remainingCards,checkedCards,underPlayer);
			game.getCategory.add(ObjP);
		}
		for (int a=1;a <= 4;a++)
		{
			int z=0;
			size=intent.getIntExtra(String.valueOf(a) + "playCategorySize",0);
			for(int b=0;b<size;b++){
				z++;
				int category=intent.getIntExtra(String.valueOf(a) + String.valueOf(b) + "playCategory",-1);
				game.playCategory[a].add(category);
				game.okay[a][b]=intent.getBooleanExtra(String.valueOf(a) + String.valueOf(b) + "okay",false);
			}
			while (z < 4)
			{
				z++;
				game.okay[a][z]=intent.getBooleanExtra(String.valueOf(a) + String.valueOf(z) + "okay",false);
			}	
		}	
		if(mA.playMenu)mDb.setPlay(game);

		for (int a=1;a <= 4;a++)
		{
			game.playerTrumpCategory[a]=intent.getIntExtra("playerTrumpCategory" + String.valueOf(a),-1);
			game.playerPoints[a]=intent.getIntExtra("playerPoints" + String.valueOf(a),-1);
			game.playerChoice[a]=intent.getIntExtra("playerChoice" + String.valueOf(a),-1);
			game.oldPlayerChoice[a]=intent.getIntExtra("oldPlayerChoice" + String.valueOf(a),-1);
		}
		mDb.setPlayers(game,0);
		
		size=intent.getIntExtra("decksize",0);
		for(int b=0;b<size;b++){
			value=intent.getIntExtra("deck" + String.valueOf(b) + "value",100);
			cardReference=intent.getIntExtra("deck" + String.valueOf(b) + "cardReference",100);
			category=intent.getIntExtra("deck" + String.valueOf(b) + "category",100);
			Card card = new Card(this,value,cardReference,category,mA);
			game.deck.add(card);
		}
		mDb.setCards(game.deck, "deck");
		
		size=intent.getIntExtra("gameDecksize",0);
		for(int b=0;b<size;b++){
			value=intent.getIntExtra("gameDeck" + String.valueOf(b) + "value",100);
			cardReference=intent.getIntExtra("gameDeck" + String.valueOf(b) + "cardReference",100);
			category=intent.getIntExtra("gameDeck" + String.valueOf(b) + "category",100);
			Card card = new Card(this,value,cardReference,category,mA);
			game.gameDeck.add(card);
		}
		mDb.setCards(game.gameDeck, "gamedeck");
		
		for(int a=0;a<=1;a++){
			size=intent.getIntExtra(String.valueOf(a) + "waitingDecksize",0);
			for(int b=0;b<size;b++){
				value=intent.getIntExtra("waitingDeck" + String.valueOf(a) + String.valueOf(b) + "value",100);
				cardReference=intent.getIntExtra("waitingDeck" + String.valueOf(a) + String.valueOf(b) + "cardReference",100);
				category=intent.getIntExtra("waitingDeck" + String.valueOf(a) + String.valueOf(b) + "category",100);
				Card card = new Card(this,value,cardReference,category,mA);
				game.waitingDeck[a].add(card);
			}
		}
		mDb.setCards(game.waitingDeck[0], "waitingdeck0");
		mDb.setCards(game.waitingDeck[1], "waitingdeck1");
		
		size=intent.getIntExtra("last4CardDecksize",0);
		for(int b=0;b<size;b++){
			value=intent.getIntExtra("last4CardDeck" + String.valueOf(b) + "value",100);
			cardReference=intent.getIntExtra("last4CardDeck" + String.valueOf(b) + "cardReference",100);
			category=intent.getIntExtra("last4CardDeck" + String.valueOf(b) + "category",100);
			Card card = new Card(this,value,cardReference,category,mA);
			game.last4CardDeck.add(card);
		}
		mDb.setCards(game.last4CardDeck, "last4carddeck");
		
		for(int a=1;a<=4;a++){
			size=intent.getIntExtra(String.valueOf(a) + "size",0);
			for(int b=0;b<size;b++){
				value=intent.getIntExtra(String.valueOf(a) + String.valueOf(b) + "value",100);
				cardReference=intent.getIntExtra(String.valueOf(a) + String.valueOf(b) + "cardReference",100);
				category=intent.getIntExtra(String.valueOf(a) + String.valueOf(b) + "category",100);
				Card card = new Card(this,value,cardReference,category,mA);
				game.playerDeck[a].add(card);
				trumpCard=intent.getBooleanExtra(String.valueOf(a) + String.valueOf(b) + "trumpCard",false);
				game.playerDeck[a].get(b).trumpCard=trumpCard;
			}
			mDb.setCards(game.playerDeck[a], "playerdeck" + String.valueOf(a));
		}
		//Toast.makeText(this, "saving game......", Toast.LENGTH_LONG).show();
		stopService(intent);//onDestroy() wordt nu ook toegepast
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (mDb != null)
		{
			mDb.close();
		}
		super.onDestroy();
		//Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}
}
