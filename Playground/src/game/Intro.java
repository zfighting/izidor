package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

// a játék indításakor elinduló animáció osztálya
public class Intro extends GameState
{
	// a kiírásokhoz használt fontok
	private Font statusFont;
	private Font textFont;
	
	// a statusbarra kiírt szöveg
	private String statusString = "Bevezető átugrása: esc vagy enter.";
	
	// az intro szövege
	private String introText[] = new String[]
	{
		"A FAILWAGON bemutatja:          Izidor Kalandjai",
		"     ",
		"     ",
		"IIT országban a szigorú Elzé király uralkodik.",
		"Az ő lánya Tektónia, a nagy arcú álomtitkár.", 
		"     ",
		"Te pedig Izidor vagy, a junior furga, akinek nagyon ",
		"megtetszett Tektónia interfésze.",
		"Hogy megszerezd a kezét, ki kell állnod Elzé király próbáját: ",
		"a kulcsokat összeszedve kijutni a labirintusból.",
		"Hű testvéred, Mortimer lesz a segítőd e merész vállalkozás során."
	};
	
	// eddig a karakterig lehet megjeleníteni a szöveget
	int lineIndex;
	float characterIndex;
	

	// konstruktor
	Intro(Game game)
	{
		super(game);
		
		// betütípusok létrehozása
		statusFont = new Font("Calibri", Font.BOLD, 24);
		textFont = new Font("Calibri", Font.BOLD, 26);
	}
	
	// ebbe a módba váltáskor lefutó inicializálás
	@Override
	public void initState()
	{
		lineIndex = 0;
		characterIndex = 0.f;
	}

	@Override
	public void handleInput()
	{
		boolean[] keys = game.getKeys();
		
		// Enterre vagy Esc-re átugrunk a főenübe
		if( keys[KeyEvent.VK_ENTER] || keys[KeyEvent.VK_ESCAPE] )
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
		if( characterIndex > introText[lineIndex].length() - 1 )
		{
			// akkor a következő sor elejére ugrunk
			lineIndex++;
			characterIndex = 0;
			
			// ha elfogytak a sorok
			if( lineIndex > introText.length - 1 )
			{
				// mindent ki lehet írni rendesen
				lineIndex = introText.length - 1;
				characterIndex = introText[lineIndex].length() - 1;
				
				// és még a statusbar szövege is megváltozik
				statusString = "Ha készen állsz a küldetésre, üss entert!";
			}
		}
	}

	// renderelés
	@Override
	public void render(Graphics2D g)
	{
		// intro szövegének kiírása
		g.setPaint(Color.black);
		g.setFont(textFont);
		
		// a már teljesen kész sorok kiírása egyben
		for( int line = 0; line < lineIndex; line++ )
		{
			g.drawString(introText[line], 10, 50 + line * 45);
		}
		// a még nem teljesen kész sor kiírása a legutolsó teljesen kész karakterig
		g.drawString(introText[lineIndex].substring(0, (int)characterIndex + 1), 10, 50 + lineIndex * 45);
		
		// ha még nincs kiírva a teljes intro szöveg, akkor van áttetsző karakter
		if( !( (lineIndex == introText.length - 1) && (characterIndex == introText[introText.length-1].length() - 1) ) )
		{
			// megfelelően áttetsző szín létrehozása az utolsó, nem teljesen kész karakterhez
			Color c = new Color(0, 0, 0, characterIndex - (int)characterIndex);
			// a már kiírt félkész sor, és a kiírandó karakter szélességének lekérdezése a félkész karakter x koordinátájának meghatározásához
			FontMetrics metrics = g.getFontMetrics();
			Rectangle2D line_rect = metrics.getStringBounds(introText[lineIndex].substring(0, (int)characterIndex + 2), g);
			Rectangle2D letter_rect = metrics.getStringBounds(introText[lineIndex].substring((int)characterIndex + 1, (int)characterIndex+2), g);
			g.setPaint(c);			
			g.drawString(introText[lineIndex].substring((int)characterIndex + 1, (int)characterIndex+2), 10 + (int)(line_rect.getWidth() - letter_rect.getWidth()), 50 + (lineIndex) * 45);
		}
		
		// statusbar string kiírása
		g.setPaint(Color.black);
		g.setFont(statusFont);
		g.drawString(statusString, 10, 585);

	}

}
