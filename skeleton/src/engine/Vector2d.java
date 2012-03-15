package engine;

// 2 dimenziós vektor
public class Vector2d
{
	// koordináták
	public float x;
	public float y;
	
	
	// konstruktor
	public Vector2d(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	// konstruktor
	public Vector2d()
	{
		this.x = this.y = 0;
	}
	
	// vektor hossza
	public double getLength()
	{
		return Math.sqrt(x*x + y*y);
	}
	
	// segédfüggvény 2 vektor összeadásához
	public static Vector2d add(Vector2d a, Vector2d b)
	{
		return new Vector2d(a.x + b.x, a.y + b.y);
	}
	
	// segédfüggvény 2 vektor kivonásához (a - b)
	public static Vector2d subtract(Vector2d a, Vector2d b)
	{
		return new Vector2d(a.x - b.x, a.y - b.y);
	}
}
