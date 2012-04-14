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
		
	// konstruktor
	public RenderSurface(Game game, int width, int height)
	{
		// default layoutmanager és engedélyezett dupla pufferelés
		super(null, true);

		// játék referencia eltárolása
		this.game = game;

		// méret beállítása
		this.setPreferredSize(new Dimension(width, height));
	}
	

	// renderelés
	@Override
    public void paintComponent(Graphics g)
	{
		// törlés
		super.paintComponent(g);
		
		// Graphics2D objektum létrehozása
		Graphics2D g2d = (Graphics2D) g;
		
		// visszaadja a Game osztálynak, hpgy az adja tovább az aktuális játékállapotnak
		game.render(g2d);
    }
}
