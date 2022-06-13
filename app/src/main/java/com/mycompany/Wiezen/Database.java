package com.mycompany.Wiezen;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.provider.*;
import android.util.*;
import java.util.*;

public class Database
{
	private static int table;

	private static final String TAG = "Database";

    //The columns we'll include in the tables
	public static final String KEY_SAVED_NAME = "SavedName";
	public static final String KEY_ACTIVE = "Active";
	public static final String KEY_DEALER = "Dealer";
	public static final String KEY_DOUBLEPOINTS = "DoublePoints";
	public static final String KEY_STARTPLAYER = "StartPlayer";
	public static final String KEY_OLDSTARTPLAYER = "OldStartPlayer";
	public static final String KEY_PLAYER = "Player";
	public static final String KEY_TRUMPCATEGORY = "TrumpCategory";
	public static final String KEY_MAINMENU= "MainMenu";
	public static final String KEY_CHOOSEMENU = "ChooseMenu";
	public static final String KEY_PLAYMENU = "PlayMenu";
	public static final String KEY_MAXCHOICE = "MaxChoice";
	public static final String KEY_TROELPLAYER = "TroelPlayer";
	public static final String KEY_TROELMEEPLAYER = "TroelMeePlayer";
	public static final String KEY_ALLEEN = "Alleen";
	public static final String KEY_GAMEEND = "GameEnd";
	public static final String KEY_ANIMATIONDEALINGCARDS = "AnimationDealingCards";

	public static final String KEY_DECK_NAME = "DeckName";
	public static final String KEY_INGAME = "InGame";
	public static final String KEY_VALUE = "Value";
	public static final String KEY_CARDREFERENCE = "CardReference";
	public static final String KEY_CATEGORY = "Category";
	public static final String KEY_TRUMPCARD = "TrumpCard";

	public static final String KEY_TRUMPCAT = "TrumpCategory";
	public static final String KEY_POINTS = "Points";
	public static final String KEY_CHOICE = "Choice";	
	public static final String KEY_OLDCHOICE = "OldChoice";

	public static final String KEY_FOLLOWED ="Followed";
	public static final String KEY_OTHERPLAYER ="OtherPlayer";

	public static final String KEY_OBJPLAYER = "ObjPlayer";
	public static final String KEY_OBJCATEGORY = "ObjCategory";
	public static final String KEY_OBJAVERAGE = "ObjAverage";
	public static final String KEY_OBJREMAININGCARDS = "ObjRemainingCards";
	public static final String KEY_OBJCHECKEDCARDS = "ObjCheckedCards";
	public static final String KEY_OBJUNDERPLAYER = "ObjUnderPlayer";

	public static final String KEY_PLAYCATEGORY = "PlayCategory";

	public static final String KEY_OKAY = "Okay";

	private static final String FTS_VIRTUAL_TABLE = "FTS";
	private static final String FTS_VIRTUAL_TABLE_CARDS = "FTSCards";
	private static final String FTS_VIRTUAL_TABLE_PLAYERS = "FTSPlayers";
	private static final String FTS_VIRTUAL_TABLE_OBJECTS = "FTSObjects";
	private static final String FTS_VIRTUAL_TABLE_FOLLOWED = "FTSFollowed";
	private static final String FTS_VIRTUAL_TABLE_PLAYPARAM = "FTSPlayCategory";

    private static final int DATABASE_VERSION = 1;

    private OpenHelper mDbHelper;
	private SQLiteDatabase mDb;
	private static String activeName;
	
    private static final HashMap<String,String> buildMap()
	{
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(KEY_SAVED_NAME, KEY_SAVED_NAME);
		map.put(KEY_ACTIVE, KEY_ACTIVE);
		map.put(KEY_DEALER, KEY_DEALER);
		map.put(KEY_DOUBLEPOINTS,KEY_DOUBLEPOINTS);
		map.put(KEY_STARTPLAYER, KEY_STARTPLAYER);
        map.put(KEY_PLAYER, KEY_PLAYER);
		map.put(KEY_OLDSTARTPLAYER, KEY_OLDSTARTPLAYER);
		map.put(KEY_TRUMPCATEGORY, KEY_TRUMPCATEGORY);
		map.put(KEY_MAINMENU, KEY_MAINMENU);
		map.put(KEY_CHOOSEMENU, KEY_CHOOSEMENU);
		map.put(KEY_PLAYMENU, KEY_PLAYMENU);	
		map.put(KEY_MAXCHOICE, KEY_MAXCHOICE);
		map.put(KEY_TROELPLAYER, KEY_TROELPLAYER);
		map.put(KEY_TROELMEEPLAYER, KEY_TROELMEEPLAYER);
		map.put(KEY_ALLEEN, KEY_ALLEEN);
		map.put(KEY_GAMEEND, KEY_GAMEEND);
		map.put(KEY_ANIMATIONDEALINGCARDS, KEY_ANIMATIONDEALINGCARDS);

        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
        return map;
    }

	private static final HashMap<String,String> buildMapCards()
	{
        HashMap<String,String> map = new HashMap<String,String>();
		map.put(KEY_SAVED_NAME, KEY_SAVED_NAME);
		map.put(KEY_INGAME, KEY_INGAME);
		map.put(KEY_DECK_NAME, KEY_DECK_NAME);
		map.put(KEY_VALUE, KEY_VALUE);
		map.put(KEY_CARDREFERENCE, KEY_CARDREFERENCE);
		map.put(KEY_CATEGORY, KEY_CATEGORY);
		map.put(KEY_TRUMPCARD, KEY_TRUMPCARD);
        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
        return map;
    }

	private static final HashMap<String,String> buildMapPlayers()
	{
        HashMap<String,String> map = new HashMap<String,String>();
		map.put(KEY_SAVED_NAME, KEY_SAVED_NAME);
        map.put(KEY_PLAYER, KEY_PLAYER);
		map.put(KEY_TRUMPCAT, KEY_TRUMPCAT);
		map.put(KEY_POINTS, KEY_POINTS);
		map.put(KEY_CHOICE, KEY_CHOICE);
		map.put(KEY_OLDCHOICE, KEY_OLDCHOICE);
        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
        return map;
    }

	private static HashMap<String,String> buildMapFollowed()
	{
        HashMap<String,String> map = new HashMap<String,String>();
		map.put(KEY_SAVED_NAME, KEY_SAVED_NAME);
		map.put(KEY_PLAYER, KEY_PLAYER);//deze is enkel voor duidelijkheid in de tabel
		map.put(KEY_OTHERPLAYER, KEY_OTHERPLAYER);//deze is enkel voor duidelijkheid in de tabel
		map.put(KEY_CATEGORY, KEY_CATEGORY);//deze is enkel voor duidelijkheid in de tabel
		map.put(KEY_FOLLOWED, KEY_FOLLOWED);
        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
        return map;
    }
	
	private static HashMap<String,String> buildMapObjects()
	{
        HashMap<String,String> map = new HashMap<String,String>();
		map.put(KEY_SAVED_NAME, KEY_SAVED_NAME);
		map.put(KEY_OBJPLAYER, KEY_OBJPLAYER);
		map.put(KEY_OBJCATEGORY, KEY_OBJCATEGORY);
		map.put(KEY_OBJAVERAGE, KEY_OBJAVERAGE);
		map.put(KEY_OBJREMAININGCARDS, KEY_OBJREMAININGCARDS);
		map.put(KEY_OBJCHECKEDCARDS, KEY_OBJCHECKEDCARDS);
		map.put(KEY_OBJUNDERPLAYER, KEY_OBJUNDERPLAYER);
        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
        return map;
    }
	
	private static HashMap<String,String> buildMapPlayParam()
	{
        HashMap<String,String> map = new HashMap<String,String>();
		map.put(KEY_SAVED_NAME, KEY_SAVED_NAME);
		map.put(KEY_PLAYCATEGORY, KEY_PLAYCATEGORY);
		map.put(KEY_OKAY, KEY_OKAY);
        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
        return map;
    }
	
	private String getTable()
	{
		switch (table)
		{
			case 1:return FTS_VIRTUAL_TABLE;
			case 2:return FTS_VIRTUAL_TABLE_CARDS;
			case 3:return FTS_VIRTUAL_TABLE_PLAYERS;
			case 4:return FTS_VIRTUAL_TABLE_FOLLOWED;
			case 5:return FTS_VIRTUAL_TABLE_OBJECTS;
			case 6:return FTS_VIRTUAL_TABLE_PLAYPARAM;
			default:return "";
		}
	}

	private HashMap<String,String> getMap()
	{
		switch (table)
		{
			case 1:return buildMap();
			case 2:return buildMapCards();
			case 3:return buildMapPlayers();
			case 4:return buildMapFollowed();
			case 5:return buildMapObjects();
			case 6:return buildMapPlayParam();
			default:return null;
		}
	}

	private Cursor query(String selection, String[] selectionArgs, String[] columns, String sortOrder)
	{
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(getTable());
        builder.setProjectionMap(getMap());
        Cursor cursor = builder.query(mDbHelper.getReadableDatabase(),             
									  columns, selection, selectionArgs, null, null, sortOrder); //DESC  ASC
		if (cursor == null)
		{                                                     
            return null;
        }
		else if (!cursor.moveToFirst())
		{
            cursor.close();
            return null;
        }
        return cursor;
    }

	private void getActiveName()//private
	{
		String selection=KEY_ACTIVE + " = ?";
		String[] selectionArgs = new String[] {"true"};
		setTableAndMap(1);
		Cursor curs=query(selection, selectionArgs, null, null);
		activeName = curs.getString(curs.getColumnIndex(Database.KEY_SAVED_NAME));
	}

	private Cursor getSavedName()
	{
		String selection=KEY_SAVED_NAME + " = ?";
		String[] selectionArgs = new String[] {activeName};
		return query(selection, selectionArgs, null, null);
	}

	private Cursor getSavedNameForCards(String deckName)
	{
		String selection=KEY_SAVED_NAME + " = ?" + " AND " + KEY_DECK_NAME + " = ?";
		String[] selectionArgs = new String[] {activeName,deckName};
		return query(selection, selectionArgs, null, null);
	}

	public boolean getParameters(Game game, MainActivity mA)
	{
		setTableAndMap(1);
		Cursor curs = getSavedName();
		if (curs.getInt(curs.getColumnIndex(Database.KEY_DEALER)) == 0)return false;
		game.dealer = curs.getInt(curs.getColumnIndex(Database.KEY_DEALER));
		game.doublePoints = curs.getInt(curs.getColumnIndex(Database.KEY_DOUBLEPOINTS));
		game.startPlayer = curs.getInt(curs.getColumnIndex(Database.KEY_STARTPLAYER));
		game.oldStartPlayer = curs.getInt(curs.getColumnIndex(Database.KEY_OLDSTARTPLAYER));
		game.player = curs.getInt(curs.getColumnIndex(Database.KEY_PLAYER));
		game.trumpCategory = curs.getInt(curs.getColumnIndex(Database.KEY_TRUMPCATEGORY));
		mA.mainMenu = Boolean.valueOf(curs.getString(curs.getColumnIndex(Database.KEY_MAINMENU)));
		mA.chooseMenu = Boolean.valueOf(curs.getString(curs.getColumnIndex(Database.KEY_CHOOSEMENU)));
		mA.playMenu = Boolean.valueOf(curs.getString(curs.getColumnIndex(Database.KEY_PLAYMENU)));
		game.maxChoice = curs.getInt(curs.getColumnIndex(Database.KEY_MAXCHOICE));
		game.troelPlayer = curs.getInt(curs.getColumnIndex(Database.KEY_TROELPLAYER));
		game.troelMeePlayer = curs.getInt(curs.getColumnIndex(Database.KEY_TROELMEEPLAYER));
		game.alleen = Boolean.valueOf(curs.getString(curs.getColumnIndex(Database.KEY_ALLEEN)));
		game.gameEnd = Boolean.valueOf(curs.getString(curs.getColumnIndex(Database.KEY_GAMEEND)));
		mA.mCheckBoxAnimationDealingCards.getcheckBox().setChecked(Boolean.valueOf(curs.getString(curs.getColumnIndex(Database.KEY_ANIMATIONDEALINGCARDS))));
		return true;
	}

	public ArrayList<Card> getCards(Context context, String deckName, MainActivity mA)
	{
		ArrayList<Card> deck = new ArrayList<Card>();
		setTableAndMap(2);
		Cursor curs = getSavedNameForCards(deckName);
		if (curs != null)
		{		
			do{
				String in=curs.getString(curs.getColumnIndex(Database.KEY_INGAME));
				boolean inGame= Boolean.valueOf(in);
				if (inGame)
				{
					int value=curs.getInt(curs.getColumnIndex(Database.KEY_VALUE));
					int cardreference=curs.getInt(curs.getColumnIndex(Database.KEY_CARDREFERENCE));
					int category=curs.getInt(curs.getColumnIndex(Database.KEY_CATEGORY));
					Card card = new Card(context, value, cardreference, category, mA);//null
					String trmpCrd=curs.getString(curs.getColumnIndex(Database.KEY_TRUMPCARD));
					boolean trumpCard= Boolean.valueOf(trmpCrd);
					card.trumpCard = trumpCard;
					deck.add(card);
				}
				else break;		
			}while (curs.moveToNext());
		}
		return deck;
	}

	public boolean getPlayers(Game game)
	{
		setTableAndMap(3);
		Cursor curs = getSavedName();
		if (curs == null) return false;
		int a=-1;
		do
		{
			a = curs.getInt(curs.getColumnIndex(Database.KEY_PLAYER));
			game.playerTrumpCategory[a] = curs.getInt(curs.getColumnIndex(Database.KEY_TRUMPCAT));
			game.playerPoints[a] = curs.getInt(curs.getColumnIndex(Database.KEY_POINTS));
			game.playerChoice[a] = curs.getInt(curs.getColumnIndex(Database.KEY_CHOICE));
			game.oldPlayerChoice[a] = curs.getInt(curs.getColumnIndex(Database.KEY_OLDCHOICE));
		}
		while(curs.moveToNext());
		return true;
	}

	private void getFollowed(Game game)
	{
		setTableAndMap(4);
		Cursor curs = getSavedName();
		if (curs != null)
		{
			String[][][]followed=new String[5][5][5];
			for (int a = 1;a <= 4;a++)
			{
				for (int b=1;b <= 4;b++)
				{
					for (int c=1;c <= 4;c++)
					{
						if (a != b)
						{
							followed[a][b][c] = curs.getString(curs.getColumnIndex(Database.KEY_FOLLOWED));
							game.followed[a][b][c] = Boolean.valueOf(followed[a][b][c]);
							curs.moveToNext();
						}	
					}		
				}			
			}	
		}
	}
	
	private void getObjects(Game game)
	{
		setTableAndMap(5);
		Cursor curs = getSavedName();
		if (curs != null)
		{
			do{
				int objP=curs.getInt(curs.getColumnIndex(Database.KEY_OBJPLAYER));
				int objC=curs.getInt(curs.getColumnIndex(Database.KEY_OBJCATEGORY));
				float objA=curs.getFloat(curs.getColumnIndex(Database.KEY_OBJAVERAGE));
				int objR=curs.getInt(curs.getColumnIndex(Database.KEY_OBJREMAININGCARDS));
				int objCH=curs.getInt(curs.getColumnIndex(Database.KEY_OBJCHECKEDCARDS));
				int objU=curs.getInt(curs.getColumnIndex(Database.KEY_OBJUNDERPLAYER));
				if (objP != 0)
				{
					ObjPlayer objPl=new ObjPlayer(objP, objC, objA, objR, objCH, objU);
					game.getCategory.add(objPl);
				}
			}while(curs.moveToNext());
		}
	}
	
	private void getPlayparam(Game game)
	{
		setTableAndMap(6);
		Cursor curs = getSavedName();
		if (curs != null)
		{
			String[][]okay=new String[5][5];
			for (int a = 1;a <= 4;a++)
			{
				for (int b=1;b <= 4;b++)
				{
					int inPlay=curs.getInt(curs.getColumnIndex(Database.KEY_PLAYCATEGORY));
					if (inPlay != 0)
					{game.playCategory[a].add(inPlay);}	
					okay[a][b] = curs.getString(curs.getColumnIndex(Database.KEY_OKAY));
					game.okay[a][b] = Boolean.valueOf(okay[a][b]);
					curs.moveToNext();	
				}
			}	
		}
	}
	
	public void getPlay(Game game)
	{
		getFollowed(game);
		getObjects(game);
		getPlayparam(game);
	}

	private static final String FTS_TABLE_CREATE =
	"CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
	" USING fts3 (" +
	KEY_SAVED_NAME + ", " +
	KEY_ACTIVE + ", " +
	KEY_DEALER + " INT " + ", " +
	KEY_DOUBLEPOINTS + " INT " + ", " +
	KEY_STARTPLAYER + " INT " + ", " +
	KEY_OLDSTARTPLAYER + " INT " + ", " +
	KEY_PLAYER + " INT " + ", " +
	KEY_TRUMPCATEGORY + " INT " + ", " +
	KEY_MAINMENU + ", " +
	KEY_CHOOSEMENU + ", " +
	KEY_PLAYMENU  + ", " +
	KEY_MAXCHOICE + " INT "  + ", " +
	KEY_TROELPLAYER + " INT "  + ", " +
	KEY_TROELMEEPLAYER + " INT "  + ", " +
	KEY_ANIMATIONDEALINGCARDS + ", " +
	KEY_ALLEEN + ", " +
	KEY_GAMEEND +
	"); ";

	private static final String FTS_TABLE_CREATE_CARDS =
	"CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE_CARDS +
	" USING fts3 (" +
	KEY_SAVED_NAME + ", " +
	KEY_INGAME + ", " +
	KEY_DECK_NAME + ", " +
	KEY_VALUE + " INT " +  ", " +
	KEY_CARDREFERENCE  + " INT " +  ", " +
	KEY_CATEGORY + " INT " + ", " +
	KEY_TRUMPCARD + 
	"); ";

	private static final String FTS_TABLE_CREATE_PLAYERS =
	"CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE_PLAYERS +
	" USING fts3 (" +
	KEY_SAVED_NAME + ", " +
	KEY_PLAYER + " INT " +  ", " +
	KEY_TRUMPCAT  + " INT " +  ", " +
	KEY_POINTS + " INT " + ", " +
	KEY_CHOICE + " INT " + ", " +
	KEY_OLDCHOICE + " INT " +
	"); ";
	
	private static final String FTS_TABLE_CREATE_FOLLOWED =
	"CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE_FOLLOWED +
	" USING fts3 (" +
	KEY_SAVED_NAME + ", " +
	KEY_PLAYER + " INT " + ", " +
	KEY_OTHERPLAYER + " INT " + ", " +
	KEY_CATEGORY + " INT " + ", " +
	KEY_FOLLOWED +
	"); ";

	private static final String FTS_TABLE_CREATE_OBJECTS =
	"CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE_OBJECTS +
	" USING fts3 (" +
	KEY_SAVED_NAME + ", " +
	KEY_OBJPLAYER + " INT " + ", " +
	KEY_OBJCATEGORY + " INT " + ", " +
	KEY_OBJAVERAGE + " FLOAT " + ", " +
	KEY_OBJREMAININGCARDS + " INT " + ", " +
	KEY_OBJCHECKEDCARDS + " INT " + ", " +
	KEY_OBJUNDERPLAYER + " INT " + "); "; 
	
	private static final String FTS_TABLE_CREATE_PLAYPARAM =  
	"CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE_PLAYPARAM +
	" USING fts3 (" +
	KEY_SAVED_NAME + ", " +
	KEY_OKAY + ", " +
	KEY_PLAYCATEGORY + " INT " + "); ";
	
	private final Context mCtx;
	private boolean isExternalStorage;

	//This creates/opens the database.
    private static class OpenHelper extends SQLiteOpenHelper
	{

		OpenHelper(Context context, final String DATABASE_NAME)
		{
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
		{
			Log.w(TAG, FTS_TABLE_CREATE);
			db.execSQL(FTS_TABLE_CREATE);
			Log.w(TAG, FTS_TABLE_CREATE_CARDS);
			db.execSQL(FTS_TABLE_CREATE_CARDS);
			Log.w(TAG, FTS_TABLE_CREATE_PLAYERS);
			db.execSQL(FTS_TABLE_CREATE_PLAYERS);
			Log.w(TAG, FTS_TABLE_CREATE_OBJECTS);
			db.execSQL(FTS_TABLE_CREATE_OBJECTS);
			Log.w(TAG, FTS_TABLE_CREATE_FOLLOWED);
			db.execSQL(FTS_TABLE_CREATE_FOLLOWED);
			Log.w(TAG, FTS_TABLE_CREATE_PLAYPARAM);
			db.execSQL(FTS_TABLE_CREATE_PLAYPARAM);
        }
		
		@Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				  + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_CARDS);
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_PLAYERS);
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_OBJECTS);
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_FOLLOWED);
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_PLAYPARAM);
            onCreate(db);
        }
    }		

	public Database(Context ctx, boolean bool)
	{
		this.isExternalStorage = bool;
		this.mCtx = ctx;
	}

    public Database open() throws SQLException
	{
		String DATABASE_NAME = "Wiezen.db"; //.db moet niet
		if (isExternalStorage)
		{ DATABASE_NAME = "/sdcard/AppProjects/Wiezen/Wiezen.db";}
		mDbHelper = new OpenHelper(mCtx, DATABASE_NAME);
		mDb = mDbHelper.getWritableDatabase();

		setTableAndMap(1);
		Cursor curs=query(null, null, null, null);
		if (curs == null) 
		{
			setActiveName("save1");
		}
		else
		{
			getActiveName();
		}
		return this;
	}

	public boolean isOpen()
	{
		return mDb.isOpen();
	}

	public void close()
	{
		if (mDbHelper != null)
		{
			mDbHelper.close();
		}
	}

	private void setTableAndMap(int number)
	{
		table = number;
	}

	public void setActiveName(String activeName)
	{
		//zet volledige lijst op false
		setTableAndMap(1);
		Cursor curs = query(null, null, null, null);
		ContentValues initialValues = new ContentValues();
		initialValues = new ContentValues();
		initialValues.put(KEY_ACTIVE, "false");
		if (curs != null)
		{
			do
			{
				long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
				mDb.update(getTable(), initialValues, "rowid = " + id, null);
			}while(curs.moveToNext());
		}
		//zet nieuwe of al bestaande naam actief
		this.activeName = activeName;
		curs = getSavedName();
		initialValues = new ContentValues();
		initialValues.put(KEY_SAVED_NAME, activeName);
		initialValues.put(KEY_ACTIVE, "true");
		if (curs == null)
		{
			mDb.insert(getTable(), null, initialValues);
		}
		else
		{
			long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
			mDb.update(getTable(), initialValues, "rowid = " + id, null);
		}		
	}

	public void setParameters(Game game, MainActivity mA)
	{
		setTableAndMap(1);
		Cursor curs =getSavedName();
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DEALER, game.dealer);
		initialValues.put(KEY_DOUBLEPOINTS, game.doublePoints);
		initialValues.put(KEY_STARTPLAYER, game.startPlayer);
		initialValues.put(KEY_OLDSTARTPLAYER, game.oldStartPlayer);
		initialValues.put(KEY_PLAYER, game.player);
		initialValues.put(KEY_TRUMPCATEGORY, game.trumpCategory);
		initialValues.put(KEY_MAINMENU, String.valueOf(mA.mainMenu));
		initialValues.put(KEY_CHOOSEMENU, String.valueOf(mA.chooseMenu));
		initialValues.put(KEY_PLAYMENU, String.valueOf(mA.playMenu));
		initialValues.put(KEY_MAXCHOICE, game.maxChoice);
		initialValues.put(KEY_TROELPLAYER, game.troelPlayer);
		initialValues.put(KEY_TROELMEEPLAYER, game.troelMeePlayer);
		initialValues.put(KEY_ALLEEN, String.valueOf(game.alleen));
		initialValues.put(KEY_GAMEEND, String.valueOf(game.gameEnd));
		initialValues.put(KEY_ANIMATIONDEALINGCARDS, String.valueOf(mA.mCheckBoxAnimationDealingCards.getcheckBox().isChecked()));
		long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
		mDb.update(getTable(), initialValues, "rowid = " + id, null);	
	}

	public void setCards(Deck deck, String deckName)
	{
		setTableAndMap(2);
		Cursor curs =getSavedNameForCards(deckName);
		ContentValues initialValues = new ContentValues();
		for (int a=1;a <= deck.size();a++)
		{
			initialValues.put(KEY_SAVED_NAME, activeName);
			initialValues.put(KEY_INGAME, "true");
			initialValues.put(KEY_DECK_NAME, deckName);
			initialValues.put(KEY_VALUE, deck.card(a).value);
			initialValues.put(KEY_CARDREFERENCE, deck.card(a).cardReference);
			initialValues.put(KEY_CATEGORY, deck.card(a).category);
			initialValues.put(KEY_TRUMPCARD, String.valueOf(deck.card(a).trumpCard));

			if (curs == null || curs.isAfterLast())
			{
				mDb.insert(getTable(), null, initialValues);
			}
			else
			{
				long id = curs.getLong(curs.getColumnIndex(BaseColumns._ID));
				mDb.update(getTable(), initialValues, "rowid = " + id, null);
				curs.moveToNext();
			}	
		}
		while (curs != null && !curs.isAfterLast())
		{
			initialValues.put(KEY_SAVED_NAME, activeName);
			initialValues.put(KEY_INGAME, "false");
			initialValues.put(KEY_DECK_NAME, deckName);
			initialValues.put(KEY_VALUE, 100);
			initialValues.put(KEY_CARDREFERENCE, 100);
			initialValues.put(KEY_CATEGORY, 100);
			initialValues.put(KEY_TRUMPCARD, "false");
			long id = curs.getLong(curs.getColumnIndex(BaseColumns._ID));
			mDb.update(getTable(), initialValues, "rowid = " + id, null);
			curs.moveToNext();
		}	
	}

	public void setPlayers(Game game, int player)
	{
		setTableAndMap(3);
		Cursor curs = getSavedName();
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SAVED_NAME, activeName);
		initialValues.put(KEY_PLAYER, player);
		initialValues.put(KEY_TRUMPCAT, game.playerTrumpCategory[player]);
		initialValues.put(KEY_POINTS, game.playerPoints[player]);
		initialValues.put(KEY_CHOICE, game.playerChoice[player]);
		initialValues.put(KEY_OLDCHOICE, game.oldPlayerChoice[player]);
		if (curs == null)
		{
			if (player == 0)
			{
				for (int a=1;a <= 4;a++)
				{
					initialValues.put(KEY_PLAYER, a);
					initialValues.put(KEY_TRUMPCAT, game.playerTrumpCategory[a]);
					initialValues.put(KEY_POINTS, game.playerPoints[a]);
					initialValues.put(KEY_CHOICE, game.playerChoice[a]);
					initialValues.put(KEY_OLDCHOICE, game.oldPlayerChoice[a]);
					mDb.insert(getTable(), null, initialValues);
				}
			}
			else
			{
				mDb.insert(getTable(), null, initialValues);//mag later weg???
			}
		}
		else
		{
			int a;
			if (player == 0)
			{
				a = 0;
				do
				{
					a++;
					initialValues.put(KEY_PLAYER, a);
					initialValues.put(KEY_TRUMPCAT, game.playerTrumpCategory[a]);
					initialValues.put(KEY_POINTS, game.playerPoints[a]);
					initialValues.put(KEY_CHOICE, game.playerChoice[a]);
					initialValues.put(KEY_OLDCHOICE, game.oldPlayerChoice[a]);
					long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
					mDb.update(getTable(), initialValues, "rowid = " + id, null);
				}
				while(curs.moveToNext());
			}
			else
			{
				do
				{
					a = curs.getInt(curs.getColumnIndex(Database.KEY_PLAYER));
					if (a == player)
					{
						long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
						mDb.update(getTable(), initialValues, "rowid = " + id, null);
					}
				}
				while(curs.moveToNext());
			}
		}		
	}

	private void setFollowed(Game game)
	{
		setTableAndMap(4);
		Cursor curs = getSavedName();
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SAVED_NAME, activeName);
		if (curs == null)
		{
			for (int a=1;a <= 4;a++)
			{
				for (int b=1;b <= 4;b++)
				{
					for (int c=1;c <= 4;c++)
					{
						if (a != b)
						{
							initialValues.put(KEY_PLAYER, a);
							initialValues.put(KEY_OTHERPLAYER, b);
							initialValues.put(KEY_CATEGORY, c);
							initialValues.put(KEY_FOLLOWED, String.valueOf(game.followed[a][b][c]));
							mDb.insert(FTS_VIRTUAL_TABLE_FOLLOWED, null, initialValues);
						}
					}	
				}					
			}	
		}
		else
		{
			for (int a=1;a <= 4;a++)
			{
				for (int b=1;b <= 4;b++)
				{
					for (int c=1;c <= 4;c++)
					{
						if (a != b)
						{
							initialValues.put(KEY_FOLLOWED, String.valueOf(game.followed[a][b][c]));
							long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
							mDb.update(FTS_VIRTUAL_TABLE_FOLLOWED, initialValues, "rowid = " + id, null);
							curs.moveToNext();
						}
					}	
				}					
			}		
		}	
	}
	
	private void setObjects(Game game)
	{
		setTableAndMap(5);
		Cursor curs = getSavedName();
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SAVED_NAME, activeName);
		int z=-1;
		if (curs == null)
		{
			for (int b=0;b < game.getCategory.size();b++)
			{
				z++;initialValues.put(KEY_OBJPLAYER, game.getCategory.get(b).player);
				initialValues.put(KEY_OBJCATEGORY, game.getCategory.get(b).category);
				initialValues.put(KEY_OBJAVERAGE, game.getCategory.get(b).average);
				initialValues.put(KEY_OBJREMAININGCARDS, game.getCategory.get(b).remainingCards);
				initialValues.put(KEY_OBJCHECKEDCARDS, game.getCategory.get(b).checkedCards);
				initialValues.put(KEY_OBJUNDERPLAYER, game.getCategory.get(b).underPlayer);
				mDb.insert(FTS_VIRTUAL_TABLE_OBJECTS, null, initialValues);
			}
			while (z < 15)
			{
				z++;initialValues.put(KEY_OBJPLAYER, 0);
				initialValues.put(KEY_OBJCATEGORY, 0);
				initialValues.put(KEY_OBJAVERAGE, 0);
				initialValues.put(KEY_OBJREMAININGCARDS, -10);
				initialValues.put(KEY_OBJCHECKEDCARDS, -10);
				initialValues.put(KEY_OBJUNDERPLAYER, -1);
				mDb.insert(FTS_VIRTUAL_TABLE_OBJECTS, null, initialValues);
			}		
		}
		else
		{
			for (int b=0;b < game.getCategory.size();b++)
			{
				z++;initialValues.put(KEY_OBJPLAYER, game.getCategory.get(b).player);
				initialValues.put(KEY_OBJCATEGORY, game.getCategory.get(b).category);
				initialValues.put(KEY_OBJAVERAGE, game.getCategory.get(b).average);
				initialValues.put(KEY_OBJREMAININGCARDS, game.getCategory.get(b).remainingCards);
				initialValues.put(KEY_OBJCHECKEDCARDS, game.getCategory.get(b).checkedCards);
				initialValues.put(KEY_OBJUNDERPLAYER, game.getCategory.get(b).underPlayer);
				long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
				mDb.update(FTS_VIRTUAL_TABLE_OBJECTS, initialValues, "rowid = " + id, null);
				curs.moveToNext();
			}	
			while (z < 15)
			{
				z++;initialValues.put(KEY_OBJPLAYER, 0);
				initialValues.put(KEY_OBJCATEGORY, 0);
				initialValues.put(KEY_OBJAVERAGE, 0);
				initialValues.put(KEY_OBJREMAININGCARDS, -10);
				initialValues.put(KEY_OBJCHECKEDCARDS, -10);
				initialValues.put(KEY_OBJUNDERPLAYER, -1);
				long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
				mDb.update(FTS_VIRTUAL_TABLE_OBJECTS, initialValues, "rowid = " + id, null);
				curs.moveToNext();
			}
		}	
	}
	
	private void setPlayparam(Game game)
	{
		setTableAndMap(6);
		Cursor curs = getSavedName();
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SAVED_NAME, activeName);
		//Date dt=new Date();
		//System.out.println(dt);
		//1 value saven=<1ms, 16=+/-180ms
		int z;
		if (curs == null)
		{
			for (int a=1;a <= 4;a++)
			{
				z = 0;
				for (int b=0;b < game.playCategory[a].size();b++)
				{
					z++;initialValues.put(KEY_PLAYCATEGORY, game.playCategory[a].get(b));
					initialValues.put(KEY_OKAY, String.valueOf(game.okay[a][z]));
					mDb.insert(FTS_VIRTUAL_TABLE_PLAYPARAM, null, initialValues);
				}
				while (z < 4)
				{
					z++;initialValues.put(KEY_PLAYCATEGORY, 0);
					initialValues.put(KEY_OKAY, String.valueOf(game.okay[a][z]));
					mDb.insert(FTS_VIRTUAL_TABLE_PLAYPARAM, null, initialValues);
				}
			}	
		}
		else
		{
			for (int a=1;a <= 4;a++)
			{
				z = 0;
				for (int b=0;b < game.playCategory[a].size();b++)
				{
					z++;initialValues.put(KEY_PLAYCATEGORY, game.playCategory[a].get(b));
					initialValues.put(KEY_OKAY, String.valueOf(game.okay[a][z]));
					long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
					mDb.update(FTS_VIRTUAL_TABLE_PLAYPARAM, initialValues, "rowid = " + id, null);
					curs.moveToNext();
				}
				while (z < 4)
				{
					z++;initialValues.put(KEY_PLAYCATEGORY, 0);
					initialValues.put(KEY_OKAY, String.valueOf(game.okay[a][z]));
					long id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
					mDb.update(FTS_VIRTUAL_TABLE_PLAYPARAM, initialValues, "rowid = " + id, null);
					curs.moveToNext();
				}
			}	
		}	
	}
	
	public void setPlay(Game game)
	{
		setFollowed(game);
		setObjects(game);
		setPlayparam(game);
	}
}

