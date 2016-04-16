package dominion;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Beurt {
	private int spel_spelerID;
	private int beurtnummer;
	private int spelID;
	private int aantalResterendeActies = 1;
	private int aantalResterendeAanschaf = 1;
	
	
	
	public Beurt(int spelID)
	{
		this.spelID = spelID;

		// select speler die mag spelen
		spel_spelerID = zoekJuisteSpeler(spelID);
	}

	private void BeurtBeëindigen()
	{
		beurtnummer ++;
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");

		String SQLStatement = "UPDATE spellen_spelers SET beurtnummer = " + beurtnummer + " WHERE spel_spelerID = " + spel_spelerID;
		cdb.insertStatement(SQLStatement);
		cdb.sluitConnectie();
	}
	
	private void speelActie(int kaartID)
	{
		//Actie actie = new Actie(kaartID, aantalResterendeActies, aantalResterendeAanschaf);
	}
	/*
	private void Aanschaf(int kaartID)
	{
		Aanschaf kopen = new Aanschaf(kaartID);
	}
	
	private void Opschonen(int kaartID)
	{
		
	}*/
	
	private int zoekJuisteSpeler(int spelID) {
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		
		String stm = "SELECT * FROM spellen_spelers	WHERE spelId = " + spelID + ""
				+ " AND (spelernummer = (SELECT MIN(spelernummer) FROM spellen_spelers WHERE spelID = " + spelID + " "
						+ " AND beurtnummer = (SELECT MIN(beurtnummer) FROM spellen_spelers WHERE spelid = " + spelID + ")))";				
				
		ResultSet rs = cdb.selectStatement(stm);
	
		int id = 0, huidigBeurtnummer = 0;

		try{
			if (rs.next())
			{
			id = rs.getInt("spel_spelerID");
			huidigBeurtnummer = rs.getInt("beurtnummer");
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		beurtnummer = huidigBeurtnummer;
		return id;
	}
	
	public static void main(String[] args) {
	
		
	}

}
