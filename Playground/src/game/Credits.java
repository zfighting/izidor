package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Credits extends GameState
{
	// a kiírásokhoz használt fontok
	private Font statusFont;
	private Font nfoFont;
	
	// nfo szöveget tároló Stringek
	ArrayList<String> nfoText;
	
	// eddig a karakterig lehet megjeleníteni a szöveget
	int lineIndex;
	float characterIndex;
	
	// a szinuszos hullámzás paramétere
	double sin_offset;

	
	// konstruktor
	Credits(Game game)
	{
		super(game);
		
		// nfo szöveg tárolására alkalmas lista létrehozása
		nfoText = new ArrayList<String>();
		
		// betütípusok létrehozása
		statusFont = new Font("Calibri", Font.BOLD, 24);
		nfoFont = new Font("Courier New", Font.BOLD, 14);
	}
	
	// ebbe a módba váltáskor lefutó inicializálás
	@Override
	public void initState()
	{
		// nfo beolvasása
		try
		{
			// eddigi szövegek törlése
			nfoText.clear();
		    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "nfo"), "UTF8"));
		    String str;
		    while( (str = in.readLine()) != null )
		    {
		        nfoText.add(str);
		    }
		    in.close();		    
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		lineIndex = 0;
		characterIndex = 0;
		sin_offset = 0;
	}

	// billentyűzetkezelés
	@Override
	public void handleInput()
	{
		boolean[] keys = game.getKeys();
		
		// esc-re lehet kilépni
		if( keys[KeyEvent.VK_ESCAPE] )
		{
			game.switchGameMode(GameStates.MAINMENU);
		}
	}

	@Override
	public void update()
	{		
		// növeljük a karakterszámlálót
		characterIndex += 0.4f;
		// ha elértük a sor végét
		if( characterIndex > nfoText.get(lineIndex).length() - 1 )
		{
			// akkor a következő sor elejére ugrunk
			lineIndex++;
			characterIndex = 0;
			
			// ha elfogytak a sorok
			if( lineIndex > nfoText.size() - 1 )
			{
				// mindent ki lehet írni rendesen
				lineIndex = nfoText.size() - 1;
				characterIndex = nfoText.get(lineIndex).length() - 1;
			}
		}
		
		// hogy a rengeteg szóköz miatt ne teljen túl sok időbe a szöveg kiírása, átugrojuk ezeket
		while( nfoText.get(lineIndex).charAt((int)characterIndex) == ' ' )
		{
			characterIndex++;
		}
		
		// a szinuszos hullámzás eléréséhez növeljük az eltolást
		sin_offset += 0.06;
	}

	// renderelés
	@Override
	public void render(Graphics2D g)
	{	
		// nfo kiírása
		g.setPaint(Color.black);
		g.setFont(nfoFont);
		FontMetrics metrics = g.getFontMetrics();
		Rectangle2D rect = metrics.getStringBounds(nfoText.get(0).substring(0, 1), g);
		
		// a sorok képernyő közepére igazításához használt eltolás
		double x_offset = 400 - (rect.getWidth() * nfoText.get(0).length() / 2);
		
		// szöveg kiírása karakterenként
		for( int line = 0; line <= lineIndex; line++ )
		{
			// ha az adott sor kész van, akkor végig mehetünk rajta, különben csak a charatcerIndex-edik karakterig
			int last_char_index = (line < lineIndex) ? (nfoText.get(line).length() - 1) : ((int)characterIndex);
			
			// az adott sor karaktereinek kiírása
			for( int character = 0; character <= last_char_index; character++ )
			{
				double pos_x = x_offset + character * rect.getWidth();
				double pos_y = 50 + line * rect.getHeight();
				double sin_offset_y = Math.sin((pos_x / 800.0 * Math.PI * 2) + sin_offset) * 10;
				
				g.drawChars(nfoText.get(line).toCharArray(), character, 1, (int)(pos_x), (int)(pos_y + sin_offset_y));
			}
		}
		
		// statusbar string kiírása
		g.setPaint(Color.black);
		g.setFont(statusFont);
		g.drawString("Visszalépés a főmenübe: esc.", 10, 585);
	}
}
