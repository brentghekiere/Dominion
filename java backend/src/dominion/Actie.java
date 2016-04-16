package dominion;

import java.sql.ResultSet;

public class Actie
{
	private int resterendeActies;
	private int resterendeAanschaffen;
	private char[] actiesVoorJezelf  = new char[10];

	private char[] actiesVoorTegenstanders  = new char[10];
	
	public Actie(int koninkrijkID, int restActies, int restAanschaffen)
	{
		resterendeActies = restActies -1;
		resterendeAanschaffen = restAanschaffen;
		// getHandelingen (actieID & aantal) bij deze kaart
		actiesVoorJezelf = getHandelingenBijDezeKaart(koninkrijkID, true);
		actiesVoorTegenstanders = getHandelingenBijDezeKaart(koninkrijkID, false);
	}
	
	public void verhoogRestAanschaffenMet(int getal)
	{
		resterendeAanschaffen += getal;
	}
	
	public void verhoogRestActiesMet(int getal)
	{
		resterendeActies += getal;
		
	}
	public char[] getHandelingenBijDezeKaart(int kaartID, boolean voorJezelf)
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM koninkrijken_acties WHERE koninkrijkID = " + kaartID + " AND voorJezelf =  " + voorJezelf;
		ResultSet rs = cdb.selectStatement(sqlStatement);
		char acties[] = new char[10];
		int actieID;
		String aantal;
		
		try
		{
			while (rs.next())
			{
				actieID = rs.getInt("actieID");
				aantal = rs.getString("aantal");
				for (int i=0;i<acties.length;i++)
				{
					acties[actieID-1] = aantal.charAt(0);
				}
			}
		} catch (Exception e)
		{	                    
			System.out.println(e.getMessage());
		}
		cdb.sluitConnectie();
		
		return acties;
		
	}
	
	public int getRestActies()
	{
		return resterendeActies;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Actie doe = new Actie(2,1,1);

	}

}
