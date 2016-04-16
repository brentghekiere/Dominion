package dominion;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Collections;

public class SpelerDeck
{
	private LinkedList<Integer> trek = new LinkedList<Integer>();
	private LinkedList<Integer> inHand = new LinkedList<Integer>();
	private LinkedList<Integer> opTafel = new LinkedList<Integer>();
	private LinkedList<Integer> afleg = new LinkedList<Integer>();
	
	private int spel_spelerID;
	
	//		CONSTRUCTOR SPELERDECK OPBOUWEN
	public SpelerDeck(int spel_spelerID)
	{
		this.spel_spelerID = spel_spelerID;
		
		trekstapelVullenMetKaartenVanSpeler();
		opschonen(); // neem 5 kaarten in je hand
	}
	
	//		PRIVATE METHODS
	private void trekstapelVullenMetKaartenVanSpeler()
	{
		ConnectieMetDatabase cdb = new ConnectieMetDatabase("dominion_db");
		String sqlStatement1 = "SELECT * FROM spellen_spelers_kaarten WHERE spel_spelerID = " + spel_spelerID;
		ResultSet rs = cdb.selectStatement(sqlStatement1);
		try
		{
			while (rs.next())
			{
				int kaartID = rs.getInt("kaartID");
				int aantal = rs.getInt("aantal");
				for (int i = 0; i < aantal; i++)
				{
					afleg.add(kaartID);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		cdb.sluitConnectie();
		
		schudAflegstapelEnLegOnderTrekstapel();
	}
	
	//		PUBLIC METHODS
	public void schudAflegstapelEnLegOnderTrekstapel()
	{
		Collections.shuffle(afleg);
		int aantal = afleg.size();
		for (int i = aantal-1; i >= 0; i--)
		{
			trek.add(afleg.get(i));
			afleg.remove(i);
		}
	}
	
	public int getKaartVanTrekstapel(int positie) // handig voor actiekaart waar je deze mag bekijken en terugleggen
	{
		return trek.get(positie);
	}
	
	public boolean kaartInHand(int kaartID) // handig voor actiekaart waar je deze mag bekijken en terugleggen
	{
		int aantal = inHand.size();
		for (int i = 0; i < aantal; i++)
		{
			if(inHand.get(i) == kaartID)
			{
				return true;
			}
		}
		return false;
	}
	
	public int aantalKaartenVanKaartsoortInHand(String kaartsoort)
	{
		int aantalKaartenVanSoort = 0;
		int aantalKaartenInHand = inHand.size();
		for (int i = 0; i < aantalKaartenInHand; i++)
		{
			if(Kaarten.getSoort(inHand.get(i)) == kaartsoort)
			{
				aantalKaartenVanSoort++;
			}
		}
		return aantalKaartenVanSoort;
	}
	
	public int[] getIDsVanKaartenVanKaartsoortInHand(String kaartsoort)
	{
		int aantalKaartenInHand = inHand.size();
		int aantalKaartenVanKaartsoortInHand = aantalKaartenVanKaartsoortInHand(kaartsoort);
		int[] arrayMetIDsVanKaartenVanKaartsoortInHand = new int[aantalKaartenVanKaartsoortInHand];
		int j = 0;
		for (int i = aantalKaartenInHand-1; i >= 0; i--)
		{
			if(Kaarten.getSoort(inHand.get(i)) == kaartsoort)
			{
				arrayMetIDsVanKaartenVanKaartsoortInHand[j] = inHand.get(i);
				j++;
			}
		}
		return arrayMetIDsVanKaartenVanKaartsoortInHand;
	}
	
	
	
	
	// BASIS SPELHANDELINGEN
	public void trekKaart() // bovenste kaart van trekstapel in hand nemen
	{
		// TODO: als de trekstapel leeg is aflegstapel schudden en eronder leggen
		
		inHand.add(trek.get(0));
		trek.remove(0);
		
	}

	public void trekKaart(int kaartID) // (method overloading) als kaart niet van kaartID is, leg hem op afleg en trek er nog één
	{
		while (trek.get(0) != kaartID)
		{
			afleg.add(trek.get(0));
			trek.remove(0);
		}
		if (trek.get(0) == kaartID)
		{
			inHand.add(trek.get(0));
			trek.remove(0);
		}
	}

	public void legKaartAf(int kaartID) // een bepaalde kaart uit je hand op de aflegstapel leggen
	{
		int aantal = inHand.size();
		for (int i = aantal-1; i >= 0; i--)
		{
			if(inHand.get(i) == kaartID)
			{
				afleg.add(inHand.get(i));
				inHand.remove(i);
				i = 0; // uit de loop gaan, de kaart is afgelegd
			}
		}
		int nieuwAantal = inHand.size();
		if(aantal != nieuwAantal)
		{
			System.err.println("Je hebt deze kaart niet en kan deze dus niet afleggen!");
		}
	}

	public void speelKaart(int kaartID) // een bepaalde kaart uit je hand op de tafel leggen
	{
		int aantal = inHand.size();
		for (int i = aantal-1; i >= 0; i--)
		{
			if(inHand.get(i) == kaartID)
			{
				opTafel.add(inHand.get(i));
				inHand.remove(i);
				i = 0; // uit de loop gaan, de kaart is gespeeld
				if(Kaarten.getSoort(kaartID) == "Actiekaart")
				{
					
					// doeActies(this, kaartID);
				}
			}
		}
		int nieuwAantal = inHand.size();
		if(aantal == nieuwAantal)
		{
			System.err.println("Je hebt deze kaart niet en kan deze dus niet spelen!");
		}
	}
	
	public void opschonen() // alle kaarten op tafel en in hand op aflegstapel leggen + 5 nieuwe kaarten trekken
	{
		int aantalInHand = inHand.size();
		for (int i = aantalInHand-1; i >= 0; i--)
		{
			afleg.add(inHand.get(i));
			inHand.remove(i);
		}
		int aantalOpTafel = opTafel.size();
		for (int i = aantalOpTafel-1; i >= 0; i--)
		{
			afleg.add(opTafel.get(i));
			opTafel.remove(i);
		}
		for(int i = 0; i < 5; i++) // neem 5 nieuwe kaarten in je hand
		{
			trekKaart();
		}
	}

	
	
	
	// EXTRA METHODS VOOR ACTIEKAARTEN
	public void verwijderKaartUitHand(int kaartID)
	{
		int aantal = inHand.size();
		for (int i = aantal-1; i >= 0; i--)
		{
			if(inHand.get(i) == kaartID)
			{
				inHand.remove(i);
				i = 0; // uit de loop gaan, de kaart is verwijderd
			}
		}
		int nieuwAantal = inHand.size();
		if(aantal != nieuwAantal)
		{
			System.err.println("Je hebt deze kaart niet!");
		}
	}
	
	public void legNieuweKaartBovenaanOpTrekstapel(int kaartID)
	{
		trek.add(kaartID);
	}
	
	public void voegNieuweKaartToeAanHand(int kaartID)
	{
		inHand.add(kaartID);
	}
	
	public void legNieuweKaartOpAflegstapel(int kaartID) // nieuwe kaart op aflegstapel leggen
	{
		afleg.add(kaartID);
	}
	
	
	
	
	//		MAIN
	public static void main(String[] args)
	{
		// nieuwe spelerdeck maken en kijken hoeveel kaarten waar zitten en welke
		SpelerDeck sd = new SpelerDeck(1);
		System.out.println("Grootte aflegstapel: " + sd.afleg.size());
		System.out.println("Grootte hand: " + sd.inHand.size());
		System.out.println("Grootte trekstapel: " + sd.trek.size());
		System.out.println("Aantal kaarten op tafel: " + sd.opTafel.size());
				
		System.out.println("Kaarten in trekstapel (van voor naar achter):");
		for(int i = 0; i < 5; i++)
		{
			System.out.println(sd.trek.get(i));
		}
		System.out.println("Kaarten in hand:");
		for(int i = 0; i < 5; i++)
		{
			System.out.println(sd.inHand.get(i));
		}
		
		System.out.println("");
		// koperen geldkaart spelen
		System.out.println("SPEEL KOPEREN GELDKAART");
		sd.speelKaart(30);
		System.out.println("Grootte aflegstapel: " + sd.afleg.size());
		System.out.println("Grootte hand: " + sd.inHand.size());
		System.out.println("Grootte trekstapel: " + sd.trek.size());
		System.out.println("Aantal kaarten op tafel: " + sd.opTafel.size());
		
		System.out.println("Kaarten in hand:");
		for(int i = 0; i < 4; i++)
		{
			System.out.println(sd.inHand.get(i));
		}
		
		System.out.println("Kaarten op tafel:");
		for(int i = 0; i < 1; i++)
		{
			System.out.println(sd.opTafel.get(i));
		}
		
		System.out.println("");
		// kijk hoeveel overwinningskaarten er in de hand zitten
		System.out.println("HOEVEEL OVERWINNINGSKAARTEN HEB IK IN MIJN HAND?");
		System.out.println(sd.aantalKaartenVanKaartsoortInHand("Overwinningskaart"));
		// welke zijn het?
		System.out.println("WELKE ZIJN HET? (ID + NAAM)");
		for(int i = 0; i < sd.aantalKaartenVanKaartsoortInHand("Overwinningskaart"); i++)
		{
			System.out.println(sd.getIDsVanKaartenVanKaartsoortInHand("Overwinningskaart")[i] + ": " + Kaarten.getNaam(sd.getIDsVanKaartenVanKaartsoortInHand("Overwinningskaart")[i]));
		}
		
		System.out.println("");
		// nieuwe avonturierkaart toevoegen aan aflegstapel
		System.out.println("AVONTURIERKAART GEKOCHT ==> TOEVOEGEN AAN AFLEGSTAPEL");
		sd.legNieuweKaartOpAflegstapel(1);
		System.out.println("Grootte aflegstapel: " + sd.afleg.size());
		System.out.println("Grootte hand: " + sd.inHand.size());
		System.out.println("Grootte trekstapel: " + sd.trek.size());
		System.out.println("Aantal kaarten op tafel: " + sd.opTafel.size());
		
		System.out.println("");
		// kaarten van op tafel en uit hand in aflegstapel doen en kijken hoeveel kaarten waar zitten en welke
		System.out.println("OPSCHONEN");
		sd.opschonen();
		System.out.println("Grootte aflegstapel: " + sd.afleg.size());
		System.out.println("Grootte hand: " + sd.inHand.size());
		System.out.println("Grootte trekstapel: " + sd.trek.size());
		System.out.println("Aantal kaarten op tafel: " + sd.opTafel.size());
		
		System.out.println("Kaarten in aflegstapel (van voor naar achter):");
		for(int i = 0; i < 6; i++)
		{
			System.out.println(sd.afleg.get(i));
		}
		System.out.println("Kaarten in hand:");
		for(int i = 0; i < 5; i++)
		{
			System.out.println(sd.inHand.get(i));
		}
	}	
}
