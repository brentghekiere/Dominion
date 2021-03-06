package dominion;

import java.sql.ResultSet;

public class SpelLadenControle
{
	public static boolean isGeldigSpel(int spelID)
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement = "SELECT * FROM spellen WHERE spelID = " + spelID;
		ResultSet rs = cdb.selectStatement(sqlStatement);
		
		boolean isBeŽindigd = true;
		boolean bestaat = false;
		
		try 
		{
			while (rs.next()) 
			{
				isBeŽindigd = rs.getBoolean("beŽindigd");
				bestaat = true;
			}
		} 
		catch (Exception e) 
		{
				System.out.println(e.getMessage());
		} 
		cdb.sluitConnectie();
		
		if (bestaat && !isBeŽindigd)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}

