package dominion;

public class Dominion {
	
	public Dominion()
	{
		// startscherm met knoppen inloggen, registreren, ... en keuze tussen oud spel laden of nieuw spel starten
		// stel dat er wordt gekozen om een nieuw spel te starten met 2 spelers:
		Spel huidigSpel = new Spel(2, true);
		
		// stel dat er wordt gekozen om oud spel 2 in te laden:
		//Spel huidigSpel = new Spel(2);
	}

	public static void main(String[] args)
	{
		Dominion dm = new Dominion();
	}
}