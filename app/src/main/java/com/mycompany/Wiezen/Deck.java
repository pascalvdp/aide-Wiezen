package com.mycompany.Wiezen;

import android.content.*;
import java.util.*;
import android.widget.*;
import android.os.*;
import android.view.*;
import java.util.stream.*;

public class Deck extends ArrayList<Card>
{

	@Override
	public static  <E extends Object> List<E> of()
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1, E e2)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1, E e2, E e3)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1, E e2, E e3, E e4)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1, E e2, E e3, E e4, E e5)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public static  <E extends Object> List<E> of(E[] elements)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public Stream<Card> stream()
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public Stream<Card> parallelStream()
	{
		// TODO: Implement this method
		return null;
	}

	static int dealer;
	Context context;//is enkel nodig voor toast
	ArrayList<Card> deckTemp = new ArrayList<Card>();
    final float schermbreedte=1280,schermhoogte=736;//recentste tablet breedte=1920 hoogte=1128
	boolean showCards;//enkel van toepassing bij playerDeck[1to4]

	public Deck(Context context)
	{
		this.context = context;//
    }

	public void restart()
	{
		for (int a=1;a <= size();a++)
		{
			card(a).roundPlayed=false;
			card(a).oldTrumpCard = false;
			card(a).selected = false;
			card(a).deal = false;
			card(a).restart=false;
			card(a).choose = false;
			card(a).bundle = false;
			card(a).divide = false;
			card(a).start = false;
			card(a).trumpCard=false;
			card(a).choose2=false;
		}	
	}
	
	//indien men alle add en addall override zou men card.player 
	//direct kunnen toepassen, moest het nodig zijn

	public void showImages(boolean show)//set cardback to afbeelding
	{
		for (int a=1;a <= size();a++)
		{
			card(a).showImage(show);
		}
	}

	public void divide()
	{
		if (isEmpty())return;
		deckTemp.clear();
		boolean found = false;
		int check = -1;
		for (int a=1;a <= size();a++)
		{
			if (found)deckTemp.add(card(a));
			if (card(a).checkPoint)
			{
				found = true;check = a;
				deckTemp.add(card(a));
			}	
		}
		removeRange(check - 1, size());
		addAll(0, deckTemp);
		deckTemp.clear();
		changeZ(false);//false
	}

	public void setMaxZ(int maxZ)
	{
		if (!isEmpty())card(1).maxz = maxZ;
	}

	public void changeZ(boolean startFirstCard)
	{
		if (isEmpty())return;
		if (startFirstCard)
		{
			for (int a=1;a <= size();a++)
			{
				card(a).maxz++;
				card(a).z = card(a).maxz;
				card(a).setZ(card(a).z);
			}			
		}
		else
		{
			for (int a=size();a > 0;a--)
			{
				card(a).maxz++;
				card(a).z = card(a).maxz;
				card(a).setZ(card(a).z);
			}		
		}
	}

	public void setVisibleShadowAndMoveTo(int startCard, int endCard, int cardToMove)
	{
		if (isEmpty())return;
		if (endCard < 1)return;//dit is enkel bij card 1
		int c=0;
		for (int a=startCard;a <= endCard;a++)//onderste en bovenste kaart schaduw behouden
		{
			c += 1;
			card(a).visibleShadow = false;
			if (c == 7)//7
			{
				c = 0;card(a).visibleShadow = true;
			}
			card(a).x = card(cardToMove).getX();
			card(a).moveAndScaleTo(card(cardToMove).getX(), card(cardToMove).getY(), 0, -1, 0, 1);
		}
		card(startCard).visibleShadow = true;//onderste kaart
		card(endCard).visibleShadow = true;//bovenste
	}

	public void setVisibilityShadow(boolean visible)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).visibleShadow=visible;
			if(visible)
			{
				card(a).ivShadow.setVisibility(View.VISIBLE);
			}
			else
			{
				card(a).ivShadow.setVisibility(View.INVISIBLE);
			}
		}	
	}
	
	public void setCardsDealer(int dealer)
	{
		if (isEmpty())return;
		card(1).dealer = dealer;
		this.dealer = dealer;
	}

	public void setCardsPlayer(int player)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).player = player;
		}	
	}

	public void setCardsEnabled(boolean enable)//dit is dus clickable, clickable zelf werkt niet
	{
		for (int a=1;a <= size();a++)
		{
			card(a).getImageView().setEnabled(enable);
		}	
	}

	public void setCardsSelectable(boolean selectable)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).selectable = selectable;
		}	
	}

	public void setCardsStart(boolean start)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).start = start;
		}	
	}

	public void setCardsRestart(boolean restart)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).restart = restart;
		}	
	}
	
	public void setCardsCheckPoint(boolean checkPoint)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).checkPoint = checkPoint;
		}	
	}

	public void setCardsDivide(boolean divide)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).divide = divide;
		}	
	}

	public void setCardsBundle(boolean bundle)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).bundle = bundle;
		}	
	}

	public void setCardsDeal(boolean deal)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).deal = deal;
		}	
	}

	public void setCardsChoose(boolean choose)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).choose = choose;
		}	
	}

	public void setCardsChoose2(boolean choose2)
	{
		for (int a=1;a <= size();a++)
		{
			card(a).choose2 = choose2;
		}	
	}

	public void setCardsNotTrumpCard(boolean setOldTrumpCard)
	{
		for (int a=1;a <= size();a++)
		{
			if (card(a).trumpCard)
			{
				card(a).trumpCard = false;
				if (setOldTrumpCard)card(a).oldTrumpCard = true;
			}
		}	
	}

	public void setCardsRotation(float rotation)
	{
		for (int a=1;a <= size();a++)
		{
			if (!card(a).trumpCard)
			{
				card(a).setRotation(rotation);
			}
		}	
	}

	public Card card(int i)
	{
		return get(i - 1);//i-1
	}

	private static Comparator<Card> sorter=new Comparator<Card>(){

		@Override
		public int compare(Card p1, Card p2)
		{//<=-1  =0  >=1  , men kan ook meerdere values vergelijken
			return p1.sortingValue - p2.sortingValue;
			//return p1.str.compareTo(p2.str);//dit gebruiken bij strings
		}
	};

	public void sort()
	{
		sort(sorter);
	}

	private static Comparator<Card> sorterReversed=new Comparator<Card>(){

		@Override
		public int compare(Card p1, Card p2)
		{//<=-1  =0  >=1  , men kan ook meerdere values vergelijken
			return p2.sortingValue - p1.sortingValue;
			//return p1.str.compareTo(p2.str);//dit gebruiken bij strings
		}
	};

	public void sortReversed()
	{
		sort(sorterReversed);
	}

	public void scramble()//schudden
	{
		for (int a=1;a <= size();a++)
		{
			Card temp = card(a);
			Random generator=new Random();
			int b=a - 1;
			int rndm= generator.nextInt(size() - b) + a;//1 to 52   2 to 52  3 to 52
			set(a - 1, card(rndm));
			set(rndm - 1, temp);
		}
	}
	
	public void scrambleLight()//beetje schudden
	{
		int div=random(20);
		while(div!=0)
		{
			int a = random(size());
			Card temp = card(a);
			Random generator=new Random();
			int b=a - 1;
			int rndm= generator.nextInt(size() - b) + a;//a to 52
			set(a - 1, card(rndm));
			set(rndm - 1, temp);
			div--;
		}
	}

	public void moveSelectedCard(int player)  
	{
		float[] startX = new float[5];
		float[] startY = new float[5];
		//indien hier aanpassingen, dan ook in setCardsGameDeck
		startX[1] = xVerh(schermbreedte / 2);
		startY[1] = yVerh(schermhoogte / 2 + (55));
		startX[2] = xVerh(schermbreedte / 2 - (100));
		startY[2] = yVerh(schermhoogte / 2);
		startX[3] = xVerh(schermbreedte / 2);
		startY[3] = yVerh(schermhoogte / 2 - (55));
		startX[4] = xVerh(schermbreedte / 2 + (100));
		startY[4] = yVerh(schermhoogte / 2);

		for (int a=1;a <= size();a++)
		{
			if (card(a).selected)
			{
				card(a).moveAndScaleTo(startX[player], startY[player], 0, 500, 0, 1);
				break;
			}
		}
	}

	public void moveSelectableCards()//enkel gebruikt bij speler1
	{
		if (size() != getNumberOfSelectableCards())
		{
			for (int a=1;a <= size();a++)
			{
				if (card(a).selectable)
				{		
					card(a).moveAndScaleTo(card(a).getX(), card(a).getY() - yVerh(25), 0, 500, 0, 1);
				}
			}
		}
	}

	private int getNumberOfSelectableCards()//enkel gebruikt bij speler1
	{
		int number=0;
		for (int a=1;a <= size();a++)
		{
			if (card(a).selectable)number++;
		}	
		return number;
	}

	public void stopRunningCards()//enkel gebruikt bij speler1
	{
		for (int a=1;a <= size();a++)
		{
			if(card(a).selectable)card(a).set.pause();
		}	
	}
	
	public void moveCardsToCenter()
	{
		for (int a=1;a <= size();a++)
		{
			card(a).moveAndScaleTo(xVerh(schermbreedte / 2), yVerh(schermhoogte / 2), 0, 500, 0, 1);
		}
	}

	public void moveCardsToPlayer(int player, long duration, long startDelay)  
	{
		showCards = true;
		if (duration == -1)duration = 1300;//500=snel 2000=traag
		int numberOfcards=size();//13
		if (dealer == player)
		{
			for (int a=1;a <= size();a++)
			{
				if (card(a).trumpCard)numberOfcards = size() - 1;//12
			}
		}
		//indien je hier aanpast, ook aanpassen in setCardsToPlayer
		float deltaX=55;//55 
		float deltaY=40;//40
		float[] startX = new float[5];
		float[] startY = new float[5];
		float deltaX1=65;//dit is voor speler1

		startX[1] = xVerh(schermbreedte / 2 - (deltaX1 / 2 * (numberOfcards - 1)));
		startY[1] = yVerh(728);//728
		startX[2] = xVerh(-10);//
		startY[2] = yVerh(schermhoogte / 2 - (deltaY / 2 * (numberOfcards - 1)));
		startX[3] = xVerh(schermbreedte / 2 + (deltaX / 2 * (numberOfcards - 1)));
		startY[3] = yVerh(0);//8
		startX[4] = xVerh(1290);//
		startY[4] = yVerh(schermhoogte / 2 + (deltaY / 2 * (numberOfcards - 1)));

		long delay = 0;

		for (int a=1;a <= size();a++)
		{
			if (card(a).trumpCard)
			{
				continue;
			}
			switch (player)
			{
				case 1:
					card(a).moveAndScaleTo(startX[player], startY[player], 0, duration, delay, 1);
					startX[player] += xVerh(deltaX1);
					break;
				case 2:
					card(a).moveAndScaleTo(startX[player], startY[player], 90, duration, delay, 1);//90
					startY[player] += yVerh(deltaY);
					break;	
				case 3:
					card(a).moveAndScaleTo(startX[player], startY[player], 0, duration, delay, 1);
					startX[player] -= xVerh(deltaX);
					break;
				case 4:
					card(a).moveAndScaleTo(startX[player], startY[player], 90, duration, delay, 1);//90
					startY[player] -= yVerh(deltaY);
					break;	
			}		
			delay += startDelay;//200
		}
	}

	public void moveCardsToPlayer(int player, long duration, long startDelay,float scaleFactor)
	{
		showCards = true;
		if (duration == -1)duration = 1300;//500=snel 2000=traag
		int numberOfcards=size();//13
		if (dealer == player)
		{
			for (int a=1;a <= size();a++)
			{
				if (card(a).trumpCard)numberOfcards = size() - 1;//12
			}
		}
		
		float deltaX=22;//55 
		float deltaY=40;//40
		float[] startX = new float[5];
		float[] startY = new float[5];
		float deltaX1=22;//65 dit is voor speler1

		startX[1] = xVerh(schermbreedte / 2 - (deltaX1 / 2 * (numberOfcards - 1)));
		startY[1] = yVerh(728);//728
		startX[2] = xVerh(-10);//
		startY[2] = yVerh(schermhoogte / 2 - (deltaY / 2 * (numberOfcards - 1)));
		startX[3] = xVerh(schermbreedte / 2 + (deltaX / 2 * (numberOfcards - 1)));
		startY[3] = yVerh(0);//8
		startX[4] = xVerh(1290);//
		startY[4] = yVerh(schermhoogte / 2 + (deltaY / 2 * (numberOfcards - 1)));

		long delay = 0;

		for (int a=1;a <= size();a++)
		{
			if (card(a).trumpCard)
			{
				continue;
			}
			switch (player)
			{
				case 1:
					card(a).moveAndScaleTo(startX[player], startY[player], 0, duration, delay, scaleFactor);
					startX[player] += xVerh(deltaX1);
					break;
				case 2:
					card(a).moveAndScaleTo(startX[player], startY[player], 90, duration, delay, scaleFactor);//90
					startY[player] += yVerh(deltaY);
					break;	
				case 3:
					card(a).moveAndScaleTo(startX[player], startY[player], 0, duration, delay, scaleFactor);
					startX[player] -= xVerh(deltaX);
					break;
				case 4:
					card(a).moveAndScaleTo(startX[player], startY[player], 90, duration, delay, scaleFactor);//90
					startY[player] -= yVerh(deltaY);
					break;	
			}		
			delay += startDelay;//200
		}
	}
	
	public void setCardsToPlayer(int player)  
	{
		showCards = true;
		int numberOfcards=size();//13
		if (dealer == player)
		{
			for (int a=1;a <= size();a++)
			{
				if (card(a).trumpCard)numberOfcards = size() - 1;//12
			}
		}
		//indien je hier aanpast, ook aanpassen in moveCardsToPlayer
		float deltaX=55;//55 
		float deltaY=40;//40
		float[] startX = new float[5];
		float[] startY = new float[5];
		float deltaX1=65;//dit is voor speler1

		startX[1] = xVerh(schermbreedte / 2 - (deltaX1 / 2 * (numberOfcards - 1)));
		startY[1] = yVerh(728);//728
		startX[2] = xVerh(-10);//
		startY[2] = yVerh(schermhoogte / 2 - (deltaY / 2 * (numberOfcards - 1)));
		startX[3] = xVerh(schermbreedte / 2 + (deltaX / 2 * (numberOfcards - 1)));
		startY[3] = yVerh(0);//8
		startX[4] = xVerh(1290);//
		startY[4] = yVerh(schermhoogte / 2 + (deltaY / 2 * (numberOfcards - 1)));

		for (int a=1;a <= size();a++)
		{
			if (card(a).trumpCard)
			{
				continue;
			}
			card(a).setX(startX[player]);
			card(a).setY(startY[player]);
			switch (player)
			{
				case 1:
					card(a).setRotation(0);
					startX[player] += xVerh(deltaX1);
					break;
				case 2:
					card(a).setRotation(90);
					startY[player] += yVerh(deltaY);
					break;	
				case 3:
					card(a).setRotation(0);
					startX[player] -= xVerh(deltaX);
					break;
				case 4:
					card(a).setRotation(90);
					startY[player] -= yVerh(deltaY);
					break;	
			}		
		}
	}
	
	public void moveTrumpCardToPlayer(long duration, long startDelay)  
	{//trumpcard kan enkel bij de deler zitten
		if (duration == -1)duration = 1300;//500=snel 2000=traag
		for (int a=1;a <= size();a++)
		{
			if (card(a).trumpCard)
			{
				//indien je hier aanpast, ook aanpassen in setTrumpCardToPlayer
				float x = 0,y = 0;
				int rotation = 0;
				switch (dealer)
				{
					case 1:
						x = xVerh(970);//
						y = yVerh(500);//
						break;
					case 2:
						rotation = 90;
						x = xVerh(220);//
						y = yVerh(530);//
						break;
					case 3:
						x = xVerh(305);//
						y = yVerh(220);//
						break;
					case 4:
						rotation = 90;
						x = xVerh(1060);//
						y = yVerh(200);//
						break;
				}
				card(a).moveAndScaleTo(x, y, rotation, duration, startDelay, 0.6f);
			}
		}
	}	

	public void setTrumpCardToPlayer()  
	{//trumpcard kan enkel bij de deler zitten
		for (int a=1;a <= size();a++)
		{
			if (card(a).trumpCard)
			{
				//indien je hier aanpast, ook aanpassen in moveTrumpCardToPlayer
				float x = 0,y = 0;
				int rotation = 0;
				switch (dealer)
				{
					case 1:
						x = xVerh(970);//
						y = yVerh(500);//
						break;
					case 2:
						rotation = 90;
						x = xVerh(220);//
						y = yVerh(530);//
						break;
					case 3:
						x = xVerh(305);//
						y = yVerh(220);//
						break;
					case 4:
						rotation = 90;
						x = xVerh(1060);//
						y = yVerh(200);//
						break;
				}
				card(a).setAndScaleTo(x,y,rotation,0.6f);
			}
		}
	}	
	
	public void moveCardsOutsideScreen(int playerSide, long duration, long startDelay) 
	{
		showCards = false;
		if (duration == -1)duration = 1300;//500=snel 2000=traag
		float x = 0,y = 0;
		long delay=0;
		int rotation;
		for (int d=1;d <= size();d++)
		{
			rotation = 0;
			switch (playerSide)
			{
				case 1:
					x = xVerh(schermbreedte / 2);
					y = yVerh(schermhoogte + 250);
					break;
				case 2:
					//rotation = 90;
					x = xVerh(-250);
					y = yVerh(schermhoogte / 2);
					break;
				case 3:
					x = xVerh(schermbreedte / 2);
					y = yVerh(-250);
					break;
				case 4:
					//rotation = 90;
					x = xVerh(schermbreedte + 250);
					y = yVerh(schermhoogte / 2);
					break;
			}
			card(d).moveAndScaleTo(x, y, rotation, duration, startDelay, 1);
			delay += startDelay;
		}
	}

	public void setCardsOutsideScreen(int playerSide)
	{
		float x = 0,y = 0;
		for (int d=1;d <= size();d++)
		{
			switch (playerSide)
			{
				case 1:
					x = xVerh(schermbreedte / 2);
					y = yVerh(schermhoogte + 250);
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
			card(d).setAndScaleTo(x,y,0,1);
		}
	}

	public void setCardsToGameDeck(int startPlayer)//dit is enkel voor gameDeck
	{
		float[] startX = new float[5];
		float[] startY = new float[5];
		//indien hier aanpassingen, dan ook in moveSelectedCard
		startX[1] = xVerh(schermbreedte / 2);
		startY[1] = yVerh(schermhoogte / 2 + (55));
		startX[2] = xVerh(schermbreedte / 2 - (100));
		startY[2] = yVerh(schermhoogte / 2);
		startX[3] = xVerh(schermbreedte / 2);
		startY[3] = yVerh(schermhoogte / 2 - (55));
		startX[4] = xVerh(schermbreedte / 2 + (100));
		startY[4] = yVerh(schermhoogte / 2);
		
		int b=startPlayer;
		for (int a=1;a <= size();a++)
		{
			card(a).setX(startX[b]);
			card(a).setY(startY[b]);
			b++;if(b==5) b=1;
		}
	}
	
	public void moveCardsTolast4CardDeck(int oldStartPlayer, long duration, long startDelay,float scaleFactor)//dit is enkel voor last4CardDeck
	{
		showCards = true;
		showImages(true);
		if (duration == -1)duration = 1300;//500=snel 2000=traag
		float[] startX = new float[5];
		float[] startY = new float[5];// schermbreedte=1280/2=640,schermhoogte=736/2=368
		//indien hier aanpassingen, dan ook in setCardsTolast4CardDeck
		startX[1] = xVerh((1015));//1025
		startY[1] = yVerh( (308));//288
		startX[2] = xVerh((970));//980
		startY[2] = yVerh( (276));//256
		startX[3] = xVerh( (1015));//1025
		startY[3] = yVerh( (238));//218
		startX[4] = xVerh( (1060));//1070
		startY[4] = yVerh( (276));//256

		int b=oldStartPlayer;
		float rotation=0;
		for (int a=1;a <= size();a++)
		{
			card(a).moveAndScaleTo(startX[b],startY[b], rotation, duration, startDelay, scaleFactor);
			b++;if(b==5) b=1;
		}
	}
	
	public void setCardsTolast4CardDeck(int oldStartPlayer,float scaleFactor)//dit is enkel voor last4CardDeck
	{
		showCards = true;
		showImages(true);
		float[] startX = new float[5];
		float[] startY = new float[5];// schermbreedte=1280/2=640,schermhoogte=736/2=368
		//indien hier aanpassingen, dan ook in moveCardsTolast4CardDeck
		startX[1] = xVerh((1015));//1025
		startY[1] = yVerh( (308));//288
		startX[2] = xVerh((970));//980
		startY[2] = yVerh( (276));//256
		startX[3] = xVerh( (1015));//1025
		startY[3] = yVerh( (238));//218
		startX[4] = xVerh( (1060));//1070
		startY[4] = yVerh( (276));//256

		int b=oldStartPlayer;
		float rotation=0;
		for (int a=1;a <= size();a++)
		{
			card(a).setAndScaleTo(startX[b],startY[b],rotation,scaleFactor);//0.4f
			b++;if(b==5) b=1;
		}
	}
	
	public void addViewsToLayout(RelativeLayout linear)
	{
		for (int a=1;a <= size();a++)
		{
			linear.addView(card(a));
		}
	}

	private float xVerh(float x)
	{//tablet10inch=1920  tablet7inch=1280 gsm=1920
		float screenWidth =context.getResources().getDisplayMetrics().widthPixels;
		float xVerh=screenWidth / schermbreedte;
		float xV =x * xVerh;
		return xV;
	}

	private float yVerh(float y)
	{//tablet10inch=1128   tablet7inch=736  gsm=1080
		float screenHeight = context.getResources().getDisplayMetrics().heightPixels;
		float yVerh=screenHeight / schermhoogte;
		float yV =y * yVerh;
		return yV;
	}

	public int random(int i)
	{//bvb i=2 dan keuze=1 of 2, i=0=error
		Random generator=new Random();
		int rndm = generator.nextInt(i) + 1;
		return rndm;
	}

	public void toast(String pp)
	{
		Toast.makeText(this.context, pp, Toast.LENGTH_SHORT).show();
	}
}
