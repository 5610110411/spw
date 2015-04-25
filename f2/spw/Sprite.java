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
	private BufferedImage sheetF = null;
	private BufferedImage sheetB = null;
	private BufferedImage sheetGm = null;
	private SpriteSheet ssp;
	private SpriteSheet ssf;
	private SpriteSheet ssb;
	private SpriteSheet ssgm;
	BufferedImage player;
	BufferedImage foe;
	BufferedImage bullet;
	BufferedImage greenMedicine;
	
	public Sprite(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		BufferedImageLoader loader = new BufferedImageLoader();
		
		
		try{
			sheetP = loader.loadImage("/f2/spw/Pictures/ship.png");
			sheetF = loader.loadImage("/f2/spw/Pictures/enemy.png");
			sheetB = loader.loadImage("/f2/spw/Pictures/bullet.png");
			sheetGm = loader.loadImage("/f2/spw/Pictures/green_medicine.png");
		}catch(IOException e){
			e.printStackTrace();
		}
		ssp = new SpriteSheet(sheetP);
		player = ssp.grabImage(1 , 1, 94, 100);
		
		ssf = new SpriteSheet(sheetF);
		foe = ssf.grabImage(1 , 1, 63, 99);
		
		ssb = new SpriteSheet(sheetB);
		bullet = ssb.grabImage(1 , 1, 19, 49);
		
		ssgm = new SpriteSheet(sheetGm);
		greenMedicine = ssgm.grabImage(1 , 1, 41, 68);
	}

	abstract public void draw(Graphics2D g);
	
	public Double getRectangle() {
		return new Rectangle2D.Double(x, y, width, height);
	}
}