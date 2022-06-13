package com.mycompany.Wiezen;

public class ObjPlayer
{
	int player, category;
	float average;
	//hieronder enkel gebruikt bij openmisere
	int remainingCards;//aantal kaarten van de vrienden - de kaarten die er ook onder zitten
	int checkedCards;//aantal mogelijke keren dat je kan uitkomen 
	//met de hoogste kaarten - remainingCards
	int underPlayer;//bij welke speler je eronder zit			 

	public ObjPlayer(int player, int category, float average, int remainingCards, int checkedCards, int underPlayer)
	{
		this.player = player;
		this.category = category;
		this.average = average;
		this.remainingCards = remainingCards;
		this.checkedCards = checkedCards;
		this.underPlayer = underPlayer;
	}	

	public void setAverage(float average)
	{
		this.average = average;
	}

	public void setRemainingCards(int remainingCards)
	{
		this.remainingCards = remainingCards;
	}

	public void setCheckedCards(int checkedCards)
	{
		this.checkedCards = checkedCards;
	}

	public void setUnderPlayer(int underPlayer)//niet in gebruik???
	{
		this.underPlayer = underPlayer;
	}
}
