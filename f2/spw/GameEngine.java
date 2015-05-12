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
	private ArrayList<Money> moneys = new ArrayList<Money>();
	private ArrayList<GreenMedicine> GreenMedicines = new ArrayList<GreenMedicine>();
	private SpaceShip v;
	private boolean gameOver = false;
	private boolean pauseGame = false;
	
	private Timer timer;
	
	private long score = 0;
	private double difficulty = 0.1;
	private double rateMedicine = 0.01;
	private double greenMed = 10;
	private double rateMoney = 0.01;
	private double enemyDamage = 25;
	
	
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
		Bullet b1 = new Bullet(v.x, v.y, 15, 35);  
		gp.sprites.add(b1);
		bullets1.add(b1);
	}
	
	private void generateGreenMedicine(){							
		GreenMedicine gm = new GreenMedicine((int)(Math.random()*390), 30, 20, 34);
		gp.sprites.add(gm);
		GreenMedicines.add(gm);
	}
	
	private void generateMoney(){								
		Money m = new Money((int)(Math.random()*390), 30, 25, 25); 
		gp.sprites.add(m);
		moneys.add(m);
	}
	
	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}
		
		if(Math.random() < rateMedicine){					
			generateGreenMedicine();
		}
		
		if(Math.random() < rateMoney){							
			generateMoney();
		}
		
		if(v.getHealth() < 150)										//HP Recovery
			v.setHealth(v.getHealth() + 0.01);
		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();
			
			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				//score += 100;
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
		
		Iterator<GreenMedicine> gm_iter = GreenMedicines.iterator();				
		while(gm_iter.hasNext()){
			GreenMedicine gm = gm_iter.next();
			gm.proceed();
			
			if(!gm.isAlive()){ 								
				gm_iter.remove();								
				gp.sprites.remove(gm);
			}
		}
		
		Iterator<Money> m_iter = moneys.iterator();				
		while(m_iter.hasNext()){
			Money m = m_iter.next();
			m.proceed();
			
			if(!m.isAlive()){ 									
				m_iter.remove();
				gp.sprites.remove(m);
			}
		}
		
		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		Rectangle2D.Double br1;
		Rectangle2D.Double mr;
		Rectangle2D.Double gmr;	
		for(GreenMedicine gm : GreenMedicines){
			gmr = gm.getRectangle();	
			if(gmr.intersects(vr)){								
				if((v.getHealth() + greenMed) < v.getOriginHealth())								
					v.setHealth(v.getHealth() + greenMed);				
				if((v.getHealth() + greenMed) >= v.getOriginHealth())								
					v.setOriginHealth();
				else	
					v.setHealth(v.getHealth() + 0);				
				gm.setAlive(false);								
			}
		}
		
		for(Money m : moneys){									
			mr = m.getRectangle();								
			if(mr.intersects(vr)){
				score += 1000;
				m.setAlive(false);										
			}
		}
		
		for(Enemy e : enemies){									
			er = e.getRectangle();						     
			if(er.intersects(vr) && e.isAlive()){				
				v.setHealth(v.getHealth() - enemyDamage);
				e.setAlive(false);								
				if(v.getHealth() <= 0)						
					die();									
				return;
			}
		
			for(Bullet b1 : bullets1){						
					br1 = b1.getRectangle();						
					if(er.intersects(br1) && e.isAlive()){			
							e.setAlive(false);						
							b1.setAlive(false);
							score += 100;
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
		v.setOriginHealth();
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
	
	void controlVehicle(KeyEvent e) {							//You can move ship in diraction x axis & y axis
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
