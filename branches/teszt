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

        int num = 30;
        for (int i = 0; i < num; i++)
        {
            g2d.drawOval(i * 10, this.getHeight() / 2, 30, 30);
        }

    }
}
