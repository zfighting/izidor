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
			
			ojjektum = new RectangularRenderableGameObject((byte)1, new Vector2d(20, 30), tp, bi.getWidth(), bi.getHeight());
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	// ------------- TESZT ---------------------------------
	BufferedImage bi;
	private RectangularRenderableGameObject ojjektum;
	
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
        
        /*
        g2d.setColor(Color.red);
        int num = 30;
        for (int i = 0; i < num; i++)
        {
        	double x = Math.sin(i * 360. / (num-1) * Math.PI / 180.) * 90;
        	double y = Math.cos(i * 360. / (num-1) * Math.PI / 180.) * 90;
            g2d.drawOval((int)Math.round(getWidth() / 2. + x) - 100, (int)Math.round(getHeight() / 2. + y) - 100, (int)Math.round(60 * (Math.sin(i/(num-1) * 720 * Math.PI / 180.)+1)*2), (int)Math.round(60 * (Math.cos(i/(num-1) * 720 * Math.PI / 180.)+1)*2));
        }
        */
    }
}
