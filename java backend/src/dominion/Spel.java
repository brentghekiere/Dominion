package dominion;

import java.sql.ResultSet;

public class Spel {

	private int spelID;
	private int aantalSpelers;
	private int[] spel_spelerID;
	public int[][] decks;
	private boolean isGeldig = true;
	
	private Spelers gastSpelers;
	private Deck speeldeck;
	
	//		CONSTRUCTOR NIEUW SPEL STARTEN
	public Spel(int aantalSpelers, boolean andereConstructorBoolean)
	{
		this.aantalSpelers = aantalSpelers;
		nieuwSpelInitialiseren();
	}
	
	//		CONSTRUCTOR OPGESLAGEN SPEL LADEN
	public Spel(int spelID)
	{
		if (SpelLadenControle.isGeldigSpel(spelID))
		{
			// vars opvullen
			this.spelID = spelID; // niet meer nodig?
			
			//laden van de spelers
			Spelers spelers = new Spelers(spelID);
			
			// arrays opvullen om te gebruiken
			aantalSpelers = spelers.getAantalSpelers(); //vanaf nu kan je de constante aantalspelers gebruiken...
			spel_spelerID = spelers.getSpel_SpelerIDsInArray();
		}
		else
		{
			isGeldig = false;
			System.err.println("Het spel dat je probeerde te laden bestaat niet, of is reeds beëindigd.");
		}
	}
	
	//		PRIVATE METHODS
	private void nieuwSpelInitialiseren()
	{
		nieuwSpelRegistreren();
		laadSpelID();
		gastSpelers = new Spelers(aantalSpelers, spelID);
		speeldeck = new Deck(spelID);
		bouwStartDecksSpelers();
	}
	
	private void nieuwSpelRegistreren()
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "INSERT INTO spellen(beëindigd) VALUES (FALSE)";
		cdb.insertStatement(sqlStatement);
		cdb.sluitConnectie();
	}
	
	private void laadSpelID()
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT MAX(spelID) AS spelID FROM spellen";
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		int huidigSpelID = 0;
		
		try
		{
			while (rs.next())
			{
				huidigSpelID = rs.getInt("spelID");
			}
		} catch (Exception e)
		{	                    
			System.out.println(e.getMessage());
		}
		cdb.sluitConnectie();
		
		spelID = huidigSpelID;
	}
		
	private void bouwStartDecksSpelers()
	{
		try
		{
			int[] spelerIDs = gastSpelers.getSpel_SpelerIDsInArray();
			geefStartkaartenAanSpelers(spelerIDs);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}	
	}
	
	private void geefStartkaartenAanSpelers(int[] spel_spelerIDs)
	{
		for (int i=0;i<aantalSpelers;i++)
		{
			voegKaartToeAanDeckVanSpeler(spel_spelerIDs[i],30,7); // 7 koperkaarten (id 30)
			voegKaartToeAanDeckVanSpeler(spel_spelerIDs[i],27,3); // 3 landgoederen (id 27)
		}
	}

	private void beurtSpelen()
	{
		Beurt beurt = new Beurt(spelID);
		
		// Actie actie = new Actie();
		// Aanschaf kopen = new Aanschaf();
		// Opschonen clean = new Opschonen();
		
	}
	
	//		PUBLIC METHODS
	public void voegKaartToeAanDeckVanSpeler(int spel_spelerID, int kaartID, int aantal) // TODO naar klasse SpelerDeck verplaatsen
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "INSERT INTO spellen_spelers_kaarten(spel_spelerID, kaartID, aantal) VALUES(" + spel_spelerID + "," + kaartID + "," + aantal + ")";
		cdb.insertStatement(sqlStatement);
		cdb.sluitConnectie();
	}

	public boolean isSpelerInDezeGame(int spel_spelerID)
	{
		boolean spelerInGame = false;
		
		for (int i = 0; i < aantalSpelers; i++)
		{
			int spel_spelerIDinArray = this.spel_spelerID[i];
			if (spel_spelerIDinArray == spel_spelerID)
			{
				spelerInGame = true;
			}
		}
		return spelerInGame;
	}

	public boolean getIsGeldig()
	{
		return isGeldig;
	}
	
	//      MAIN
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Spel oud = new Spel(5); // laden van spel met spelID 5 // werkt
		Spel nieuw = new Spel(4,true); // aanmaken nieuw spel met 2 spelers // werkt
	}
}