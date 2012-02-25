package game;

import java.awt.*;
import javax.swing.*;

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
	}
	
	// renderelés
	@Override
    public void paintComponent(Graphics g)
	{
		// törlés, stb.
		super.paintComponent(g);

		// móka...
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);
        int num = 50;
        for (int i = 0; i < num; i++)
        {
        	double x = Math.sin(i * 360. / (num-1) * Math.PI / 180.) * 90;
        	double y = Math.cos(i * 360. / (num-1) * Math.PI / 180.) * 90;
            g2d.drawOval((int)Math.round(getWidth() / 2. + x), (int)Math.round(getHeight() / 2. + y), 60, 60);
        }
    }
}
