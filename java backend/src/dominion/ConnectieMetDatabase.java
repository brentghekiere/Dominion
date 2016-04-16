package dominion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectieMetDatabase {
	private Connection dbConnection;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	public ConnectieMetDatabase(String DB_NAAM)
	{
		String DB_CONNECTION = "jdbc:mysql://localhost:3307/" + DB_NAAM;
		dbConnection = null;

		try
		{
			//init
			dbConnection = DriverManager.getConnection(DB_CONNECTION, "root" , "Dominion");
		} catch (Exception e) {
			e.getMessage();
		}
		try{
			if (!dbConnection.isClosed()){
			}}catch (Exception e){
				System.out.println("fout bij het openen van de connectie");
			}
	}
	
	public void sluitConnectie()
	{
		try
		{
			if (ps != null) 
			{
				ps.close();
			}
			if (rs != null) 
			{
				rs.close();
			}
			if (dbConnection != null) 
			{
				dbConnection.close();
			}
		} catch (Exception e)
		{
			System.out.println("Fout bij sluiten connectie");
		}
	}

	public ResultSet selectStatement(String SQLStatement)
	{
		try{
		ps = dbConnection.prepareStatement(SQLStatement);
		
		rs = ps.executeQuery();
		
		} catch (Exception e) 
		{
			System.out.println(e.getMessage());
		} 

		return rs;
	}
	
	private void nonSelectStatement(String SQLStatement)
	{
		try{
			ps = dbConnection.prepareStatement(SQLStatement);
			
			ps.executeUpdate();
			
		} catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void updateStatement(String SQLStatement)
	{
		nonSelectStatement(SQLStatement);
	}
	
	public void insertStatement(String SQLStatement)
	{
		nonSelectStatement(SQLStatement);
	}
	
	public void deleteStatement(String SQLStatement)
	{
		nonSelectStatement(SQLStatement);
	}
	
	public static void main(String[] args)
	{
		// later wegdoen, dit is er om jullie te helpen de klasse te snappen
		// als je in het project meerdere malen cdb gebruikt geef deze steeds dezelfde naam (cdb) hier kon dit niet
		// wegens errors 
		
		// select statement uitvoeren
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement1 = "SELECT * from spellen";
		ResultSet rs = cdb.selectStatement(sqlStatement1);
		try
			{
				while (rs.next())
				{
					int spelID = rs.getInt("spelID");
					System.out.println(spelID);
				}
			}catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		cdb.sluitConnectie();
				
		// insert statement uitvoeren
		ConnectieMetDatabase cdb2 = new ConnectieMetDatabase("dominion_db");
		String sqlStatement2 = "INSERT INTO spellen(beëindigd) VALUES (0)";
		cdb2.insertStatement(sqlStatement2);
		cdb2.sluitConnectie();

		// delete statement uitvoeren
		ConnectieMetDatabase cdb3 = new ConnectieMetDatabase("dominion_db");
		String sqlStatement3 = "DELETE FROM spellen WHERE spelID = 5";
		cdb3.deleteStatement(sqlStatement3);
		cdb3.sluitConnectie();
	}	
}	
