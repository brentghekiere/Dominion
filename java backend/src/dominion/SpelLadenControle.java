package dominion;

import java.sql.ResultSet;

public class SpelLadenControle
{
	public static boolean isGeldigSpel(int spelID)
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM spellen WHERE spelID = " + spelID;
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		boolean isBeëindigd = true;
		boolean bestaat = false;
		
		try 
		{
			while (rs.next()) 
			{
				isBeëindigd = rs.getBoolean("beëindigd");
				bestaat = true;
			}
		} 
		catch (Exception e) 
		{
				System.out.println(e.getMessage());
		} 
		cdb.sluitConnectie();
		
		if (bestaat && !isBeëindigd)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}

