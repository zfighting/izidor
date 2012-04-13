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
	// konstruktor
	public RenderSurface(int width, int height)
	{
		// default layoutmanager és engedélyezett dupla pufferelés
		super(null, true);
		
		// méret beállítása
		this.setPreferredSize(new Dimension(width, height));
		
		// ------------- TESZT ---------------------------------
		try
		{
			File f = new File(System.getProperty("user.dir") + File.separatorChar + "res" + File.separatorChar + "texture.bmp");
			bi = ImageIO.read(f);
			TexturePaint tp = new TexturePaint(bi, new Rectangle2D.Float(0, 0, bi.getWidth(), bi.getHeight()));
			
			ojjektum = new RectangularRenderableGameObject((byte)1, new Vector2d(20, 30), tp, 200.0f, 100.0f);
			ojjektum2 = new RectangularRenderableGameObject((byte)1, new Vector2d(20, 100), Color.red, 40, 20);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	// ------------- TESZT ---------------------------------
	BufferedImage bi;
	private RectangularRenderableGameObject ojjektum;
	private RectangularRenderableGameObject ojjektum2;
	
	// renderelés
	@Override
    public void paintComponent(Graphics g)
	{
		// ------------- TESZT ---------------------------------
		
		// törlés, stb.
		super.paintComponent(g);

		// móka...
        Graphics2D g2d = (Graphics2D) g;
        //g2d.translate(10, 10);
        
        ojjektum.position.x += 0.5f;
        ojjektum.render(g2d);
        
        ojjektum2.position.x += 0.3f;
        ojjektum2.render(g2d);
        
        //ojjektum.position.x = ojjektum.position.x + 250;
        //ojjektum.width = ojjektum.width * 0.8f;
        //ojjektum.render(g2d);
        
        /*
        int img_num = 10;
        for( int i = 0; i < img_num; i++ )
        {
        	float w = ((float)i / (img_num-1) * bi.getWidth());
        	float h = ((float)i / (img_num-1) * bi.getHeight());
        	
        	ojjektum.position = new Vector2d(320 - w / 2.f, 240 - h / 2.f);
        	ojjektum.width = w;
        	ojjektum.height = h;
        	ojjektum.render(g2d);
        }
        */
    }
}
