package dominion;

import java.util.Random;
import java.sql.ResultSet;

public class Deck {
	private int spelID;
	private int aantalKoninkrijkenInDeck;
	private int aantalBestaandeKoninkrijken;
	private int[] deckint;
	
	//		CONSTRUCTOR
	public Deck(int spelID)
	{
		aantalKoninkrijkenInDeck = 10; // HARDCODED
		aantalBestaandeKoninkrijken = hoogsteKoninkrijkIDinDB();
		this.spelID = spelID;
		
		// array vullen met 10 ints
		 deckint = vulArrayVanLengteMetVerschillendeIntsVan1Tot(aantalKoninkrijkenInDeck,aantalBestaandeKoninkrijken);
		 
		// de mogelijke kaarten opslaan in DB
		koninkrijkenOpslaanInDB(deckint);	
		}		
	
	//		PRIVATE METHODS
	private void koninkrijkenOpslaanInDB(int[] spelDeck)
	{
		int len = spelDeck.length;
		for (int i=0; i<len; i++)
		{
			try
			{
				insertKoninkrijkInKONINKRIJKEN_SPELLEN(spelDeck[i], spelID);
			} catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}	
	}
	
	private int hoogsteKoninkrijkIDinDB()
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT MAX(koninkrijkID) AS maxKoninkrijkID FROM koninkrijken";
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		int hoogste = 0;
		
		try
		{
			while (rs.next())
			{
				hoogste = rs.getInt("maxKoninkrijkID");
			}
		} catch (Exception e)
		{	                    
			System.out.println(e.getMessage());
		}
		cdb.sluitConnectie();
		return hoogste;
	}
	
	private void insertKoninkrijkInKONINKRIJKEN_SPELLEN(int koninkrijkID, int spelID)
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "INSERT INTO koninkrijken_spellen(koninkrijkID, spelID) VALUES (" + koninkrijkID + "," + spelID + ")";
		cdb.insertStatement(sqlStatement);
		cdb.sluitConnectie();
	}
	
	private int[] vulArrayVanLengteMetVerschillendeIntsVan1Tot(int lengte, int maxGetal)
	{
		int[] gevuldeArray = new int[lengte];
		for (int i=0; i<lengte;i++)
		{
			int n = willekeurigGetalVan1Tot(maxGetal);
			for (int j =0; j<lengte;j++)
			{
				while (n == gevuldeArray[j])
				{
					n = willekeurigGetalVan1Tot(maxGetal);
					j=0;
				}
			}
			gevuldeArray[i] = n;
		}
		return gevuldeArray;
	}

	private int willekeurigGetalVan1Tot(int getal)
	{
		Random rand = new Random();
		int n;
		n = rand.nextInt(getal) + 1;
		return n;
	}
	
	//		MAIN
	public static void main(String[] args)
	{
		Deck startdeck = new Deck(1);
	}
	
}
