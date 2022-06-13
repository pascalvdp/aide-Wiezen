package com.mycompany.Wiezen;
import java.util.*;
import android.content.*;
import android.widget.*;
import android.os.*;    

public class Game extends GameChoices
{
	Context mCtx;
	int card;//0 tot 12
	boolean[][][] followed=new boolean[5][5][5];//[player][otherplayer][category]
	int cat, turn, round;
	boolean[][] okay=new boolean[5][5];//[player][category] enkel openmisere
	
	public ArrayList<Integer>[] playCategory = new ArrayList[5];
	//weggooien bij de miserespeler komt hier op de eerste rij
	//niet volgen is verwijderen
	//niet volgen volgens elke speler wordt het niet verwijderd, 50% regel, enkel als
	//ze "all" toepassen (100% regel) dan wel
	//bij openmisere wordt dit niet gebruikt
	
	public ArrayList<ObjPlayer> getCategory = new ArrayList<ObjPlayer>();
	//rangorde wordt hier steeds bepaald in elke ronde, maar indien "kan er niet meer aan", dan
	//verwijderen(enkel bij de niet-passers), niet volgen is ook verwijderen
	//getCategory(Player) bestaat ook
	//bij openmisere wordt dit gedeeltelijk op een andere manier gebruikt=>
	//elke passer gaat zien of zijn laagste kaart onder de laagste kaart zit van de openmiserespelers
	//indien ja dan wordt dit hier in gezet
	
	//miserespeler => getcategory= direct uit lijst ,niet volgen= op einde ronde
	//passer(misere) => getcategory= niet uit lijst, niet volgen= direct toegepast
	//openmiserespeler => getcategory= direct uit lijst ,niet volgen= direct toegepast
	//passer(openmisere) => getcategory= direct uit lijst ,niet volgen= direct toegepast
	
	public Game(Context context){
		super(context);//enkel voor toast
		mCtx=context;
		for(int a=1;a<=4;a++){
			playCategory[a]=new ArrayList<Integer>();
		}
	}
	
	public void checkGameEnd(){//enkel bij misere en openmisere, of de laatste ronde is gespeeld
		//indien de misere- of openmiserespeler eraan is dan omzetten naar de passers
		if(maxChoice==MISERE){playCategory[startPlayer].clear();}
		if(playerChoice[startPlayer]==MISERE){playerChoice[startPlayer]=PAS;oldPlayerChoice[startPlayer]=MISERE;}
		if(playerChoice[startPlayer]==OPENMISERE){
			playerChoice[startPlayer]=PAS;oldPlayerChoice[startPlayer]=OPENMISERE;
		}
		//nu ook nog de vrienden en vijanden herinstellen
		setFriendsEnemies();
	
		if(playerDeck[startPlayer].isEmpty()){gameEnd=true;}
		if(!gameEnd){
			if((maxChoice==MISERE || maxChoice==OPENMISERE) && getNumberOfPlayersMisereOrOpenmisere()==0){gameEnd=true;}
		}
	}
	
	public void restartGame(){
		card=-1;
		for(int a=1;a<=4;a++){
			for(int b=1;b<=4;b++){
				okay[a][b]=false;
				for(int c=1;c<=4;c++){
					followed[a][b][c]=true;
				}
			}
			playCategory[a].clear();//enkel gebruikt voor misere en openmisere
		}
		setRangeCategory(0,true);//enkel gebruikt voor misere en openmisere
	}
	
	public void startGameSetVars(){
		turn=gameDeck.size()+1;
		round=(waitingDeck[0].size()/4)+(waitingDeck[1].size()/4)+1;
	}
	
	public void startGame(){
		turn=gameDeck.size()+1;
		if(player==startPlayer){ 
			cat=0;
			round=(waitingDeck[0].size()/4)+(waitingDeck[1].size()/4)+1;
			if(player!=1)chooseCardToStart();
		}else{
			cat = gameDeck.get(0).category;
			if(player!=1)chooseCardToFollow();
		}
	}

	private void chooseCardToStart(){
		switch(playerChoice[player]){
			case SOLOSLIM:
				alleenStart();	
				break;
			case SOLO:
				alleenStart();
				break;
			case OPENMISERE:
				misereStart();
				break;	
			case TROELMEE:
				vraagStart();
				break;	
			case TROEL:
				vraagStart();
				break;	
			case DANSEN12INTROEF:
				alleenStart();
				break;		
			case DANSEN12:
				alleenStart();
				break;	
			case DANSEN11INTROEF:
				alleenStart();
				break;		
			case DANSEN11:
				alleenStart();
				break;		
			case DANSEN10INTROEF:
				alleenStart();
				break;		
			case DANSEN10:
				alleenStart();
				break;	
			case MISERE:
				misereStart();
				break;
			case DANSEN9INTROEF:
				alleenStart();
				break;		
			case DANSEN9:
				alleenStart();
				break;	
			case MEEGAAN:
				vraagStart();
				break;
			case VRAAG:
				vraagStart();
				break;	
			case ALLEEN:
				alleenStart();
				break;
			case PAS:
				pasStart();
				break;	
		}
	}
	
	private void chooseCardToFollow(){
		switch(playerChoice[player]){
			case SOLOSLIM:
				vraagFollow();
				break;
			case SOLO:
				vraagFollow();
				break;
			case OPENMISERE:
				misereFollow();
				checkFollowed();
				break;	
			case TROELMEE:
				vraagFollow();
				break;	
			case TROEL:
				vraagFollow();
				break;
			case DANSEN12INTROEF:
				vraagFollow();
				break;		
			case DANSEN12:
				vraagFollow();
				break;	
			case DANSEN11INTROEF:
				vraagFollow();
				break;		
			case DANSEN11:
				vraagFollow();
				break;		
			case DANSEN10INTROEF:
				vraagFollow();
				break;		
			case DANSEN10:
				vraagFollow();
				break;	
			case MISERE:
				misereFollow();
				checkFollowed();
				break;
			case DANSEN9INTROEF:
				vraagFollow();
				break;		
			case DANSEN9:
				vraagFollow();
				break;	
			case MEEGAAN:
				vraagFollow();
				break;
			case VRAAG:
				vraagFollow();
				break;	
			case ALLEEN:
				vraagFollow();
				break;
			case PAS:
				pasFollow();
				break;	
		}
	}
	
	private void pasStart(){
		if(maxChoice==DANSEN9 || maxChoice==DANSEN9INTROEF || maxChoice==DANSEN10 || maxChoice==DANSEN10INTROEF ||
		   maxChoice==DANSEN11 || maxChoice==DANSEN11INTROEF || maxChoice==DANSEN12 || maxChoice==DANSEN12INTROEF ||
			maxChoice>OPENMISERE){
			vraagStart();
		}
		if(maxChoice==OPENMISERE){
			pasStartOpenMisere();
			setCardOneValue(true);
		}
		if(maxChoice==TROELMEE){
			vraagStart();
		}
		if(maxChoice==MISERE){
			pasStartMisere();
			setCardOneValue(true);
		}
		if(maxChoice==MEEGAAN){
			vraagStart();
		}	
		if(maxChoice==ALLEEN){
			vraagStart();
		}
	}
	
	private void pasFollow(){
		if(maxChoice==DANSEN9 || maxChoice==DANSEN9INTROEF || maxChoice==DANSEN10 || maxChoice==DANSEN10INTROEF ||
		   maxChoice==DANSEN11 || maxChoice==DANSEN11INTROEF || maxChoice==DANSEN12 || maxChoice==DANSEN12INTROEF || 
			maxChoice>OPENMISERE){
			pasFollowAlleen();
		}
		if(maxChoice==OPENMISERE){
			pasFollowOpenMisere();
		}
		if(maxChoice==TROELMEE){
			vraagFollow();
		}
		if(maxChoice==MISERE){
			pasFollowMisere();
		}
		if(maxChoice==MEEGAAN){
			vraagFollow();
		}	
		if(maxChoice==ALLEEN){
			pasFollowAlleen();
		}
	}
	
	private boolean everyoneNotFollowed(int category){
		int notFollowed=0;
		for(int a=1;a<=4;a++){
			if(a!=player){
				if(!followed[player][a][category]){notFollowed++;}
			}
		}	
		if(notFollowed==3){return true;}
		return false;
	}
	               
	public void checkFollowed(){//enkel bij volgen(enkel bij de miserespelers)
		//indien de miserespeler niet gevolgd heeft dan de afgelegde category 
		//in playCategory zetten op de eerste plaats
		if(maxChoice==MISERE && turn!=1){//turn!=1 bijgezet voor speler1
			if(playerDeck[player].get(card).category!=gameDeck.get(0).category){
				//eerst de category verwijderen als die er in zit
				for(int c=0;c<playCategory[player].size();c++){
					if(playCategory[player].get(c)==playerDeck[player].get(card).category){playCategory[player].remove(c);}
				}	
				playCategory[player].add(0,playerDeck[player].get(card).category);	
			}
		}
	}
	
	public void setFollowAfterPlayingCard(){
		//indien de speler waar je de kaarten kunt van zien zijn laatste kaart heeft afgelegd dan nietgevolgd toepassen
		if(maxChoice==OPENMISERE && (playerChoice[player]==OPENMISERE || oldPlayerChoice[player]==OPENMISERE)){
			int categ=gameDeck.get(gameDeck.size()-1).category;//laatste kaart in gamedeck
			if(getNumberOfCards(categ)==0){setFollow(0,player,categ);}
		}	
		//bij het starten voor ronde 1 wordt dit bepaald in "game.setRangeCategory"
	}
	
	public void setOkayAfterPlayingCard(){
		//okay bepalen,enkel bij 1 openmiserespeler
		if(getNumberOfPlayersMisereOrOpenmisere()==1 && maxChoice==OPENMISERE && 
			playerChoice[player]!=OPENMISERE && oldPlayerChoice[player]!=OPENMISERE){
			int categoryStart=gameDeck.get(0).category;
			int categ=gameDeck.get(gameDeck.size()-1).category;//laatste kaart in gamedeck
			if(categ!=categoryStart){
				 for(int c=0;c<getCategory.size();c++){
					 int category=getCategory.get(c).category;
					 int underPlayer=getCategory.get(c).underPlayer;
					 if(underPlayer!=0 && getCategory.get(c).player!=player){
						 if(categ==category){
							 break;
						 }else{
							 okay[player][category]=true;
							 toast("okay is true gezet!!!");
						 }
					 }
				 } 
			}
		}	
	}
	
	public void setFollow(int player,int otherPlayer, int category){
		if(otherPlayer==0 || category==0){toast("fout in programma");return;}
		if(player==0){
			for(int a=1;a<=4;a++){
				if(a!=otherPlayer){followed[a][otherPlayer][category]=false;}		
			}	
		}else{
			if(player!=otherPlayer){followed[player][otherPlayer][category]=false;}
		}
	}
	
	private boolean playTrump(){
		int notFollowedEnemies=0;
		int followedC=-1;
		for(int c=0;c<enemy[player].length;c++){
			if(!followed[player][enemy[player][c]][trumpCategory]){
				notFollowedEnemies++;
			}else{
				followedC=c;
			}
		}	
		if(notFollowedEnemies>1){return false;}
		int totalCards=getNumberOfPlayedCards(trumpCategory) + getNumberOfCards(trumpCategory);
		if(totalCards>10){return false;}
		if(notFollowedEnemies==1){
			if(lastPlayedCard(enemy[player][followedC]).value>29){return false;}
		}
		if(getNumberOfHatchedTrump()>1){return false;}
		return true;
	}
	
	private boolean playTrumpAlone(){  
		int followedEnemies=0;
		for(int c=0;c<enemy[player].length;c++){
			if(followed[player][enemy[player][c]][trumpCategory]){followedEnemies++;}
		}	
		boolean highestValue=false;
		for(int a=1;a<=4;a++){
			if(a!=getTrumpCategory()){
				if(hasHighestValue(a)){highestValue=true;}
			}	
		}
		if(followedEnemies==0){return false;}
		if(getNumberOfCards(trumpCategory)==1){
			if(hasHighestValue(trumpCategory)){
				if(!highestValue){return false;}
				//if(random(2)==1){return false;}
			}else{
				return false;
			}
		}	
		//indien maar 1 troefkaart bij de 3 vijanden dan followedEnemies=1
		int totalCards=getNumberOfPlayedCards(trumpCategory) + getNumberOfCards(trumpCategory);
		if(totalCards==12){followedEnemies=1;}
		//indien niet hoogste kaart niet-troef dan...
		if(!highestValue){
			//zien of men de hoogste troef heeft en vijanden nog wel troef
			//om troeven van de vijanden af te nemen
			if(!hasHighestValue(trumpCategory) && followedEnemies<2){return false;}
		}
		
		//zien of men de hoogste troef heeft en vijanden nog wel troef
		//om troeven van de vijanden af te nemen
		//if(!hasHighestValue(trumpCategory) && followedEnemies<2){return false;}
		return true;
	}
	
	private void vraagStart(){
		int cate=random(4);//1 tot 4
		if(playerChoice[player]==TROELMEE && round==1){
			if(getHighestValue(trumpCategory)!=0){toast("hoogste in troef,round1");return;}
		}
		if(playerChoice[player]!=PAS){
			if(playTrump()){
				if(hasHighestValue(getTrumpCategory())){toast("allerhoogste in troef");return;}
				if(getLowestValue(getTrumpCategory())!=0){toast("laagste in troef");return;}
			}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(!enemyCanBuy(cate) && hasHighestValue(cate)){toast("allerhoogste in categorie...+ !enemyCanBuy"+String.valueOf(cate));return;}
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(friendCanBuy(cate)){
					if(hasHighestValue(cate)){toast("allerhoogste in categorie...+ friendCanBuy "+String.valueOf(cate));return;}
					if(getLowestValue(cate)!=0){toast("laagste in...+ friendCanBuy"+String.valueOf(cate));return;}
				}  
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){                             //getNumberOfCards(trumpCategory)>0   24/10/16
				if(!enemyCanBuy(cate) && getNumberOfCards(cate)==1 && getNumberOfCards(trumpCategory)>0 
					&& getLowestValue(cate)!=0){
					toast("nog 1 van...+ !enemyCanBuy + zelf nog troef, "+String.valueOf(cate));return;
				}
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(!enemyCanBuy(cate) && getLowestValue(cate,true)!=0){
					toast("laagste in...+ !enemyCanBuy"+String.valueOf(cate));return;
				}
			}
			cate++;if(cate==5){cate=1;}
		}
		//zien of men de hoogste troef heeft en vriend geen troef en vijanden nog wel troef
		//om troeven van de vijanden af te nemen
		if(hasHighestValue(trumpCategory) && !followed[player][friend[player][0]][trumpCategory]){
			for(int b=0;b<enemy[player].length;b++){
				if(followed[player][enemy[player][b]][trumpCategory]){
					toast("allerhoogste in troef");return;
				}
			}		
		}
		if(getLowestValue(true)!=0){toast("laagste in...");return;}
		//vanaf hier enkel nog troef
		if(hasHighestValue(getTrumpCategory())){toast("allerhoogste in troef");return;}
		if(getLowestValue(getTrumpCategory())!=0){toast("laagste in troef");return;}
	}
	
	private void vraagFollow(){
		boolean playTrumpCard=true;
		if(turn==2 || turn==3){
			if(enemyHighestValue()){
				if(!stillPlayEnemies() && getValueHigherThan(getWinnerValue(),cat, true, true)){toast("hoger dan...");return;}
				boolean enemyFollowed=false;
				for(int b=0;b<enemy[player].length;b++){
					if(stillPlay(enemy[player][b]) && followed[player][enemy[player][b]][cat]){enemyFollowed=true;}
				}	
				if(enemyFollowed && hasHighestValue(cat)){toast("allerhoogste in categorie... "+String.valueOf(cat));return;}//!enemyCanBuy(cat)
				//regel hierboven aangepast, 26/10/2017
				if(!enemyCanBuy(cat) && getValueHigherThan(getWinnerValue(),cat, true, true)){toast("hoger dan...stap2... "+String.valueOf(cat));return;}
				//regel hierboven bijgezet, 26/10/2017
				if(stillPlayFriends()){
					if(getValueHigherThan(getWinnerValue(),cat, true, true)){toast("hoger dan...");return;}
				}else{
					if(getValueBetween(getWinnerValue(), getHighestValueToPlay(cat)+1, cat, false, true)){
						toast("value tussen...stap1.2");return;
					}
					if(getRandomValueHigherThan(getWinnerValue(),cat, false, true)){toast("random hoger dan...");return;}
				}	
			}else{
				if(!hasHighestValue(getWinnerValue(),cat) && !enemyCanBuy(cat)){//  !enemyCanBuy &&
					if(hasHighestValue(cat) && !hasAllTheHigherValues()){
						toast("allerhoogste in categorie... "+String.valueOf(cat));return;
					} 
					if(getValueBetween(getWinnerValue(), getHighestValueToPlay(cat)+1, cat, false, false)){
						toast("value tussen...stap2.1");return;
					} 
					if(getValueHigherThan(getWinnerValue(),cat, true, false)){toast("hoger dan...");return;}
				}	
			}
			if(getLowestValue(cat)!=0){toast("laagste dat ik kan volgen");return;}
			setFollow(0,player,cat);
			if(enemyHighestValue()){
				if(enemyCanBuy(cat) && getRandomValueHigherThan(getWinnerValue(),trumpCategory,false,true)){
					toast("gekocht, randomvalue");return;
				}
			}	
			else{
				if(enemyCanBuy(cat) && getWinnerValue()<20 && getRandomValueHigherThan(getWinnerValue(),trumpCategory,false,true)){
					toast("gekocht, randomvalue");return;
				}
			}
			if(enemyHighestValue()){
				//dit is om te bepalen of men echt wel moet kopen
				if(!stillPlayEnemies() && !hasHighestValue(getWinnerValue(),cat)){
					for(int b=0;b<friend[player].length;b++){
						if(stillPlay(friend[player][b]) && followed[player][friend[player][b]][cat]){
							if(random(2)==1) {playTrumpCard=false;}
							break;
						}
					}
				}
				if(playTrumpCard && getValueHigherThan(getWinnerValue(),trumpCategory, true, true)){checkCardPlayed();toast("gekocht");return;}
			}else{
				if(!hasHighestValue(getWinnerValue(),cat)){
					if(getValueHigherThan(getWinnerValue(),trumpCategory,false, false)){checkCardPlayed();toast("gekocht");return;}
				}
			}
		}
		if(turn==4){
			if(enemyHighestValue()){
				if(getValueHigherThan(getWinnerValue(),cat, false, true)){toast("hoger dan...");return;}
			}
			if(getLowestValue(cat)!=0){toast("laagste dat ik kan volgen");return;}
			setFollow(0,player,cat);
			if(enemyHighestValue()){
				if(getValueHigherThan(getWinnerValue(),trumpCategory, false, true)){checkCardPlayed();toast("gekocht");return;}
			}
		}
		//vanaf hier niet gekocht maar je moest wel kopen dan geen troef meer
		if(playTrumpCard){
			if(enemyHighestValue() || !enemyHighestValue() && 
			   !hasHighestValue(getWinnerValue(),cat)){
				if(getWinnerValue()>22){
					//indien er nog gekocht is
					if(getHighestValueInAscendingOrder(trumpCategory)>=getWinnerValue()){
						setFollow(0,player,trumpCategory);
						toast("geen troef meer want je moest kopen");
					}   
				}else{
					setFollow(0,player,trumpCategory);
					toast("geen troef meer want je moest kopen");
				}
			}
		}
		//vanaf hier de kaarten die je weggooit
		int cate=random(4);//1 tot 4
		for(int a=1;a<=4;a++){
			if(cate!=trumpCategory && !hasHighestValue(getHighestValue(cate),cate) && 
			   getNumberOfCards(trumpCategory)>0 && getNumberOfCards(cate)==1 && getLowestValue(cate)!=0){
				toast("nog 1 van...en zelf nog troef hebben"+String.valueOf(cate));return;	
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=trumpCategory && !hasHighestValue(getHighestValue(cate),cate) && getNumberOfCards(cate)>1 &&
			   getNumberOfCardsToPlay(cate)>0 && getLowestValue(cate)!=0){
				for(int c=0;c<enemy[player].length;c++){
					if(!followed[player][enemy[player][c]][cate] && followed[player][c][trumpCategory]){
						toast("weg omdat vijand zou kunnen kopen, rekening houdend met hogere kaarten "+String.valueOf(cate));return;
					}
				}		
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=trumpCategory && getNumberOfCards(cate)>1){
				if(getNumberOfCardsToPlay(cate)>0 && getLowestValue(cate)!=0){
					toast("mag weg, rekening houdend met hogere kaarten");return;
				}
			} 
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=trumpCategory && !hasHighestValue(getHighestValue(cate),cate) && 
					getNumberOfCards(cate)>1  &&  getLowestValue(cate)!=0){
				toast("mag weg indien men niet de allerhoogste kaart heeft");return;
			} 
			cate++;if(cate==5){cate=1;}
		}
		if(getLowestValue()!=0){toast("laagste in...");return;}
		//vanaf hier enkel nog troef
		if(getLowestValue(getTrumpCategory())!=0){toast("laagste in troef");return;}
	}
	
	private void alleenStart(){
		int cate=random(4);//1 tot 4
		if(playerChoice[player]!=PAS){
			if(playTrumpAlone()){
				if(hasHighestValue(getTrumpCategory())){toast("allerhoogste in troef");return;}
				if(getValueBetween(0, getHighestValueToPlay(getTrumpCategory())+1, getTrumpCategory(), false, true)){
					toast("value tussen...stap1.0");return;
				}
				if(getLowestValue(getTrumpCategory())!=0){toast("laagste in troef");return;}
			}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(!enemyCanBuy(cate) && hasHighestValue(cate)){
					setCardOneValue(false);//bij alleen kan je beter een kaart lager gaan
					toast("allerhoogste in categorie...+setCardOneValue + !enemyCanBuy"+String.valueOf(cate));return;}
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(!enemyCanBuy(cate) && getNumberOfCards(cate)==1 && getLowestValue(cate)!=0){
					toast("nog 1 van...+ !enemyCanBuy"+String.valueOf(cate));return;
				}
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(!enemyCanBuy(cate) && getNumberOfCardsToPlay(cate)==getNumberOfCards(cate)){
					if(getValueBetween(0, getHighestValueToPlay(cate)+1, cate, false, true)){
						toast("value tussen...stap1.0, rekening houdend met hogere kaarten + !enemyCanBuy");return;
					}
				}
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(!enemyCanBuy(cate)){
					if(getValueBetween(0, getHighestValueToPlay(cate)+1, cate, false, true)){
						toast("value tussen...stap1.0 + !enemyCanBuy");return;
					}
				}
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(!enemyCanBuy(cate) && getLowestValue(cate)!=0){
					toast("laagste in...+ !enemyCanBuy"+String.valueOf(cate));return;
				}
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(getNumberOfCardsToPlay(cate)==getNumberOfCards(cate)){//rekening houdend met hogere kaarten
					if(getValueBetween(0, getHighestValueToPlay(cate)+1, cate, false, true)){
						toast("value tussen...stap1.0, rekening houdend met hogere kaarten");return;
					}	
				}
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=getTrumpCategory()){
				if(getValueBetween(0, getHighestValueToPlay(cate)+1, cate, false, true)){
					toast("value tussen...stap1.0");return;
				}	
			}
			cate++;if(cate==5){cate=1;}
		}
		if(getLowestValue()!=0){toast("laagste in...");return;}
		//vanaf hier enkel nog troef
		if(hasHighestValue(getTrumpCategory())){toast("allerhoogste in troef");return;}
		if(getValueBetween(0, getHighestValueToPlay(getTrumpCategory())+1, getTrumpCategory(), false, true)){
			toast("value tussen...stap1.0");return;
		}
		if(getLowestValue(getTrumpCategory())!=0){toast("laagste in troef");return;}
	}
	
	private void pasFollowAlleen(){
		boolean enemyCanBuy=false;
		for(int b=0;b<enemy[player].length;b++){
			if(stillPlay(enemy[player][b]) && !followed[player][enemy[player][b]][cat] &&
			   followed[player][enemy[player][b]][trumpCategory]){enemyCanBuy=true;}
		}	
		boolean playTrumpCard=true;
		if(turn==2 || turn==3){
			if(enemyHighestValue()){
				if(getValueHigherThan(getWinnerValue(),cat, true, true)){toast("hoger dan...");return;}
			}else{
				if(stillPlayEnemies()){
					if(!hasHighestValue(getWinnerValue(),cat)){
						if(hasHighestValue(cat) && !hasAllTheHigherValues()){
							toast("allerhoogste in categorie... "+String.valueOf(cat));return;
						} 
						if(getValueBetween(getWinnerValue(), getHighestValueToPlay(cat)+1, cat, false, false)){
							toast("value tussen...stap2.1");return;
						}
						if(getValueHigherThan(getWinnerValue(),cat, true, false)){toast("hoger dan...");return;}
					}	
				}
			}
			if(getLowestValue(cat)!=0){toast("laagste dat ik kan volgen");return;}
			setFollow(0,player,cat);
			if(enemyHighestValue()){
				if(enemyCanBuy && getRandomValueHigherThan(getWinnerValue(),trumpCategory,false,true)){
					toast("gekocht, randomvalue");return;
				}
			}	
			else{
				if(enemyCanBuy && getWinnerValue()<20 && getRandomValueHigherThan(getWinnerValue(),trumpCategory,false,true)){
					toast("gekocht, randomvalue");return;
				}
			}
			if(enemyHighestValue()){
				//dit is om te bepalen of men echt wel moet kopen
				if(!stillPlayEnemies() && !hasHighestValue(getWinnerValue(),cat)){
					for(int b=0;b<friend[player].length;b++){
						if(stillPlay(friend[player][b]) && followed[player][friend[player][b]][cat]){
							if(random(2)==1) {playTrumpCard=false;}
							break;
						}
					}
				}
				if(playTrumpCard && getValueHigherThan(getWinnerValue(),trumpCategory, true, true)){
					checkCardPlayed();toast("gekocht");return;
				}
			}else{
				if(stillPlayEnemies() && !hasHighestValue(getWinnerValue(),cat)){
					if(getValueHigherThan(getWinnerValue(),trumpCategory,false, false)){checkCardPlayed();toast("gekocht");return;}
				}
			}
		}
		if(turn==4){
			if(enemyHighestValue()){
				if(getValueHigherThan(getWinnerValue(),cat, false, true)){toast("hoger dan...");return;}
			}
			if(getLowestValue(cat)!=0){toast("laagste dat ik kan volgen");return;}
			setFollow(0,player,cat);
			if(enemyHighestValue()){
				if(getValueHigherThan(getWinnerValue(),trumpCategory, false, true)){checkCardPlayed();toast("gekocht");return;}
			}
		}
		//vanaf hier niet gekocht maar je moest wel kopen dan geen troef meer
		if(playTrumpCard){
			if(enemyHighestValue() || !enemyHighestValue() && 
			   !hasHighestValue(getWinnerValue(),cat) && stillPlayEnemies()){
				if(getWinnerValue()>22){
					//indien er nog gekocht is
					if(getHighestValueInAscendingOrder(trumpCategory)>=getWinnerValue()){
						setFollow(0,player,trumpCategory);
						toast("geen troef meer want je moest kopen");
					}   
				}else{
					setFollow(0,player,trumpCategory);
					toast("geen troef meer want je moest kopen");
				}
			}
		}
		//vanaf hier de kaarten die je weggooit
		int cate=random(4);//1 tot 4
		for(int a=1;a<=4;a++){
			if(cate!=trumpCategory && !hasHighestValue(getHighestValue(cate),cate) && 
			   getNumberOfCards(trumpCategory)>0 && getNumberOfCards(cate)==1 && getLowestValue(cate)!=0){
				toast("nog 1 van...en zelf nog troef hebben"+String.valueOf(cate));return;	
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=trumpCategory && !hasHighestValue(getHighestValue(cate),cate) && getNumberOfCards(cate)>1 &&
			   getNumberOfCardsToPlay(cate)>0 && getLowestValue(cate)!=0){
				for(int c=0;c<enemy[player].length;c++){
					if(!followed[player][enemy[player][c]][cate] && followed[player][c][trumpCategory]){
						toast("weg omdat vijand zou kunnen kopen, rekening houdend met hogere kaarten "+String.valueOf(cate));return;
					}
				}		
			}
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=trumpCategory && getNumberOfCards(cate)>1){
				if(getNumberOfCardsToPlay(cate)>0 && getLowestValue(cate)!=0){
					toast("mag weg, rekening houdend met hogere kaarten");return;
				}
			} 
			cate++;if(cate==5){cate=1;}
		}
		for(int a=1;a<=4;a++){
			if(cate!=trumpCategory && !hasHighestValue(getHighestValue(cate),cate) && 
			   getNumberOfCards(cate)>1  &&  getLowestValue(cate)!=0){
				toast("mag weg indien men niet de allerhoogste kaart heeft");return;
			} 
			cate++;if(cate==5){cate=1;}
		}
		if(getLowestValue()!=0){toast("laagste in...");return;}
		//vanaf hier enkel nog troef
		if(getLowestValue(getTrumpCategory())!=0){toast("laagste in troef");return;}
	}
	
	private void misereStart(){
		int cate=random(4);//1 tot 4
		//als men bvb enkel 3 heeft dan kan men deze kaart wel uitkomen,enkel bij misere
		if(maxChoice==MISERE){
			for(int a=1;a<=4;a++){
				if(getNumberOfCards(a)==1 && getLowestValue(a)!=0 && playerDeck[player].get(card).value==3){
					toast("enkel 3");return;
				}
			}	
		}
		//zoek hoogste kaart en ga dan 1 waarde lager enz..
		//indien allerlaagste kaart=2 dan ok
		for(int a=1;a<=4;a++){
			if(getHighestValue(cate)!=0){
				setCardOneValue(false);
				if(playerDeck[player].get(card).value==2){
					toast("alle kaarten zijn allerlaagste en laagste=2, kleur="+String.valueOf(cate));return;
				}
			}
			cate++;if(cate==5){cate=1;}		
		}  
		//indien er spelers zijn waar je de kaarten kunt van zien en die nog een kaart moeten afleggen
		//dan kan je gewoon onder deze kaart gaan als je er maar 1 van hebt
		if(maxChoice==OPENMISERE){//enkel bij openmisere
			for(int a=1;a<=4;a++){
				if(getNumberOfCards(cate)==1){
					int winnerValue=0;
					int z=player;
					z++;if(z==5){z=1;}
					while(z!=startPlayer){
						if(playerChoice[z]==OPENMISERE || oldPlayerChoice[z]==OPENMISERE){
							if(getLowestValue(z,cate)>winnerValue){winnerValue=getLowestValue(z,cate);}
						}
						z++;if(z==5){z=1;}
					}	
					if(getValueLowerThan(winnerValue,cate,true) ){//&& playerDeck[player].get(card).value!=getLowestValue(cate)
						//getValueLowerThan(winnerValue,cate,true);
						toast("lager dan...,rekeninghoudend met de kaarten dat je kan zien, en maar 1 kaart");return;
					}
				}
				cate++;if(cate==5){cate=1;}
			}	
		}
		//bvb 2345 bvb 2356 dan 2345 de laagste kaart, enkel 2 wordt niet gekozen
		int numberOf=0;int category=0;
		for(int a=1;a<=4;a++){
			if(getLowestValue(a)!=0 && playerDeck[player].get(card).value==2){
				setCardOneValue(true);
				int numberOf2=getNumberOfCardsBetween(1,playerDeck[player].get(card).value+1,a);
				if(numberOf2>numberOf){
					numberOf=numberOf2;category=a;
				}
			}
		}	
		if(numberOf>1 && category!=0 && getLowestValue(category)!=0){toast("laagste van 2345...");return;}	
		//vanaf hier heeft men ofwel enkel 245... of 256..enz... of 345...356...enz...ook 357... kan
		//bij openmisere is dit wel belangrijk
		//eerst zien of dat men start met de 2,bvb.256...vergelijken met 246...
		//2+5=7 2+4=6, dus van de 246... wordt deze 2 gekozen
		int totalz=100;category=0;
		for(int z=2;z<=14;z++){//value
			for(int a=1;a<=4;a++){//category
				if(getNumberOfCards(a)>1  && getLowestValue(a)!=0 && playerDeck[player].get(card).value==z){
					if(totalz > z + playerDeck[player].get(card+1).value){
						totalz = z + playerDeck[player].get(card+1).value;
						category = a;
					}
				}
			}
			if(category!=0 && getLowestValue(category)!=0){toast("laagste van bvb.245...");return;}
		}
		//de allerlaagste kaart
		if(getLowestValue()!=0){toast("laagste in...");return;}
	}
	
	private void misereFollow(){  
		//indien er spelers zijn waar je de kaarten kunt van zien en die nog een kaart moeten afleggen
		//dan kan je gewoon onder deze kaart gaan en anders gewoon onder getwinnervalue
		if(maxChoice==OPENMISERE){//enkel bij openmisere
			int winnerValue=getWinnerValue();
			int z=player;
			z++;if(z==5){z=1;}
			while(z!=startPlayer){
				if(playerChoice[z]==OPENMISERE || oldPlayerChoice[z]==OPENMISERE){
					if(getLowestValue(z,cat)>winnerValue){winnerValue=getLowestValue(z,cat);}
				}
				z++;if(z==5){z=1;}
			}	
			if(getValueLowerThan(winnerValue,cat, false)){toast("lager dan...,rekeninghoudend met de kaarten dat je kan zien");return;}
		}
		if(maxChoice==MISERE){
			if(getValueLowerThan(getWinnerValue(),cat, false)){toast("lager dan...");return;}
		}
		//indien verloren...
		if(getLowestValue(cat)!=0){
			if(getNumberOfPlayersMisereOrOpenmisere()>1 && isLost() && getHighestValue(cat)!=0){
				toast("hoogste dat ik kan volgen want ik ben verloren");return;
			}
			toast("laagste dat ik kan volgen");return;
		}
		
		//vanaf hier de kaarten die je weggooit
		//eerst de category verwijderen als die er nog in zit
	/*	if(maxChoice!=OPENMISERE){
			for(int c=0;c<getCategory.size();c++){
				if(getCategory.get(c).player==player && getCategory.get(c).category==cat){getCategory.remove(c);}
			}	
		}*/
		if(maxChoice==MISERE){
			for(int c=0;c<playCategory[player].size();c++){
				if(playCategory[player].get(c)==cat){
					playCategory[player].remove(c);
				}
			}
		}
		
		//setRangeCategory(player,false);
		ArrayList<ObjPlayer> arList = getCategory(player);
		int cate;
		for(int d=0;d<arList.size();d++){	
			cate=arList.get(d).category;
			if(hasOnlyAllTheLowestValues(cate)){continue;}
			if(getHighestValue(cate)!=0){
				for(int c=0;c<enemy[player].length;c++){
					if(followed[player][enemy[player][c]][cate]){
						toast("hoogste kaart in getCategory en vijand nog gevolgd"+String.valueOf(cate));return;
					}	
				}				
			}
		}	
		if(getHighestValue()!=0){toast("hoogste in...");return;}
	}
	
	private void pasStartMisere(){
		//kom de category uit die de vijanden hebben afgelegd bij het niet-volgen
		for(int b=0;b<enemy[player].length;b++){
			for(int c=0;c<playCategory[enemy[player][b]].size();c++){
				int category=playCategory[enemy[player][b]].get(c);
				//zien of vijand nog zou volgen, 50% regel
				if(!followed[player][enemy[player][b]][category]){continue;}
				//controle of je enkel al de allerhoogsten hebt
				if(hasOnlyAllTheHighestValues(category)){continue;}
				//controle of je enkel deze kleur hebt
				if(everyoneNotFollowed(category)){continue;}
				//is volgende speler=misere of alle vrienden niet gevolgd
				if(isNextPlayer()==enemy[player][b] || allFriendsNotFollowed(category)){
					if(getLowestValue(category)!=0){
						toast("laagste in playCategory "+String.valueOf(category));return;
					}
				}else{
					int minValue=getLowestValue(category)-1;
					int maxValue=getValueHighestValue(category)-getValueLowestValue(category)+1;
					if(getValueBetween(minValue,maxValue,category,false,true)){
			 			toast("value tussen... in playCategory "+String.valueOf(category));return;		
					}else{
						if(getLowestValue(category)!=0){
							toast("laagste in playCategory ,stap2  "+String.valueOf(category));return;
						}
					}
				} 
			}	
		}

		//category uitkomen van de laatste die is uitgekomen... +enemycanfollowed
		if(lastReleasedPlayerIsFriend()){
			int category=lastPlayedCard(oldStartPlayer).category;
			for(int b=0;b<enemy[player].length;b++){
				//controle of je enkel al de allerhoogsten hebt
				if(hasOnlyAllTheHighestValues(category)){break;}
				if(followed[player][enemy[player][b]][category]){
					if(isNextPlayer()==enemy[player][b] || allFriendsNotFollowed(category)){
						if(getLowestValue(category)!=0){
							toast("laagste in...category uitkomen van de laatste die is uitgekomen +enemycanfollowed "+String.valueOf(category));return;
						}
					}else{
						int minValue=getLowestValue(category)-1;
						int maxValue=getValueHighestValue(category)-getValueLowestValue(category)+1;
						if(getValueBetween(minValue,maxValue,category,false,true)){
							toast("value tussen...category uitkomen van de laatste die is uitgekomen +enemycanfollowed "+String.valueOf(category));return;		
						}else{
							if(getLowestValue(category)!=0){
								toast("laagste in...stap2 + category uitkomen van de laatste die is uitgekomen +enemycanfollowed "+String.valueOf(category));return;
							}
						}
					}		
				}	
			}	
		}

		int cate=random(4);//1 tot 4
		//zoek hoogste kaart en ga dan 1 waarde lager enz..
		//indien allerlaagste kaart en vijand kan volgen
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				if(getHighestValue(cate)!=0){
					setCardOneValue(false);
					int value=playerDeck[player].get(card).value;
					getLowestValue(cate);
					if(value==playerDeck[player].get(card).value && hasLowestValue(cate) &&
					   followed[player][enemy[player][b]][cate] && getLowestValue(cate)!=0){
						toast("alle kaarten zijn allerlaagste + enemyCanFollowed "+String.valueOf(cate));return;
					}
				}
			}
			cate++;if(cate==5){cate=1;}
		}              

		//laagste in...	+enemycanfollowed					  
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				//controle of je enkel al de allerhoogsten hebt
				if(hasOnlyAllTheHighestValues(cate)){break;}
				if(followed[player][enemy[player][b]][cate]){
					if(isNextPlayer()==enemy[player][b] || allFriendsNotFollowed(cate)){
						if(getLowestValue(cate)!=0){
							toast("laagste in...+ enemyCanFollowed "+String.valueOf(cate));return;
						}
					}else{
						int minValue=getLowestValue(cate)-1;
						int maxValue=getValueHighestValue(cate)-getValueLowestValue(cate)+1;
						if(getValueBetween(minValue,maxValue,cate,false,true)){
							toast("value tussen... + enemyCanFollowed "+String.valueOf(cate));return;		
						}else{
							if(getLowestValue(cate)!=0){
								toast("laagste in...stap2 + enemyCanFollowed "+String.valueOf(cate));return;
							}
						}
					}	
				}
			}	
			cate++;if(cate==5){cate=1;}
		}
		//laagste in...				  
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				//controle of je enkel al de allerhoogsten hebt
				if(hasOnlyAllTheHighestValues(cate)){break;}
				//controle of je enkel deze kleur hebt
				if(everyoneNotFollowed(cate)){break;}
				if(getLowestValue(cate)!=0){toast("laagste in... "+String.valueOf(cate));return;}
			}	
			cate++;if(cate==5){cate=1;}
		}
		if(getLowestValue()!=0){toast("laagste in...");return;}
	}
	
	private void pasStartOpenMisere(){
		//zien of je je laagste kaart kunt uitkomen
		for(int c=0;c<getCategory.size();c++){
			int b=getCategory.get(c).player;
			int a=getCategory.get(c).category;
			//en zien of je onder de laagste kaart zit van een openmiserespeler
			if(playerChoice[b]==PAS && b==player){
				boolean ok=false;
				int lowestV;
				int getNumberOfCardsUnderPlayer;
				int remainingCards;
				int numberOfStartFriends;
				int numberOfOkay;
				for(int z=1;z<=4;z++){
					if(z!=b && playerChoice[z]==OPENMISERE){
						getNumberOfCardsUnderPlayer=0;
						lowestV=getLowestValue(z,a);
						if(getLowestValue(b,a)<lowestV){
							ok=true;
							for(int d=1;d<=4;d++){
								if(d!=b && oldPlayerChoice[d]==OPENMISERE){//openmiserespeler die eraan is
									if(getLowestValue(d,a)>lowestV){ok=false;break;}
								}
							}	
							if(ok){
								//alles van de vrienden bijeentellen waar je de kaarten niet van kunt zien
								remainingCards=0;numberOfStartFriends=0;numberOfOkay=0;
								for(int d=1;d<=4;d++){//alles van de vrienden bijeentellen 
									if(d!=b && playerChoice[d]==PAS && oldPlayerChoice[d]!=OPENMISERE){
										remainingCards=remainingCards+getNumberOfCards(d,a);
										//de kaarten die er onder zitten tellen 
										getNumberOfCardsUnderPlayer=getNumberOfCardsUnderPlayer+getNumberOfCardsBetween(0,lowestV,d,a);
										//aantal startvrienden tellen die ook nog gevolgd hebben
										if(followed[b][d][a]){numberOfStartFriends+=1;}
										//okay tellen
										if(okay[d][a]){numberOfOkay++;}
									}	
								}
								//numberOfOkay=2 dan return
								if(numberOfOkay==2 && getLowestValue(a)!=0){toast("laagste uitkomen, okay gelukt");return;}
								//checken of alles ok is
								if(numberOfStartFriends==1 && remainingCards>0 && getNumberOfCardsUnderPlayer==0){ok=false;}
								if(numberOfStartFriends>1 && remainingCards!=getNumberOfCardsUnderPlayer){ok=false;}
							}
							if(ok && getLowestValue(a)!=0){toast("laagste uitkomen, plan gelukt");return;}
						}
					}
				}
			}
		}	
		//kom de category uit in volgorde van getcategory, in de aanval...
		for(int c=0;c<getCategory.size();c++){
			if(getCategory.get(c).player==player){
				int category=getCategory.get(c).category;
				int underPlayer=getCategory.get(c).underPlayer;
				if(getCategory.get(c).checkedCards<=0){
					if(getCategory.get(c).remainingCards>0){        
						if(getHighestValue(category)!=0 && getNumberOfCards(category)!=1 && //==1 dan is dit de kaart die eronder zit
							getCategory.get(c).checkedCards!=getCategory.get(c).remainingCards){//if== dan heeft vijand maar 1 kaart meer
							toast("hoogste volgens getcategory,remainingcards>0,checkedCards<=0");return;
						}
					}	
				}		
				if(getCategory.get(c).checkedCards>0 && getCategory.get(c).checkedCards<20){
					if(getCategory.get(c).remainingCards>0){                         
						if(getHighestValue(category)!=0 && getNumberOfCards(category)!=1 && //==1 dan is dit de kaart die eronder zit
						   getCategory.get(c).checkedCards!=getCategory.get(c).remainingCards){//if== dan heeft vijand maar 1 kaart meer
							toast("hoogste volgens getcategory,remainingcards>0,checkedCards>0...<20");return;
						}
					}
				}
				//kaarten vrienden weghalen waar je kaarten kunt van zien
				if(underPlayer!=0){
					int cardsToPlay=getNumberOfCards(underPlayer,category)-1;
					for(int a=1;a<=4;a++){//category
						boolean found=false;
						for(int b=1;b<=4;b++){//speler
							//vriend waar je de kaarten kunt van zien
							if(b!=player && oldPlayerChoice[b]==OPENMISERE && getNumberOfCards(b,category)!=0){
								found=true;
								int totalEnemy=getNumberOfCards(underPlayer,a)+cardsToPlay;
								int totalFriend=getNumberOfCards(b,a)+getNumberOfCards(b,category);
								if(totalFriend>totalEnemy || getNumberOfCards(a)<totalFriend){found=false;break;}
							}	
						}
						if(found){
							if(getHighestValue(a)!=0){
								toast("kaarten vrienden weghalen waar je kaarten kunt van zien");return;
							}
						}
					}
				}
				//kaarten startvriend weghalen als andere vriend niet kan volgen
				if(underPlayer!=0){
					int cardsToPlay=getNumberOfCards(underPlayer,category)-1;
					for(int a=1;a<=4;a++){//category
						boolean found=false;
						boolean found1=false;
						boolean found2=false;
						for(int b=1;b<=4;b++){//speler
							//we zoeken 1 vriend die niet kan volgen...
							if(b!=player && playerChoice[b]==PAS && !followed[player][b][category]){found1=true;}
							//...en 1 startvriend waar we kaarten kunnen wegkrijgen
							if(b!=player && oldPlayerChoice[b]!=OPENMISERE && playerChoice[b]==PAS && getNumberOfCards(b,category)!=0){
								found2=true;
								int totalEnemy=getNumberOfCards(underPlayer,a)+cardsToPlay;
								int totalFriend=getNumberOfCards(b,a)+getNumberOfCards(b,category);
								if(totalFriend>totalEnemy || getNumberOfCards(a)<totalFriend){found2=false;}
							}	
						}
						if(found1 &&found2){found=true;}
						if(found){
							if(getHighestValue(a)!=0){
								toast("kaarten startvriend weghalen als andere vriend niet kan volgen");return;
							}
						}
					}
				}
				//kaarten startvrienden weghalen
				if(underPlayer!=0){
					int remainingCards=getCategory.get(c).remainingCards;
					int cardsToPlay=getNumberOfCards(underPlayer,category)-1;
					for(int a=1;a<=4;a++){//category
						boolean found=false;
						for(int b=1;b<=4;b++){//speler
							if(b!=player && oldPlayerChoice[b]==OPENMISERE){break;}
							//zoeken naar de 2 startvrienden waar we kaarten kunnen wegkrijgen
							if(b!=player && oldPlayerChoice[b]!=OPENMISERE && playerChoice[b]==PAS){
								if(followed[player][b][category] && !followed[player][b][a]){
									found=true;
									int totalEnemy=getNumberOfCards(underPlayer,a)+cardsToPlay;
									int totalFriend=remainingCards;
									if(totalFriend>totalEnemy || getNumberOfCards(a)<totalFriend){found=false;break;}
								}
							}
						}
						if(found){
							if(getHighestValue(a)!=0){
								toast("kaarten startvrienden weghalen");return;
							}
						}
					}
				}			
			}
		}
		
		//kom de category uit in volgorde van getcategory, in de aanval...maar dan volgens je vrienden
		//waar je de kaarten kunt van zien
		for(int c=0;c<getCategory.size();c++){
			int playerFriend=getCategory.get(c).player;
			if(playerFriend!=player && oldPlayerChoice[playerFriend]==OPENMISERE){
				int category=getCategory.get(c).category;
				//int underPlayer=getCategory.get(c).underPlayer;
				if(getCategory.get(c).checkedCards<=0){
					if(getCategory.get(c).remainingCards>0){     //remainingcards??????????? ok 
						if(getHighestValue(category)!=0 && getNumberOfCards(playerFriend,category)!=1 && //==1 dan is dit de kaart die eronder zit
							getCategory.get(c).checkedCards!=getCategory.get(c).remainingCards){//if== dan heeft vijand maar 1 kaart meer
							toast("hoogste volgens getcategory bij vrienden,remainingcards>0,checkedCards<=0");return;
						}
					}	
				}		
				if(getCategory.get(c).checkedCards>0 && getCategory.get(c).checkedCards<20){
					if(getCategory.get(c).remainingCards>0){       //remainingcards???????????     ok             
						if(getHighestValue(category)!=0 && getNumberOfCards(playerFriend,category)!=1 && //==1 dan is dit de kaart die eronder zit
						   getCategory.get(c).checkedCards!=getCategory.get(c).remainingCards){//if== dan heeft vijand maar 1 kaart meer
							toast("hoogste volgens getcategory bij vrienden,remainingcards>0,checkedCards>0...<20");return;
						}
					}
				}
			}
		}	
		
		//kom de category uit waarvan een openmiserespeler niet meer aan kan, wel zien dat een andere openmiserespeler 
		//er ook niet aan kan, kaarten van de vrienden proberen weg te halen
		for(int a=1;a<=4;a++){//category
			boolean ok=true;
			for(int b=1;b<=4;b++){//speler
				if(playerChoice[b]==OPENMISERE && getNumberOfCards(b,a)!=0){
					ArrayList<ObjPlayer> arList = getCategory(b);
					for(int d=0;d<arList.size();d++){	
						if(a==arList.get(d).category){ok=false;break;}
					}
				}
				if(playerChoice[b]==OPENMISERE && getNumberOfCards(b,a)==0){ok=false;}
			}
			if(ok){
				if(getHighestValue(a)!=0){
					toast("kaarten vrienden weghalen die niet in getcategory(speler) staan");return;
				}
			}
		}
		
		//laagste in...
		int cate=random(4);//1 tot 4
		
		//indien een vriend waar je de kaarten kunt van zien underplayer is dan proberen dat deze aan slag geraakt,andere vriend niet gevolgd
		for(int b=1;b<=4;b++){
			if(!okFriendsVisibleCards(cate)){
				for(int a=1;a<=4;a++){
					if(a!=player && oldPlayerChoice[a]==OPENMISERE){
						for(int c=0;c<getCategory.size();c++){
							int underPlayer = getCategory.get(c).underPlayer;
							if(underPlayer!=0 && getCategory.get(c).category==cate &&
							   getCategory.get(c).player==a && getNumberOfCards(a,cate)==1){
								//zoeken of vriend een kaart heeft die hoger is	  
								for(int d=1;d<=4;d++){
									if(d!=cate && getLowestValue(d)!=0 && getHighestValue(a,d)>getLowestValue(d)){  
										//indien meer dan 1 openmiserespeler dan return
										if(getNumberOfPlayersMisereOrOpenmisere()>1){
											toast("laagste in...,stap1,indien een vriend waar je de kaarten kunt van zien underplayer is dan proberen dat deze aan slag geraakt "+String.valueOf(d));return;
										}else{//nu blijft er nog 1 vriend over
											for(int e=1;e<=4;e++){//vriend zoeken
												if(e!=player && e!=a && playerChoice[e]!=OPENMISERE && !followed[player][e][d]){//not followed
													toast("laagste in...,stap2,indien een vriend waar je de kaarten kunt van zien underplayer is dan proberen dat deze aan slag geraakt "+String.valueOf(d));return;
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
			cate++;if(cate==5){cate=1;}
		}
		
		//indien een vriend waar je de kaarten kunt van zien underplayer is dan proberen dat deze aan slag geraakt,
		//andere vriend waar je de kaarten kunt van zien of vriend waar je de kaarten niet van kunt zien
		for(int b=1;b<=4;b++){
			if(!okFriendsVisibleCards(cate)){
				for(int a=1;a<=4;a++){
					if(a!=player && oldPlayerChoice[a]==OPENMISERE){
						for(int c=0;c<getCategory.size();c++){
							int underPlayer = getCategory.get(c).underPlayer;
							if(underPlayer!=0 && getCategory.get(c).category==cate &&
							   getCategory.get(c).player==a && getNumberOfCards(a,cate)==1){
								//zoeken of vriend een kaart heeft die hoger is	  
								for(int d=1;d<=4;d++){
									if(d!=cate && getLowestValue(d)!=0 && getHighestValue(a,d)>getLowestValue(d)){  
									//nu blijft er nog 1 vriend over waar we zeker van zijn
										for(int e=1;e<=4;e++){//vriend zoeken waar we de kaarten kunnen van zien
											if(e!=player && e!=a && oldPlayerChoice[e]==OPENMISERE){//
												if(getLowestValue(e,d)!=0 && getHighestValue(a,d)>getLowestValue(e,d)){
													toast("laagste in...,stap3,indien een vriend waar je de kaarten kunt van zien underplayer is dan proberen dat deze aan slag geraakt "+String.valueOf(d));return;
												}
											}//vriend zoeken waar we de kaarten niet kunnen van zien
											if(e!=player && e!=a && oldPlayerChoice[e]!=OPENMISERE && playerChoice[e]==PAS){//
												toast("laagste in...,stap4,indien een vriend waar je de kaarten kunt van zien underplayer is dan proberen dat deze aan slag geraakt "+String.valueOf(d));return;
											}
										}	
									}
								}   
							}
						}	
					}
				}
			}		
			cate++;if(cate==5){cate=1;}
		}
		
		//laagste in...	+enemycanfollowed + not underplayer + vijand meer dan 1 kaart			  
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				//controle isInGetcategory
				if(!isInGetcategory(cate,enemy[player][b])){continue;}
				//controle okFriendsVisibleCards
				if(!okFriendsVisibleCards(cate)){break;}
				//controle of je enkel al de allerhoogsten hebt
				if(hasOnlyAllTheHighestValues(cate)){break;}
				if(followed[player][enemy[player][b]][cate] && !isUnderPlayer(player,cate) && getNumberOfCards(enemy[player][b],cate)>1){
					if(isNextPlayer()==enemy[player][b] || allFriendsNotFollowed(cate)){
						if(getLowestValue(cate)!=0){
							toast("laagste in...+ enemyCanFollowed+ not underplayer+vijand meer dan 1 kaart "+String.valueOf(cate));return;
						}
					}else{
						int minValue=getLowestValue(cate)-1;
						int maxValue=getValueHighestValue(cate)-getValueLowestValue(cate)+1;
						if(getValueBetween(minValue,maxValue,cate,false,true)){
							toast("value tussen... + enemyCanFollowed+ not underplayer+vijand meer dan 1 kaart "+String.valueOf(cate));return;		
						}else{
							if(getLowestValue(cate)!=0){
								toast("laagste in...stap2 + enemyCanFollowed+ not underplayer+vijand meer dan 1 kaart "+String.valueOf(cate));return;
							}
						}
					}	
				}
			}	
			cate++;if(cate==5){cate=1;}
		}
		//laagste in...	+enemycanfollowed + not underplayer+allerhoogste?
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				//controle isInGetcategory
				if(!isInGetcategory(cate,enemy[player][b])){continue;}
				//controle okFriendsVisibleCards
				if(!okFriendsVisibleCards(cate)){break;}
				//controle of je enkel al de allerhoogsten hebt en checken underplayer
				if(!isUnderPlayer(player) && hasOnlyAllTheHighestValues(cate)){break;}
				if(hasOnlyAllTheHighestValues(cate)){
					boolean oke=false;
					for(int f=0;f<friend[player].length;f++){//indien vriend niet kan volgen dan ok
					 	if(!followed[player][friend[player][f]][cate]){oke=true;}
					}		
					if(!oke){break;}
				}
				if(followed[player][enemy[player][b]][cate]&& !isUnderPlayer(player,cate)){
					if(isNextPlayer()==enemy[player][b] || allFriendsNotFollowed(cate)){
						if(getLowestValue(cate)!=0){
							toast("laagste in...+ enemyCanFollowed + not underplayer+allerhoogste?"+String.valueOf(cate));return;
						}
					}else{
						int minValue=getLowestValue(cate)-1;
						int maxValue=getValueHighestValue(cate)-getValueLowestValue(cate)+1;
						if(getValueBetween(minValue,maxValue,cate,false,true)){
							toast("value tussen... + enemyCanFollowed + not underplayer+allerhoogste?"+String.valueOf(cate));return;		
						}else{
							if(getLowestValue(cate)!=0){
								toast("laagste in...stap2 + enemyCanFollowed + not underplayer+allerhoogste?"+String.valueOf(cate));return;
							}
						}
					}	
				}
			}	
			cate++;if(cate==5){cate=1;}
		}
		//laagste in...	+enemycanfollowed + not underplayer+ allerhoogste
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				//controle isInGetcategory
				//if(!isInGetcategory(cate,enemy[player][b])){continue;}  //11/08/2017
				//controle okFriendsVisibleCards
				if(!okFriendsVisibleCards(cate)){break;}
				//controle of je enkel al de allerhoogsten hebt en checken underplayer
				if(!isUnderPlayer(player) && hasOnlyAllTheHighestValues(cate)){break;}
				//if(hasOnlyAllTheHighestValues(cate) && random(2)==1){break;}//extra bijgezet + random
				if(followed[player][enemy[player][b]][cate]&& !isUnderPlayer(player,cate)){
					if(isNextPlayer()==enemy[player][b] || allFriendsNotFollowed(cate)){
						if(getLowestValue(cate)!=0){
							toast("laagste in...+ enemyCanFollowed + not underplayer+allerhoogste"+String.valueOf(cate));return;
						}
					}else{
						int minValue=getLowestValue(cate)-1;
						int maxValue=getValueHighestValue(cate)-getValueLowestValue(cate)+1;
						if(getValueBetween(minValue,maxValue,cate,false,true)){
							toast("value tussen... + enemyCanFollowed + not underplayer+allerhoogste"+String.valueOf(cate));return;		
						}else{
							if(getLowestValue(cate)!=0){
								toast("laagste in...stap2 + enemyCanFollowed + not underplayer+allerhoogste"+String.valueOf(cate));return;
							}
						}
					}	
				}
			}	
			cate++;if(cate==5){cate=1;}
		}
		//hoogste in...	+ underplayer + not enemycanfollowed=>bvb 1 onder de kaarten en de rest kan de vijand niet meer volgen
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				if(isUnderPlayer(player,cate) && getNumberOfCards(enemy[player][b],cate)>1){
					//nu zoeken naar kaarten waar de vijand geen meer van heeft
					for(int c=1;c<=4;c++){
						if(getNumberOfCards(enemy[player][b],c)==0 && getHighestValue(c)!=0){
							toast("hoogste in...+ underplayer + not enemycanfollowed "+String.valueOf(c));return;
						}
					}	
				}
			}	
			cate++;if(cate==5){cate=1;}
		}
		//laagste in...	+enemycanfollowed + vijand meer dan 1 kaart	  + allerhoogste
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				//controle isInGetcategory
				//if(!isInGetcategory(cate,enemy[player][b])){continue;}//11/08/2017
				//controle okFriendsVisibleCards
				if(!okFriendsVisibleCards(cate)){break;}
				//controle of je enkel al de allerhoogsten hebt en checken underplayer
				if(!isUnderPlayer(player) && hasOnlyAllTheHighestValues(cate)){break;}
				if(followed[player][enemy[player][b]][cate] && getNumberOfCards(enemy[player][b],cate)>1){
					if(isNextPlayer()==enemy[player][b] || allFriendsNotFollowed(cate)){
						if(getLowestValue(cate)!=0){
							toast("laagste in...+ enemyCanFollowed+vijand meer dan 1 kaart + allerhoogste "+String.valueOf(cate));return;
						}
					}else{
						int minValue=getLowestValue(cate)-1;
						int maxValue=getValueHighestValue(cate)-getValueLowestValue(cate)+1;
						if(getValueBetween(minValue,maxValue,cate,false,true)){
							toast("value tussen... + enemyCanFollowed+vijand meer dan 1 kaart + allerhoogste "+String.valueOf(cate));return;		
						}else{
							if(getLowestValue(cate)!=0){
								toast("laagste in...stap2 + enemyCanFollowed+vijand meer dan 1 kaart + allerhoogste "+String.valueOf(cate));return;
							}
						}
					}	
				}
			}	
			cate++;if(cate==5){cate=1;}
		}
		//laagste in...	+enemycanfollowed
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				//controle okFriendsVisibleCards
				if(!okFriendsVisibleCards(cate)){break;}
				//controle of je enkel al de allerhoogsten hebt en checken underplayer
				if(!isUnderPlayer(player) && hasOnlyAllTheHighestValues(cate)){break;}
				if(followed[player][enemy[player][b]][cate]){
					if(isNextPlayer()==enemy[player][b] || allFriendsNotFollowed(cate)){
						if(getLowestValue(cate)!=0){
							toast("laagste in...+ enemyCanFollowed "+String.valueOf(cate));return;
						}
					}else{
						int minValue=getLowestValue(cate)-1;
						int maxValue=getValueHighestValue(cate)-getValueLowestValue(cate)+1;
						if(getValueBetween(minValue,maxValue,cate,false,true)){
							//eerst controleren of er minstens 1 vijand is met minimum 2 kaarten en checken underplayer
							for(int c=0;c<enemy[player].length;c++){
								if(getNumberOfCards(enemy[player][c],cate)>1 || !isUnderPlayer(player,cate)){
									toast("value tussen... + enemyCanFollowed"+String.valueOf(cate));return;
								}
							}	
						}//toch maar laagste kaart	
						if(getLowestValue(cate)!=0){
							toast("laagste in...stap2 + enemyCanFollowed"+String.valueOf(cate));return;
						}
					}	
				}
			}	
			cate++;if(cate==5){cate=1;}
		}
		//laagste in...				  
		for(int a=1;a<=4;a++){
			for(int b=0;b<enemy[player].length;b++){
				//controle okFriendsVisibleCards
				if(!okFriendsVisibleCards(cate)){break;}
				//controle of je enkel al de allerhoogsten hebt
				if(hasOnlyAllTheHighestValues(cate)){break;}
				//controle of je enkel deze kleur hebt
				if(everyoneNotFollowed(cate)){break;}
				if(getLowestValue(cate)!=0){toast("laagste in... "+String.valueOf(cate));return;}
			}	
			cate++;if(cate==5){cate=1;}
		}
		if(getLowestValue()!=0){toast("laagste in...");return;}
	}
	
	private void pasFollowMisere(){ 
		if(willNotCome()){
			if(getValueLowerThan(getWinnerValue(),cat, false)){toast("lager dan...+ wil niet uitkomen");return;}
			if(turn==4 && getHighestValue(cat)!=0){toast("hoogste dan maar + wil niet uitkomen");return;}
			if(getLowestValue(cat)!=0){toast("laagste dat ik kan volgen + wil niet uitkomen");return;}
		}
		if(turn==2 || turn==3){
			if(enemyHighestValue() && !stillPlayEnemies()){
				if(getValueLowerThan(getWinnerValue(),cat, false)){toast("lager dan...");return;}
			}else{
				if(stillPlayEnemies()){
					//zien of er minstens 1 miserespeler achter je kan volgen
					int z=player;
					z++;if(z==5){z=1;}
					boolean ok=false;
					while(z!=startPlayer){
						if(playerChoice[z]==MISERE && followed[player][z][cat]){
							ok=true;
						}
						z++;if(z==5){z=1;}
					}	
					if(ok){
						if(getWinnerValue()>6 && getValueLowerThan(getWinnerValue()+3,cat, false)){//bvb vrouw en dan aas
							setCardOneValue(true);toast("lager dan...+3,getwinnervalue>6");return;
						}
						if(getLowestValue(cat)!=0){toast("laagste dat ik kan volgen");return;}
					}else{
						if(enemyHighestValue()){
							if(getValueLowerThan(getWinnerValue(),cat, false)){toast("lager dan...,stap2");return;}
						}
					}	
				}
			}
			if(getHighestValue(cat)!=0){toast("hoogste dat ik kan volgen");return;}
		}	
		if(turn==4){
			if(enemyHighestValue()){
				if(getValueLowerThan(getWinnerValue(),cat, false)){toast("lager dan...");return;}
			}
			if(getHighestValue(cat)!=0){toast("hoogste dat ik kan volgen");return;}
			
		}
		//vanaf hier de kaarten die je weggooit
		setFollow(0,player,cat);
		
		//leg de hoogste category uit die de vijanden hebben afgelegd bij het niet-volgen
		for(int b=0;b<enemy[player].length;b++){
			for(int c=0;c<playCategory[enemy[player][b]].size();c++){
				if(getHighestValue(playCategory[enemy[player][b]].get(c))!=0 && playerDeck[player].get(card).value>8){
					toast("hoogste in playCategory en >8 "+String.valueOf(playCategory[enemy[player][b]].get(c)));return;
				}
			}	
		}
		//indien vriend al een kaart heeft afgelegd en ook niet gevolgd heeft dan hoogste kaart van deze category en >8 en 
		//vijand gevolgd en minstens 1 vriend
		int categoryStart=gameDeck.get(0).category;
		int b=startPlayer;b--;if(b==0){b=4;}
		for(int a=0;a<gameDeck.size();a++){
			b++;if(b==5){b=1;}
			boolean ok=false;         //zien naar alle kaarten die niet gevolgd hebben en passer zijn
			if(gameDeck.get(a).category!=categoryStart && playerChoice[b]==PAS){ok=true;}
			if(ok){
				int cate=gameDeck.get(a).category;
				if(getHighestValue(cate)!=0 && playerDeck[player].get(card).value>8){//9 niet
					for(int c=0;c<enemy[player].length;c++){
						if(followed[player][enemy[player][c]][cate]){
							for(int f=0;f<friend[player].length;f++){
								if(followed[player][friend[player][f]][cate]){
									toast("meevolgen in wat de vrienden afleggen en >8 en vijand nog gevolgd+minstens 1 vriend gevolgd"+String.valueOf(cate));return;
								}	
							}		
						}	
					}				
				}
			}
		}	
		ArrayList<ObjPlayer> arList = getCategory(player);
		int cate;
		for(int d=0;d<arList.size();d++){	
			cate=arList.get(d).category;
			if(getHighestValue(cate)!=0 && playerDeck[player].get(card).value>8){//9 niet
				for(int c=0;c<enemy[player].length;c++){
					if(followed[player][enemy[player][c]][cate]){
						for(int f=0;f<friend[player].length;f++){
							if(followed[player][friend[player][f]][cate]){
								toast("hoogste kaart in getCategory en >8 en vijand nog gevolgd+minstens 1 vriend gevolgd"+String.valueOf(cate));return;
							}	
						}		
					}	
				}				
			}
		}	
		if(getHighestValue()!=0){toast("hoogste in...");return;}
	}
	
	private void pasFollowOpenMisere(){
		if(willNotCome()){
			if(getValueLowerThan(getWinnerValue(),cat, false)){toast("lager dan...+ wil niet uitkomen");return;}
			if(turn==4 && getHighestValue(cat)!=0){toast("hoogste dan maar + wil niet uitkomen");return;}
			if(getLowestValue(cat)!=0){toast("laagste dat ik kan volgen + wil niet uitkomen");return;}
		}
		if(turn==2 || turn==3){
			if(enemyHighestValue() && !stillPlayEnemies()){
				//remainingcards of checkedcards kunnen we niet gebruiken okkkkkkkk
				if(getValueLowerThan(getWinnerValue(),cat, false)){toast("lager dan...");return;}
			}else{
				if(stillPlayEnemies()){
					boolean ok;
					if(enemyHighestValue()){ok=true;}else{ok=false;}
					int winnerValue=getWinnerValue();
					boolean check=false;
					int lowestValuePas=0;
					int z=player;
					z++;if(z==5){z=1;}
					while(z!=startPlayer){
						int lowestV=getLowestValue(z,cat);
						if(oldPlayerChoice[z]==OPENMISERE){
							if(lowestV>winnerValue){winnerValue=lowestV;ok=false;}
						}
						if(playerChoice[z]==OPENMISERE){
							if(lowestV>winnerValue){winnerValue=lowestV;ok=true;}
						}
						//indien 2 openmiserespelers dan weet je wat je vriend heeft
						if(playerChoice[z]==PAS && oldPlayerChoice[z]!=OPENMISERE && getNumberOfPlayersMisereOrOpenmisere()==2){
							check=true;lowestValuePas=lowestV;
						}
						z++;if(z==5){z=1;}
					}	
					//controle check
					if(check && lowestValuePas>winnerValue){
						if(getHighestValue(cat)!=0){toast("hoogste dat ik kan volgen want vriend is ook te hoog");return;}
					}
					//zien wanneer we de lagere kaart moeten spelen
					if(ok && getValueLowerThan(winnerValue,cat, false)){toast("lager dan...,rekeninghoudend met de kaarten dat je kan zien");return;}
					//zien of we een lagere of hogere kaart moeten spelen
					//zoek de hoogste kaart en hoger als winnervalue bij de openmiserespeler(s)(max.2)
					int valueEnemy1=0;
					int valueEnemy2=0;
					int valueEnemy=0;
					z=player;
					z++;if(z==5){z=1;}
					while(z!=startPlayer){
						if(playerChoice[z]==OPENMISERE && getHighestValue(z,cat)>winnerValue){
							valueEnemy=playerDeck[z].get(card).value;
							if(valueEnemy1!=0){valueEnemy2=valueEnemy;}
							if(valueEnemy1==0){valueEnemy1=valueEnemy;}	
						}
						z++;if(z==5){z=1;}
					}	
					if(valueEnemy1!=0 && valueEnemy2!=0){
						if(valueEnemy1<valueEnemy2){
							valueEnemy=valueEnemy1;
						}else{
							valueEnemy=valueEnemy2;
						}
					}else{
						if(valueEnemy1!=0){valueEnemy=valueEnemy1;}
					}
					if(getValueLowerThan(valueEnemy,cat, false) && !isUnderPlayer(player,cat)){toast("tussenin...st1,rekeninghoudend met de kaarten dat je kan zien");return;}
					//indien geen resultaat,dan zien of 1 van de 2 enemies toch hoger zit in waarde
					if(valueEnemy1!=0 && valueEnemy2!=0){
						if(valueEnemy1>valueEnemy2){
							valueEnemy=valueEnemy1;
						}else{
							valueEnemy=valueEnemy2;
						}
						if(getValueLowerThan(valueEnemy,cat, false) && !isUnderPlayer(player,cat)){toast("tussenin...st2,rekeninghoudend met de kaarten dat je kan zien");return;}
					}
				}
			}
			if(getHighestValue(cat)!=0){toast("hoogste dat ik kan volgen");return;}
		}	
		if(turn==4){
			if(enemyHighestValue()){
				if(getValueLowerThan(getWinnerValue(),cat, false)){toast("lager dan...");return;}
			}
			if(getHighestValue(cat)!=0){toast("hoogste dat ik kan volgen");return;}

		}
		//vanaf hier de kaarten die je weggooit
		setFollow(0,player,cat);
		toast("playerDead=" + String.valueOf(getPlayerWhoDies()));//test
		//hoogste kaart volgens getCategory en checken playerwhodies
		int playerWhoDies=getPlayerWhoDies();
		for(int c=0;c<getCategory.size();c++){
			if(playerChoice[getCategory.get(c).player]==OPENMISERE && getCategory.get(c).player!=playerWhoDies){
				int category=getCategory.get(c).category;
				if(getHighestValue(category)!=0){
					//checken underplayer, zien dat 1 van de vrienden de laagste kaart heeft en dat je zelf
					//niet de laagste kaart hebt, bij openmiserespelers is underplayer==0
					for(int a=1;a<=4;a++){
						if(a!=player && isUnderPlayer(a,category) && !isUnderPlayer(player,category)){
							toast("hoogste volgens getCategory en checken underplayer(bij jezelf en de vrienden)"+String.valueOf(category));return;
						}	
					}
				}
			}	
		}
		//hoogste kaart volgens getCategory en checken playerwhodies
		for(int c=0;c<getCategory.size();c++){
			if(playerChoice[getCategory.get(c).player]==OPENMISERE && getCategory.get(c).player!=playerWhoDies){
				int category=getCategory.get(c).category;
				//checken underplayer, zien dat je zelf niet de laagste kaart hebt
				if(getHighestValue(category)!=0 && !isUnderPlayer(player,category)){
					//nu zien dat de vrienden niet underplayer zijn
					boolean ok=true;
					for(int a=1;a<=4;a++){if(a!=player && isUnderPlayer(a,category)){ok=false;}}
					//nu zien dat deze kaart boven de 2de hoogste kaart zit
					if(ok){
						int valueEnemy=0;
						for(int a=1;a<=4;a++){//speler
							if(playerChoice[a]==OPENMISERE && getNumberOfCards(a,category)>1){
								getLowestValue(a,category);
								if(valueEnemy<playerDeck[a].get(card+1).value){valueEnemy=playerDeck[a].get(card+1).value;}	
							}	
						}	
						if(getHighestValue(category)<valueEnemy){ok=false;}
					}	
					if(ok){toast("hoogste volgens getCategory en checken underplayer"+String.valueOf(category));return;}
				}
			}	
		}
		//indien vriend al een kaart heeft afgelegd en ook niet gevolgd heeft dan hoogste kaart van deze category en >8 en 
		//vijand gevolgd en minstens 1 vriend
		int categoryStart=gameDeck.get(0).category;
		int b=startPlayer;b--;if(b==0){b=4;}
		for(int a=0;a<gameDeck.size();a++){
			b++;if(b==5){b=1;}
			boolean ok=false;         //zien naar alle kaarten die niet gevolgd hebben en passer zijn
			if(gameDeck.get(a).category!=categoryStart && playerChoice[b]==PAS){ok=true;}
			if(ok){
				int cate=gameDeck.get(a).category;
				if(getHighestValue(cate)!=0 && playerDeck[player].get(card).value>8 && !isUnderPlayer(player,cate)){
					for(int c=0;c<enemy[player].length;c++){
						if(followed[player][enemy[player][c]][cate]){
							for(int f=0;f<friend[player].length;f++){
								if(followed[player][friend[player][f]][cate]){
									toast("meevolgen in wat de vrienden afleggen en >8 en vijand nog gevolgd+minstens 1 vriend gevolgd + en niet underplayer"+String.valueOf(cate));return;
								}	
							}		
						}	
					}				
				}
			}
		}	
		//hoogste in en niet underplayer
		int value=15;       
		while(value>2){
			value--;
			for(card=playerDeck[player].size()-1;card>=0;card--){
				if(playerDeck[player].get(card).value==value && !isUnderPlayer(player,playerDeck[player].get(card).category)){
					   toast("hoogste in...en niet underplayer");return;
				}	
			}
		}
		if(getHighestValue()!=0){toast("hoogste in...");return;}
	}
	
	private boolean isUnderPlayer(int player,int category){
		//checken underplayer, zien of dat je de laagste kaart hebt
		for(int d=0;d<getCategory.size();d++){
				if(getCategory.get(d).underPlayer!=0 && getCategory.get(d).player==player &&
				   getCategory.get(d).category==category){
					return true;
				}	
		}
		return false;
	}
	
	private boolean isUnderPlayer(int player){
		//checken underplayer, zien of dat je de laagste kaart hebt
		for(int a=1;a<=4;a++){
			for(int d=0;d<getCategory.size();d++){
				if(getCategory.get(d).underPlayer!=0 && getCategory.get(d).player==player &&
				   getCategory.get(d).category==a){
					return true;
				}	
			}
		}
		return false;
	}
	
	            //(is verloren)
	private boolean isLost(){//enkel gebruikt bij misere en openmisere (en volgen)
		int categoryStart=gameDeck.get(0).category;
		//zien of de spelers achter je niet kunnen volgen
		int z=player;
		z++;if(z==5){z=1;}
		while(z!=startPlayer){
			if(followed[player][z][categoryStart]){return false;}
			z++;if(z==5){z=1;}
		}
		return true;
	}
	
	private boolean willNotCome(){//wil niet uitkomen, enkel bij het volgen
		//indien speler kan volgen
		//indien de miserespelers die gespeeld hebben en niet kunnen volgen
		//indien de miserespelers die nog moeten spelen en nietgevolgd hebben
		//al de kleuren van de speler(behalve de uitgekomen kleur) nagaan of al de vijanden 
		//niet kunnen volgen en !hasOnlyAllTheHighestValues en !everyoneNotFollowed
		//dit nu ook toegepast bij openmisere
		int categoryStart=gameDeck.get(0).category;
		if(getNumberOfCards(categoryStart)<2){return false;}//als je maar 1 kaart nog hebt dan kan je enkel deze nog afleggen
		int b=startPlayer;b--;if(b==0){b=4;}
		for(int a=0;a<gameDeck.size();a++){
			b++;if(b==5){b=1;}
			if(playerChoice[b]==MISERE && gameDeck.get(a).category==categoryStart){return false;}
			if(playerChoice[b]==OPENMISERE && followed[player][b][categoryStart]){return false;}
		}	
		//zien of de (open)miserespelers achter je niet kunnen volgen
		int z=b;
		z++;if(z==5){z=1;}
		while(z!=startPlayer){
			if(playerChoice[z]==MISERE && followed[b][z][categoryStart]){return false;}
			if(playerChoice[z]==OPENMISERE && getNumberOfCards(z,categoryStart)>1){return false;}
			z++;if(z==5){z=1;}
		}		
		//al de kleuren van de speler....
		for(int a=1;a<=4;a++){
			if(a!=categoryStart && getNumberOfCards(a)!=0){
				for(int c=0;c<enemy[player].length;c++){
					if(followed[player][enemy[player][c]][a] && 
					   !hasOnlyAllTheHighestValues(a) &&
					   !everyoneNotFollowed(a)){return false;}
				}	
			}
		}	
		return true;
	}
	
	private boolean allFriendsNotFollowed(int category){
		for(int b=0;b<friend[player].length;b++){
			if(followed[player][friend[player][b]][category]){
				return false;
			}
		}	
		return true;
	}
	
	private void checkCardPlayed(){//dit is enkel als men gekocht heeft
		//als men gekocht heeft met de allerhoogste kaart en er nog niet gekocht is door de andere spelers dan setfollow
		if(getWinnerValue()<20){
			if(hasHighestValue(playerDeck[player].get(card).value, playerDeck[player].get(card).category)){
				setFollow(0,player,trumpCategory);//indien 11 12 13 dan pech...
			}//indien 2de hoogste dan 50%
			if(hasHighestValue(playerDeck[player].get(card).value+1, playerDeck[player].get(card).category) &&
			  		 random(2)==1){
				setFollow(0,player,trumpCategory);//indien 11 12 13 dan pech...
			}
		}
	}
	
	public boolean enemyCanBuy(int category){
		for(int b=0;b<enemy[player].length;b++){
			if(stillPlay(enemy[player][b]) &&
			   !followed[player][enemy[player][b]][category] &&
			   followed[player][enemy[player][b]][trumpCategory]){return true;}
		}	
		return false;
	}
	
	public boolean friendCanBuy(int category){
		for(int b=0;b<friend[player].length;b++){
			if(stillPlay(friend[player][b]) &&
			   !followed[player][friend[player][b]][category] &&
			   followed[player][friend[player][b]][trumpCategory]){return true;}
		}	
		return false;
	}
	
	private boolean stillPlay(int player){
		int a=this.player;
		a++;if(a==5)a=1;
		while(a!=startPlayer){
			if(a==player){return true;}
			a++;if(a==5)a=1;
		}
		return false;	
	}
	
	private boolean stillPlayEnemies(){
		for(int b=0;b<enemy[player].length;b++){
			if(stillPlay(enemy[player][b])){return true;}
		}	
		return false;
	}
	
	private boolean stillPlayFriends(){
		for(int b=0;b<friend[player].length;b++){
			if(stillPlay(friend[player][b])){return true;}
		}	
		return false;
	}
	
	private boolean hasAllTheHigherValues(){
		int value=getHighestValue(cat);
		for(int a=card;a>=0;a--){
			if(playerDeck[player].get(a).category==cat){
				if(playerDeck[player].get(a).value!=value){break;}
				value--;
			}	
		}	
		if(value==getWinnerValue()){return true;}
		return false;
	}
	
	private boolean hasOnlyAllTheHighestValues(int category){//enkel gebruikt bij misere en openmisere
		//controle of je enkel al de allerhoogsten hebt,bvb vka, maar ook a en tbv enz...
		if(getHighestValue(category)!=0 && hasHighestValue()){
			setCardOneValue(false);
			int value=playerDeck[player].get(card).value;
			if(getLowestValue(category)!=0 && playerDeck[player].get(card).value==value){
				return true;
			}
		}	
		return false;
	}
	
	private boolean hasOnlyAllTheLowestValues(int category){//enkel gebruikt bij misere
		//controle of je enkel al de allerlaagsten hebt,bvb 234, maar ook 6 en 567 enz...
		if(getLowestValue(category)!=0 && hasLowestValue()){
			setCardOneValue(true);
			int value=playerDeck[player].get(card).value;
			if(getHighestValue(category)!=0 && playerDeck[player].get(card).value==value){
				return true;
			}
		}	
		return false;
	}
	
	public int getHighestValueInAscendingOrder(int category){
		//ascending order=oplopende volgorde, descending=aflopende
		//opgepast dit is enkel voor spelers die volgen of gekocht hebben
		int value=1;
		if(category==trumpCategory){value+=20;}
		boolean ok=true;
		while(ok){
			ok=false;
			value++;
			for(int a=0;a<gameDeck.size();a++){
				if(gameDeck.get(a).category==category && gameDeck.get(a).value==value){
					ok=true;break;
				}
			}
			if(!ok){
				for(int a=0;a<last4CardDeck.size();a++){
					if(last4CardDeck.get(a).category==category && last4CardDeck.get(a).value==value){
						ok=true;break;
					}
				}
			}
		}
		value--;
		return value; //return value-- werkt niet
	}

	private int getStartValueInDescendingOrder(int deck, int cat){
		if(gameDeck.size()!=4){return 0;}
		int value=0;
		switch(deck){
			case 0:value=getValueHighestValue(gameDeck,cat);break;
			case 1:value=getValueHighestValue(playerDeck[1],cat);break;
			case 2:value=getValueHighestValue(playerDeck[2],cat);break;
			case 3:value=getValueHighestValue(playerDeck[3],cat);break;
			case 4:value=getValueHighestValue(playerDeck[4],cat);break;
		}
		if(value==0){return 0;}
		boolean ok=false;
		if (hasHighestValue(value, cat)){ok=true;}
		if(!ok){return 0;}
		return value;
	}	
	
	private int getStartValueInAscendingOrder(int deck, int cat){
		if(gameDeck.size()!=4){return 15;}
		int value=15;
		switch(deck){
			case 0:value=getValueLowestValue(gameDeck,cat);break;
			case 1:value=getValueLowestValue(playerDeck[1],cat);break;
			case 2:value=getValueLowestValue(playerDeck[2],cat);break;
			case 3:value=getValueLowestValue(playerDeck[3],cat);break;
			case 4:value=getValueLowestValue(playerDeck[4],cat);break;
		}
		if(value==15){return 15;}
		boolean ok=false;
		if (hasLowestValue(value, cat)){
			ok=true;
		}
		if(!ok){return 15;}
		return value;
	}	
	
	public int getLowestValueInDescendingOrder(int deck, int player, int cat){
		//sortingDeck(checkDeck,false);
		checkDeck.sortReversed();
		//ascending order=oplopende volgorde, descending=aflopende
		int value2;
		int value=value2=getStartValueInDescendingOrder(deck,cat);
		if(value==0 || gameDeck.size()!=4){return 0;}
		for(int a=0;a<checkDeck.size();a++){
			if(checkDeck.get(a).category==cat && (checkDeck.get(a).player==0 ||
			   checkDeck.get(a).player==player)){
				if(checkDeck.get(a).value==value){value--;}
			}	
		}	
		value++;
		if(value==value2+1){value=0;}
		return value;	
	}
	
	public int getMissingCardsInDescendingOrder(int deck, int player, Card card){
		//sortingDeck(checkDeck,false);
		checkDeck.sortReversed();
		int value=getStartValueInDescendingOrder(deck, card.category);
		if(value==0 || gameDeck.size()!=4){return -1;}
		int missingCards=0;
		if(value<card.value){toast("minValue te hoog, FOUT");return -1;}//extra
		value++;
		while(value!=card.value){
			value--;
			boolean found=false;
			for(int a=0;a<checkDeck.size();a++){
				if(checkDeck.get(a).category==card.category && (checkDeck.get(a).player==0 ||
				   checkDeck.get(a).player==player)){
					if(checkDeck.get(a).value==value){found=true;break;}
				}	
			}
			if(!found){
				missingCards++;
				if(value==card.value){toast("FOUTE KAART");return -1;}//extra
			}
		}
		return missingCards;	
	}
	
	public int getMissingCardsInAscendingOrder(int deck, int player, Card card){
		//sortingDeck(checkDeck,false);
		checkDeck.sortReversed();
		int value=getStartValueInAscendingOrder(deck, card.category);
		if(value==15 || gameDeck.size()!=4){return -1;}
		int missingCards=0;
		if(value>card.value){toast("minValue te laag, FOUT");return -1;}//extra
		value--;
		while(value!=card.value){
			value++;
			boolean found=false;
			for(int a=0;a<checkDeck.size();a++){
				if(checkDeck.get(a).category==card.category && (checkDeck.get(a).player==0 ||
				   checkDeck.get(a).player==player)){
					if(checkDeck.get(a).value==value){found=true;break;}
				}	
			}
			if(!found){
				missingCards++;
				if(value==card.value){toast("FOUTE KAART");return -1;}//extra
			}
		}
		return missingCards;	
	}
	
	private boolean enemyHighestValue(){
		for(int c=0;c<enemy[player].length;c++){
			if(enemy[player][c]==getWinner()){return true;}
		}
		return false;
	}
	
	private int getHighestValue(){
		int value=35;//troef is er ook bij
		while(value>2){
			value--;
			for(card=playerDeck[player].size()-1;card>=0;card--){
				if(playerDeck[player].get(card).value==value){
					return playerDeck[player].get(card).value;
				}	
			}
		}
		return 0;	
	}
	
	public int getHighestValue(int category){
		for(card=playerDeck[player].size()-1;card>=0;card--){
			if(playerDeck[player].get(card).category==category){
				return playerDeck[player].get(card).value;
			}	
		}
		return 0;	
	}
	
	private int getHighestValue(int player,int category){
		for(card=playerDeck[player].size()-1;card>=0;card--){
			if(playerDeck[player].get(card).category==category){
				return playerDeck[player].get(card).value;
			}	
		}
		return 0;
	}
	
	private int getHighestValueToPlay(int category){//starten of volgen als men bvb een 2de hoogste kaart heeft...
		int startValue=getValueHighestValue(category);
		int numberOfCards=getNumberOfCards(category);
		if (startValue==0){return 0;}
		for(card=playerDeck[player].size()-1;card>=0;card--){
			if(playerDeck[player].get(card).category==category){
				if(playerDeck[player].get(card).value < startValue-(numberOfCards-1)){
					return playerDeck[player].get(card).value;
				}
				numberOfCards--;
			}	
		}
		return 0;
	}
	
	private boolean hasHighestValue(int category){//hier wordt ook rekening gehouden met troef in gamedeck
		int value=0;
		for(card=playerDeck[player].size()-1;card>=0;card--){
			if(playerDeck[player].get(card).category==category){
				value=playerDeck[player].get(card).value;break;
			}	
		}
		if(value==0){return false;}
		for(int a=0;a<gameDeck.size();a++){
			if(gameDeck.get(a).category==category || gameDeck.get(a).category==trumpCategory){
				if(gameDeck.get(a).value>value){
					return false;
				}
			}
		}
		for(int a=1;a<=4;a++){
			if(a!=player){
				for(int b=playerDeck[a].size()-1;b>=0;b--){
					if(playerDeck[a].get(b).category==category){
						if(playerDeck[a].get(b).value>value){
							return false;
						}
					}	
				}
			}
		}
		return true;
	}
	
	private boolean hasHighestValue(){//hier moet je altijd een kaart gekozen hebben
		if(playerDeck[player].get(card).value==0){return false;}
		for(int a=0;a<gameDeck.size();a++){
			if(gameDeck.get(a).category==playerDeck[player].get(card).category){
				if(gameDeck.get(a).value>playerDeck[player].get(card).value){
					return false;
				}
			}
		}
		for(int a=1;a<=4;a++){
			if(a!=player){
				for(int b=playerDeck[a].size()-1;b>=0;b--){
					if(playerDeck[a].get(b).category==playerDeck[player].get(card).category){
						if(playerDeck[a].get(b).value>playerDeck[player].get(card).value){
							return false;
						}
					}	
				}
			}
		}
		return true;
	}
	
	public boolean hasHighestValue(int value, int cat){
		for(int a=1;a<=4;a++){
			for(int b=playerDeck[a].size()-1;b>=0;b--){
				if(playerDeck[a].get(b).category==cat){
					if(playerDeck[a].get(b).value>value){
						return false;
					}
				}	
			}	
		}
		for(int a=0;a<gameDeck.size();a++){//dit is erbij gezet
			if(gameDeck.get(a).category==cat){
				if(gameDeck.get(a).value>value){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean hasLowestValue(int category){
		int value=0;
		for(card=playerDeck[player].size()-1;card>=0;card--){
			if(playerDeck[player].get(card).category==category){
				value=playerDeck[player].get(card).value;break;
			}	
		}
		if(value==0){return false;}
		for(int a=0;a<gameDeck.size();a++){
			if(gameDeck.get(a).category==category){
				if(gameDeck.get(a).value<value){
					return false;
				}
			}
		}
		for(int a=1;a<=4;a++){
			if(a!=player){
				for(int b=playerDeck[a].size()-1;b>=0;b--){
					if(playerDeck[a].get(b).category==category){
						if(playerDeck[a].get(b).value<value){
							return false;
						}
					}	
				}
			}
		}
		return true;
	}
	
	private boolean hasLowestValue(){//hier moet je altijd een kaart gekozen hebben
		if(playerDeck[player].get(card).value==0){return false;}
		for(int a=0;a<gameDeck.size();a++){
			if(gameDeck.get(a).category==playerDeck[player].get(card).category){
				if(gameDeck.get(a).value<playerDeck[player].get(card).value){
					return false;
				}
			}
		}
		for(int a=1;a<=4;a++){
			if(a!=player){
				for(int b=playerDeck[a].size()-1;b>=0;b--){
					if(playerDeck[a].get(b).category==playerDeck[player].get(card).category){
						if(playerDeck[a].get(b).value<playerDeck[player].get(card).value){
							return false;
						}
					}	
				}
			}
		}
		return true;
	}
	
	public boolean hasLowestValue(int value, int cat){
		for(int a=1;a<=4;a++){
			for(int b=playerDeck[a].size()-1;b>=0;b--){
				if(playerDeck[a].get(b).category==cat){
					if(playerDeck[a].get(b).value<value){
						return false;
					}
				}	
			}	
		}
		for(int a=0;a<gameDeck.size();a++){//dit is erbij gezet
			if(gameDeck.get(a).category==cat){
				if(gameDeck.get(a).value<value){
					return false;
				}
			}
		}
		return true;
	}
	
	public int getValueHighestValue(int category){//hier wordt geen rekening gehouden met troef in gamedeck
		int value=getValueHighestValue(gameDeck, category);
		for(int a=1;a<=4;a++){
			int value2=getValueHighestValue(playerDeck[a], category);
			if(value2>value){value=value2;}
		}	
		return value;
	}
	
	public int getValueHighestValue(ArrayList<Card> deck, int category){//hier wordt geen rekening gehouden met troef in deck
		int value=0;
		for(int a=deck.size()-1;a>=0;a--){
			if(deck.get(a).category==category && deck.get(a).value>value){
				value=deck.get(a).value;
			}
		}
		return value;
	}
	
	//dit is de allerlaagste kaart
	public int getValueLowestValue(int category){//hier wordt geen rekening gehouden met troef in gamedeck
		int value=getValueLowestValue(gameDeck, category);
		for(int a=1;a<=4;a++){
			int value2=getValueLowestValue(playerDeck[a], category);
			if(value2<value){value=value2;}
		}	
		return value;
	}
	
	public int getValueLowestValue(ArrayList<Card> deck, int category){//hier wordt geen rekening gehouden met troef in deck
		int value=15;
		for(int a=deck.size()-1;a>=0;a--){
			if(deck.get(a).category==category && deck.get(a).value<value){
				value=deck.get(a).value;
			}
		}
		return value;
	}
	
	private int getLowestValue(){
		int value=1;
		while(value<14){//
			value++;
			for(card=0;card<playerDeck[player].size();card++){
				if(playerDeck[player].get(card).value==value){
					return playerDeck[player].get(card).value;
				}	
			}
		}
		return 0;	
	}
	
	private int getLowestValue(boolean setOneValueHigher){
		int value=1;
		while(value<14){//
			value++;
			for(card=0;card<playerDeck[player].size();card++){
				if(playerDeck[player].get(card).value==value){
					//indien volgende kaart 1 waarde hoger is enz...
					if(setOneValueHigher){setCardOneValue(true);}
					return playerDeck[player].get(card).value;
				}	
			}
		}
		return 0;	
	}
	
	private int getLowestValue(int category){
		for(card=0;card<playerDeck[player].size();card++){
			if(playerDeck[player].get(card).category==category){
				return playerDeck[player].get(card).value;
			}	
		}
		return 0;
	}
	
	private int getLowestValue(int category, boolean setOneValueHigher){
		for(card=0;card<playerDeck[player].size();card++){
			if(playerDeck[player].get(card).category==category){
				//indien volgende kaart 1 waarde hoger is enz...
				if(setOneValueHigher){setCardOneValue(true);}
				return playerDeck[player].get(card).value;
			}	
		}
		return 0;
	}
	
	private int getLowestValue(int player, int category){
		for(card=0;card<playerDeck[player].size();card++){
			if(playerDeck[player].get(card).category==category){
				return playerDeck[player].get(card).value;
			}	
		}
		return 0;	
	}
	
	public int getNumberOfHatchedTrump(){// aantal uitgekomen troef
		int number=0;
		for(int b=0;b<=1;b++){
			for(int a=0;a<waitingDeck[b].size();a+=4){
				if(waitingDeck[b].get(a).category==trumpCategory){
					number++;
				}
			}
		}	
		return number;
	}	
	
	public int getNumberOfPlayedCards(int category){//dit is enkel in waitingdeck
		int number=0;
		for(int b=0;b<=1;b++){
			for(int a=0;a<waitingDeck[b].size();a++){
				if(waitingDeck[b].get(a).category==category){
					number++;
				}
			}
		}
		return number;
	}
	
	public int getNumberOfPlayedCardsGameDeck(int category){//dit is enkel in gamedeck
		int number=0;
		for(int a=0;a<gameDeck.size();a++){
			if(gameDeck.get(a).category==category){
				number++;
			}
		}
		return number;
	}
	
	public int getNumberOfCards(int category){
		return getPlayerDeckCategory(player,category).size();
	}
	
	public int getNumberOfCards(int player, int category){
		return getPlayerDeckCategory(player,category).size();
	}
	
	private int getNumberOfCardsHigherThan(int value, int category){
		int number=0;
		for(int a=0;a<playerDeck[player].size();a++){
			if(playerDeck[player].get(a).category==category && playerDeck[player].get(a).value>value) {
				number++;
			}
		}	
		return number;
	}
	
	private int getNumberOfCardsBetween(int minValue, int maxValue, int category){
		int number=0;
		for(int a=0;a<playerDeck[player].size();a++){
			if(playerDeck[player].get(a).category==category && playerDeck[player].get(a).value>minValue
			   && playerDeck[player].get(a).value<maxValue) {
				number++;
			}
		}	
		return number;
	}
	
	private int getNumberOfCardsBetween(int minValue, int maxValue, int player, int category){//gebruikt bij openmisere
		int number=0;
		for(int a=0;a<playerDeck[player].size();a++){
			if(playerDeck[player].get(a).category==category && playerDeck[player].get(a).value>minValue
			   && playerDeck[player].get(a).value<maxValue) {
				number++;
			}
		}	
		return number;
	}
	
						//het aantal laagste kaarten, rekening houdend met hogere kaarten
	private int getNumberOfCardsToPlay(int cat){
		if(getNumberOfCards(cat)==0){return 0;}
		if(getNumberOfCards(cat)==1){return 1;}
		return getNumberOfCards(cat)-(getNumberOfCardsHigherThan(getHighestValueToPlay(cat),cat)*2);
	}
	
	private boolean getValueHigherThan(int value, int category, boolean setOneValueHigher, boolean oneValueHigherThanValue){
		//if(value<20 && category==trumpCategory){value=20;}//extra
		for(card=0;card<playerDeck[player].size();card++){
			if(playerDeck[player].get(card).category==category && playerDeck[player].get(card).value>value){
				if(!oneValueHigherThanValue){
					if(playerDeck[player].get(card).value==value+1){
						//zoeken achter de kaart die meer dan 1 hoger is
						setCardOneValue(true);
						value=playerDeck[player].get(card).value;
						boolean found=false;
						if(card<playerDeck[player].size()-1){
							card++;value++;
							if(playerDeck[player].get(card).category==category && playerDeck[player].get(card).value!=value){
								found=true;
							} 
							if(playerDeck[player].get(card).category!=category){
								return false;
							}	
						}
						if(!found){return false;}
					}
				}
				//if(!setOneValueHigher){return true;}
				//indien volgende kaart 1 waarde hoger is enz...
				if(setOneValueHigher){setCardOneValue(true);}
				return true;
			}	
		}
		return false;
	}
	
	private boolean getRandomValueHigherThan(int value, int category, boolean setOneValueHigher, boolean oneValueHigherThanValue){
		if(value<20 && category==trumpCategory){value=20;}//extra ingezet,dit wel
		int total=getNumberOfCardsHigherThan(value, category);
		if (total>0){
			int number=random(total);
			for(card=0;card<playerDeck[player].size();card++){
				if(playerDeck[player].get(card).category==category && playerDeck[player].get(card).value>value) {
					number--;if(number==0){break;}
				}	
			}
			if(!oneValueHigherThanValue){
				//eerst zien of men net 1 kaart lager heeft...
				setCardOneValue(false);
				if(playerDeck[player].get(card).value==value+1){
					//zoeken achter de kaart die meer dan 1 hoger is
					setCardOneValue(true);
					value=playerDeck[player].get(card).value;
					boolean found=false;
					if(card<playerDeck[player].size()-1){
						card++;value++;
						if(playerDeck[player].get(card).category==category && playerDeck[player].get(card).value!=value){
							found=true;
						} 
						if(playerDeck[player].get(card).category!=category){
							return false;
						}	
					}
					if(!found){return false;}
				}
			}
			//if(!setOneValueHigher){return true;}
			//indien volgende kaart 1 waarde hoger is enz...
			if(setOneValueHigher){setCardOneValue(true);}
			return true;
		}
		return false;
	}
	
	private boolean getValueBetween(int minValue, int maxValue, int category,boolean setOneValueHigher, boolean oneValueHigherThanMinValue){
		//if(value<20 && category==trumpCategory){value=20;}//extra ingezet
		int total=getNumberOfCardsBetween(minValue, maxValue, category);
		if (total>0){
			int number=random(total);
			for(card=0;card<playerDeck[player].size();card++){
				if(playerDeck[player].get(card).category==category && playerDeck[player].get(card).value>minValue
				   && playerDeck[player].get(card).value<maxValue  ) {
					number--;if(number==0){break;}
				}	
			}
			if(!oneValueHigherThanMinValue){
				//eerst zien of men net 1 kaart lager heeft...
				setCardOneValue(false);
				if(playerDeck[player].get(card).value==minValue+1){
					//zoeken achter de kaart die meer dan 1 hoger is
					setCardOneValue(true);
					int value=playerDeck[player].get(card).value;
					boolean found=false;
					if(card<playerDeck[player].size()-1){
						card++;value++;
						if(playerDeck[player].get(card).category==category && playerDeck[player].get(card).value!=value){
							found=true;
						} 
						if(playerDeck[player].get(card).category!=category){
							return false;
						}	
					}
					if(!found){return false;}
				}
			}
		//	if(!setOneValueHigher){return true;}
			//indien volgende kaart 1 waarde hoger is enz...
			if(setOneValueHigher){setCardOneValue(true);}
			return true;
		}
		return false;
	}
	
	private boolean getValueLowerThan(int value, int category, boolean setOneValueLower){
		for(card=playerDeck[player].size()-1;card>=0;card--){
			if(playerDeck[player].get(card).category==category && playerDeck[player].get(card).value<value) {
				//indien volgende kaart 1 waarde lager is enz...
				if(setOneValueLower){setCardOneValue(false);}
				return true;
			}	
		}
		return false;
	}
	
	private void setCardOneValue(boolean increase){
		int value=playerDeck[player].get(card).value;
		int category=playerDeck[player].get(card).category;
		if(increase){//verhogen
			while(card<playerDeck[player].size()-1){
				card++;value++;
				if(playerDeck[player].get(card).category==category && playerDeck[player].get(card).value!=value ||
				   playerDeck[player].get(card).category!=category){
					card--;break;
				} 
			}	
		}else{
			while(card>0){
				card--;value--;
				if(playerDeck[player].get(card).category==category && playerDeck[player].get(card).value!=value ||
				   playerDeck[player].get(card).category!=category){
					card++;break;
				} 
			}	
		}
		
	}
	
	public int getWinner(){
		int cat=gameDeck.get(0).category;
		int val=gameDeck.get(0).value;
		for(int a=1;a<gameDeck.size();a++){//1 2 en 3
			if(gameDeck.get(a).category==cat || gameDeck.get(a).category==trumpCategory){
				if(gameDeck.get(a).value>val){val=gameDeck.get(a).value;}
			}
		}
		int p=startPlayer;
		for(int a=0;a<gameDeck.size();a++){//0 1 2 en 3
			if(gameDeck.get(a).category==cat || gameDeck.get(a).category==trumpCategory){
				if(gameDeck.get(a).value==val)return p;
			}	
			p++;if(p==5)p=1;	
		}
		return 0;
	}	
	
	public int getWinnerValue(){
		int cat=gameDeck.get(0).category;
		int val=gameDeck.get(0).value;
		for(int a=1;a<gameDeck.size();a++){//1 2 en 3
			if(gameDeck.get(a).category==cat || gameDeck.get(a).category==trumpCategory){
				if(gameDeck.get(a).value>val){val=gameDeck.get(a).value;}
			}
		}
		return val;
	}	
	
	public int getWinnerValueWithoutBuying(){
		int cat=gameDeck.get(0).category;
		int val=gameDeck.get(0).value;
		for(int a=1;a<gameDeck.size();a++){//1 2 en 3
			if(gameDeck.get(a).category==cat){
				if(gameDeck.get(a).value>val){val=gameDeck.get(a).value;}
			}
		}
		return val;
	}	
	
	private Card lastPlayedCard(int player){
		int p=oldStartPlayer;
		for(int a=0;a<last4CardDeck.size();a++){
			if(player==p){return last4CardDeck.get(a);}
			p++;if(p==5)p=1;
		}
		return null;
	}
	
	private boolean lastReleasedPlayerIsFriend(){
		for(int b=0;b<friend[player].length;b++){//was de vorige uitkomer vriend of jezelf
			if(oldStartPlayer==friend[player][b] || oldStartPlayer==player){
				return true;
			}
		}	
		return false;
	}
	
	public String getPlay(int player){
		int numberOfStrokesPas=waitingDeck[0].size()/4;
		int numberOfStrokes=waitingDeck[1].size()/4;
		String str="";
		switch(playerChoice[player]){
			case SOLOSLIM:str="Soloslim";break;
			case SOLO:str="Solo";break;
			case OPENMISERE:str="Openmisere";break;
			case TROEL:str="Troel";break;
			case TROELMEE:str="Troelmee";break;
			case DANSEN12INTROEF:str="Dansen12introef";break;
			case DANSEN12:str="Dansen12";break;
			case DANSEN11INTROEF:str="Dansen11introef";break;
			case DANSEN11:str="Dansen11";break;
			case DANSEN10INTROEF:str="Dansen10introef";break;
			case DANSEN10:str="Dansen10";break;
			case MISERE:str="Misere";break;
			case DANSEN9INTROEF:str="Dansen9introef";break;
			case DANSEN9:str="Dansen9";break;
			case VRAAG:str="Vraag";break;
			case MEEGAAN:str="Meegaan";break;
			case ALLEEN:str="Alleen";break;
			case PAS:
				if(oldPlayerChoice[player]==MISERE || oldPlayerChoice[player]==OPENMISERE){
					str="Pas en eraan!!!";break;
				}
				str="Pas";break;
			default: str="no play";
		}
		if(playerChoice[player]==PAS){
			str=str+" ("+String.valueOf(numberOfStrokesPas)+")"+"("+String.valueOf(playerPoints[player])+")";
		}else{
			str=str+" ("+String.valueOf(numberOfStrokes)+")"+"("+String.valueOf(playerPoints[player])+")";
		}
		return str;
	}
	
	public void setRangeCategory2(){//enkel bij misere of openmisere
		if(maxChoice==MISERE){
			//indien de laatste kaart gespeeld van een kleur, dan onmiddellijk verwijderen uit getcategory
			//zowel passers als miserespelers
			int categ=gameDeck.get(gameDeck.size()-1).category;//laatste kaart in gamedeck
			for(int c=0;c<getCategory.size();c++){
				if(getCategory.get(c).player==player && getCategory.get(c).category==categ &&
				   getNumberOfCards(categ)==0){
					getCategory.remove(c);c-=1;
				}
			}	
		}
		if(maxChoice==OPENMISERE){
			//indien de laatste kaart gespeeld van een kleur, dan zien waar de passers onder zitten en 
			//dan onmiddellijk verwijderen uit getcategory
			//openmiserespelers als underplayer en passers als player(idem als misere)
			int categ=gameDeck.get(gameDeck.size()-1).category;//laatste kaart in gamedeck
			for(int c=0;c<getCategory.size();c++){
				if((getCategory.get(c).underPlayer==player || getCategory.get(c).player==player)&& 
				   getCategory.get(c).category==categ &&
				   getNumberOfCards(categ)==0){
					getCategory.remove(c);c-=1;
				}
			}	
		}
	}
	     
	public void setRangeCategory(int player, boolean resetAll){//enkel bij misere en openmisere
		toast("setRangeCategory...");
		if(resetAll){getCategory.clear();}
		if(maxChoice==MISERE){
			int number=9;//waarde boven dit = hogere kaarten
			float average;
			int totalValue;
			for(int a=1;a<=4;a++){//=category
				for(int b=1;b<=4;b++){//=player
					average=0;
					totalValue=getTotalValue(b, a);
					if(resetAll){
						if(totalValue!=0){//als totalValue!=0 dan is getNumberOfCards ook!=0 (delen door 0)
							average=(float)totalValue/getNumberOfCards(b,a);
							ObjPlayer ObjP=new ObjPlayer(b,a,average,0,0,-1);
							getCategory.add(ObjP);
						}
					}else{
						if(player==0){
							//controleren of men nog hoge kaarten heeft
							for(int c=0;c<getCategory.size();c++){
								if(getCategory.get(c).player==b && getCategory.get(c).category==a &&
								   getCategory.get(c).average>49 && getHighestValue(b,getCategory.get(c).category)<=number){
									if(totalValue!=0){
										average=(float)totalValue/getNumberOfCards(b,a);
									}
									getCategory.get(c).setAverage(average);
								}
							}
						}else{
							toast("wordt dit nog gebruikt????");
							//controleren of men nog hoge kaarten heeft,enkel player
							for(int c=0;c<getCategory.size();c++){
								if(getCategory.get(c).player==player && getCategory.get(c).category==a &&
								   getCategory.get(c).average>49 && getHighestValue(getCategory.get(c).category)<=number){
									if(totalValue!=0){average=(float)totalValue/getNumberOfCards(player,a);}
									getCategory.get(c).setAverage(average);
								}
							}
						}
					}
				}
			}

			//dit is een extra controle, mag altijd weg
			for(int c=0;c<getCategory.size();c++){
				if(getCategory.get(c).average>0 && getCategory.get(c).average<2){//tussen 0 en 2
					toast("FOUT IN PROGRAMMA, average="+String.valueOf(getCategory.get(c).average)+",player="+
						  String.valueOf(getCategory.get(c).player)+",category="+
						  String.valueOf(getCategory.get(c).category));
				}
			}	

			//indien safely dan verwijderen uit arraylist enkel bij de niet-passers
			for(int c=0;c<getCategory.size();c++){
				if(playerChoice[getCategory.get(c).player]!=PAS && safely(getCategory.get(c).player,getCategory.get(c).category)){
					getCategory.remove(c);c-=1;
				}
			}	

			//sorteren volgens hoogste average
			for(int c=0;c<getCategory.size()-1;c++){//laatste kaart moet niet meer gecontroleerd worden
				for(int d=c+1;d<getCategory.size();d++){
					float averagec=getCategory.get(c).average;
					float averaged=getCategory.get(d).average;
					if(averagec < averaged){
						getCategory.add(c,getCategory.get(d));
						getCategory.remove(d+1);
						c-=1;break;
					}
				}
			}	
			if(resetAll){
				//controleren of men heel hoge kaarten heeft en weinig in totaal...
				//bvb. 48ta,24678tbvka dan toch de eerste aas eerst wegspelen
				//eerst de kleuren met hogere kaarten naar voren halen
				for(int c=0;c<getCategory.size();c++){
					if(getHighestValue(getCategory.get(c).player,getCategory.get(c).category)>number){
						getCategory.add(0,getCategory.get(c));
						getCategory.remove(c+1);
					}
				}
				//...en dan daarna sorteren volgens het aantal kaarten, indien gelijk sorteren volgens average
				for(int c=0;c<getCategory.size()-1;c++){//laatste kaart moet niet meer gecontroleerd worden
					int categoryc=getCategory.get(c).category;
					int playerc=getCategory.get(c).player;
					float averagec=getCategory.get(c).average;
					if(getHighestValue(playerc,categoryc)<number+1){break;}
					for(int d=c+1;d<getCategory.size();d++){
						int categoryd=getCategory.get(d).category;
						int playerd=getCategory.get(d).player;
						float averaged=getCategory.get(d).average;
						if(getHighestValue(playerc,categoryc)>number && getHighestValue(playerd,categoryd)>number){
							if(getNumberOfCards(playerc,categoryc) > getNumberOfCards(playerd,categoryd) ||
							   (getNumberOfCards(playerc,categoryc) == getNumberOfCards(playerd,categoryd) && averagec < averaged)){
								getCategory.add(c,getCategory.get(d)); //dit op 0 niet goed     c dus
								getCategory.remove(d+1);
								c-=1;break;
							}	
						}
					}
				}	
				//...en nu de average van de hogere kaarten aanpassen,50 51 52 enz...
				int numb=50;
				for(int c=getCategory.size()-1;c>=0;c--){
					int categoryc=getCategory.get(c).category;
					int playerc=getCategory.get(c).player;
					if(getHighestValue(playerc,categoryc)>number){
						getCategory.get(c).setAverage(numb);
						numb++;
					}
				}	
			}
		}
		if(maxChoice==OPENMISERE){
			float average;
			int totalValue,remainingCards,checkedCards;
			for(int a=1;a<=4;a++){//=category
				for(int b=1;b<=4;b++){//=player
					average=0;remainingCards=20;checkedCards=20;
					totalValue=getTotalValue(b, a);
					if(resetAll){
						if(totalValue!=0){//als totalValue!=0 dan is getNumberOfCards ook!=0 (delen door 0)
							average=(float)totalValue/getNumberOfCards(b,a);
							//vastleggen remainingCards
							//eerst zien of je onder de laagste kaart zit van de openmiserespeler
							int underPlayer=0;
							if(playerChoice[b]==PAS){
								boolean ok=false;
								int numberOfCardsToPlay=-1;
								int lowestV=0;
								underPlayer=0;
								for(int z=1;z<=4;z++){
									if(z!=b && playerChoice[z]==OPENMISERE){
										if(getLowestValue(b,a)<getLowestValue(z,a)){
											ok=true;
											int numberOfCardsSelf=getNumberOfCards(b,a)-1;
											int numberOfCardsEnemy=getNumberOfCards(z,a)-1;
											//if(b==2 && a==2){toast(String.valueOf(numberOfCardsEnemy));}//test  1 en 0
											if(numberOfCardsSelf<=numberOfCardsEnemy && numberOfCardsSelf>numberOfCardsToPlay){
												numberOfCardsToPlay=numberOfCardsSelf;lowestV=getLowestValue(z,a);underPlayer=z;
											}
											if(numberOfCardsSelf>numberOfCardsEnemy && numberOfCardsEnemy>numberOfCardsToPlay){
												numberOfCardsToPlay=numberOfCardsEnemy;lowestV=getLowestValue(z,a);underPlayer=z;
											}
										}
									}
								}
								if(ok){//nu de overgebleven kaarten tellen
									remainingCards=0;
									for(int z=1;z<=4;z++){//alles van de vrienden bijeentellen - de kaarten die er ook onder zitten
										if(z!=b && playerChoice[z]==PAS){// && oldPlayerChoice[z]!=OPENMISERE
											remainingCards=remainingCards+getNumberOfCards(z,a)-getNumberOfCardsBetween(0,lowestV,z,a);
										}	
									}
									//checkedCards tellen
									checkedCards = remainingCards - numberOfCardsToPlay;
								}
							}	
							ObjPlayer ObjP=new ObjPlayer(b,a,average,remainingCards,checkedCards,underPlayer);
							getCategory.add(ObjP);
						}else{
							if(playerChoice[b]==OPENMISERE){setFollow(0,b,a);}
						}
					}else{ 
					//	if(player==0)
					}
				}
			}

			//checkplayers
			checkPlayers(0);
			
			//dit is een extra controle, mag altijd weg
			for(int c=0;c<getCategory.size();c++){
				if(getCategory.get(c).average>0 && getCategory.get(c).average<2){//tussen 0 en 2
					toast("FOUT IN PROGRAMMA, average="+String.valueOf(getCategory.get(c).average)+",player="+
						  String.valueOf(getCategory.get(c).player)+",category="+
						  String.valueOf(getCategory.get(c).category));
				}
			}	

			//indien safely dan verwijderen uit arraylist enkel bij de niet-passers
			for(int c=0;c<getCategory.size();c++){
				if(playerChoice[getCategory.get(c).player]!=PAS && safely(getCategory.get(c).player,getCategory.get(c).category)){
					getCategory.remove(c);c-=1;
				}
			}	

			//sorteren volgens hoogste average en enkel de openmiserespelers
			for(int c=0;c<getCategory.size()-1;c++){//laatste kaart moet niet meer gecontroleerd worden
				for(int d=c+1;d<getCategory.size();d++){
					float averagec=getCategory.get(c).average;
					float averaged=getCategory.get(d).average;
					int playerc=getCategory.get(c).player;
					int playerd=getCategory.get(d).player;
					if(playerChoice[playerc]==OPENMISERE && playerChoice[playerd]==OPENMISERE && averagec < averaged){
						getCategory.add(c,getCategory.get(d));
						getCategory.remove(d+1);
						c-=1;break;
					}
				}
			}	
			if(resetAll){
				//sorteren volgens laagste checkedCards en enkel de passers
				for(int c=0;c<getCategory.size()-1;c++){//laatste kaart moet niet meer gecontroleerd worden
					for(int d=c+1;d<getCategory.size();d++){
						int checkedCardsc=getCategory.get(c).checkedCards;
						int checkedCardsd=getCategory.get(d).checkedCards;
						int playerc=getCategory.get(c).player;
						int playerd=getCategory.get(d).player;
						if(playerChoice[playerc]==PAS && playerChoice[playerd]==PAS && checkedCardsc > checkedCardsd){
							getCategory.add(c,getCategory.get(d));
							getCategory.remove(d+1);
							c-=1;break;
						}
					}
				}	
			}
		}
	}	
	
	public boolean gameEndOpenmisere(){
		if(maxChoice==OPENMISERE){
			//hier bepalen of alle openmiserespelers er niet meer aan kunnen, einde spel dus
			for(int b=1;b<=4;b++){
				if(playerChoice[b]==OPENMISERE && !getCategory(b).isEmpty()){return false;}
			}	
			return true;
		}	
		return false;
	}
	
	private boolean safely(int player, int category){//ok ook voor openmisere
		//bvb.246789...  vergelijken met 35=veilig, als men niet uit moet komen in de eerste ronde
		//deck[0]=de niet openmiserespelers samengevoegd
		//deck[hoger dan 0]=de openmiserespelers apart
		int z=1;
		for(int a=1;a<=4;a++){if(a!=player && playerChoice[a]==OPENMISERE){z++;}}
		//ArrayList<Card>[] deck = new ArrayList[z];
		Deck[] deckTemp=new Deck[z];
		for(int a=0;a<z;a++){
			deckTemp[a]=new Deck(mCtx);
		}
		int d=0;
		for(int a=1;a<=4;a++){
			if(a!=player){
				if(playerChoice[a]==OPENMISERE){
					d++;deckTemp[d].addAll(getPlayerDeckCategory(a,category));
				}else{
					deckTemp[0].addAll(getPlayerDeckCategory(a,category));
				}	
			}
		}	
		deckTemp[0].sort();
		
		Deck selfDeck = new Deck(mCtx);
		selfDeck=getPlayerDeckCategory(player,category);
		
		//als misere of openmiserespeler moet uitkomen(kan enkel 1ste round zijn) dan enkel safely als kaarten bvb 23456 zijn
		if(playerChoice[player]!=PAS && round==0 && startPlayer==player){
			for(int a=1;a<=selfDeck.size();a++){
				if(selfDeck.card(a).value!=a+1){return false;}
			}	
		}else{
			for(int c=0;c<z;c++){
				for(int a=1;a<=selfDeck.size();a++){
					for(int b=1;b<=deckTemp[c].size();b++){
						if(a==b && selfDeck.card(a).value>deckTemp[c].card(b).value){return false;}
						if(a==b){break;}
					}
				}	
			}
		}
		return true;
	}
	
	private int isNextPlayer(){
		int a=player+1;
		if(a==5){a=1;}
		return a;
	}
	
	public ArrayList<ObjPlayer> getCategory(int player){
		ArrayList<ObjPlayer> arList=new ArrayList<ObjPlayer>();
		for(int c=0;c<getCategory.size();c++){
			if(getCategory.get(c).player==player){
				arList.add(getCategory.get(c));
			}
		}	
		return arList;
	}
	
	private boolean isInGetcategory(int cate, int player){
		ArrayList<ObjPlayer> arList = getCategory(player);
		for(int d=0;d<arList.size();d++){	
			if(cate==arList.get(d).category){return true;}
		}	
		return false;
	}
	
	private boolean okFriendsVisibleCards(int category){
		//als men een category uitkomt waar een andere vriend(waar je de kaarten kunt van zien) onder de kaarten 
		//zit en deze heeft er maar 1 van en de underplayer heeft er meer dan 1 van dan==false
		for(int a=1;a<=4;a++){
			if(a!=player && oldPlayerChoice[a]==OPENMISERE){
				for(int c=0;c<getCategory.size();c++){
					int underPlayer = getCategory.get(c).underPlayer;
					if(underPlayer!=0 && getCategory.get(c).category==category &&
						getCategory.get(c).player==a && getNumberOfCards(a,category)==1 &&
						getNumberOfCards(underPlayer,category)>1){return false;}//deze regel moet blijven staan
				}	
			}
		}
		return true;
	}
	
	private int getPlayerWhoDies(){//zien of een openmiserespeler eraan gaat in deze ronde en bij het volgen
		//zien of er meer dan 1 openmiserespeler is
		if(getNumberOfPlayersMisereOrOpenmisere()<2){return 0;}
		//eerst zien of er al kaarten zijn gespeeld en we als speler een passer zijn
		if(gameDeck.size()==0){return 0;}
		if(playerChoice[player]!=PAS){return 0;}
		//zien of er wel een openmiserespeler eraan gaat in deze ronde:
		//zoeken hoeveel startvrienden er zijn in het gamedeck en die volgen
		//en nu de laagste value zoeken na gamedeck waarvan je de kaarten kunt zien
		int categoryStart=gameDeck.get(0).category;
		int winnerValue=getWinnerValue();
		int numberOfStartFriends=0;
		int b=startPlayer;b--;if(b==0){b=4;}
		for(int a=0;a<gameDeck.size();a++){
			b++;if(b==5){b=1;}
			if(playerChoice[b]==PAS && oldPlayerChoice[b]!=OPENMISERE && followed[player][b][categoryStart]){
				numberOfStartFriends++;
			}
		}
		int z=b;
		z++;if(z==5){z=1;}
		while(z!=startPlayer){
			if(playerChoice[z]==OPENMISERE || oldPlayerChoice[z]==OPENMISERE){
				if(getLowestValue(z,categoryStart)>winnerValue){
					winnerValue=getLowestValue(z,categoryStart);
				}
			}
			z++;if(z==5){z=1;}
		}	
		//zien of de startvrienden achter je niet kunnen volgen of onder de winnervalue zitten
		//=>indien men kan volgen en een startvriend bent dan nagaan of er ergens nog een startvriend is die kan volgen
		//=>indien ja dan weet men niet of deze speler een lagere kaart heeft of remainingcards=0 dan wel
		z=b;
		z++;if(z==5){z=1;}
		while(z!=startPlayer){
			if(playerChoice[z]==PAS && oldPlayerChoice[z]!=OPENMISERE && followed[player][z][categoryStart]){
				if(numberOfStartFriends==0){
					if(getLowestValue(z,categoryStart)>winnerValue){return 0;}
				}else{//>0
					//het aantal remainingcards tellen
					int remainingCards=0;
					for(int a=1;a<=4;a++){
						if(a!=startPlayer){
							if(playerChoice[a]==PAS && oldPlayerChoice[a]!=OPENMISERE && followed[player][a][categoryStart]){
								//alles van de startvrienden bijeentellen - de kaarten die er ook onder zitten
								remainingCards=remainingCards+getNumberOfCards(a,categoryStart)-getNumberOfCardsBetween(0,winnerValue,a,categoryStart);
							}	
						}
					}	
					if(remainingCards>0){return 0;}
				}
			}
			z++;if(z==5){z=1;}
		}			
		//zoek de speler die eraan gaat zijn indien het een openmiserespeler is
		winnerValue=getWinnerValue();
		int playerDead=getWinner();
		z=b;
		z++;if(z==5){z=1;}
		while(z!=startPlayer){
			if(getLowestValue(z,categoryStart)>winnerValue){
				winnerValue=getLowestValue(z,categoryStart);playerDead=z;
			}
			z++;if(z==5){z=1;}
		}	
		if(playerChoice[playerDead]!=OPENMISERE){return 0;}
		return playerDead;
	}
	
	public void checkPlayers(int category){//enkel gebruikt bij openmisere
	//als er 2 spelers zijn waar je de kaarten kunt van zien dan kan de 3de weten wat de 4de heeft en andersom ook
	//als er 3 spelers zijn waar je de kaarten kunt van zien dan kunnen deze 3 weten wat de 4de heeft
	//hier wordt enkel setfollowed bepaald
		int totalVisiblePlayers=0,person1=0,person2=0;
		for(int z=1;z<=4;z++){
			if(playerChoice[z]==OPENMISERE || oldPlayerChoice[z]==OPENMISERE){totalVisiblePlayers++;}
			if(playerChoice[z]==PAS && oldPlayerChoice[z]!=OPENMISERE){
				if(person1==0){person1=z;}else{person2=z;}
			}
		}
		if(totalVisiblePlayers==2 && person1!=0 && person2!=0){
			if(category==0){
				for(int a=1;a<=4;a++){//=category
					if(getNumberOfCards(person1,a)==0){setFollow(person2,person1,a);}
					if(getNumberOfCards(person2,a)==0){setFollow(person1,person2,a);}
				}	
			}else{
				if(getNumberOfCards(person1,category)==0){setFollow(person2,person1,category);}
				if(getNumberOfCards(person2,category)==0){setFollow(person1,person2,category);}
			}
		}
		if(totalVisiblePlayers==3 && person1!=0 && person2==0){
			if(category==0){
				for(int a=1;a<=4;a++){//=category
					if(getNumberOfCards(person1,a)==0){setFollow(0,person1,a);}
				}	
			}else{
				if(getNumberOfCards(person1,category)==0){setFollow(0,person1,category);}
			}
		}
	}
}
