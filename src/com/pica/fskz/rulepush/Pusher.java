package com.pica.fskz.rulepush;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.pica.fskz.util.ConfigUtil;

public class Pusher {

	private static Logger logger = Logger.getLogger(Pusher.class);
	private RuleManager ruleManager = new RuleManager();

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean push() throws Exception {
		Map<String, String> fskzRules = ruleManager.checkAndGenerateRules();
		if (fskzRules == null) {
			logger.debug("None rules.");
			return false;
		}

		String portalFskzRuleReceiver = ConfigUtil
				.getStringByName("portalFskzRuleReceiver");
		BufferedReader reader = null;
		OutputStreamWriter wr = null;
		URLConnection conn = new URL(portalFskzRuleReceiver).openConnection();
		conn.setDoOutput(true);
		conn.setConnectTimeout(1000 * 5);
		wr = new OutputStreamWriter(conn.getOutputStream());
		for (String user : fskzRules.keySet()) {
			wr.write(user + "," + fskzRules.get(user) + ";");
		}
		wr.flush();

		reader = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		StringBuilder response = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		wr.close();
		reader.close();

		if (response.toString().equals("RuleReceived")) {
			logger.info("Push rule to portal success");
			return true;
		} else {
			logger.info("Push rule to portal failed: " + response);
			return false;
		}
	}
}
