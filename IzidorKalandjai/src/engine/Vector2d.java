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
}
