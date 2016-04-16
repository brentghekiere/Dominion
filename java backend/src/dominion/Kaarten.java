package dominion;

import java.sql.ResultSet;

public class Kaarten 
{
	
	//		PUBLIC METHODS
	public static String getNaam(int kaartID)
	{
		String kaartnaam = "";
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM Kaarten WHERE kaartID = " + kaartID;
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		try
		{
			if(rs.first()) kaartnaam = rs.getString("naam");
		}
		catch (Exception e)
		{	        
			System.err.println(e.getMessage());
		}
		cdb.sluitConnectie();
		if (kaartnaam.contentEquals("")) System.err.println("De kaartnaam kon niet gevonden worden, het opgegeven kaartID bestaat waarschijnlijk niet");
		
		return kaartnaam;
	}
	
	public static int getKost(int kaartID)
	{
		int kostPrijs = 10000; 
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM Kaarten WHERE kaartID = " + kaartID;
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		try
		{
			if(rs.first()) kostPrijs = rs.getInt("kost");
		}
		catch (Exception e)
		{	        
			System.err.println(e.getMessage());
		}
		cdb.sluitConnectie();
		if (kostPrijs == 10000) System.err.println("De Prijs kon niet gevonden worden, het opgegeven kaartID bestaat waarschijnlijk niet");
		return kostPrijs;
	}

	public static int getID(String kaartnaam)
	{
		int kaartID = 10000;
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM Kaarten WHERE naam = '" + kaartnaam + "'";
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		try
		{
			if(rs.first()) kaartID = rs.getInt("kaartID");
		}
		catch (Exception e)
		{	        
			System.err.println(e.getMessage());
		}
		cdb.sluitConnectie();
		if (kaartID == 10000) System.err.println("Het kaartID kon niet gevonden worden, De opgegeven kaartnaam bestaat waarschijnlijk niet");
		return kaartID;
	}

	public static String getSoort(int kaartID)
	{
		String kaartType = "";
		if (kaartID == 26) kaartType = "Vloekkaart";
		else
		{
			ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
			String sqlStatement = "SELECT * FROM Actiekaarten";
			ResultSet rs1 = cdb.selectStatement(sqlStatement);
			
			try
			{
				while (rs1.next())
				{
					if (rs1.getInt("kaartID") == kaartID) 
					{
						kaartType = "Actiekaart";
						rs1.afterLast();
					}
				}
			}
			catch (Exception e)
			{	        
				System.err.println(e.getMessage());
			}
			if (!kaartType.contentEquals("")) cdb.sluitConnectie();
			else
			{
				sqlStatement = "SELECT * FROM Geldkaarten";
				ResultSet rs2 = cdb.selectStatement(sqlStatement);
				try
				{
					while (rs2.next())
					{
						if (rs2.getInt("kaartID") == kaartID) 
						{
							kaartType = "Geldkaart";
							rs2.afterLast();
						}
					}
				}
				catch (Exception e)
				{	        
					System.err.println(e.getMessage());
				}
				if (!kaartType.contentEquals("")) cdb.sluitConnectie();
				else kaartType = "Overwinningskaart";
			}
		}
		if (kaartType.contentEquals("")) System.err.println("Het kaartType kon niet gevonden worden, het opgegeven kaartID bestaat waarschijnlijk niet");
		return kaartType;
	}
	
	/*
	 * public static String getSoort(String kaartNaam)
	 * {
	 * 		Kaarten.getSoort(Kaarten.getID(kaartNaam));
	 * }
	 */
	
	public static int getBeschikbaarAantal(int kaartID)
	{
		int aantal = -1;
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM Kaarten WHERE kaartID = " + kaartID;
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		try
		{
			if(rs.first()) aantal = rs.getInt("aantalBeschikbaar");
		}
		catch (Exception e)
		{	        
			System.err.println(e.getMessage());
		}
		cdb.sluitConnectie();
		if (aantal == -1) System.err.println("Het beschikbaaraantal kon niet gevonden worden, Het opgegeven kaartID bestaat waarschijnlijk niet");
		return aantal;
	}
	
	//		MAIN
	public static void main(String [ ] args)
	{
		System.out.println(Kaarten.getNaam(2));
		System.out.println("KaartID 2: (moet lukken) " + Kaarten.getID("Bureaucraat"));
		System.out.println(Kaarten.getKost(1000));
		System.out.println("Moet Vloekkaart zijn (26): " + Kaarten.getSoort(26));
		System.out.println("Moet Actiekaart zijn (1): " + Kaarten.getSoort(1));
		System.out.println("Moet Actiekaart zijn (24): " + Kaarten.getSoort(24));
		System.out.println("Moet Actiekaart zijn (13): " + Kaarten.getSoort(13));
		System.out.println("Moet Victory zijn (27): " + Kaarten.getSoort(27));
		System.out.println("Moet Victory zijn (25): " + Kaarten.getSoort(25));
		System.out.println("Moet Victory zijn (29): " + Kaarten.getSoort(29));
		System.out.println("Moet geldkaart zijn (30): " + Kaarten.getSoort(30));
		System.out.println("Moet Actiekaart zijn (31): " + Kaarten.getSoort(31));
		System.out.println("Moet Actiekaart zijn (32): " + Kaarten.getSoort(32));
		System.out.println("Beschikbaar aantal van kaart 30 (moet 60 zijn): " + Kaarten.getBeschikbaarAantal(30));
	}

}
