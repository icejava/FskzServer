package com.pica.fskz.rulepush;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import com.pica.fskz.LocalCache;
import com.pica.fskz.util.ConfigUtil;

public class RuleManager {

	private static final SimpleDateFormat dateFormet = new SimpleDateFormat(
			"yyyyMMddHHmm");

	/**
	 * 检查并生成过滤规则，如11251212_0代表在11251212前采用普通图形验证码进行过滤 0代表普通，1代表提示访问受限，2代表直接拒绝响应
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Map<String, String> checkAndGenerateRules() throws ParseException {

		Map<String, String> fskzRules = new HashMap<String, String>();

		LocalCache localCache = LocalCache.getInstance();
		TreeSet<String> userSet = new TreeSet<String>(localCache.keySet());

		Integer rule_auth = ConfigUtil.getIntegerByName("rule_auth");
		Integer rule_auth_time = ConfigUtil.getIntegerByName("rule_auth_time");
		Integer rule_warn = ConfigUtil.getIntegerByName("rule_warn");
		Integer rule_warn_time = ConfigUtil.getIntegerByName("rule_warn_time");
		Integer rule_refuse = ConfigUtil.getIntegerByName("rule_refuse");
		Integer rule_refuse_time = ConfigUtil
				.getIntegerByName("rule_refuse_time");
		for (String user : userSet) {
			Integer visitTimes = localCache.get(user);
			Calendar expireTime = Calendar.getInstance();
			String visitMinute = user.split("_")[0];
			expireTime.setTime(dateFormet.parse(visitMinute));
			Calendar clearTime = expireTime;
			if (visitTimes > rule_auth && visitTimes <= rule_warn) {
				expireTime.add(Calendar.MINUTE, rule_auth_time);
				fskzRules.put(user.substring(user.indexOf("_") + 1),
						dateFormet.format(expireTime.getTime()) + "_1");
			} else if (visitTimes > rule_warn && visitTimes <= rule_refuse) {
				expireTime.add(Calendar.MINUTE, rule_warn_time);
				fskzRules.put(user.substring(user.indexOf("_") + 1),
						dateFormet.format(expireTime.getTime()) + "_2");
			} else if (visitTimes > rule_refuse) {
				expireTime.add(Calendar.MINUTE, rule_refuse_time);
				fskzRules.put(user.substring(user.indexOf("_") + 1),
						dateFormet.format(expireTime.getTime()) + "_3");
			}

			// 清理过期访问数据
			clearTime.add(Calendar.MINUTE,
					ConfigUtil.getIntegerByName("rule_clear_time_interval"));
			if (expireTime.compareTo(Calendar.getInstance()) <= 0
					&& clearTime.compareTo(Calendar.getInstance()) <= 0) {
				localCache.remove(user);
			}
		}
		if (fskzRules.isEmpty()) {
			return null;
		} else {
			return fskzRules;
		}
	}
}
