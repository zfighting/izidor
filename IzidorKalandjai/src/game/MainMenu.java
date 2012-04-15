package game;

import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;

import engine.Vector2d;


// a főmenü játékállapot osztálya
public class MainMenu extends GameState
{
	// játék címe
	private RectangularRenderableGameObject titleSprite;
	
	
	// konstruktor
	MainMenu(Game game)
	{
		super(game);
		
		try
		{
			File f = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "texture.bmp");
			BufferedImage bi;
			bi = ImageIO.read(f);
			TexturePaint tp = new TexturePaint(bi, new Rectangle2D.Float(0, 0, bi.getWidth(), bi.getHeight()));
			
			titleSprite = new RectangularRenderableGameObject((byte)1, new Vector2d(0, 0), tp, 200.0f, 100.0f);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public void handleInput()
	{
		if( game.getKeys()[KeyEvent.VK_DOWN] == true )
		{
			titleSprite.position.y += 0.5f;
		}
		
		// enterre lépjünk át gameplay módba
		if( game.getKeys()[KeyEvent.VK_ENTER] )
		{
			game.startGame("res" + File.separatorChar + "teststage2.xml");
		}
	}

	@Override
	public void update()
	{
		;
	}

	@Override
	public void render(Graphics2D g)
	{
		// játék címének kirajzolása
		titleSprite.render(g);
	}

}
