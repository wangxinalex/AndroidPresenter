package org.alex;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.util.Transimission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TouchEvent extends Activity {
	public final int DEFAULT_SENSITIVITY = 2;
	int sensitivitynum;
	TextView labelView = null;
	Socket s;
	OutputStream os;
	int formerX = 0, formerY = 0;
	ImageButton left, right, middle;
	SeekBar sensitivity;
	ImageButton back;
	String ip;
	int port;
	boolean keyFlag = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch_event);
		try {
			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			ip = (String) data.getSerializable("ip");
			port = (Integer) data.getSerializable("port");

			s = new Socket(ip, port);
			s.setSoTimeout(2000);
			os = s.getOutputStream();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextView touchView = (TextView) findViewById(R.id.touch_area);
		left = (ImageButton) findViewById(R.id.left);
		right = (ImageButton) findViewById(R.id.right);
		middle = (ImageButton) findViewById(R.id.middle);
		sensitivity = (SeekBar) findViewById(R.id.sensitivity);
		back = (ImageButton) findViewById(R.id.back);
		sensitivitynum = DEFAULT_SENSITIVITY;
		// labelView = (TextView) findViewById(R.id.event_label);
		// final TextView historyView = (TextView)
		// findViewById(R.id.history_label);
		touchView.setOnTouchListener(new View.OnTouchListener() {
			// @Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case (MotionEvent.ACTION_DOWN):
					Display("ACTION_DOWN", event);
					keyFlag = true;
					break;
				case (MotionEvent.ACTION_UP):
					// historyView.setText("历史数据量：" + historySize);
					Display("ACTION_UP", event);
					keyFlag = false;
					break;
				case (MotionEvent.ACTION_MOVE):
					Display("ACTION_MOVE", event);
					keyFlag = false;
					break;
				case MotionEvent.ACTION_POINTER_UP:
					try {
						Transimission.mouseRight(os);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					keyFlag = false;
					break;
				}
				return true;
			}
		});

		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle data = new Bundle();
				data.putSerializable("ip", ip);
				data.putSerializable("port", port);

				Intent intent = new Intent(TouchEvent.this, SimpleClient.class);
				intent.putExtras(intent);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);
			}
		});
		left.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Transimission.mouseLeft(os);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		right.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Transimission.mouseRight(os);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		middle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Transimission.mouseMiddle(os);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		sensitivity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				sensitivitynum = progress;
			}
		});

	}


	private void Display(String eventType, MotionEvent event) {
		if (!checkSocket()) {
			Toast.makeText(TouchEvent.this, "Connection lost",
					Toast.LENGTH_SHORT).show();
			return;
		}
		int x = (int) event.getX();
		int y = (int) event.getY();
//		float pressure = event.getPressure();
//		float size = event.getSize();
//		int RawX = (int) event.getRawX();
//		int RawY = (int) event.getRawY();
		int deltaX = x - formerX;
		int deltaY = y - formerY;
		if (eventType.equals("ACTION_UP")) {
			if (keyFlag) {
				try {
					Transimission.mouseLeft(os);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (!eventType.equals("ACTION_MOVE")) {
			deltaX = deltaY = 0;
		}

		try {
			if (x < 10 && deltaY > 0) {
				Transimission.mouseWheelDown(os);
			} else if (x < 10 && deltaY < 0) {
				Transimission.mouseWheelUp(os);
			} else {
				os.write((String.valueOf(deltaX) + "," + String.valueOf(deltaY)
						+ "," + sensitivitynum + "\r\n").getBytes("utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		formerX = x;
		formerY = y;
	}

	public Boolean checkSocket() {
		return os != null;
	}

}