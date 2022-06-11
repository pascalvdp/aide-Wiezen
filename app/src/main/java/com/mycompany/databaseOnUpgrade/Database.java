package com.mycompany.databaseOnUpgrade;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.provider.*;
import android.widget.*;
import com.mycompany.databaseOnUpgrade.*;
import java.util.*;

public class Database
{
	//bij een virtuele table kan je geen kolom toevoegen, bij een gewone table wel, dus oplossing =
	//hier zetten we gegevens over naar een andere table waar de extra kolom(men) al zijn toegevoegd
	//dan delete eerste table, maken een nieuwe table met de extra kolom(men) en zetten dan alle
	//gegevens terug naar deze table
	public static final String KEY_PLAYER = "Player";
	public static final String COLUMN_COLOR = "Color";//dit wordt er ook bijgezet

	private static final String FTS_VIRTUAL_TABLE = "FTSVirtualTable";
	private static final String VIRTUAL_DAT = "VirtualDat";

    private static final int DATABASE_VERSION = 1;
	//eerst app starten, dan op 2 zetten
	//als de kolommen zijn toegevoegd dit op 3 zetten

    private FoodOpenHelper mDbHelper;
	private SQLiteDatabase mDb;//java.sql kan je ook importeren
    private static final HashMap<String,String> mColumnMap = buildColumnMap();

    private static HashMap<String,String> buildColumnMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(KEY_PLAYER, KEY_PLAYER);
	//	map.put(COLUMN_COLOR, COLUMN_COLOR);//dit is erbij gezet
        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
        return map;
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns, String sortOrder) {
        //contentprovider moet geen kolomnamen kennen
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);
        builder.setProjectionMap(mColumnMap);
        Cursor cursor = builder.query(mDbHelper.getReadableDatabase(),             
									  columns, selection, selectionArgs, null, null, sortOrder); //DESC  ASC
		if (cursor == null) {                                                     
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

	public int getPlayer(){
		int player=0;
		Cursor curs = query(null, null, null, null);
		if (curs != null){player = curs.getInt(curs.getColumnIndex(Database.KEY_PLAYER));}
		return player;
	}
	
	public int getColor(){
		int color=0;
		Cursor curs = query(null, null, null, null);
		if (curs != null){color = curs.getInt(curs.getColumnIndex(Database.COLUMN_COLOR));}
		return color;
	}

	public long getId(){
		long id=-10;
		Cursor curs = query(null, null, null, null);
		if (curs != null){id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));}
		return id;
	}

	//een UNIQUE key gebruiken we als alle rows verschillend moeten zijn
	//rowid is automatisch een unieke identificatie
	private static final String FTS_TABLE_CREATE =
	"CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
	" USING fts3 (" +
//	COLUMN_COLOR + " INT " + ", " + //dit is erbij gezet
	KEY_PLAYER + " INT " + "); "; //DATETIME TEXT INTEGER INT REAL
	
	private static final String FTS_TABLE2_CREATE =
	"CREATE VIRTUAL TABLE " + VIRTUAL_DAT +
	" USING fts3 (" +
	COLUMN_COLOR + " INT DEFAULT 50 " + ", " + //DEFAULT 50 werkt niet
	KEY_PLAYER + " INT " + "); "; 
	
	//private static final String DATABASE_ALTER_DATABASE = "ALTER TABLE " +  //alter=wijzigen
	//FTS_VIRTUAL_TABLE + " ADD COLUMN" + COLUMN_COLOR + " INTEGER DEFAULT 0;";
	//bij een virtuele table kan je geen kolom toevoegen, bij een gewone table wel, dit werkt dus niet

	private final Context mCtx;

	//This creates/opens the database.
    private static class FoodOpenHelper extends SQLiteOpenHelper {

		FoodOpenHelper(Context context,final String DATABASE_NAME) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
			db.execSQL(FTS_TABLE_CREATE);
			db.execSQL(FTS_TABLE2_CREATE);
        }

		@Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//hier gaat men 1 keer in als oldVersion != newVersion
           // onCreate(db);dit is ook mogelijk
			int upgradeTo = oldVersion+1;
			while(upgradeTo <= newVersion){
				switch(upgradeTo){
					case 2:
						//dit is als de kolommen hetzelfde zijn, de volgorde van de kolommen speelt geen rol
						//db.execSQL("INSERT INTO " + VIRTUAL_DAT + " SELECT * FROM " + FTS_VIRTUAL_TABLE);
						//dit is als de kolommen niet hetzelfde zijn
						db.execSQL("INSERT INTO " + VIRTUAL_DAT + "(" + KEY_PLAYER + ")" +
								   " SELECT " + KEY_PLAYER + " FROM " + FTS_VIRTUAL_TABLE);
						//INSERT INTO `toDB`.`tableName` SELECT * FROM `fromDB`.`tableName`
						//INSERT INTO X.TABLE(fieldname1, fieldname2) SELECT fieldname1, fieldname2 FROM Y.TABLE
						//na dit de kolommen bijvoegen in FTS_TABLE_CREATE en buildColumnMap
						break;
					case 3:
						db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
						db.execSQL(FTS_TABLE_CREATE);
						db.execSQL("INSERT INTO " + FTS_VIRTUAL_TABLE + " SELECT * FROM " + VIRTUAL_DAT);
						break;
					default:
						throw new IllegalStateException(
							"onUpgrade() with unknown oldVersion " + oldVersion);	
				}
				upgradeTo++;
			}
			
        }
    }		

	public Database(Context ctx) {
		this.mCtx = ctx;
	}

    public Database open() throws SQLException {
		String DATABASE_NAME = "WhistTest.db"; //.db moet niet
		mDbHelper = new FoodOpenHelper(mCtx,DATABASE_NAME);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public void setPlayer(int player){
		Cursor curs = query(null, null, null, null);
		long id=0;
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PLAYER, player);
		if (curs==null ){//|| curs.isAfterLast()
			mDb.insert(FTS_VIRTUAL_TABLE, null, initialValues);
		}else{
			id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
			mDb.update(FTS_VIRTUAL_TABLE, initialValues,"rowid = "+ id,null);
		}	
	}
	
	public void setColor(int color){
		Cursor curs = query(null, null, null, null);
		long id=0;
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_COLOR, color);
		if (curs==null ){//|| curs.isAfterLast()
			mDb.insert(FTS_VIRTUAL_TABLE, null, initialValues);
		}else{
			id=curs.getLong(curs.getColumnIndex(BaseColumns._ID));
			mDb.update(FTS_VIRTUAL_TABLE, initialValues,"rowid = "+ id,null);
		}	
	}

	public void toast(String msg){//opgepast toast kan problemen geven in database
        Toast.makeText(this.mCtx, msg, Toast.LENGTH_SHORT).show();
    }
}
