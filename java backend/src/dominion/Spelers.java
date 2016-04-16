package dominion;

import java.sql.ResultSet;

public class Spelers {
	
	private int spelID;
	private int aantalSpelers;
	private String[] spelSpelers; // = Gebruikersnamen!!!!!!!!
	private int[] spelSpelersID; // = gebruikersID!!!!!! sorry :p
	private int[] spel_SpelerID;
	
	//		CONSTRUCTOR SPELERS TOEVOEGEN
	public Spelers(int aantal, int spelID) //TODO later na implementatie van gebruikers zeggen welke gebruiker
	{	
		this.spelID = spelID;
		for (int i=1; i <= aantal;i++)
		{
			insertSpelerInDB(i);
		}
		laadEnDeclareerStuff();
	}
	
	//		CONSTRUCTOR SPELERS LADEN
	public Spelers(int spelID)
	{
		this.spelID = spelID;
		laadEnDeclareerStuff();
	}
	
	//		PRIVATE
	private void insertSpelerInDB(int spelernr) //TODO: later na implementatie gebruikers zeggen welke gebruikers (adhv gebruikersID)
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String SQLStatement = "INSERT INTO spellen_spelers(spelID, gebruikerID, spelernummer, beurtnummer)VALUES (" + spelID + ", null," + spelernr + ",1)";
		cdb.insertStatement(SQLStatement);		
		cdb.sluitConnectie();
	}

	private void aantalSpelersLaden()
	{
		aantalSpelers = 0;
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM spellen_spelers where spelid = " + spelID;
		ResultSet rs = cdb.selectStatement(sqlStatement);;
		try 
		{	
			if (rs.last())
			{
				aantalSpelers = rs.getRow();
			}
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		} 
		cdb.sluitConnectie();
	}
	
	private String getGebruikersnaam(int gebruikersID)
	{
		String username = "";
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM gebruikers WHERE gebruikerID = " + gebruikersID;
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		try 
		{
			if (rs.first()) username = rs.getString("gebruikersnaam");
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		} 
		cdb.sluitConnectie();
		if (username != "")
			return username;
		else
			return "ERROR getGebruikersnaam werkt niet naar behoren";
	}
	
	private void laadEnDeclareerStuff()
	{
		aantalSpelersLaden(); //vanaf nu kan je de constante aantalspelers gebruiken...
		
		// declareer de arrays
		spel_SpelerID = new int[aantalSpelers];
		spelSpelers = new String[aantalSpelers];
		spelSpelersID = new int[aantalSpelers];
		
		// vul de arrays
		laadSpelers();
	}

	private void laadSpelers()
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM spellen_spelers WHERE spelID = " + spelID;
		ResultSet rs = cdb.selectStatement(sqlStatement);

		try 
		{
			int i = 0;
			while (rs.next())
			{
				
				int userID = rs.getInt("gebruikerID");
				if (userID == 0)
				{
					spelSpelers[i] = "Guest";
				}
				else
				{
					spelSpelers[i] = getGebruikersnaam(userID);
				}
				i++;
			}
			rs.beforeFirst();
			int b = 0;
			while (rs.next())
			{
				int userID = rs.getInt("gebruikerID");
				spelSpelersID[b] = userID; 
				b++;
			}
			rs.beforeFirst();
			int c = 0;
			while (rs.next())
			{
				int spelerID = rs.getInt("spel_spelerID");
				spel_SpelerID[c] = spelerID; 
				c++;
			}
			
		} 
		catch (Exception e) 
		{
				System.out.println(e.getMessage());
		} 
		cdb.sluitConnectie();
	}
	
	//		PUBLIC
	public String[] getGebruikersnamenInArray()
	{
		return spelSpelers;
	}
	
	public int[] getGebruikersIDsInArray()
	{
		return spelSpelersID;	
	}

	public int getAantalSpelers() 
	{
		return aantalSpelers;
	}
	
	public int[] getSpel_SpelerIDsInArray()
	{
		return spel_SpelerID;
	}
}