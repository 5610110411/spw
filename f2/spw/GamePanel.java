package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.IOException;
import java.awt.Font;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;	
	private BufferedImage background = null;
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	BufferedImageLoader bg = new BufferedImageLoader();

	public GamePanel() {
		bi = new BufferedImage(400, 600, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
		background = new BufferedImage(400, 600, BufferedImage.TYPE_INT_ARGB);
		try{
			background = bg.loadImage("/f2/spw/Pictures/background.png");
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void updateGameUI(GameReporter reporter){
		updateGameUI(reporter,1);
	}
	
	public void updateGameUI(GameReporter reporter,int state){
		big.clearRect(0, 0, 400, 650);
		
		Background();
		
		switch (state){
			case 0:	gameOver(reporter);
				break;
			case 1:	gameOn(reporter);
				break;
		}
		repaint();
	}

	public void gameOn(GameReporter reporter){
		
		big.setColor(Color.WHITE);
		big.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		big.drawString(String.format("%08d", reporter.getScore()), 300, 40); 
		for(Sprite s : sprites){
			s.draw(big);
		}
	}
	
	public void gameOver(GameReporter reporter){
		big.clearRect(0, 0, 400, 650);  
		big.setBackground(Color.BLACK);
		big.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		big.setColor(Color.red);
		big.drawString("Mission Failure!", 100, 280);
		big.setColor(Color.white);
		big.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		big.drawString("Press [ENTER] to Try again", 80, 320);
	}
	
	public void Background(){		
		big.drawImage(background, 0, 0, 400, 600, null);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}
