package org.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Transimission {
	// public static OutputStream os;

	public static void pageDown(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.PAGE_DOWN + "\r\n").getBytes("utf-8"));
	}

	public static void pageUp(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.PAGE_UP + "\r\n").getBytes("utf-8"));
	}

	public static void pageFirst(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.PAGE_FIRST + "\r\n").getBytes("utf-8"));
	}

	public static void pageLast(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.PAGE_LAST + "\r\n").getBytes("utf-8"));
	}

	public static void pageBegin(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.PAGE_BEGIN + "\r\n").getBytes("utf-8"));
	}

	public static void pageEnd(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.PAGE_END + "\r\n").getBytes("utf-8"));
	}

	public static void mouseLeft(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.MOUSE_LEFT + "\r\n").getBytes("utf-8"));
	}

	public static void mouseRight(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.MOUSE_RIGHT + "\r\n").getBytes("utf-8"));
	}

	public static void mouseMiddle(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.MOUSE_MIDDLE + "\r\n").getBytes("utf-8"));
	}

	public static void mouseWheelUp(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.MOUSE_WHEELUP + "\r\n").getBytes("utf-8"));
	}

	public static void mouseWheelDown(OutputStream os)
			throws UnsupportedEncodingException, IOException {
		os.write((Motion.MOUSE_WHEELDOWN + "\r\n").getBytes("utf-8"));
	}
}
