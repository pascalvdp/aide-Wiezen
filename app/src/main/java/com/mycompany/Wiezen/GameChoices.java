package com.mycompany.Wiezen;
import android.content.*;
import java.util.*;
import java.text.*;
import android.widget.*;
import android.os.*;

public class GameChoices extends GamePlayer implements Choices
{
	private final Context mCtx;//enkel nodig voor toast
	private ArrayList<Choice> arraylist1=new ArrayList<Choice>(); 
	private ArrayList<Choice> arraylist2=new ArrayList<Choice>(); 
	private ArrayList<Card> deckCardTemp = new ArrayList<Card>();

	int[] playerPoints=new int[5];
	int[] playerChoice=new int[5];
	int[] oldPlayerChoice=new int[5];//gebruikt bij misere en openmisere
	int[] playerTrumpCategory=new int[5];
	public int maxChoice=0;
	public int oldMaxChoice=0;
	public int choice;
	public int choiceSelf=-1;//dit is als we zelf kiezen
	private boolean ok;
	private String[] st;//hier zit wel ! in
	private String cardsString;//hier ook ! dus
	public int troelPlayer,troelMeePlayer;
	public boolean alleen;
	public Integer[][] friend=new Integer[5][0];
	public Integer[][] enemy=new Integer[5][0];
	int doublePoints=1;
	
	int spelerdatvraagt=0;//extra, dit wordt niet opgeslagen

	boolean gameEnd=false;
	
	private Random generator;

	public GameChoices(Context context){
		super(context);
		mCtx=context;
		generator=new Random();
	}
	
	public void setPoints(){
		//indien einde spel = tellen punten
		if(maxChoice==SOLO || maxChoice==SOLOSLIM){//ok
			setThePoints(13,45,false,false);
		}
		if(maxChoice==DANSEN12 || maxChoice==DANSEN12INTROEF){//ok
			setThePoints(12,18,false,true);
		}
		if(maxChoice==DANSEN11 || maxChoice==DANSEN11INTROEF){//ok
			setThePoints(11,15,false,true);
		}
		if(maxChoice==DANSEN10 || maxChoice==DANSEN10INTROEF){//ok
			setThePoints(10,12,false,true);
		}
		if(maxChoice==DANSEN9 || maxChoice==DANSEN9INTROEF){//ok
			setThePoints(9,9,false,true);
		}
		if(maxChoice==MEEGAAN || maxChoice==TROELMEE){//ok
			setThePoints(8,2,true,true);
		}
		if(maxChoice==ALLEEN){//ok
			setThePoints(5,2,true,true);
		}
		if(maxChoice==MISERE || maxChoice==OPENMISERE){//ok
			//zoek het aantal (open)miserespelers bij het begin van het spel
			int numberOf=0;
			for(int a=1;a<=4;a++){
				if(playerChoice[a]==PAS && oldPlayerChoice[a]==MISERE || playerChoice[a]==MISERE){
					numberOf+=1;
				}else{
					if(playerChoice[a]==PAS && oldPlayerChoice[a]==OPENMISERE || playerChoice[a]==OPENMISERE){
						numberOf+=1;
					}	
				}	
			}	
			//aantal overblijvende (open)miserespelers
			int numberOfPlayers=getNumberOfPlayersMisereOrOpenmisere();
			//verdeel de punten
			int points;
			if(maxChoice==MISERE){points=15*doublePoints;}else{points=30*doublePoints;}
			switch(numberOf){
				case 1:
					if(numberOfPlayers==1){
						for(int a=1;a<=4;a++){
							if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){
								playerPoints[a]+=points;
							}else{
								playerPoints[a]-=points/3;
							}	
						}	
					}
					if(numberOfPlayers==0){
						for(int a=1;a<=4;a++){
							if(oldPlayerChoice[a]==MISERE || oldPlayerChoice[a]==OPENMISERE){
								playerPoints[a]-=points;
							}else{
								playerPoints[a]+=points/3;
							}	
						}	
					}
					break;
				case 2:
					if(numberOfPlayers==2){
						for(int a=1;a<=4;a++){
							if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){
								playerPoints[a]+=points/3*2;
							}else{
								playerPoints[a]-=points/3*2;
							}
						}	
					}
					if(numberOfPlayers==1){
						for(int a=1;a<=4;a++){
							if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){playerPoints[a]+=points/3*2;}
							if(oldPlayerChoice[a]==MISERE || oldPlayerChoice[a]==OPENMISERE){playerPoints[a]-=points/3*2;}
						}	
					}
					if(numberOfPlayers==0){
						for(int a=1;a<=4;a++){
							if(oldPlayerChoice[a]==MISERE || oldPlayerChoice[a]==OPENMISERE){
								playerPoints[a]-=points/3*2;
							}else{
								playerPoints[a]+=points/3*2;
							}	
						}	
					}
					break;
				case 3:
					if(numberOfPlayers==3){
						for(int a=1;a<=4;a++){
							if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){
								playerPoints[a]+=points/3;
							}else{
								playerPoints[a]-=points;
							}
						}	
					}
					if(numberOfPlayers==2){
						for(int a=1;a<=4;a++){
							if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){playerPoints[a]+=points/3;}
							if(oldPlayerChoice[a]==MISERE || oldPlayerChoice[a]==OPENMISERE){playerPoints[a]-=points/3*2;}
						}	
					}
					if(numberOfPlayers==1){
						for(int a=1;a<=4;a++){
							if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){playerPoints[a]+=points/3*2;}
							if(oldPlayerChoice[a]==MISERE || oldPlayerChoice[a]==OPENMISERE){playerPoints[a]-=points/3;}
						}	
					}
					if(numberOfPlayers==0){
						for(int a=1;a<=4;a++){
							if(oldPlayerChoice[a]==MISERE || oldPlayerChoice[a]==OPENMISERE){
								playerPoints[a]-=points/3;
							}else{
								playerPoints[a]+=points;
							}
						}	
					}
					break;
				case 4:
					//numberOfPlayers=4 kan niet omdat er minstens atijd 1 speler verliest
					if(numberOfPlayers==3){
						for(int a=1;a<=4;a++){
							if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){
								playerPoints[a]+=points/3;
							}else{
								playerPoints[a]-=points;
							}
						}	
					}
					if(numberOfPlayers==2){
						for(int a=1;a<=4;a++){
							if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){playerPoints[a]+=points/3*2;}
							if(oldPlayerChoice[a]==MISERE || oldPlayerChoice[a]==OPENMISERE){playerPoints[a]-=points/3*2;}
						}	
					}
					if(numberOfPlayers==1){
						for(int a=1;a<=4;a++){
							if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){playerPoints[a]+=points;}
							if(oldPlayerChoice[a]==MISERE || oldPlayerChoice[a]==OPENMISERE){playerPoints[a]-=points/3;}
						}	
					}
					//als iedereen eraan is dan punten=0
					break;
			}
		}
		if(playerPoints[1]+playerPoints[2]+playerPoints[3]+playerPoints[4]!=0){
			String msg = "totaal puntenaantal klopt niet, fout in programma";
			Toast.makeText(mCtx, msg, Toast.LENGTH_SHORT).show();
		}	
	}
	
	public void setThePoints(int numberOfStrokesToObtain,int startPoints,boolean countExtraPoints,boolean countDoublePoints){
		int numberOfStrokes=waitingDeck[1].size()/4;
		boolean successful=true;
		if(numberOfStrokes<numberOfStrokesToObtain){successful=false;}
		int points=0;
		if(countExtraPoints){
			points=numberOfStrokes-numberOfStrokesToObtain;
			points=Math.abs(points);//omzetten naar positief
		}
		points+=startPoints;
		if(countDoublePoints &&(numberOfStrokes==13 || numberOfStrokes==0)){points*=2;}
		if(!successful){points=-points;}
		points=points*doublePoints;
		if(maxChoice==DANSEN9 || maxChoice==DANSEN9INTROEF || maxChoice==DANSEN10 || maxChoice==DANSEN10INTROEF ||
		   maxChoice==DANSEN11 || maxChoice==DANSEN11INTROEF || maxChoice==DANSEN12 || maxChoice==DANSEN12INTROEF ||
		   maxChoice==SOLO || maxChoice==SOLOSLIM){
			if(playerChoice[player]==PAS){
				playerPoints[player]-=points/3;
				for(int c=0;c<friend[player].length;c++){
					int a=friend[player][c];//dit zo moeten zetten, anders problemen bij starten
					playerPoints[a]-=points/3;
				}	
				for(int c=0;c<enemy[player].length;c++){
					int a=enemy[player][c];
					playerPoints[a]+=points;
				}
			}else{
				playerPoints[player]+=points;
				for(int c=0;c<friend[player].length;c++){
					int a=friend[player][c];
					playerPoints[a]+=points;
				}	
				for(int c=0;c<enemy[player].length;c++){
					int a=enemy[player][c];
					playerPoints[a]-=points/3;
				}
			}
		}
		switch(maxChoice){
			case MEEGAAN:
				if(playerChoice[player]==PAS){
					playerPoints[player]-=points;
					for(int c=0;c<friend[player].length;c++){
						int a=friend[player][c];//dit zo moeten zetten, anders problemen bij starten
						playerPoints[a]-=points;
					}	
					for(int c=0;c<enemy[player].length;c++){
						int a=enemy[player][c];
						playerPoints[a]+=points;
					}
				}else{
					playerPoints[player]+=points;
					for(int c=0;c<friend[player].length;c++){
						int a=friend[player][c];
						playerPoints[a]+=points;
					}	
					for(int c=0;c<enemy[player].length;c++){
						int a=enemy[player][c];
						playerPoints[a]-=points;
					}
				}
				break;	
			case TROELMEE://verschil met meegaan=extra dubbel
				if(playerChoice[player]==PAS){
					playerPoints[player]-=points*2;
					for(int c=0;c<friend[player].length;c++){
						int a=friend[player][c];//dit zo moeten zetten, anders problemen bij starten
						playerPoints[a]-=points*2;
					}	
					for(int c=0;c<enemy[player].length;c++){
						int a=enemy[player][c];
						playerPoints[a]+=points*2;
					}
				}else{
					playerPoints[player]+=points*2;
					for(int c=0;c<friend[player].length;c++){
						int a=friend[player][c];
						playerPoints[a]+=points*2;
					}	
					for(int c=0;c<enemy[player].length;c++){
						int a=enemy[player][c];
						playerPoints[a]-=points*2;
					}
				}
				break;	
			case ALLEEN:	
				if(playerChoice[player]==PAS){
					playerPoints[player]-=points;
					for(int c=0;c<friend[player].length;c++){
						int a=friend[player][c];//dit zo moeten zetten, anders problemen bij starten
						playerPoints[a]-=points;
					}	
					for(int c=0;c<enemy[player].length;c++){
						int a=enemy[player][c];
						playerPoints[a]+=points*3;//*3 bij alleen
					}
				}else{
					playerPoints[player]+=points*3;
					for(int c=0;c<friend[player].length;c++){
						int a=friend[player][c];
						playerPoints[a]+=points*3;
					}	
					for(int c=0;c<enemy[player].length;c++){
						int a=enemy[player][c];
						playerPoints[a]-=points;
					}
				}
				break;
		}		
	}
	
	public void setFriendsEnemies(){
		ArrayList<Integer> arListFriends=new ArrayList<Integer>();
		ArrayList<Integer> arListEnemies=new ArrayList<Integer>();
		int b;
		for(int a=1;a<=4;a++){
			arListFriends.clear();
			arListEnemies.clear();
			b=a;b++;if(b==5){b=1;}
			while(b!=a){
				if(playerChoice[a]==PAS){
					if(playerChoice[b]==PAS){arListFriends.add(b);}else{arListEnemies.add(b);}
				}else{
					if(playerChoice[b]==PAS){arListEnemies.add(b);}else{arListFriends.add(b);}
				}	
				b++;if(b==5){b=1;}
			}
			friend[a]=arListFriends.toArray(new Integer[arListFriends.size()]);
			enemy[a]=arListEnemies.toArray(new Integer[arListEnemies.size()]);
		}	
	}
	
	public void setOldMaxChoice(){
		oldMaxChoice=maxChoice;
	}

	public void setMaxChoice(){
		maxChoice=oldMaxChoice;
	}

	public String getChoice(int player){//aangepast van () naar (int player)
		String strCat="";
		switch(playerTrumpCategory[player]){
			case 1:strCat="♡";break;
			case 2:strCat="♧";break;
			case 3:strCat="◇";break;
			case 4:strCat="♤";break;	
		}
		String str="";
		switch(playerChoice[player]){
			case SOLOSLIM:str="Soloslim ("+strCat+")";break;
			case SOLO:str="Solo ("+strCat+")";break;
			case OPENMISERE:str="Openmisere";break;
			case TROEL:str="Troel ("+strCat+")";break;
			case TROELMEE:str="Troelmee ("+strCat+")";break;
			case DANSEN12INTROEF:str="Dansen12introef ("+strCat+")";break;
			case DANSEN12:str="Dansen12 ("+strCat+")";break;
			case DANSEN11INTROEF:str="Dansen11introef ("+strCat+")";break;
			case DANSEN11:str="Dansen11 ("+strCat+")";break;
			case DANSEN10INTROEF:str="Dansen10introef ("+strCat+")";break;
			case DANSEN10:str="Dansen10 ("+strCat+")";break;
			case MISERE:str="Misere";break;
			case DANSEN9INTROEF:str="Dansen9introef ("+strCat+")";break;
			case DANSEN9:str="Dansen9 ("+strCat+")";break;
			case VRAAG:str="Vraag";break;
			case MEEGAAN:str="Meegaan";break;
			case ALLEEN:str="Alleen";break;
			case PAS:str="Pas";break;
			default: str="no choice";
		}
		return str;
	}
	
	public void checkTroel(){
		for(int a=1;a<=4;a++){//player
			cardsString=getCardsString(a);
			deckCardTemp.clear();
			deckCardTemp.addAll(playerDeck[a]);
			setStringsInList1(cardsString);
			ok=false;
			arraylist2.clear();
			c("*a,*a,*a,*a");//eerst 4 azen
			c("*a,*a,*a");
			ok=choiceOk();
			if(ok){
				troelPlayer=a;break;
			}
		}	
		if(troelPlayer!=0){
			for(int aa=1;aa<=4;aa++){
				if(aa!=troelPlayer){
					if(getPlayerDeckValue(aa,14).size()==1){
						playerTrumpCategory[troelPlayer]=getPlayerDeckValue(aa,14).get(0).category;
						troelMeePlayer=aa;break;
					}
				}
			}	
			if(troelMeePlayer==0){//indien 4 azen...
				playerTrumpCategory[troelPlayer]=1;//harten is dan zeker troef
				int sz=getPlayerDeckCategory(troelPlayer,1).size()-1;
				int val=14;
				while(sz>-1 && getPlayerDeckCategory(troelPlayer,1).get(sz).value==val){
					sz--;val--;
				}
				for(int aa=1;aa<=4;aa++){
					if(aa!=troelPlayer){
						for(int bb=0;bb<getPlayerDeckCategory(aa,1).size();bb++){//bb=1
							if(getPlayerDeckCategory(aa,1).get(bb).value==val){
								troelMeePlayer=aa;break;
							}
						}
					}
				}	
			}
		}
	}

	public void restartChoice(){
		for(int a=1;a<=4;a++){oldPlayerChoice[a]=-1;playerChoice[a]=-1;playerTrumpCategory[a]=0;}
		maxChoice=0;
		oldMaxChoice=0;
		troelMeePlayer=0;
		troelPlayer=0;
		alleen=false;
		spelerdatvraagt=0;//extra
		setDealer(dealer);//dit mag later weg
	}

	public void startChoice(){
		cardsString=getCardsString(player);
		deckCardTemp.clear();
		deckCardTemp.addAll(playerDeck[player]);
		setStringsInList1(cardsString);
		ok=false;
		choice=SOLOSLIM;
		if(alleen){choice=ALLEEN;maxChoice=PAS;}
		int m=0;
		if(maxChoice==OPENMISERE || maxChoice==MISERE){m=1;}
		while(choice>maxChoice-m){
			setStringsInList2();
			choiceOk();
			if(ok)break;
			choice--;
		}
		if(ok){
			playerChoice[player]=choice;maxChoice=choice;
		}else{
			choice=0;playerChoice[player]=choice;
		}
	}	

	public void startChoiceSelf(){
		choice=choiceSelf;
		choice++;
		if(choice>SOLOSLIM){choice=PAS;}
		boolean troe=false;
		boolean troeMee=false;
		if(player==troelPlayer){troe=true;}
		if(player==troelMeePlayer && maxChoice==TROEL){troeMee=true;}
		int m=0;
		if(choice==PAS){
			if(troe && maxChoice<TROEL){
				choice=TROEL;m=1;
				if(getPlayerDeckValue(troelMeePlayer,14).size()==1){//3 azen
					playerTrumpCategory[player]=getPlayerDeckValue(troelMeePlayer,14).get(0).category;
				}else{
					playerTrumpCategory[player]=1;//4 azen
				}	
			}
			if(troeMee && maxChoice<TROELMEE){
				choice=TROELMEE;m=1;
				playerTrumpCategory[troelMeePlayer]=playerTrumpCategory[troelPlayer];
			}
		} 
		if(maxChoice==OPENMISERE || maxChoice==MISERE){m=1;}
		while(((troe && maxChoice<TROEL && choice<TROEL) || (troeMee && maxChoice<TROELMEE && choice<TROELMEE)) ||
		    (troe && choice==TROELMEE) || (troeMee && choice==TROEL) ||
			(!troe && choice==TROEL) || (!troeMee && choice==TROELMEE) ||
			(player!=dealer && choice==ALLEEN) || (choice==MEEGAAN && maxChoice!=VRAAG) ||
			(player==dealer && choice==VRAAG) || (player==dealer && choice==MEEGAAN && maxChoice!=VRAAG) ||  
			(choice<=maxChoice-m && choice!=PAS) || 
			choice==OPENMISERE-1 || choice==MISERE-1 || choice>SOLOSLIM){
		  		choice++;
		  		if(choice>SOLOSLIM){choice=PAS;}
		}	
		if(choice==DANSEN9INTROEF || choice==DANSEN10INTROEF || choice==DANSEN11INTROEF
		   || choice==DANSEN12INTROEF || choice==SOLOSLIM){
			playerTrumpCategory[player]=trumpCategory;
		}
		choiceSelf=choice;
		playerChoice[player]=choice;
		if(choice!=PAS){maxChoice=choice;}
	}
	
	private void setStringsInList2(){
		arraylist2.clear();
		switch(choice){
			case SOLOSLIM:
				c("*+!29tbvka,*9tbvka");//alles ok
				c("*+!29tbvka,*tbvka,*a");//
				c("*+!29tbvka,*bvka,*ka");//
				c("*+!29tbvka,*bvka,*a,*a");//
				c("*+!29tbvka,*vka,*vka");//
				c("*+!29tbvka,*vka,*ka,*a");//
				c("*+!29tbvka,*ka,*ka,*ka");//
				c("*+!234tbvka,*tbvka");// ok
				c("*+!234tbvka,*bvka,*a");//
				c("*+!234tbvka,*vka,*ka");//
				c("*+!234tbvka,*vka,*a,*a");//
				c("*+!234tbvka,*ka,*ka,*a");//
				c("*+!23456bvka,*bvka");// ok
				c("*+!23456bvka,*vka,*a");//
				c("*+!23456bvka,*ka,*ka");//
				c("*+!23456bvka,*ka,*a,*a");//
				c("*+!2345678vka,*vka");// ok
				c("*+!2345678vka,*ka,*a");//
				c("*+!2345678vka,*a,*a,*a");//
				c("*+!23456789tka,*ka");// ok
				c("*+!23456789tka,*a,*a");//
				c("*+!23456789tbva,*a");// ok
				c("!23456789tbvka");// ok
				break;
			case SOLO:
				c("*+29tbvka,*9tbvka");//alles ok
				c("*+29tbvka,*tbvka,*a");//
				c("*+29tbvka,*bvka,*ka");//
				c("*+29tbvka,*bvka,*a,*a");//
				c("*+29tbvka,*vka,*vka");//
				c("*+29tbvka,*vka,*ka,*a");//
				c("*+29tbvka,*ka,*ka,*ka");//
				c("*+234tbvka,*tbvka");// ok
				c("*+234tbvka,*bvka,*a");//
				c("*+234tbvka,*vka,*ka");//
				c("*+234tbvka,*vka,*a,*a");//
				c("*+234tbvka,*ka,*ka,*a");//
				c("*+23456bvka,*bvka");// ok
				c("*+23456bvka,*vka,*a");//
				c("*+23456bvka,*ka,*ka");//
				c("*+23456bvka,*ka,*a,*a");//
				c("*+2345678vka,*vka");// ok
				c("*+2345678vka,*ka,*a");//
				c("*+2345678vka,*a,*a,*a");//
				c("*+23456789tka,*ka");// ok
				c("*+23456789tka,*a,*a");//
				c("*+23456789tbva,*a");// ok
				c("23456789tbvka");// ok
				break;
			case OPENMISERE:
				//-a kunnen we wel toepassen want de andere a kan lager zijn
				c("-2456789tbvka,-5");//ok  
				c("-246789tbvka,-4t");//ok
				c("-246789tbvka,-5,-5");//ok
				c("-24689tbvka,-36t");//ok
				c("-24689tbvka,-4t,-5");//ok
				c("-24689tbvka,-4,-4,-4");//ok
				c("-2478tbvka,-247t");//ok
				c("-2478tbvka,-258,-4");//ok
				c("-2478tbvka,-48,-37");//ok
				c("-2478bvka,-247tv");//ok
				c("-2478bvka,-247t,-4");//ok
				c("-2478bvka,-258,-36");//ok
				c("-2478bvka,-258,-4,-4");//ok
				c("-2478bvka,-36,-36,-4");//ok
				c("-2478tka,-2478ta");//ok
				c("-2478tka,-247tv,-4");//ok
				c("-2478tka,-247t,-36");//ok
				c("-2478tka,-247t,-4,-4");//ok
				c("-2478tka,-258,-258");//ok
				c("-2478tka,-258,-36,-4");//ok
				c("-2478tka,-36,-36,-36");//ok
				c("-2478ta,-2478tv,-4");//ok
				c("-2478ta,-247tv,-36");//ok
				c("-2478ta,-247tv,-4,-4");//ok
				c("-2478ta,-247t,-258");//ok
				c("-2478ta,-247t,-36,-4");//ok
				c("-2478ta,-258,-258,-4");//ok
				c("-2478ta,-258,-36,-36");//ok
				c("-247tv,-246tv,-258");//ok
				c("-247tv,-246tv,-36,-4");//ok
				c("-247tv,-246t,-258,-4");//ok
				c("-247tv,-246t,-36,-36");//ok
				c("-247tv,-246t,-246t");
				c("-247tv,-258,-248,-36");//ok  
				break;	
			case TROELMEE:
				if(maxChoice==TROEL && troelMeePlayer==player){
					maxChoice=TROELMEE;
					playerChoice[troelMeePlayer]=TROELMEE;
					ok=true;
					for(int aa=1;aa<=4;aa++){
						if(playerChoice[aa]==TROEL){playerTrumpCategory[player]=playerTrumpCategory[aa];break;}
					}
				}	
				break;	
			case TROEL:
				if(troelPlayer==player){
					maxChoice=TROEL;
					playerChoice[troelPlayer]=TROEL;
					ok=true;
					if(getPlayerDeckValue(troelMeePlayer,14).size()==1){//3 azen
						playerTrumpCategory[player]=getPlayerDeckValue(troelMeePlayer,14).get(0).category;
					}else{
						playerTrumpCategory[player]=1;//4 azen
					}	
				}
				break;
			case DANSEN12INTROEF:
				c("*+!89tbvk,*89tbvka");//alles ok
				c("*+!89tbvk,*9tbvka,*a");//
				c("*+!89tbvk,*tbvka,*ka");//
				c("*+!89tbvk,*bvka,*vka");//
				c("*+!789tbvk,*9tbvka");//
				c("*+!789tbvk,*tbvka,*a");//
				c("*+!789tbvk,*bvka,*ka");//
				c("*+!789tbvk,*vka,*vka");//
				c("*+!6789tbvk,*tbvka");//
				c("*+!6789tbvk,*bvka,*a");//
				c("*+!6789tbvk,*vka,*ka");//
				c("*+!56789tbvk,*bvka");//
				c("*+!56789tbvk,*vka,*a");//
				c("*+!56789tbvk,*ka,*ka");//
				c("*+!456789tbvk,*vka");//
				c("*+!456789tbvk,*ka,*a");//
				
				c("*!9tbvka,*9tbvka");//ok
				c("*+!29tbvka,*tbvka");// ok
				c("*+!234tbvka,*bvka");// ok
				c("*+!23456bvka,*vka");// ok
				c("*+!2345678vka,*ka");// ok
				c("*+!23456789tka,*a");// ok
				c("*+!23456789tbva");//  ok
				break;		
			case DANSEN12:
				c("*+89tbvk,*89tbvka");//alles ok
				c("*+89tbvk,*9tbvka,*a");//
				c("*+89tbvk,*tbvka,*ka");//
				c("*+89tbvk,*bvka,*vka");//
				c("*+789tbvk,*9tbvka");//
				c("*+789tbvk,*tbvka,*a");//
				c("*+789tbvk,*bvka,*ka");//
				c("*+789tbvk,*vka,*vka");//
				c("*+6789tbvk,*tbvka");//
				c("*+6789tbvk,*bvka,*a");//
				c("*+6789tbvk,*vka,*ka");//
				c("*+56789tbvk,*bvka");//
				c("*+56789tbvk,*vka,*a");//
				c("*+56789tbvk,*ka,*ka");//
				c("*+456789tbvk,*vka");//
				c("*+456789tbvk,*ka,*a");//
				
				c("*9tbvka,*9tbvka");//ok
				c("*+29tbvka,*tbvka");// ok
				c("*+234tbvka,*bvka");// ok
				c("*+23456bvka,*vka");// ok
				c("*+2345678vka,*ka");// ok
				c("*+23456789tka,*a");// ok
				c("*+23456789tbva");//  ok
				break;	
			case DANSEN11INTROEF:
				c("*+!89tbvk,*9tbvka");//alles ok
				c("*+!89tbvk,*tbvka,*a");//
				c("*+!89tbvk,*bvka,*ka");//
				c("*+!89tbvk,*vka,*vka");//
				c("*+!789tbvk,*tbvka");//
				c("*+!789tbvk,*bvka,*a");//
				c("*+!789tbvk,*vka,*ka");//
				c("*+!6789tbv,*9tbvka");//
				c("*+!6789tbv,*tbvka,*a");//
				c("*+!6789tbv,*bvka,*ka");//
				c("*+!6789tbv,*vka,*vka");//
				c("*+!6789tbvk,*bvka");//
				c("*+!6789tbvk,*vka,*a");//
				c("*+!6789tbvk,*ka,*ka");//
				c("*+!56789tbv,*tbvka");//
				c("*+!56789tbv,*bvka,*a");//
				c("*+!56789tbv,*vka,*ka");//
				c("*+!56789tbvk,*vka");//
				c("*+!56789tbvk,*ka,*a");//
				c("*+!456789tbv,*bvka");//
				c("*+!456789tbv,*vka,*a");//
				c("*+!456789tbv,*ka,*ka");//
				c("*+!456789tbvk,*ka");//
				c("*+!456789tbvk,*a,*a");//
				c("*+!3456789tbv,*vka");//
				c("*+!3456789tbv,*ka,*a");//
				c("*+!23456789tbvk");//
				
				c("*!9tbvka,*tbvka");//ok
				c("*+!29tbvka,*bvka");// ok
				c("*+!234tbvka,*vka");// ok
				c("*+!234567vka,*ka,*+2k");//  ok
				c("*+!234567vka,*vka");//  ok
				c("*+!23456bvka,*ka");// ok
				c("*+!23456789tb,*ka,*a");// ok
				c("*+!23456789va,*ka");// ok
				c("*+!2345678vka,*a");// ok
				c("*+!23456789tbv,*ka");//  ok
				c("*+!23456789tbv,*a,*a");//  ok
				c("*+!23456789tka");// ok
				c("*+!23456789tbvk");//  ok
				break;		
			case DANSEN11:
				c("*+89tbvk,*9tbvka");//alles ok
				c("*+89tbvk,*tbvka,*a");//
				c("*+89tbvk,*bvka,*ka");//
				c("*+89tbvk,*vka,*vka");//
				c("*+789tbvk,*tbvka");//
				c("*+789tbvk,*bvka,*a");//
				c("*+789tbvk,*vka,*ka");//
				c("*+6789tbv,*9tbvka");//
				c("*+6789tbv,*tbvka,*a");//
				c("*+6789tbv,*bvka,*ka");//
				c("*+6789tbv,*vka,*vka");//
				c("*+6789tbvk,*bvka");//
				c("*+6789tbvk,*vka,*a");//
				c("*+6789tbvk,*ka,*ka");//
				c("*+56789tbv,*tbvka");//
				c("*+56789tbv,*bvka,*a");//
				c("*+56789tbv,*vka,*ka");//
				c("*+56789tbvk,*vka");//
				c("*+56789tbvk,*ka,*a");//
				c("*+456789tbv,*bvka");//
				c("*+456789tbv,*vka,*a");//
				c("*+456789tbv,*ka,*ka");//
				c("*+456789tbvk,*ka");//
				c("*+456789tbvk,*a,*a");//
				c("*+3456789tbv,*vka");//
				c("*+3456789tbv,*ka,*a");//
				c("*+23456789tbvk");//
				
				c("*9tbvka,*tbvka");//ok
				c("*+29tbvka,*bvka");// ok
				c("*+234tbvka,*vka");// ok
				c("*+234567vka,*ka,*+2k");//  ok
				c("*+234567vka,*vka");//  ok
				c("*+23456bvka,*ka");// ok
				c("*+23456789tb,*ka,*+a");// ok
				c("*+23456789va,*ka");// ok
				c("*+2345678vka,*a");// ok
				c("*+23456789tbv,*ka");//  ok
				c("*+23456789tbv,*a,*a");//  ok
				c("*+23456789tka");// ok
				break;		
			case DANSEN10INTROEF:
				c("*+!89tbvk,*tbvka");//ok
				c("*+!89tbvk,*bvka,*a");//ok
				c("*+!89tbvk,*vka,*ka");//ok
				c("*+!789tbvk,*bvka");//ok
				c("*+!789tbvk,*vka,*a");//ok
				c("*+!789tbvk,*ka,*ka");//ok
				c("*+!6789tbv,*tbvka");//ok
				c("*+!6789tbv,*bvka,*a");//ok
				c("*+!6789tbv,*vka,*ka");//ok
				c("*+!56789tb,*9tbvka");//ok
				c("*+!56789tb,*tbvka,*a");//ok
				c("*+!56789tb,*bvka,*ka");//ok
				c("*+!56789tb,*vka,*vka");//ok
				c("*+!6789tbvk,*vka");//ok
				c("*+!6789tbvk,*ka,*a");//ok
				c("*+!56789tbv,*bvka");//ok
				c("*+!56789tbv,*vka,*a");//ok
				c("*+!56789tbv,*ka,*ka");//ok
				c("*+!456789tb,*tbvka");//ok
				c("*+!456789tb,*bvka,*a");//ok
				c("*+!456789tb,*vka,*ka");//ok
				c("*+!56789tbvk,*ka");//ok
				c("*+!56789tbvk,*a,*a");//ok
				c("*+!456789tbv,*vka");//ok
				c("*+!456789tbv,*ka,*a");//ok
				c("*+!3456789tb,*bvka");//ok
				c("*+!3456789tb,*vka,*a");//ok
				c("*+!3456789tb,*ka,*ka");//ok
				c("*+!456789tbvk,*a");//ok
				c("*+!3456789tbv,*ka");//ok
				c("*+!3456789tbv,*a,*a");//ok
				c("*+!23456789tb,*vka");//ok
				c("*+!23456789tb,*ka,*a");//ok
				c("*+!3456789tbvk");//ok

				c("*+!9tbvka,*bvka");//ok
				c("*+!234bvka,*vka");//ok
				c("*+!29tbvka,*vka");// ok
				c("*+!23456vka,*vka");// ok
				c("*+!234tbvka,*ka");// ok
				c("*+!2345678ka,*ka");//ok
				c("*+!234567vka,*a");//  ok
				c("*+!23456789tb,*a,*a");// ok
				c("*+!23456789tb,*ka");//ok
				c("*+!23456789va,*a");// ok
				c("*+!2345678vka");// ok
				c("*+!23456789tbv,*a");//  ok
				break;		
			case DANSEN10:
				c("*+89tbvk,*tbvka");//ok
				c("*+89tbvk,*bvka,*a");//ok
				c("*+89tbvk,*vka,*ka");//ok
				c("*+789tbvk,*bvka");//ok
				c("*+789tbvk,*vka,*a");//ok
				c("*+789tbvk,*ka,*ka");//ok
				c("*+6789tbv,*tbvka");//ok
				c("*+6789tbv,*bvka,*a");//ok
				c("*+6789tbv,*vka,*ka");//ok
				c("*+56789tb,*9tbvka");//ok
				c("*+56789tb,*tbvka,*a");//ok
				c("*+56789tb,*bvka,*ka");//ok
				c("*+56789tb,*vka,*vka");//ok
				c("*+6789tbvk,*vka");//ok
				c("*+6789tbvk,*ka,*a");//ok
				c("*+56789tbv,*bvka");//ok
				c("*+56789tbv,*vka,*a");//ok
				c("*+56789tbv,*ka,*ka");//ok
				c("*+456789tb,*tbvka");//ok
				c("*+456789tb,*bvka,*a");//ok
				c("*+456789tb,*vka,*ka");//ok
				c("*+56789tbvk,*ka");//ok
				c("*+56789tbvk,*a,*a");//ok
				c("*+456789tbv,*vka");//ok
				c("*+456789tbv,*ka,*a");//ok
				c("*+3456789tb,*bvka");//ok
				c("*+3456789tb,*vka,*a");//ok
				c("*+3456789tb,*ka,*ka");//ok
				c("*+456789tbvk,*a");//ok
				c("*+3456789tbv,*ka");//ok
				c("*+3456789tbv,*a,*a");//ok
				c("*+23456789tb,*vka");//ok
				c("*+23456789tb,*ka,*a");//ok
				c("*+3456789tbvk");//ok
				
				c("*+9tbvka,*bvka");//ok
				c("*+234bvka,*vka");//ok
				c("*+29tbvka,*vka");// ok
				c("*+23456vka,*vka");// ok
				c("*+234tbvka,*ka");// ok
				c("*+2345678ka,*ka");//ok
				c("*+234567vka,*a");//  ok
				c("*+23456789va,*a");// ok
				c("*+2345678vka");// ok
				c("*+23456789tbv,*a");//  ok
				break;		
			case MISERE:
				c("-2456789tbvka,-a");//ok  -a kunnen we wel toepassen want de andere a kan lager zijn
				c("-246789tbvka,-4a");//ok
				c("-246789tbvka,-a,-5");//ok
				c("-246789tbvka,-8,-8");//ok
				c("-24689tbvka,-47a");//ok
				c("-24689tbvka,-4a,-5");//ok
				c("-24689tbvka,-8,-8,-8");//ok
				c("-2478tbvka,-468a");//ok
				c("-2478tbvka,-47a,-5");//ok
				c("-2478tbvka,-4a,-48");//ok
				c("-2478bvka,-368ta");//ok
				c("-2478bvka,-468a,-5");//ok
				c("-2478bvka,-47a,-48");//ok
				c("-2478bvka,-47a,-5,-5");//ok
				c("-2478bvka,-4a,-48,-5");//ok
				c("-3478vka,-3689ta");//ok
				c("-3478vka,-368ta,-5");//ok
				c("-3478vka,-468a,-48");//ok
				c("-3478vka,-468a,-5,-5");//ok
				c("-3478vka,-47a,-47a");//ok
				c("-3478vka,-47a,-48,-5");//ok
				c("-3478vka,-4a,-48,-48");//ok
				c("-3478ka,-3689ta,-5");//ok
				c("-3478ka,-368ta,-4a");//ok
				c("-3478ka,-368ta,-5,-5");//ok
				c("-3478ka,-468a,-47a");//ok
				c("-3478ka,-468a,-48,-5");//ok
				c("-3478ka,-47a,-47a,-5");//ok
				c("-3478ka,-47a,-48,-48");//ok
				c("-347ka,-368ta,-47a");//ok
				c("-347ka,-368ta,-48,-5");//ok
				c("-347ka,-468a,-47a,-5");//ok
				c("-347ka,-468a,-48,-48");//ok
				c("-347ka,-47a,-47a,-48");//ok
				break;
			case DANSEN9INTROEF:
				//hier aanpassingen zie dan ook lager
				if(random(2)==1){
					c("*+!23bvka,*bvka");
					c("*+!23456vk,*ka,*ka");
					c("*+!23456vka,*+2k");
					c("*+!23456789t,*a,*a");
					c("*+!23456789t,*ka");
					c("*+!2345678ka,*+2k");
					c("*+!23456789ta");
				}
				c("*+!9tbvk,*bvka,*bvka");//8 11 ok
				c("*+!89tbvk,*bvka");//9 ok
				c("*+!89tbvk,*vka,*a");//9 ok
				c("*+!89tbvk,*ka,*ka");//9 ok
				c("*+!789tbv,*tbvka");//8 10 ok
				c("*+!789tbv,*bvka,*a");//8 10 ok
				c("*+!789tbv,*vka,*ka");//8 10 ok
				c("*+!789tbvk,*vka");//9 ok
				c("*+!789tbvk,*ka,*a");//9 ok
				c("*+!6789tbv,*bvka");//9 ok
				c("*+!6789tbv,*vka,*a");//9 ok
				c("*+!6789tbv,*ka,*ka");//9 ok
				c("*+!56789tb,*tbvka");//9 ok
				c("*+!56789tb,*bvka,*a");//9 ok
				c("*+!56789tb,*vka,*ka");//9 ok
				c("*+!456789t,*9tbvka");//9 ok
				c("*+!456789t,*tbvka,*a");//9 ok
				c("*+!456789t,*bvka,*ka");//9 ok
				c("*+!456789t,*vka,*vka");//9 ok
				
				c("*+!9tbvka,*vka");//ok
				c("*+!2345vka,*ka,*+2k");
				c("*+!2345vka,*vka");
				c("*+!234bvka,*ka");
				c("*+!234bvka,*a,*+2k");
				c("*+!23456789,*ka,*ka");
				c("*+!23456vka,*a");
				c("*+!23456789t,*ka,*a");
				c("*+!2345678ka,*a");
				c("*+!234567vka");
				c("*+!23456789tb,*a");
				c("*+!23456789va");
				c("*+!23456789tbv");
				break;		
			case DANSEN9:
				//indien hier aanpassingen dan ook nakijken in dansen9introef 10....12 en solo
				if(random(2)==1){
					c("*+23bvka,*bvka");//ok
					c("*+23456vk,*ka,*ka");//8 9  ok
					c("*+23456vka,*+2k");//8 9    ok    (+a staat lager)
					c("*+23456789t,*a,*a");//8 9   2k niet gebruiken  ok
					c("*+23456789t,*ka");//8 9   ok
					c("*+2345678ka,*+2k");//8 9  ok
					c("*+23456789ta");//8 9  ok
				}
				//2*2k niet gebruiken
				c("*+9tbvk,*bvka,*bvka");//8 11 ok
				c("*+89tbvk,*bvka");//9 ok
				c("*+89tbvk,*vka,*a");//9 ok
				c("*+89tbvk,*ka,*ka");//9 ok
				c("*+789tbv,*tbvka");//8 10 ok
				c("*+789tbv,*bvka,*a");//8 10 ok
				c("*+789tbv,*vka,*ka");//8 10 ok
				c("*+789tbvk,*vka");//9 ok
				c("*+789tbvk,*ka,*a");//9 ok
				c("*+6789tbv,*bvka");//9 ok
				c("*+6789tbv,*vka,*a");//9 ok
				c("*+6789tbv,*ka,*ka");//9 ok
				c("*+56789tb,*tbvka");//9 ok
				c("*+56789tb,*bvka,*a");//9 ok
				c("*+56789tb,*vka,*ka");//9 ok
				c("*+456789t,*9tbvka");//9 ok
				c("*+456789t,*tbvka,*a");//9 ok
				c("*+456789t,*bvka,*ka");//9 ok
				c("*+456789t,*vka,*vka");//9 ok
				
				c("*+9tbvka,*vka");//ok
				c("*+2345vka,*ka,*+2k");//8 10  ok
				c("*+2345vka,*vka");//ok
				c("*+234bvka,*ka");//9  ok
				c("*+234bvka,*a,*+2k");//9  ok
				c("*+23456789,*ka,*ka");//9  ok
				c("*+23456vka,*a");//9  ok
				c("*+23456789t,*ka,*a");//9  ok
				c("*+2345678ka,*a");//9  ok
				c("*+234567vka");//9  ok
				c("*+23456789tb,*a");//9  ok
				c("*+23456789va");//9  ok
				c("*+23456789tbv");//9  ok
				break;	
			case MEEGAAN:
				if (maxChoice==VRAAG){
					if(random(2)==1) {
						c("*!+234,*a");
						c("*!+234,*+2k");
						c("*!+23a");
						c("*!+2345");
					}
					c("*!+234,*ka");
					c("*!+234,*a,*a");
					c("*!+234,*a,*+2k");
					c("*!+234,*+2k,*+2k");
					c("*!+23a,*a");
					c("*!+23a,*+2k");
					c("*!+2345,*a");
					c("*!+2345,*+2k");
					c("*!+234a");
					c("*!+23456");
				}	
				break;
			case VRAAG:
				if(player!=dealer){
					if(random(2)==1) {
						c("*!+2a,*ka,*+2k,*+2k");
						//hieronder is eentje lager, normaal gezien 6
						c("*!+234,*ka,*a");//
						c("*!+234,*ka,*+2k");//
						c("*!+23a,*ka");//
						c("*!+23a,*+2k,*+2k");//
						c("*!+23a,*a,*+2k");//
						c("*!+2ka,*a");//
						c("*!+2ka,*+2k");//
						c("*!+2345,*ka,*a");//
						c("*!+2345,*ka,*+2k");//
						c("*!+234v,*ka");
						c("*!+234v,*a,*a");
						c("*!+234v,*a,*+2k");
						c("*!+234v,*+2k,*+2k");
						c("*!+23ka,*a");//
						c("*!+23ka,*+2k");//
						c("*!+2vka");//
						c("*!+23456,*a");//
						c("*!+23456,*+2k");//
						c("*!+2345a");//
						c("*!+234ba");//
						c("*!+234567,*a");//
						c("*!+234567,*+2k");//
						c("*!+23456v");//
					}	
					c("*!+234,*ka,*ka");//7
					c("*!+234,*ka,*+2k,*+2k");//7
					c("*!+234,*ka,*a,*+2k");//7
					c("*!+23a,*ka,*+2k");//7
					c("*!+23a,*+2k,*+2k,*+2k");//7
					c("*!+23a,*a,*+2k,*+2k");//7
					c("*!+2ka,*ka");//7 8
					c("*!+2ka,*+2k,*+2k");//7 8
					c("*!+2ka,*a,*+2k");//7 8 
					c("*!+2345,*ka,*ka");//7 8
					c("*!+2345,*ka,*+2k,*+2k");//7 8
					c("*!+2345,*ka,*a,*+2k");//7 8
					c("*!+234a,*ka,*+2k");//7
					c("*!+234a,*+2k,*+2k,*+2k");//7
					c("*!+234a,*a,*+2k,*+2k");//7
					c("*!+23ka,*a,*+2k");//7
					c("*!+23ka,*+2k,*+2k");//7
					c("*!+2vka,*a");//7
					c("*!+2vka,*+2k");//7
					c("*!+23456,*ka");//7 
					c("*!+23456,*a,*a");//7
					c("*!+23456,*+2k,*+2k");//7 
					c("*!+23456,*a,*+2k");//7
					c("*!+2345a,*a");//7
					c("*!+2345a,*+2k");//7
					c("*!+234ka");//7
					c("*!+234567,*ka");//7
					c("*!+234567,*a,*a");//7
					c("*!+234567,*+2k,*+2k");//7
					c("*!+234567,*a,*+2k");//7
					c("*!+23456a");//6 7
					c("*!+2345678");
				}else{//alleen (bij 4de speler)
					alleen=true;
				}	
				break;	
			case ALLEEN:
				//enkel 2k niet
				if(alleen){
					if(random(2)==1) {
						c("*!+23a,*ka,*+2k");
						c("*!+23a,*a,*+2k,*+2k");
						c("*!+9tbv,*a,*a,*+2k");//26/07/16
						c("*!+9tbv,*a,*+2k,*+2k");
						c("*!+9tbv,*ka,*a");
						c("*!+9tbv,*ka,*+2k");
						c("*!+tbvk,*a,*a");//26/07/16
						c("*!+tbvk,*a,*+2k");
						c("*!+tbvk,*ka");
						c("*!+234a,*ka,*+2k");
						c("*!+234a,*a,*+2k,*+2k");
						c("*!+2345v,*ka");
						c("*!+2345v,*a,*a");
						c("*!+2345v,*a,*+2k");
						c("*!+234ba,*a");
						c("*!+234ba,*+2k");
						c("*!+23vka");
						c("*!+234567,*ka,*a");
						c("*!+234567,*ka,*+2k");
						c("*!+234567,*a,*a,*+2k");
						c("*!+234567,*a,*+2k,*+2k");
						c("*!+23456a");
					}
					c("*!+2ka,*ka,*+2k");
					c("*!+2ka,*a,*+2k,*+2k");
					c("*!+9tbv,*ka,*a,*+2k");//26/07/16
					c("*!+9tbv,*ka,*+2k,*+2k");
					c("*!+9tbv,*ka,*ka");
					c("*!+tbvk,*ka,*a");//26/07/16
					c("*!+tbvk,*ka,*+2k");
					c("*!+tbvk,*a,*a,*+2k");
					c("*!+tbvk,*a,*+2k,*+2k");
					c("*!+23ba,*ka,*+2k");
					c("*!+23ba,*a,*+2k,*+2k");
					c("*!+23ka,*ka");
					c("*!+23ka,*a,*+2k");
					c("*!+234ba,*ka");
					c("*!+234ba,*a,*+2k");
					c("*!+23vka,*a");
					c("*!+23vka,*+2k");
					c("*!+2bvka");
					c("*!+23456b,*ka,*a");
					c("*!+23456b,*ka,*+2k");
					c("*!+23456b,*a,*a,*+2k");
					c("*!+23456b,*a,*+2k,*+2k");
					c("*!+23456a,*a");
					c("*!+23456a,*+2k");
					c("*!+2345ka");
					c("*!+2345678");
				}
				alleen=false;
				break;	
		}
	}

	public int getNumberOfPlayersMisereOrOpenmisere(){
		int numberOf=-1;
		if(maxChoice==MISERE || maxChoice==OPENMISERE){numberOf=0;}
		for(int a=1;a<=4;a++){
			if(playerChoice[a]==MISERE || playerChoice[a]==OPENMISERE){numberOf++;}
		}
		return numberOf;
	}
	
	public void setHighestChoice(){
		if(maxChoice==TROEL){//indien troelmee nog niet gekend is
			maxChoice=TROELMEE;
			playerChoice[troelMeePlayer]=TROELMEE;
			for(int aa=1;aa<=4;aa++){
				if(playerChoice[aa]==TROEL){
					playerTrumpCategory[troelMeePlayer]=playerTrumpCategory[aa];
					break;
				}
			}	
		}
		if(maxChoice==VRAAG){
			for(int aa=2;aa<=4;aa++){ //speler 1 maakt de keuze zelf
				if(playerChoice[aa]==VRAAG){
					spelerdatvraagt=aa;//extra
					alleen=true;
					setPlayer(aa);
					startChoice();
					setPlayer(startPlayer);
					break;
				}	
			}	
		}
		//de rest op pas zetten
		boolean bool;
		int a=dealer;
		do{
			a++;if(a==5)a=1;
			if(playerChoice[a]!=maxChoice){
				bool=true;
				if(maxChoice==TROELMEE && playerChoice[a]==TROEL)bool=false;
				if(maxChoice==MEEGAAN && playerChoice[a]==VRAAG)bool=false;
				if(bool){playerChoice[a]=PAS;playerTrumpCategory[a]=0;}
			}else{
				if(maxChoice==DANSEN9 || maxChoice==DANSEN9INTROEF || maxChoice==DANSEN10 || maxChoice==DANSEN10INTROEF ||
				   maxChoice==DANSEN11 || maxChoice==DANSEN11INTROEF || maxChoice==DANSEN12 || maxChoice==DANSEN12INTROEF ||
					maxChoice==TROELMEE || maxChoice>OPENMISERE){
					setTrumpCategory(playerTrumpCategory[a]);
				}
				if(maxChoice==MISERE || maxChoice==OPENMISERE){
					setTrumpCategory(0);
				}
				//startspeler en speler vastleggen
				if(maxChoice==DANSEN9 || maxChoice==DANSEN9INTROEF || maxChoice==DANSEN10 || maxChoice==DANSEN10INTROEF ||
				   maxChoice==DANSEN11 || maxChoice==DANSEN11INTROEF || maxChoice==DANSEN12 || maxChoice==DANSEN12INTROEF ||
					maxChoice>OPENMISERE || maxChoice==TROELMEE){
					startPlayer=a;player=a;//r;
				}	
			}
		}while(a!=dealer);
	}

	private void setStringsInList1(String string){
		arraylist1.clear();
		st=string.split(",");
		String[] str = null;
		switch(st.length){
			case 4:
				str=new String[]{"3210","3201","3120","3102","3021","3012","2310","2301","2130","2103",
					"2031","2013","1320","1302","1230","1203","1032","1023","0321","0312","0231","0213",
					"0132","0123"};//van 3 tot 0 gezet
				break;
			case 3:
				str=new String[]{"210","201","120","102","021","012"};//2 tot 0
				break;
			case 2:
				str=new String[]{"10","01"};//1 tot 0
				break;	
			case 1:
				str=new String[]{"0"};
				break;	
		}
		for(int i=0;i<str.length;i++){
			String stri="";
			for(int z=0;z<str[i].length();z++){
				int in=Integer.parseInt(String.valueOf(str[i].charAt(z)));
				if(z==0){stri=stri+st[in];}else{stri=stri+","+st[in];}
			}	
			Choice choi=new Choice(stri);
			arraylist1.add(choi);
		}		
	}

	private void c(String str){
		if (stringIsGood(str)){
			Choice ch=new Choice(str);
			arraylist2.add(ch);
		}	
	}

	private boolean choiceOk(){
		if(arraylist1.isEmpty()){toast("arraylist1 empty!!!!!");return false;}
		if(arraylist2.isEmpty()){return false;}
		if (ok) return true;
		for(int a=0;a<arraylist1.size();a++){
			for(int b=0;b<arraylist2.size();b++){
				ok=true;
				if(arraylist1.get(a).arrayLength < arraylist2.get(b).arrayLength){
					ok=false;continue;
				}	
				for(int i=0;i<arraylist2.get(b).arrayLength;i++){
					if(arraylist2.get(b).sameLength[i] && getNumberOfCharacters(arraylist1.get(a).str[i])!=
					   getNumberOfCharacters(arraylist2.get(b).str[i])){ok=false;break;}
					int z1=arraylist1.get(a).str[i].length();
					int z2=arraylist2.get(b).str[i].length();
					if(arraylist2.get(b).trump[i]){// !
						if(!arraylist1.get(a).trump[i]){ok=false;break;}
					}	
					if(arraylist2.get(b).plus[i]){// +
						while(z1>0 && z2>0){
							z1--;z2--;
							if(getCharacterValue(arraylist1.get(a).str[i].charAt(z1))<getCharacterValue((arraylist2.get(b).str[i].charAt(z2)))){
								ok=false;break;
							}
						}
						if(ok && z2!=0){ok=false;break;}//break
					}else if(arraylist2.get(b).min[i]){// -
						z1=-1;
						z2=-1;
						while(z1<arraylist1.get(a).str[i].length()-1 && z2<arraylist2.get(b).str[i].length()-1){
							z1++;z2++;
							if(getCharacterValue(arraylist1.get(a).str[i].charAt(z1))>getCharacterValue((arraylist2.get(b).str[i].charAt(z2)))){
								ok=false;break;
							}
						}
						if(ok && z2!=arraylist2.get(b).str[i].length()-1){ok=false;break;}//break
					}else{
						while(z1>0 && z2>0){
							z1--;z2--;
							if(getCharacterValue(arraylist1.get(a).str[i].charAt(z1))!=getCharacterValue((arraylist2.get(b).str[i].charAt(z2)))){
								ok=false;break;
							}
						}
						if(ok && z2!=0){ok=false;break;}//break
					}	
				}//for i
				if (ok) {
					if(choice==TROELMEE){
						for(int aa=1;aa<=4;aa++){
							if(playerChoice[aa]==TROEL){playerTrumpCategory[player]=playerTrumpCategory[aa];break;}
						}
					}
					if(choice==TROEL){
						boolean found=false;
						for(int aa=1;aa<=4;aa++){
							if(aa!=player){
								if(getPlayerDeckValue(aa,14).size()==1){
									playerTrumpCategory[player]=getPlayerDeckValue(aa,14).get(0).category;
									found=true;break;
								}
							}
						}	
						if(!found){//indien 4 azen...
							playerTrumpCategory[player]=1;//harten is dan zeker troef
						}
					}
					if(choice==DANSEN9 || choice==DANSEN9INTROEF || choice==DANSEN10 || choice==DANSEN10INTROEF ||
					   choice==DANSEN11 || choice==DANSEN11INTROEF || choice==DANSEN12 || choice==DANSEN12INTROEF ||
						choice==SOLOSLIM || choice==SOLO){
						setTrumpCategory(arraylist1.get(a).str[0]);//in str[0] zit geen !,opgepast in string wel	
					}
					return ok;
				}
			}
		}
		return ok;
	}
	
	private String getCardsString(int player){
		String str="";
		int cat=1;
		int oldCat=0;
		for(int b=1;b<=13;b++){
			while(playerDeck[player].card(b).category!=cat){cat++;}
			if(oldCat!=cat){
				if(str.compareTo("")!=0) str=str+",";
				if(playerDeck[player].card(b).category==getTrumpCategory()) str=str+"!";
				oldCat=cat;
			}
			str=str+playerDeck[player].card(b).getStringValue();
		}	
		return str;
	}

	private void setTrumpCategory(String str){
		String[]st2=cardsString.split(",");
		for(int i=0;i<st2.length;i++){
			if(st2[i].charAt(0)=='!'){st2[i]=st2[i].substring(1);break;}
		}
		int pos=12;
		for(int i=st2.length-1;i>-1;i--){//van rechts controleren	
			if(str.compareTo(st2[i])!=0){
				pos=pos-st2[i].length();
			}else{
				break;
			}
		}
		playerTrumpCategory[player]=deckCardTemp.get(pos).category;
	}

	private boolean stringIsGood(String str){
		char[]chars = str.toCharArray();
		CharacterIterator charIt=new StringCharacterIterator("+-*!,23456789tbvka");
		for(int i=0;i<str.length();i++){
			charIt.first();
			while(charIt.current()!=chars[i] && charIt.current()!=CharacterIterator.DONE){charIt.next();}
			if(charIt.current()==CharacterIterator.DONE){
				toast(String.valueOf(chars[i]) + " = " +  "wrong character");
				return false;
			}
		}	
		return true;
	}

	private int getNumberOfCharacters(String str){
		char[]chars = str.toCharArray();
		int number=0;
		CharacterIterator charIt=new StringCharacterIterator("23456789tbvka");
		for(int i=0;i<str.length();i++){
			charIt.first();
			while(charIt.current()!=chars[i] && charIt.current()!=CharacterIterator.DONE){charIt.next();}
			if(charIt.current()!=CharacterIterator.DONE){number++;}
		}
		return number;
	}

	private int getCharacterValue(char ch){
		CharacterIterator charIt=new StringCharacterIterator("0123456789tbvka");
		while(charIt.current()!=ch && charIt.current()!=CharacterIterator.DONE){charIt.next();}
		return charIt.getIndex();
	}

	public void setPlayerTrumpCategory(){
		playerTrumpCategory[player]+=1;
		if(playerTrumpCategory[player]==5){
			playerTrumpCategory[player]=1;
		}
	}
}
