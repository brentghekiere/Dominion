package dominion;

import java.sql.ResultSet;

public class Aanschaf 
{
	//private int aanschaffingen;
	private Beurt dezebeurt;
	private SpelerDeck jouwSpeeldeck;
	private int spel_spelerID;
	
	//		CONSTRUCTOR
	public Aanschaf(Beurt dezebeurt, SpelerDeck jouwSpeeldeck, int spel_spelerID)
	{
		this.dezebeurt = dezebeurt;
		this.jouwSpeeldeck = jouwSpeeldeck;
		this.spel_spelerID = spel_spelerID;
	}
	
	//		PUBLIC METHODS
	public boolean kanKopen(int kaartID)
	{
		boolean kanKopen = false;
		//if (dezebeurt.getAanschaffingenOver() > 0) // TODO: getAanschaffingenOver() maken in Beurt
		{
			//if (dezebeurt.getGeld() >= Kaarten.getKost(kaartID)) kanKopen = true; //TODO: getGeld() maken in Beurt
		}
		return kanKopen;
	}

	public void schafAan(int kaartID)
	{
		if(kanKopen(kaartID))
		{
			//dezebeurt.setAanschaffingenOver(dezebeurt.getAanschaffingenOver() - 1);
			//dezebeurt.setGeld(dezebeurt.getGeld - Kaarten.getKost(kaartID));
			jouwSpeeldeck.legNieuweKaartOpAflegstapel(kaartID);
			
			// in spellen_spelers_kaarten kaart inzetten na aanschaf
			
			voegKaartToeAanKaartenVanSpelerInDatabase(kaartID);
		}
		else System.err.println("Je kan de kaart niet kopen! Je hebt al je koopbeurten al opgebruikt, of je hebt geen geld genoeg.");
	}

	private void voegKaartToeAanKaartenVanSpelerInDatabase(int kaartID)
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM Spellen_spelers_kaarten WHERE spel_spelerID = " + spel_spelerID + " AND kaartID = " + kaartID;
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		try
		{
			if(rs.first()) // De speler heeft deze kaart al minstens één keer --> + 1 bij aantal 
			{
				int nieuwAantal = rs.getInt("aantal") + 1;
				String sqlUpdate = "UPDATE Spellen_spelers_kaarten SET aantal = " + nieuwAantal + " WHERE spel_spelerID = " + spel_spelerID + " AND kaartID = " + kaartID;
				cdb.updateStatement(sqlUpdate);
			}
			else // Speler heeft de kaart nog niet
			{
				String sqlInsert = "INSERT INTO Spellen_spelers_kaarten(spel_spelerID, kaartID, aantal) VALUES (" + spel_spelerID + ", " + kaartID + ", 1)";
				cdb.insertStatement(sqlInsert);
			}
		}
		catch (Exception e)
		{	        
			System.err.println(e.getMessage());
		}
		cdb.sluitConnectie();
	}

	/*
	public void krijgExtraKoopBeurt()
	{
		aanschaffingen++;
	}
	*/
	
	//		MAIN
	public static void main(String [ ] args)
	{
		/*Aanschaf hehe = new Aanschaf();
		System.out.println("kost 6 we hebben 5, kan kopen?: " + hehe.kanKopen(1));
		System.out.println("kost 4 we hebben 5, kan kopen?: " + hehe.kanKopen(2));
		hehe.schafAan(2);
		System.out.println("Na kopen van de tweede, kost 6, kan kopen? (dus 1 geld over): " + hehe.kanKopen(1));
		System.out.println("Geld in hand (normaal 1): " + hehe.getGeldOpTafel());				
		Aanschaf he = new Aanschaf(100);
		System.out.println("\n***Nieuwe koopbeurt!");
		System.out.println("***Je hebt nu 1 koopbeurt en 100 geld");
		System.out.println("kost 6 kan kopen?: " + he.kanKopen(1));
		he.schafAan(1);
		System.out.println("na kopen van die van 6, Current geld: " + he.getGeldOpTafel());
		System.out.println("Kan kopen 1 van 5? (en geen beurten meer over): " + he.kanKopen(2));
		he.krijgExtraKoopBeurt();
		System.out.println("Na toevoegen extra koop beurt, kost 5, kan kopen?: " + he.kanKopen(1));*/
	}

}
