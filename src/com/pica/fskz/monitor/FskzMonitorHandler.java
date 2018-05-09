package com.pica.fskz.monitor;

import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.pica.fskz.LocalCache;
import com.pica.fskz.rulepush.RuleManager;

public class FskzMonitorHandler extends IoHandlerAdapter {

	private static Logger logger = Logger.getLogger(FskzMonitorHandler.class);

	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		session.write("User visit times:");
		LocalCache localCache = LocalCache.getInstance();
		TreeSet<String> userSet = new TreeSet<String>(localCache.keySet());
		for (String user : userSet) {
			Integer visitTimes = localCache.get(user);
			session.write(user + ", VisitTimes: " + visitTimes + ", TPS:"
					+ visitTimes / 60);
		}

		session.write("Rules:");
		Map<String, String> rules = new RuleManager().checkAndGenerateRules();
		if (rules != null && !rules.isEmpty()) {
			TreeSet<String> rulesSet = new TreeSet<String>(rules.keySet());
			for (String ruleKey : rulesSet) {
				session.write(ruleKey + ", rule: " + rules.get(ruleKey));
			}
		} else {
			session.write("None");
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("FskzMonitorHandler exception:", cause);
	}
}
