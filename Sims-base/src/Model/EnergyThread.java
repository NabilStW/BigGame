package Model;

import View.Window;

public class EnergyThread implements Runnable{
	private int waitingTime = 10;
	private double perteEnergie = 0.001;
	private double perteVie = 0.001;
	private Player p;
	private Window window;

	public EnergyThread(int waitingTime, double perteEnergie, double perteVie, Player p,Window window) {
		this.waitingTime = waitingTime;
		this.perteEnergie = perteEnergie;
		this.p = p;
		this.window = window;
		this.perteVie = perteVie;		
	}
	
	public void run() {
		try {
			while (true) {
				Thread.sleep(waitingTime);
				if (p.energy > 0) {
					p.energy -= perteEnergie;				}
				if (p.vie > 0) {
					p.vie -= perteVie;
				}
				window.update();

			}
		}
		catch(Exception e){
			
		}
	}
}
