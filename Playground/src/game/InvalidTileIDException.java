package game;

// érvénytelen tile-azonosítót jelentő kivétel
public class InvalidTileIDException extends Exception
{
	// az érvénytelen azonosító, ami a kivételt okozta
	protected byte invalidTileID;
	
	
	// konstruktor
	public InvalidTileIDException(byte tileID)
	{
		super("Invalid tileID: " + tileID);
		this.invalidTileID = tileID;
	}
	
	// érvénytelen azonosító lekérdezése
	public byte getInvalidTileID()
	{
		return invalidTileID;
	}
}
