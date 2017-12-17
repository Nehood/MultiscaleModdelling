package MW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class EnergyDistribution {
	
	EnergyArea energyArea;
	public EnergyDistribution(int cells, int size) {
		
		energyArea = new EnergyArea(cells, size);
	}
	public void show() {
		JFrame frame = new JFrame("Energy Distribution");
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(energyArea, BorderLayout.CENTER);
		frame.setSize(656, 679);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public void work() {
		energyArea.work();
	}
}

class EnergyArea extends JComponent{

	private static final long serialVersionUID = 1L;
	private static Image image;
	private static Graphics2D g2;
	
	int cells;
	int size;
	
	public EnergyArea(int cells, int size) {
		this.cells = cells;
		this.size = size;
		setDoubleBuffered(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			g2 = (Graphics2D) image.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(image, 0, 0, null);
	}
	
	public void clear() {
		image.flush();
		g2.setPaint(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(Color.BLACK);
		draw();
		repaint();
	}
	
	private void draw() {
		for (int i = 0; i < cells; i++) {
			for (int j = 0; j < cells; j++) {
				if (g2 == null) {
					image = createImage(getSize().width, getSize().height);
					g2 = (Graphics2D) image.getGraphics();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					clear();
				}
				if (DrawArea.energyTab[i][j] == 5) {
					g2.setPaint(Color.RED);
				}
				if (DrawArea.energyTab[i][j] == 3) {
					g2.setPaint(Color.GREEN);
				}
				if (DrawArea.energyTab[i][j] == 1) {
					g2.setPaint(Color.BLUE);
				}
				g2.fillRect(i * size, j * size, size, size);
			}
		}
		repaint();
	}
	
	public void work() {
		while (true) {
			draw();
		}
	}
}
