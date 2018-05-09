package com.pica.fskz.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.pica.fskz.LocalCache;

public class BehaviorCollectorHandler extends IoHandlerAdapter {

	private static Logger logger = Logger
			.getLogger(BehaviorCollectorHandler.class);

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
		String behaviorLog = message.toString();

		int index = behaviorLog.indexOf(31);
		String visitTime = behaviorLog.substring(0, index);
		behaviorLog = behaviorLog.substring(index + 1);
		index = behaviorLog.indexOf(31);
		String ip = behaviorLog.substring(0, index);
		behaviorLog = behaviorLog.substring(index + 1);
		index = behaviorLog.indexOf(31);
		String msisdn = behaviorLog.substring(0, index);
		behaviorLog = behaviorLog.substring(index + 1);
		index = behaviorLog.indexOf(31);
		String sessionId = behaviorLog.substring(0, index);
		String page = behaviorLog.substring(index + 1);

		LocalCache localCache = LocalCache.getInstance();
		String visitMinute = visitTime.substring(0, visitTime.length() - 2);

		if (msisdn != null && !msisdn.isEmpty()) {
			localCache.timesPlus(visitMinute + "_" + ip + "_" + msisdn);
		}
		if (sessionId != null && !sessionId.isEmpty()) {
			localCache.timesPlus(visitMinute + "_" + ip + "_" + sessionId);
		}

//		logger.info("VisitTime: " + visitTime + " ,IP: " + ip + " ,Msisdn: "
//				+ msisdn + " ,SessionID: " + sessionId + " ,Page: " + page);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("BehaviorCollectorHandler exception:", cause);
	}
}
