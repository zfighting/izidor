package game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import engine.Vector2d;

// osztály, ami lehetővé teszi hogy rajzoljunk rá a paintComponent felüldefiniálásával
public class RenderSurface extends JPanel
{
	// referencia a játék objektumra
	private Game game;
	
	// játéktér hátterének képe
	private RectangularRenderableGameObject backgroundSprite;
		
	// alsó statusbar képe
	private RectangularRenderableGameObject statusBarSprite;


	// konstruktor
	public RenderSurface(Game game, int width, int height)
	{
		// default layoutmanager és engedélyezett dupla pufferelés
		super(null, true);

		// játék referencia eltárolása
		this.game = game;

		// méret beállítása
		this.setPreferredSize(new Dimension(width, height));
		
		// képek létrehozása, betöltése
		try
		{
			// háttér létrehozása
			File f = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "backgroundSprite.png");
			BufferedImage bi;
			bi = ImageIO.read(f);
			TexturePaint background_paint = new TexturePaint(bi, new Rectangle2D.Float(0, 0, bi.getWidth(), bi.getHeight()));
			backgroundSprite = new RectangularRenderableGameObject((byte)1, new Vector2d(0, 0), background_paint, 800, 600);
			
			// statusbar képének létrehozása
			GradientPaint statusbar_paint = new GradientPaint(0, 0, new Color(0, 0, 0, 0), 0, 50, new Color(70, 70, 70), false);
			statusBarSprite = new RectangularRenderableGameObject((byte)1, new Vector2d(0, 550), statusbar_paint, 800, 50);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	

	// renderelés
	@Override
    public void paintComponent(Graphics g)
	{
		// törlés
		super.paintComponent(g);
		
		// Graphics2D objektum létrehozása
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// a minden egyes játékállapotban szükséges elemeket itt rajzoljuk ki: ilyen a háttér és a statusbar
		
		// háttér kirajzolása
		backgroundSprite.render(g2d);
		
		// statusbar kirajzolása
		statusBarSprite.render(g2d);
		
		// visszaadja a Game osztálynak, hpgy az adja tovább az aktuális játékállapotnak
		game.render(g2d);
    }
}
