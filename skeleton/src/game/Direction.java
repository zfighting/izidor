package game;


// a pályát felépítő tile-ok felcserélésének iránya ezek közül kerülhet ki
// az irány a lenyomott billentyűt jelenti, tehát a NEM üres tile fog ebbe az irányba menni
public enum Direction
{
	// fel
	UP,
	// le
	DOWN,
	// balra
	LEFT,
	// jobbra
	RIGHT
}
