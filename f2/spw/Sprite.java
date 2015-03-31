package f2.spw;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.IOException;					
import java.awt.image.BufferedImage;

public abstract class Sprite {
	int x;
	int y;
	int width;
	int height;
	private BufferedImage sheetP = null;
	private SpriteSheet ssp;
	BufferedImage player;
	
	public Sprite(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		BufferedImageLoader loaderP = new BufferedImageLoader();
		
		try{
			sheetP = loaderP.loadImage("/f2/spw/Pictures/ship.png");
		}catch(IOException e){
			e.printStackTrace();
		}
		ssp = new SpriteSheet(sheetP);
		player = ssp.grabImage(1 , 1, 94, 100);
	}

	abstract public void draw(Graphics2D g);
	
	public Double getRectangle() {
		return new Rectangle2D.Double(x, y, width, height);
	}
}