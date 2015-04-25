package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public class SpaceShip extends Sprite{

	int step = 18;
	private double HEALTH = 150;
	private double oriH = HEALTH;
	private boolean alive = true;
	
	public SpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(player, x, y, width , height, null);
		g.setColor(Color.gray);
		g.fillRect(5, 5, 150, 15);
		
		if(HEALTH >= 75){
			g.setColor(Color.green);
			g.fillRect(5, 5,(int) HEALTH, 15);
		}
		else if(HEALTH >= 50 && HEALTH < 75){
			g.setColor(Color.orange);
			g.fillRect(5, 5,(int) HEALTH, 15);
		}
		
		else 
			g.setColor(Color.red);
			g.fillRect(5, 5,(int) HEALTH, 15);
		
		g.setColor(Color.white);
		g.drawRect(5, 5, 150, 15);
	}

	public void moveX(int direction){
		x += (step * direction);
		if(x < 0)
			x = 0;
		if(x > 400 - width)
			x = 400 - width;
	}
	
	public void moveY(int direction){
		y += (step * direction);
		if(y < 0)
			y = 0;
		if(y > 600 - height)   
			y = 600 - height;
		
	}
	
	public void setAlive(boolean alive){
		this.alive = alive;
	}
	
	public double getHealth(){
		return HEALTH;
	}
	
	public void setOriginHealth(){
		HEALTH = oriH;
	}
	
	public double getOriginHealth(){
		return oriH;
	}
	
	public void setHealth(double HEALTH){
		this.HEALTH = HEALTH;
	}

}
