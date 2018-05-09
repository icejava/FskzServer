package com.pica.fskz;

import org.apache.log4j.Logger;

import com.pica.fskz.monitor.FskzMonitor;
import com.pica.fskz.rulepush.PushTimer;
import com.pica.fskz.server.BehaviorCollector;

public class Start {

	private static Logger logger = Logger.getLogger(BehaviorCollector.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new BehaviorCollector().start();
			logger.info("FskzServer started. Port: 8080");
			new FskzMonitor().start();
			logger.info("FskzServer Monitor started，Port: 8089");
			new PushTimer().start();
			logger.info("FskzServer Pusher timeer started.");
		} catch (Exception e) {
			logger.error("FskzServer start failed. exception info:", e);
			e.printStackTrace();
		}
	}

}
