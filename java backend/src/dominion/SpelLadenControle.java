package dominion;

import java.sql.ResultSet;

public class SpelLadenControle
{
	public static boolean isGeldigSpel(int spelID)
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM spellen WHERE spelID = " + spelID;
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		boolean isBe�indigd = true;
		boolean bestaat = false;
		
		try 
		{
			while (rs.next()) 
			{
				isBe�indigd = rs.getBoolean("be�indigd");
				bestaat = true;
			}
		} 
		catch (Exception e) 
		{
				System.out.println(e.getMessage());
		} 
		cdb.sluitConnectie();
		
		if (bestaat && !isBe�indigd)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}

