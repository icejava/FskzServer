package com.pica.fskz.rulepush;


public class PushTimer extends Thread{

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(3000);
				new Pusher().push();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
