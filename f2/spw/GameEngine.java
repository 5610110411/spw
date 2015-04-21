package f2.spw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
private ArrayList<Bullet> bullets1 = new ArrayList<Bullet>();	
	private SpaceShip v;
	private boolean gameOver = false;
	private boolean pauseGame = false;
	
	private Timer timer;
	
	private long score = 0;
	private double difficulty = 0.1;
	
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		timer.setRepeats(true);
	}
	
	public void start(){
		timer.start();
	}
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*390), 30, 40, 50);
		gp.sprites.add(e);
		enemies.add(e);
	}
	
	private void generateBullet(){
		Bullet b1 = new Bullet(v.x, v.y, 15, 35);   			//(v.x==bullet from leftgun, v.y, 15, 35);
		gp.sprites.add(b1);
		bullets1.add(b1);
	}
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}
		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				score += 100;
			}
		}
		
		Iterator<Bullet> b_iter1 = bullets1.iterator();			// Using left-bullet on screen
		while(b_iter1.hasNext()){
			Bullet b1 = b_iter1.next();
			b1.proceed();
			
			if(!b1.isAlive()){
				b_iter1.remove();
				gp.sprites.remove(b1);
			}
		}
		
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		Rectangle2D.Double br1;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				die();
				e.setAlive(false);
				return;
			}
		
			for(Bullet b1 : bullets1){						
					br1 = b1.getRectangle();						
					if(er.intersects(br1) && e.isAlive()){			
							e.setAlive(false);						
							b1.setAlive(false);			
					}
				}
		
		}
		
	}
	
	public void die(){
		timer.stop();
		gameOver = true;
		gp.updateGameUI(this,0);
	}
	
	private void continueGame(){
		gameOver = false;
		timer.start();
		gp.updateGameUI(this);
	}
	
	private void pauseGame(){
		pauseGame = true;
		timer.stop();
		gp.updateGameUI(this,2);
	}
	
	private void resumeGame(){
		pauseGame = false;
		timer.start();
	}
	
	void controlVehicle(KeyEvent e) {							//You can move ship indiraction x axis & y axis
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_LEFT){
			v.moveX(-1);
		}else if(key == KeyEvent.VK_RIGHT){
			v.moveX(1);
		}else if(key == KeyEvent.VK_DOWN){
			v.moveY(1);
		}else if(key == KeyEvent.VK_UP){
			v.moveY(-1);
		}else if(key == KeyEvent.VK_ENTER && pauseGame == false){
			pauseGame();
		}else if(key == KeyEvent.VK_D){
			difficulty += 0.1;
		}else if(key == KeyEvent.VK_A){
			generateBullet();
		}
		
	}

	public long getScore(){
		return score;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(timer.isRunning())
			controlVehicle(e);
		else if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(gameOver){				
				continueGame();
			}
			else if(pauseGame){				
				resumeGame();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//do nothing
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}
}
