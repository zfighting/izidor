package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import engine.Vector2d;

// a főmenü játékállapot osztálya
public class MainMenu extends GameState
{
	// játék címe
	private RectangularRenderableGameObject titleSprite;
	
	// menüelemek szövegei
	private String menuItems[] = new String[]{ "Játék kezdése", "Zene kikapcsolása", "Készítők", "Kilépés" };
	
	// statusbaron megjelenő szövegek
	private String statusStrings[] = new String[]{ "Pálya kiválasztása: jobbra-balra nyilak, játék indítása: enter.", "Zeneszám váltása: jobbra-balra nyilak, zene ki/bekapcsolása: enter.", "Néhány hasznos és kevésbé hasznos információ a Failwagonról.", "Gyenge vagy? Feladod? KILÉPSZ?!" };
	
	// melyik menüelem az aktív kezdetben
	private int activeMenuItem = 0;
	
	// tudjuk-e a kijelölést mozgatni a menüelemeken (ahhoz kell, hogy lenyomva tartott gombok esetén is csak egyet lépjen)
	private boolean canMoveMenuItemSelection = false;
	
	// tudjuk-e a jobbra-balra nyilakkal mozgatni a kijelölést (zeneszám váltása / pálya váltása)
	private boolean canMoveSelection = false;
		
	// aktiválhat-e menüpontot az enter leütésével
	private boolean canActivateMenuItem = false;
	
	// a kiírásokhoz használt fontok
	private Font statusFont;
	private Font menuItemsFont;
	
	
	// konstruktor
	MainMenu(Game game)
	{
		super(game);		
		
		// betütípusok létrehozása
		statusFont = new Font("Calibri", Font.BOLD, 24);
		menuItemsFont = new Font("Calibri", Font.BOLD, 46);
		
		// képek betöltése, sprite-ok létrehozása
		try
		{
			// főcím képének betöltése, elhelyezése...
			File f = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "titleSprite.png");
			BufferedImage bi;
			bi = ImageIO.read(f);
			TexturePaint tp = new TexturePaint(bi, new Rectangle2D.Float(0, 0, bi.getWidth(), bi.getHeight()));
			
			titleSprite = new RectangularRenderableGameObject((byte)1, new Vector2d(400 - bi.getWidth() / 2.f, 0), tp, bi.getWidth(), bi.getHeight());
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
		// billentyűzetkezelés inicializálása
		// azért kell, hogy ha pl. lenyomott nyílbillentyűkkel érkezünk ebbe az állapotba,
		// akkor se legyen annak hatása, amíg fel nem engedik egyszer
		canMoveMenuItemSelection = false;
		canMoveSelection = false;
		canActivateMenuItem = false;
	}

	// billentyűzetkezelés
	@Override
	public void handleInput()
	{
		boolean[] keys = game.getKeys();
		
		// ha tudunk menüelemet váltani
		if( canMoveMenuItemSelection )
		{
			// lefele nyíl...
			if( keys[KeyEvent.VK_DOWN] )
			{
				activeMenuItem++;
				if( activeMenuItem == menuItems.length )
				{
					activeMenuItem = 0;
				}
				canMoveMenuItemSelection = false;
			}
			
			// felfele nyíl...
			if( keys[KeyEvent.VK_UP] )
			{
				activeMenuItem--;
				if( activeMenuItem < 0 )
				{
					activeMenuItem = menuItems.length - 1;
				}
				canMoveMenuItemSelection = false;
			}
		}
		// ha nem, de felengedték a gombokat, akkor már igen
		else if( !keys[KeyEvent.VK_UP] && !keys[KeyEvent.VK_DOWN] )
		{
			canMoveMenuItemSelection = true;
		}
		
		// ha tudunk jobbra-balra gombokkal váltogatni
		if( canMoveSelection )
		{
			// ha az első menüelem aktív
			if( activeMenuItem == 0 )
			{
				// balra léptetés, ha lehet
				if( (game.selectedStageIndex > 0) && (keys[KeyEvent.VK_LEFT]) )
				{
					game.selectedStageIndex--;
					canMoveSelection = false;
				}
				
				// jobbra léptetés, ha lehet
				if( (game.selectedStageIndex < game.stageFileNames.length - 1) && (keys[KeyEvent.VK_RIGHT]) )
				{
					game.selectedStageIndex++;
					canMoveSelection = false;
				}
			}
			// ha a második menüelem aktív
			else if( activeMenuItem == 1 )
			{
				// balra léptetés, ha lehet
				if( (game.musicFileNames != null) && (game.selectedMusicIndex > 0) && (keys[KeyEvent.VK_LEFT]) )
				{
					game.selectedMusicIndex--;
					canMoveSelection = false;
					
					// zeneszámot váltunk, ha épp be van kapcsolva
					if( game.isMusicPlaying() )
					{
						game.stopMusic();
						game.playSelectedMusic();
					}
				}
				
				// jobbra léptetés, ha lehet
				if( (game.musicFileNames != null) && (game.selectedMusicIndex < game.musicFileNames.length - 1) && (keys[KeyEvent.VK_RIGHT]) )
				{
					game.selectedMusicIndex++;
					canMoveSelection = false;
					
					// zeneszámot váltunk, ha épp be van kapcsolva
					if( game.isMusicPlaying() )
					{
						game.stopMusic();
						game.playSelectedMusic();
					}
				}
			}
		}
		// ha nem, de felengedték a gombokat, akkor már igen
		else if( !keys[KeyEvent.VK_LEFT] && !keys[KeyEvent.VK_RIGHT] )
		{
			canMoveSelection = true;
		}
		
		
		// ha használhatja az entert
		if( canActivateMenuItem )
		{
			// ENTER lekezelése
			if( game.getKeys()[KeyEvent.VK_ENTER] )
			{
				// a felengedésig nem kezelhetjük le újra
				canActivateMenuItem = false;
				
				// a kijelölt menüelemnek megfelelően
				switch( activeMenuItem )
				{
					// játék indítása
					case 0 : game.startGameOnSelectedStage(); break;
					// zenelejátszás ki/bekapcsolása
					case 1 :
					{
						// ha megy, leállítjuk
						if( game.isMusicPlaying() )
						{
							game.stopMusic();
							menuItems[1] = "Zene bekapcsolása";
						}
						// ha nem megy, elindítjuk
						else
						{
							game.playSelectedMusic();
							menuItems[1] = "Zene kikapcsolása";
						}
						break;
					}
					// játékmód váltás CREDITS-be
					case 2 : game.switchGameMode(GameStates.CREDITS); break;
					// kilépés
					case 3 : System.exit(0); break;
				}
			}
		}
		// ha nem használhatjuk az entert, de már felengedték
		else if( !keys[KeyEvent.VK_ENTER] )
		{
			// akkor újra használható
			canActivateMenuItem = true;
		}
	}

	@Override
	public void update()
	{
	}

	@Override
	public void render(Graphics2D g)
	{
		// játék címének kirajzolása
		titleSprite.render(g);
		
		// menüelemek kiírása
		g.setFont(menuItemsFont);
		for( int i = 0; i < menuItems.length; i++ )
		{
			// az aktív menüelemet feketével írjuk ki, a többit szürkével
			if( i == activeMenuItem )
			{
				g.setPaint(Color.black);
			}
			else
			{
				g.setPaint(Color.DARK_GRAY);
			}
			g.drawString(menuItems[i], 10, 220 + i * 80);
		}
		
		// az aktív menüelemnek megfelelő statusbar string kiírása
		g.setPaint(Color.black);
		g.setFont(statusFont);
		g.drawString(statusStrings[activeMenuItem], 10, 585);
		
		// ha a játékindítás menüpont az aktív, akkor mellé kiírjuk a kijelölt pályát is...
		if( activeMenuItem == 0 )
		{
			// pálya neve
			String selectedStageName = game.stageFileNames[game.selectedStageIndex];
			// lecsípjük a kiterjesztést és hozzárakunk egy kis szöveget
			int pos = selectedStageName.lastIndexOf(".");
			if( pos != -1)
			{
				selectedStageName = selectedStageName.substring(0, pos);
			}
			selectedStageName = selectedStageName + ". pálya";
			
			// ha nem a legelső pálya van kiválasztva, azaz lehet balra léptetni
			if( game.selectedStageIndex > 0 )
			{
				// akkor jelezzük ezt egy <- karakterrel
				selectedStageName = "<-  " + selectedStageName;
			}
			
			// ha nem a legutolsó pálya van kiválasztva, azaz lehet jobbra léptetni
			if( game.selectedStageIndex < game.stageFileNames.length - 1 )
			{
				// akkor jelezzük ezt egy -> karakterrel
				selectedStageName = selectedStageName + "  ->";
			}
			
			// kiválasztott pálya nevének kiírása
			g.drawString(selectedStageName, 450, 218);
		}
		// ha a zene ki/bekapcsolás menüpont az aktív, akkor mellé kiírjuk a kijelölt pályát is...
		else if( (activeMenuItem == 1) && (game.musicFileNames != null) )
		{
			// zeneszám neve
			String selectedMusicName = game.musicFileNames[game.selectedMusicIndex];
			// lecsípjük a kiterjesztést és hozzárakunk egy kis szöveget
			int pos = selectedMusicName.lastIndexOf(".");
			if( pos != -1)
			{
				selectedMusicName = selectedMusicName.substring(0, pos);
			}
			
			// ha nem a legelső zene van kiválasztva, azaz lehet balra léptetni
			if( game.selectedMusicIndex > 0 )
			{
				// akkor jelezzük ezt egy <- karakterrel
				selectedMusicName = "<-  " + selectedMusicName;
			}
			
			// ha nem a legutolsó zene van kiválasztva, azaz lehet jobbra léptetni
			if( game.selectedMusicIndex < game.musicFileNames.length - 1 )
			{
				// akkor jelezzük ezt egy -> karakterrel
				selectedMusicName = selectedMusicName + "  ->";
			}
			
			// kiválasztott zeneszám nevének kiírása
			g.drawString(selectedMusicName, 450, 298);
		}
	}

}
