package Model;

import View.Window;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.omg.CosNaming.IstringHelper;

public class Game implements DeletableObserver {
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Bot> bots = new ArrayList<Bot>();
    private Player active_player = null;   // quel joueur est joué ?
    //private ArrayList<Thread> threads = new ArrayList<Thread>();
   
    private Window window;
    private int size;
    // private int bombTimer = 3000;
    private int numberOfBreakableBlocks = 100;
    //private String lettres= "abcdefghijklmnopqrstuvwxyz";
    
    
    public Game(Window window) {
        this.window = window;
        size = window.getMapSize();
        Player p = new Player(10, 10,666666);
        
        /*for (int i= 0; i < 15; i++) {
        	bots.add(new Bot(i,i,6,1000,window,this));
        	objects.add(new Bot(i,i,6,1000,window,this)); 
        }
		for (Bot bot : bots) {
			threads.add(new Thread((bot)));	
		}
		for (Thread thread : threads) {
			thread.start();
		}*/
		
        /*Bot b1 = new Bot(11,11,6,1000,window,this);Bot b2 = new Bot(16,16,6,1000,window,this);Bot b3 = new Bot(1,1,6,1000,window,this);Bot b6 = new Bot(2,2,6,1000,window,this);Bot b4 = new Bot(50,50,6,1000,window,this);Bot b5 = new Bot(40,40,6,1000,window,this);
        Thread energyt = new Thread(new EnergyThread(100,0.05,0.05,p,window)); energyt.start();
        Thread IAbot = new Thread(b1); IAbot.start();
        Thread IAbot2 = new Thread(b2); IAbot2.start();
        Thread IAbot3 = new Thread(b3); IAbot3.start();
        Thread IAbot4 = new Thread(b4); IAbot4.start();
        Thread IAbot5 = new Thread(b5); IAbot5.start();
        Thread IAbot6 = new Thread(b6); IAbot6.start();
        objects.add(p);objects.add(b1);objects.add(b2);objects.add(b3);objects.add(b4);objects.add(b5);objects.add(b6);
        */
        objects.add(p);
        players.add(p);
        window.setPlayer(p);
        active_player = p;

        // Map building
        for (int i = 0; i < size; i++) {
            objects.add(new BlockUnbreakable(i, 0));
            objects.add(new BlockUnbreakable(0, i));
            objects.add(new BlockUnbreakable(i, size - 1));
            objects.add(new BlockUnbreakable(size - 1, i));
        }
        Random rand = new Random();
        for (int i = 0; i < numberOfBreakableBlocks; i++) {
            int x = rand.nextInt(size-4) + 2;
            int y = rand.nextInt(size-4) + 2;
            int lifepoints = rand.nextInt(5)+1;
            int energy = rand.nextInt(2);
            BlockBreakable block = new BlockBreakable(x, y, lifepoints,energy);
            block.attachDeletable(this);
            objects.add(block);
        }
        
        window.setGameObjects(this.getGameObjects());
        notifyView();
    }
    public boolean Obstacle(int x,int y) {
    	boolean obstacle = false;
        for (GameObject object : objects) {
            if (object.isAtPosition(x, y)) {
                obstacle = object.isObstacle();
            }
            if (obstacle == true) {
                break;
            }
            
        }
    	return obstacle;
    }


    public void movePlayer(int x, int y) {
        int nextX = active_player.getPosX() + x;
        int nextY = active_player.getPosY() + y;
        
        boolean obstacle = this.Obstacle(nextX, nextY);
        active_player.rotate(x, y);
        if (obstacle == false) {
            active_player.move(x, y);
        }
        notifyView();
    }

    public void tirePlayer() {
    	active_player.tire();
    	notifyView();
    }
    
    public void bouledefeu() {
    	bouledefeu(active_player.getPosX(), active_player.getPosY(),active_player.getDirection());
    }
    public void bouledefeu(int x, int y, int d) {
    	
    	
    	objects.add(new BouleDeFeu(x,y,6,d,30,this));
    	//Thread feubouge = new Thread(feu);
        //feubouge.start();
    }
    
    public void quatreFeux() {
    	/*for (int i= 0; i<3 ;i++) {
    		BouleDeFeu feu = new BouleDeFeu(active_player.getPosX(), active_player.getPosY(),6,i,30,this);
        	objects.add(feu); Thread feubouge = new Thread(feu);feubouge.start();notifyView();
    	}*/
    	bouledefeu(active_player.getPosX(), active_player.getPosY(),0);
    	bouledefeu(active_player.getPosX(), active_player.getPosY(),1); 
    	bouledefeu(active_player.getPosX(), active_player.getPosY(),2);
    	bouledefeu(active_player.getPosX(), active_player.getPosY(),3);
    		
    	/*
    	//objects.add(feu1); Thread feubouge1 = new Thread(feu1);feubouge1.start();notifyView();
    	
    	BouleDeFeu feu2 = new BouleDeFeu(active_player.getPosX(), active_player.getPosY(),6,1,30,this);
    	//objects.add(feu2); Thread feubouge2 = new Thread(feu2);feubouge2.start();notifyView();
    	
    	BouleDeFeu feu3 = new BouleDeFeu(active_player.getPosX(), active_player.getPosY(),6,2,30,this);
    	//objects.add(feu3); Thread feubouge3 = new Thread(feu3);feubouge3.start();notifyView();
    	
    	BouleDeFeu feu4 = new BouleDeFeu(active_player.getPosX(), active_player.getPosY(),6,3,30,this);
    	//objects.add(feu4); Thread feubouge4 = new Thread(feu4);feubouge4.start();notifyView();
    */ } 
    
    public void action() {
    		action(active_player);
    	}
    public void action(Charactere cara) {
        Activable aimedObject = null;
        
		for(GameObject object : objects){
			if(object.isAtPosition(cara.getFrontX(),cara.getFrontY())){
			    if(object instanceof Activable){
			        aimedObject = (Activable) object;
			    }
				
			}
			
			
			if (object.isAtPosition(cara.getFrontX(),cara.getFrontY()) && (object instanceof Player || object instanceof BlockUnbreakable)) {
				//objects.remove(cara);
				if (object instanceof Player) {
					if (cara instanceof BouleDeFeu) {
						BouleDeFeu boule = (BouleDeFeu) cara;
						boule.setThread_active(false);
						boule.stopThread();
					}
					objects.remove(cara); 
					System.out.println("AIE");
					active_player.vie -= 10;}
				else if (object instanceof Block) {
					if (cara instanceof BouleDeFeu) {
						BouleDeFeu boule = (BouleDeFeu) cara;
						boule.setThread_active(false);
						boule.stopThread();
					}
					System.out.println("Ca fonctionne !");
					
					objects.remove(cara);
					}
				
				
			}notifyView();
			
		}
		
		if(aimedObject != null){
			if (aimedObject instanceof BlockBreakable) {
				if (((BlockBreakable) aimedObject).getEnergy()!=0) {
					if (active_player.energy < 95) {
						active_player.energy += 5;
					}
				}
				
			}
		    aimedObject.activate();
            
		}notifyView();
        
    }
    

    private void notifyView() {
        window.update();
    }

    public ArrayList<GameObject> getGameObjects() {
        return this.objects;
    }

    @Override
    synchronized public void delete(Deletable ps, ArrayList<GameObject> loot) {
        objects.remove(ps);
        if (loot != null) {
            objects.addAll(loot);
        }
        notifyView();
    }


    public void playerPos() {
        System.out.println(active_player.getPosX() + ":" + active_player.getPosY());
        
    }

	public void stop() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}


	public void sendPlayer(int x, int y) {
		Thread t = new Thread(new AStarThread(this, active_player, x,  y));
		t.start();
	}


}