package com.mycompany.Wiezen;
import android.content.*;
import java.util.*;
import android.widget.*;
import android.os.*;

public class GamePlayer
{
	Context mCtx;
	Deck deck;
	Deck[] playerDeck=new Deck[5];
	Deck gameDeck;
	Deck[] waitingDeck=new Deck[2];//waitingDeck[0] gebruiken we ook als startdeck
	Deck last4CardDeck;
	Deck oldLast4CardDeck;//dit wordt niet opgeslagen

	public int startPlayer=1;
	public int oldStartPlayer=0;
	public int player=1;//1
	public int trumpCategory=0;
	public int dealer=4;//4
	Deck checkDeck;

	public GamePlayer(Context context)
	{
		mCtx = context;
		deck = new Deck(context);
		for (int a=1;a <= 4;a++)
		{
			playerDeck[a] = new Deck(context);
		}
		gameDeck = new Deck(context);
		waitingDeck[0] = new Deck(context);
		waitingDeck[1] = new Deck(context);
		last4CardDeck = new Deck(context);
		oldLast4CardDeck = new Deck(context);
		checkDeck = new Deck(context);
	}

	public int getDealer()
	{
		return dealer;
	}

	public void setDealer()
	{
		dealer += 1;
		if (dealer == 5) dealer = 1;
		setStartPlayer();
		setPlayer(startPlayer);
	}

	public void setDealer(int dealer)
	{
		this.dealer = dealer;
		setStartPlayer();
		setPlayer(startPlayer);
	}

	private void setStartPlayer()
	{
		startPlayer = dealer + 1;
		if (startPlayer == 5) startPlayer = 1;
	}

	public void setStartPlayer(int startPlayer)
	{
		this.startPlayer = startPlayer;
	}

	public void setOldStartPlayer()
	{
		this.oldStartPlayer = startPlayer;
	}
	
	public void setOldStartPlayer(int oldStartPlayer)
	{
		this.oldStartPlayer = oldStartPlayer;
	}
	
	public int getStartPlayer()
	{
		return startPlayer;
	}

	public void setPlayer()
	{
		player += 1;
		if (player == 5) player = 1;
	}

	public void setPlayer(int player)
	{
		this.player = player;
	}

	public int getPlayer()
	{
		return player;
	}

	public void setTrumpCategory()
	{
		trumpCategory += 1;
		if (trumpCategory == 5) trumpCategory = 1;
	}

	public void setTrumpCategory(int trumpCategory)
	{
		this.trumpCategory = trumpCategory;
	}

	public int getTrumpCategory()
	{
		return trumpCategory;
	}

	public void setCheckDeck()
	{
		checkDeck.clear();
		for (int a=1;a <= 4;a++)
		{
			for (int b=0;b < playerDeck[a].size();b++)
			{
				playerDeck[a].get(b).player = a;
			}	
			checkDeck.addAll(playerDeck[a]);
		}
		for (int b=0;b < gameDeck.size();b++)
		{
			gameDeck.get(b).player = 0;
		}	
		for (int b=0;b < last4CardDeck.size();b++)
		{
			last4CardDeck.get(b).player = 0;
		}	
		checkDeck.addAll(gameDeck);
		checkDeck.addAll(last4CardDeck);
	}

	public int random(int i)//ok
	{//bvb i=2 dan keuze=1 of 2, i=0=error
		Random generator=new Random();
		int rndm = generator.nextInt(i) + 1;
		return rndm;
	}

	public void swapDecks(Deck deck1, Deck deck2)
	{
		Deck deckTemp=new Deck(mCtx);
		deckTemp.addAll(deck1);
		setDeck(deck1, deck2);
		setDeck(deck2, deckTemp);
	}	

	public void setDeck(Deck deck, Deck deckSaved)
	{
		deck.clear();
		deck.addAll(deckSaved);
	}

	public void swapCards(Deck deck1, int card1, Deck deck2, int card2)
	{
		if (deck1 == deck2 && card1 == card2)return;
		if (deck1.card(card1).trumpCard)
		{
			deck1.card(card1).trumpCard = false;
			deck2.card(card2).trumpCard = true;
			setTrumpCategory(deck2.card(card2).category);
		}
		else
		{
			if (deck2.card(card2).trumpCard)
			{
				deck1.card(card1).trumpCard = true;
				setTrumpCategory(deck1.card(card1).category);
				deck2.card(card2).trumpCard = false;
			}
		}
		deck1.add(deck2.card(card2));
		deck2.add(deck1.card(card1));
		if (deck1 == deck2)
		{
			if (card1 > card2)
			{
				deck1.remove(card1 - 1);
				deck2.remove(card2 - 1);
			}
			else
			{
				deck2.remove(card2 - 1);
				deck1.remove(card1 - 1);
			}
		}
		else
		{
			deck1.remove(card1 - 1);
			deck2.remove(card2 - 1);
		}
	}

	public Deck getPlayerDeckCategory(int player, int category)
	{
		Deck deckTemp=new Deck(mCtx);
		for (int a=1;a <= playerDeck[player].size();a++)
		{
			if (playerDeck[player].card(a).category == category)
			{
				deckTemp.add(playerDeck[player].card(a));
			}
		}
		return deckTemp;
	}

	public Deck getPlayerDeckValue(int player, int value)
	{
		Deck deckTemp=new Deck(mCtx);
		for (int a=1;a <= playerDeck[player].size();a++)
		{
			if (playerDeck[player].card(a).value == value)
			{
				deckTemp.add(playerDeck[player].card(a));
			}
		}	
		return deckTemp;
	}

	public int getTotalValue(int category)
	{//enkel gebruikt bij misere en openmisere
		int totalValue=0;
		Deck deckTemp=new Deck(mCtx);
		deckTemp = getPlayerDeckCategory(player, category);
		for (int a=1;a <= deckTemp.size();a++)
		{totalValue += deckTemp.card(a).value;}
		return totalValue;
	}

	public int getTotalValue(int player, int category)
	{//enkel gebruikt bij misere en openmisere
		int totalValue=0;
		Deck deckTemp=new Deck(mCtx);
		deckTemp = getPlayerDeckCategory(player, category);
		for (int a=1;a <= deckTemp.size();a++)
		{totalValue += deckTemp.card(a).value;}
		return totalValue;
	}

	public void toast(String msg)
	{
		 //Toast.makeText(mCtx, msg, Toast.LENGTH_SHORT).show();
    } 
}
