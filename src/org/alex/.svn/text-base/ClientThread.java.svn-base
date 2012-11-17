package org.alex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

public class ClientThread implements Runnable {
	private Socket s;
	private Handler handler;
	BufferedReader br = null;
	
	public ClientThread(Socket s, Handler handler) throws IOException{
		this.s=s;
		this.handler=handler;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
	}
	

	public void run() {
		// TODO Auto-generated method stub
		String content = null;
		try {
			while((content = br.readLine())!=null){
				Message msg = new Message();
				msg.what=0x123;
				msg.obj = content;
				handler.sendMessage(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
