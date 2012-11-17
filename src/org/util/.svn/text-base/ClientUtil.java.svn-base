package org.util;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class ClientUtil {
	public static String longToIP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp & 0x000000FF)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp >>> 24) & 0x000000FF));
		return sb.toString();
	}

	public static String clientToServer(String clientStr) {
		if (!clientStr.matches("\\d*\\.\\d*\\.\\d*\\.\\d*")
				|| clientStr.equals("0.0.0.0"))
			return "null";
		String[] ips = clientStr.split("\\.");
		System.out.println("length = " + ips.length);
		ips[3] = "1";
		String serverStr = ips[0] + "." + ips[1] + "." + ips[2] + "." + ips[3];
		return serverStr;
	}

	public static String getServerIP(WifiInfo wifiinfo) {
		String clientIP = ClientUtil.longToIP((long) wifiinfo.getIpAddress());
		String serverIP = ClientUtil.clientToServer(clientIP);
		return serverIP;
	}
}
