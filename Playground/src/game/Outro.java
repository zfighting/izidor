package game;


import engine.Vector2d;
import game.GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

// a játék indításakor elinduló animáció osztálya
public class Outro extends GameState
{
	// a kiírásokhoz használt fontok
	private Font statusFont;
	private Font textFont;
	
	// a statusbarra kiírt szöveg
	private String statusString = "Visszalépés a főmenübe: esc.";
	
	// az intro szövege
	private String outroText[] = new String[]
	{
		"Gratulálunk! A küldetést teljesítetted!",
		"     ",
		"     ",
		"Megbírkóztál a feladattal, így méltóvá váltál Tektónia kezére.",
		"Szerencsére Elzé király állja a szavát,", 
		"tehát megkapod, amiért oly sokat küzdöttél!"
	};
	
	// eddig a karakterig lehet megjeleníteni a szöveget
	int lineIndex;
	float characterIndex;
	
	// meg kell-e jeleníteni a képet alul
	boolean showImage;
	
	// az alsó kép
	private RectangularRenderableGameObject endSprite;
	

	// konstruktor
	Outro(Game game)
	{
		super(game);
		
		// betütípusok létrehozása
		statusFont = new Font("Calibri", Font.BOLD, 24);
		textFont = new Font("Calibri", Font.BOLD, 26);
		
		// képek betöltése, sprite-ok létrehozása
		try
		{
			// főcím képének betöltése, elhelyezése...
			File f = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "endSprite.png");
			BufferedImage bi;
			bi = ImageIO.read(f);
			TexturePaint tp = new TexturePaint(bi, new Rectangle2D.Float(0, 0, bi.getWidth(), bi.getHeight()));
			
			endSprite = new RectangularRenderableGameObject((byte)1, new Vector2d(400 - bi.getWidth() / 2.f, 550 - bi.getHeight()), tp, bi.getWidth(), bi.getHeight());
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	// ebbe a módba váltáskor lefutó inicializálás
	@Override
	public void initState()
	{
		lineIndex = 0;
		characterIndex = 0.f;
		showImage = false;
	}

	@Override
	public void handleInput()
	{
		boolean[] keys = game.getKeys();
		
		// Enterre vagy Esc-re átugrunk a főenübe
		if( keys[KeyEvent.VK_ESCAPE] )
		{
			game.switchGameMode(GameStates.MAINMENU);
		}
	}

	@Override
	public void update()
	{
		// növeljük a karakterszámlálót
		characterIndex += 0.2f;
		// ha elértük a sor végét
		if( characterIndex > outroText[lineIndex].length() - 1 )
		{
			// akkor a következő sor elejére ugrunk
			lineIndex++;
			characterIndex = 0;
			
			// ha elfogytak a sorok
			if( lineIndex > outroText.length - 1 )
			{
				// mindent ki lehet írni rendesen
				lineIndex = outroText.length - 1;
				characterIndex = outroText[lineIndex].length() - 1;
				
				// és megjelenik a kép
				showImage = true;
			}
		}
	}

	// renderelés
	@Override
	public void render(Graphics2D g)
	{
		// outro szövegének kiírása
		g.setPaint(Color.black);
		g.setFont(textFont);
		
		// a már teljesen kész sorok kiírása egyben
		for( int line = 0; line < lineIndex; line++ )
		{
			g.drawString(outroText[line], 10, 50 + line * 45);
		}
		// a még nem teljesen kész sor kiírása a legutolsó teljesen kész karakterig
		g.drawString(outroText[lineIndex].substring(0, (int)characterIndex + 1), 10, 50 + lineIndex * 45);
		
		// ha még nincs kiírva a teljes intro szöveg, akkor van áttetsző karakter
		if( !( (lineIndex == outroText.length - 1) && (characterIndex == outroText[outroText.length-1].length() - 1) ) )
		{
			// megfelelően áttetsző szín létrehozása az utolsó, nem teljesen kész karakterhez
			Color c = new Color(0, 0, 0, characterIndex - (int)characterIndex);
			// a már kiírt félkész sor, és a kiírandó karakter szélességének lekérdezése a félkész karakter x koordinátájának meghatározásához
			FontMetrics metrics = g.getFontMetrics();
			Rectangle2D line_rect = metrics.getStringBounds(outroText[lineIndex].substring(0, (int)characterIndex + 2), g);
			Rectangle2D letter_rect = metrics.getStringBounds(outroText[lineIndex].substring((int)characterIndex + 1, (int)characterIndex+2), g);
			g.setPaint(c);			
			g.drawString(outroText[lineIndex].substring((int)characterIndex + 1, (int)characterIndex+2), 10 + (int)(line_rect.getWidth() - letter_rect.getWidth()), 50 + (lineIndex) * 45);
		}
		
		// statusbar string kiírása
		g.setPaint(Color.black);
		g.setFont(statusFont);
		g.drawString(statusString, 10, 585);
		
		
		// alsó kép megjelenítése, ha kell
		if( showImage )
		{
			endSprite.render(g);
		}
	}
}
