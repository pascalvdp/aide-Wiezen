package com.mycompany.Wiezen;
import java.util.*;

public interface Choices
{
	//solo slim  13slagen
	//solo   13 slagen
	//openmisere  
	//troel  8 slagen
	//misere 
	//abondance  9slagen 10 11 12
	//vragen en meegaan  5+3slagen    alleen 5slagen
	//bij misere en openmisere:
	//niet voortgaan op het aantal slagen maar wel zien wat playerchoice is en oldplayerchoice
	//bvb.indien oldplayerchoice=misere en playerchoice=pas dan is deze er aan
	//puntentelling:
	public static final int SOLOSLIM=19;//45
	public static final int SOLO=18;//45
	public static final int OPENMISERE=17;//30
	public static final int TROELMEE=15;//dus 28 punten max.
	public static final int TROEL=14;//2 overslag=+1 ,maar dubbel dan, alle slagen=dubbel, bij verlies=alles omgekeerd
	public static final int DANSEN12INTROEF=13;
	public static final int DANSEN12=12;//18, alle slagen=dubbel
	public static final int DANSEN11INTROEF=11;
	public static final int DANSEN11=10;//15, alle slagen=dubbel
	public static final int DANSEN10INTROEF=9;
	public static final int DANSEN10=8;//12, alle slagen=dubbel
	public static final int MISERE=7;  //15 of 5,5 of 5,5,5, indien 5,5 dan bij 1 verlies moet de andere 10 betalen,bvb 10,-10,0,0
	//2misereplayers => w  w 
	//				    10 10 -10 -10
	//				    w  l
	//					10 -10  0  0	
	//					l  l
	//				   -10 -10 10 10
	//3misereplayers => w  w  w
	//					5  5  5 -15
	//					w  w  l
	//					5  5 -10 0
	//					w  l  l
	//					10 -5 -5 0
	//					l  l  l
	//					-5 -5 -5 15
	//4miserespelers => w  w  w  l
	//					5  5  5 -15
	//					w  w  l  l
	//				   10  10 -10 -10
	//					w  l  l  l
	//				   15 -5 -5 -5
	//					l  l  l  l
	//					0  0  0  0			
	public static final int DANSEN9INTROEF=5;
	public static final int DANSEN9=4;//9, alle slagen=dubbel
	public static final int MEEGAAN=3;
	public static final int VRAAG=2;//2 overslag=+1 , alle slagen=dubbel,bij verlies=alles omgekeerd,bvb.2,2,-2,-2
	public static final int ALLEEN=1;//2 overslag=+1 , alle slagen=dubbel,bvb.6,-2,-2,-2 of 12,-4,-4,-4 => punten*3
	public static final int PAS=0;

	public static final String soloslim="SOLOSLIM";
	public static final String soloslimHelp="13 slagen";
	public static final String solo="SOLO";
	public static final String soloHelp="13 slagen";
	public static final String openmisere="OPENMISERE";
	public static final String openmisereHelp="0 slagen";
	public static final String troelmee="TROELMEE";
	public static final String troelmeeHelp="8 slagen";
	public static final String troel="TROEL";
	public static final String troelHelp="8 slagen";
	public static final String dansen12introef="DANSEN12INTROEF";
	public static final String dansen12introefHelp="12 slagen";
	public static final String dansen12="DANSEN12";
	public static final String dansen12Help="12 slagen";
	public static final String dansen11introef="DANSEN11INTROEF";
	public static final String dansen11introefHelp="11 slagen";
	public static final String dansen11="DANSEN11";
	public static final String dansen11Help="11 slagen";
	public static final String dansen10introef="DANSEN10INTROEF";
	public static final String dansen10introefHelp="10 slagen";
	public static final String dansen10="DANSEN10";
	public static final String dansen10Help="10 slagen";
	public static final String misere="MISERE";
	public static final String misereHelp="0 slagen";
	public static final String dansen9introef="DANSEN9INTROEF";
	public static final String dansen9introefHelp="9 slagen";
	public static final String dansen9="DANSEN9";
	public static final String dansen9Help="9 slagen";
	public static final String meegaan="MEEGAAN";
	public static final String meegaanHelp="8 slagen";
	public static final String vraag="VRAAG";
	public static final String vraagHelp="8 slagen";
	public static final String alleen="ALLEEN";
	public static final String alleenHelp="5 slagen";
	public static final String pas="PAS";
	public static final String pasHelp="Je past";

	public static final String soloslimToolTip="Alle slagen halen in troef(13)";
	public static final String soloToolTip="Alle slagen halen(13)";
	public static final String openmisereToolTip="Al je kaarten laten zien en geen enkele slag halen";
	public static final String troelmeeToolTip="Jij hebt de 4de aas, samen met je collega minstens 8 slagen halen";
	public static final String troelToolTip="Jij hebt minstens 3 azen, samen met je collega minstens 8 slagen halen";
	public static final String dansen12introefToolTip="Minstens 12 slagen halen in troef";
	public static final String dansen12ToolTip="Minstens 12 slagen halen";
	public static final String dansen11introefToolTip="Minstens 11 slagen halen in troef";
	public static final String dansen11ToolTip="Minstens 11 slagen halen";
	public static final String dansen10introefToolTip="Minstens 10 slagen halen in troef";
	public static final String dansen10ToolTip="Minstens 10 slagen halen";
	public static final String misereToolTip="Geen enkele slag halen";
	public static final String dansen9introefToolTip="Minstens 9 slagen halen in troef";
	public static final String dansen9ToolTip="Minstens 9 slagen halen";
	public static final String meegaanToolTip="Samen met je collega minstens 8 slagen halen";
	public static final String vraagToolTip="Samen met je collega minstens 8 slagen halen";
	public static final String alleenToolTip="Minstens 5 slagen halen";
	public static final String pasToolTip="Je past, probeer je tegenstander slagen af te nemen";

	public static final int HARTEN=101;
	public static final int KLAVEREN=102;
	public static final int KOEKEN=103;
	public static final int SCHUPPEN=104;
	public static final int TERUG=105;
}
