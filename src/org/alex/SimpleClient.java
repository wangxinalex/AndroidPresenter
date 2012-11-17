package org.alex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.util.ClientUtil;
import org.util.Transimission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SimpleClient extends Activity {
	public static final String DEFAULT_IP = "192.168.137.1";
	public static final int DEFAULT_PORT = 30000;
	EditText input;
	AutoCompleteTextView ip;
	ImageButton up, down, begin, end, first, last, send, ipTest, ipSet, mouse,
			gravity;
	OutputStream os;
	Handler handler;
	String targetIp;
	int port;
	Thread th;
	Socket s;
	BufferedReader br = null;
	String ipListArray[] = { "192.168.16.1", "192.168.137.1", "10.0.2.2" };
	AlertDialog ipDialog;
	EditText suggestIP;
	String suggestionIP;
	boolean gravityFlag;
	SensorManager manager;
	Sensor sensor;
	SensorEventListener listener;
	Vibrator vibrator;
	Transimission transmission;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_simple_client);
		System.out.println("onCreate");
		gravityFlag = false;
		WifiManager wifi_service = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiinfo = wifi_service.getConnectionInfo();
		targetIp = suggestionIP = ClientUtil.getServerIP(wifiinfo);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		try {
			s = new Socket(suggestionIP, DEFAULT_PORT);
			s.setSoTimeout(2000);
			os = s.getOutputStream();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// input = (EditText) findViewById(R.id.input);
		// send = (ImageButton) findViewById(R.id.send);
		up = (ImageButton) findViewById(R.id.up);
		down = (ImageButton) findViewById(R.id.down);
		ipSet = (ImageButton) findViewById(R.id.setip);
		// show = (EditText) findViewById(R.id.show);
		first = (ImageButton) findViewById(R.id.first);
		last = (ImageButton) findViewById(R.id.last);
		begin = (ImageButton) findViewById(R.id.begin);
		end = (ImageButton) findViewById(R.id.end);
		mouse = (ImageButton) findViewById(R.id.mouse);
		gravity = (ImageButton) findViewById(R.id.gravity);
		// if (!gravityFlag) {
		// gravity.setBackgroundResource(R.drawable.gravityoff);
		// }
		final Builder builder = new AlertDialog.Builder(this);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					// show.append("\n" + msg.obj.toString());
				}
			}
		};

		up.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Transimission.pageUp(os);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		down.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Transimission.pageDown(os);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		first.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Transimission.pageFirst(os);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		last.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Transimission.pageLast(os);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		begin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Transimission.pageBegin(os);
					// input.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		end.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Transimission.pageEnd(os);
					// input.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mouse.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle data = new Bundle();
				data.putSerializable("ip", targetIp);
				data.putSerializable("port", DEFAULT_PORT);

				Intent intent = new Intent(SimpleClient.this, TouchEvent.class);
				intent.putExtras(data);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}
		});
		gravity.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				gravityFlag = !gravityFlag;
				if (gravityFlag) {
					gravity.setImageResource(R.drawable.gravityon);

				} else {
					gravity.setImageResource(R.drawable.gravityoff);
				}
			}
		});
		ipSet.setOnClickListener(new OnClickListener() {

			public void onClick(View source) {
				// TODO Auto-generated method stub
				TableLayout ipsetLayout = (TableLayout) getLayoutInflater()
						.inflate(R.layout.setting, null);
				builder.setView(ipsetLayout);
				ip = (AutoCompleteTextView) ipsetLayout
						.findViewById(R.id.ipsetting);
				ipTest = (ImageButton) ipsetLayout.findViewById(R.id.testip);
				suggestIP = (EditText) ipsetLayout.findViewById(R.id.suggestIP);
				ipTest.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						targetIp = ip.getText().toString();
						try {
							if ("".equals(targetIp) || targetIp == null) {
								targetIp = DEFAULT_IP;
							}
							s = new Socket(targetIp, DEFAULT_PORT);
							s.setSoTimeout(2000);
							(th = new Thread(new ClientThread(s, handler)))
									.start();
							os = s.getOutputStream();
							checkLink();
							if (!checkReceived()) {
								Toast.makeText(SimpleClient.this,
										"Link Failure", Toast.LENGTH_LONG)
										.show();
								return;
							} else {
								Toast.makeText(SimpleClient.this,
										"Link Success", Toast.LENGTH_LONG)
										.show();
								ipDialog.dismiss();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				WifiManager wifi_service = (WifiManager) getSystemService(WIFI_SERVICE);
				WifiInfo wifiinfo = wifi_service.getConnectionInfo();
				suggestIP.setText("Available IP: "
						+ ClientUtil.getServerIP(wifiinfo));
				ArrayAdapter<String> ipAdapter = new ArrayAdapter<String>(
						SimpleClient.this,
						android.R.layout.simple_dropdown_item_1line,
						ipListArray);

				ip.setAdapter(ipAdapter);
				ipDialog = builder.create();

				ipDialog.show();
				ipDialog.getWindow().setLayout(480, 300);

			}
		});
		registerListener();
	}

	private void registerListener() {
		// TODO Auto-generated method stub
		listener = new SensorEventListener() {

			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				float x = event.values[SensorManager.DATA_X];
				float z = event.values[SensorManager.DATA_Z];
				try {
					if ((x > 15 || x < -15) && gravityFlag) {
						if (z > 0) {
							Transimission.pageDown(os);
						} else {
							Transimission.pageUp(os);
						}
						vibrator.vibrate(200);
						makeSensorDelay();
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		
			private void makeSensorDelay() {
				// TODO Auto-generated method stub
				manager.unregisterListener(listener);
				Handler handler = new Handler() {
					public void handleMessage(Message msg) {
						if (msg.what == 1) {
							manager.registerListener(listener, sensor,
									SensorManager.SENSOR_DELAY_GAME);
						}
					}
				};
				Message message = Message.obtain();
				message.what = 1;
				handler.sendMessageDelayed(message, 1000);
			}

			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	public void onResume() {

		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		if (data != null) {
			targetIp = (String) data.getSerializable("ip");
			System.out.println("targetIp = " + targetIp);
			if (targetIp != null) {
				try {
					s = new Socket(targetIp, DEFAULT_PORT);
					s.setSoTimeout(2000);
					(th = new Thread(new ClientThread(s, handler))).start();
					os = s.getOutputStream();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		super.onResume();
		manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		manager.registerListener(listener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	protected boolean checkReceived() throws IOException {
		// TODO Auto-generated method stub
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String receivedStr = br.readLine();
		System.out.println("Received String = " + receivedStr);
		return (receivedStr.equals("linked"));

	}

	protected void checkLink() throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		os.write(("link\r\n").getBytes("utf-8"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_simple_client, menu);
		return true;
	}

	@Override
	protected void onPause() {
		manager.unregisterListener(listener);
		super.onPause();
	}

}
