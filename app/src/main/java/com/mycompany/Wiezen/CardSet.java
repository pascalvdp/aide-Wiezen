package com.mycompany.Wiezen;
import android.content.*;
import java.util.*;
import java.util.stream.*;

public class CardSet extends Deck//ArrayList<Card>
{
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

	MainActivity mA;
	public CardSet(Context ctx, MainActivity mA)
	{
		super(ctx);
		this.mA = mA;
		Card hearts2 = new Card(ctx, 2, R.drawable.hearts2, 1, mA);
		Card hearts3 = new Card(ctx, 3, R.drawable.hearts3, 1, mA);
		Card hearts4 = new Card(ctx, 4, R.drawable.hearts4, 1, mA);
		Card hearts5 = new Card(ctx, 5, R.drawable.hearts5, 1, mA);
		Card hearts6 = new Card(ctx, 6, R.drawable.hearts6, 1, mA);
		Card hearts7 = new Card(ctx, 7, R.drawable.hearts7, 1, mA);
		Card hearts8 = new Card(ctx, 8, R.drawable.hearts8, 1, mA);
		Card hearts9 = new Card(ctx, 9, R.drawable.hearts9, 1, mA);
		Card hearts10 = new Card(ctx, 10, R.drawable.hearts10, 1, mA);
		Card heartsj = new Card(ctx, 11, R.drawable.heartsj, 1, mA);
		Card heartsq = new Card(ctx, 12, R.drawable.heartsq, 1, mA);
		Card heartsk = new Card(ctx, 13, R.drawable.heartsk, 1, mA);
		Card heartsa = new Card(ctx, 14, R.drawable.heartsa, 1, mA);

		Card clubs2 = new Card(ctx, 2, R.drawable.clubs2, 2, mA);
		Card clubs3 = new Card(ctx, 3, R.drawable.clubs3, 2, mA);
		Card clubs4 = new Card(ctx, 4, R.drawable.clubs4, 2, mA);
		Card clubs5 = new Card(ctx, 5, R.drawable.clubs5, 2, mA);
		Card clubs6 = new Card(ctx, 6, R.drawable.clubs6, 2, mA);
		Card clubs7 = new Card(ctx, 7, R.drawable.clubs7, 2, mA);
		Card clubs8 = new Card(ctx, 8, R.drawable.clubs8, 2, mA);
		Card clubs9 = new Card(ctx, 9, R.drawable.clubs9, 2, mA);
		Card clubs10 = new Card(ctx, 10, R.drawable.clubs10, 2, mA);
		Card clubsj = new Card(ctx, 11, R.drawable.clubsj, 2, mA);
		Card clubsq = new Card(ctx, 12, R.drawable.clubsq, 2, mA);
		Card clubsk = new Card(ctx, 13, R.drawable.clubsk, 2, mA);
		Card clubsa = new Card(ctx, 14, R.drawable.clubsa, 2, mA);

		Card diamonds2 = new Card(ctx, 2, R.drawable.diamonds2, 3, mA);
		Card diamonds3 = new Card(ctx, 3, R.drawable.diamonds3, 3, mA);
		Card diamonds4 = new Card(ctx, 4, R.drawable.diamonds4, 3, mA);
		Card diamonds5 = new Card(ctx, 5, R.drawable.diamonds5, 3, mA);
		Card diamonds6 = new Card(ctx, 6, R.drawable.diamonds6, 3, mA);
		Card diamonds7 = new Card(ctx, 7, R.drawable.diamonds7, 3, mA);
		Card diamonds8 = new Card(ctx, 8, R.drawable.diamonds8, 3, mA);
		Card diamonds9 = new Card(ctx, 9, R.drawable.diamonds9, 3, mA);
		Card diamonds10 = new Card(ctx, 10, R.drawable.diamonds10, 3, mA);
		Card diamondsj = new Card(ctx, 11, R.drawable.diamondsj, 3, mA);
		Card diamondsq = new Card(ctx, 12, R.drawable.diamondsq, 3, mA);
		Card diamondsk = new Card(ctx, 13, R.drawable.diamondsk, 3, mA);
		Card diamondsa = new Card(ctx, 14, R.drawable.diamondsa, 3, mA);

		Card spades2 = new Card(ctx, 2, R.drawable.spades2, 4, mA);
		Card spades3 = new Card(ctx, 3, R.drawable.spades3, 4, mA);
		Card spades4 = new Card(ctx, 4, R.drawable.spades4, 4, mA);
		Card spades5 = new Card(ctx, 5, R.drawable.spades5, 4, mA);
		Card spades6 = new Card(ctx, 6, R.drawable.spades6, 4, mA);
		Card spades7 = new Card(ctx, 7, R.drawable.spades7, 4, mA);
		Card spades8 = new Card(ctx, 8, R.drawable.spades8, 4, mA);
		Card spades9 = new Card(ctx, 9, R.drawable.spades9, 4, mA);
		Card spades10 = new Card(ctx, 10, R.drawable.spades10, 4, mA);
		Card spadesj = new Card(ctx, 11, R.drawable.spadesj, 4, mA);
		Card spadesq = new Card(ctx, 12, R.drawable.spadesq, 4, mA);
		Card spadesk = new Card(ctx, 13, R.drawable.spadesk, 4, mA);
		Card spadesa = new Card(ctx, 14, R.drawable.spadesa, 4, mA);

		//Add all the cards to the deck
		add(hearts2);
		add(hearts3);
		add(hearts4);
		add(hearts5);
		add(hearts6);
		add(hearts7);
		add(hearts8);
		add(hearts9);
		add(hearts10);
		add(heartsj);
		add(heartsq);
		add(heartsk);
		add(heartsa);

		add(clubs2);
		add(clubs3);
		add(clubs4);
		add(clubs5);
		add(clubs6);
		add(clubs7);
		add(clubs8);
		add(clubs9);
		add(clubs10);
		add(clubsj);
		add(clubsq);
		add(clubsk);
		add(clubsa);

		add(diamonds2);
		add(diamonds3);
		add(diamonds4);
		add(diamonds5);
		add(diamonds6);
		add(diamonds7);
		add(diamonds8);
		add(diamonds9);
		add(diamonds10);
		add(diamondsj);
		add(diamondsq);
		add(diamondsk);
		add(diamondsa);

		add(spades2);
		add(spades3);
		add(spades4);
		add(spades5);
		add(spades6);
		add(spades7);
		add(spades8);
		add(spades9);
		add(spades10);
		add(spadesj);
		add(spadesq);
		add(spadesk);
		add(spadesa);
	}
}
