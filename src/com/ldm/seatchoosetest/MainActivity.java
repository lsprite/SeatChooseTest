package com.ldm.seatchoosetest;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.Toast;

import com.ldm.seatchoosetest.model.Seat;
import com.ldm.seatchoosetest.model.SeatInfo;
import com.ldm.seatchoosetest.view.SSThumView;
import com.ldm.seatchoosetest.view.SSView;

public class MainActivity extends Activity {
	private static final int ROW = 16;
	private static final int EACH_ROW_COUNT = 28;
	private SSView mSSView;
	private SSThumView mSSThumView;
	private ArrayList<SeatInfo> list_seatInfos = new ArrayList<SeatInfo>();
	private Handler handler = new Handler() {
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		mSSView = (SSView) this.findViewById(R.id.mSSView);
		mSSThumView = (SSThumView) this.findViewById(R.id.ss_ssthumview);
		setSeatInfo();
		mSSView.init(EACH_ROW_COUNT, ROW, list_seatInfos, mSSThumView, 5,
				Math.abs(EACH_ROW_COUNT / 2 - 4),
				Math.abs(EACH_ROW_COUNT / 2 + 4), Math.abs(ROW / 2 - 3),
				Math.abs(ROW / 2 + 3));
		mSSView.setOnSeatClickListener(new OnSeatClickListener() {
			@Override
			public boolean b(int column_num, int row_num, boolean paramBoolean) {
				String desc = "您选择了第" + (row_num + 1) + "排" + " 第"
						+ (column_num + 1) + "列";
				Toast.makeText(MainActivity.this, desc.toString(),
						Toast.LENGTH_SHORT).show();
				return false;
			}

			@Override
			public boolean a(int column_num, int row_num, boolean paramBoolean) {
				String desc = "您取消了第" + (row_num + 1) + "排" + " 第"
						+ (column_num + 1) + "列";
				Toast.makeText(MainActivity.this, desc.toString(),
						Toast.LENGTH_SHORT).show();
				return false;
			}

			@Override
			public void a() {

			}
		});
	}

	boolean isFirst = true;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		toCenter();
	}

	// 用模拟手势让画移动到中间
	private void toCenter() {
		if (isFirst) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 1580,1264||1080,1363
					// 2100,1680||1080,1363
					int viewWidth = SSView.getViewWidth(mSSView);
					int viewHeight = SSView.getViewHeight(mSSView);
					int measuredWidtht = SSView.getmyMeasuredWidth(mSSView);
					int measuredHeight = SSView.getmyMeasuredHeight(mSSView);
					long dowTime = SystemClock.uptimeMillis();
					float difW = viewWidth > measuredWidtht ? (viewWidth - measuredWidtht)
							: 0;
					float difH = viewHeight > measuredHeight ? (viewHeight - measuredHeight)
							: 0;
					difW = -Math.abs(difW / 2);
					difH = -Math.abs(difH / 2);
					System.out.println("---difW:" + difW);
					System.out.println("---difH:" + difH);
					if (viewWidth != 0 && viewHeight != 0) {
						isFirst = false;
						MotionEvent move = MotionEvent.obtain(dowTime,
								dowTime + 20, MotionEvent.ACTION_MOVE, difW,
								difH, 0);
						mSSView.dispatchTouchEvent(move);
					}
				}
			}, 100);
		}
	}

	private void setSeatInfo() {
		for (int i = 0; i < ROW; i++) {// 16行
			SeatInfo mSeatInfo = new SeatInfo();
			ArrayList<Seat> mSeatList = new ArrayList<Seat>();
			for (int j = 0; j < EACH_ROW_COUNT; j++) {// 每排20个座位
				Seat mSeat = new Seat();
				if (j < 0) {
					mSeat.setStatus(0);
				} else {
					if (j > 10) {
						mSeat.setStatus(2);
					} else if (j == 5) {
						mSeat.setStatus(0);
					} else {
						mSeat.setStatus(1);
					}
				}
				mSeat.setDes(String.valueOf(j + 1));
				mSeatList.add(mSeat);
			}
			mSeatInfo.setDes(String.valueOf(i + 1));
			mSeatInfo.setSeatList(mSeatList);
			list_seatInfos.add(mSeatInfo);
		}
	}
}
