﻿package com.ldm.seatchoosetest.view;

import java.util.ArrayList;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.ldm.seatchoosetest.model.Seat;
import com.ldm.seatchoosetest.model.SeatSelect;

class GestureListener extends GestureDetector.SimpleOnGestureListener {
	private SSView mSsView;

	GestureListener(SSView paramSSView) {
		mSsView = paramSSView;
	}

	public boolean onDoubleTap(MotionEvent paramMotionEvent) {
		return super.onDoubleTap(paramMotionEvent);
	}

	public boolean onDoubleTapEvent(MotionEvent paramMotionEvent) {
		return super.onDoubleTapEvent(paramMotionEvent);
	}

	public boolean onDown(MotionEvent paramMotionEvent) {
		return false;
	}

	public boolean onFling(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		return false;
	}

	public void onLongPress(MotionEvent paramMotionEvent) {
	}

	public boolean onScroll(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float x_scroll_distance,
			float y_scroll_distance) {
		// 是否可以移动和点击
		if (!SSView.isCanMove(mSsView)) {
			return false;
		}
		// 显示缩略图
		SSView.a(mSsView, true);
		boolean bool1 = true;
		boolean bool2 = true;
		if ((SSView.getViewWidth(mSsView) < mSsView.getMeasuredWidth())
				&& (0.0F == SSView.v(mSsView))) {
			bool1 = false;
		}

		if ((SSView.getViewHeight(mSsView) < mSsView.getMeasuredHeight())
				&& (0.0F == SSView.w(mSsView))) {
			bool2 = false;
		}
		// 修改排数相关坐标(左右滑动)
		if (bool1) {
			int k = Math.round(x_scroll_distance);
			// 修改排数x轴的偏移量
			SSView.c(mSsView, (float) k);
			// 修改座位距离排数的横向距离
			SSView.k(mSsView, k);
			if (SSView.r(mSsView) < 0) {
				// 滑到最左
				SSView.i(mSsView, 0);
				SSView.a(mSsView, 0.0F);
			}
			if (SSView.r(mSsView) + mSsView.getMeasuredWidth() > SSView
					.getViewWidth(mSsView)) {
				// 滑到最右
				SSView.i(
						mSsView,
						SSView.getViewWidth(mSsView)
								- mSsView.getMeasuredWidth());
				SSView.a(mSsView, (float) (mSsView.getMeasuredWidth() - SSView
						.getViewWidth(mSsView)));
			}
			SSView.setRowOffsetHorizontal(mSsView, (float) k);
		}
		// 上下滑动
		if (bool2) {
			// 上负下正- 往下滑则减
			int j = Math.round(y_scroll_distance);
			// 修改排数y轴的偏移量
			SSView.d(mSsView, (float) j);
			// 修改可视座位距离顶端的距离
			SSView.l(mSsView, j);
			Log.i("TAG", SSView.t(mSsView) + "");
			if (SSView.t(mSsView) < 0) {
				// 滑到顶
				SSView.j(mSsView, 0);
				SSView.b(mSsView, 0.0F);
			}

			if (SSView.t(mSsView) + mSsView.getMeasuredHeight() > SSView
					.getViewHeight(mSsView)) {
				// 滑到底
				SSView.j(
						mSsView,
						SSView.getViewHeight(mSsView)
								- mSsView.getMeasuredHeight());
				SSView.b(mSsView, (float) (mSsView.getMeasuredHeight() - SSView
						.getViewHeight(mSsView)));
			}

			SSView.setRowOffsetVertical(mSsView, j);
		}

		mSsView.invalidate();
		return false;
	}

	public void onShowPress(MotionEvent paramMotionEvent) {
	}

	public boolean onSingleTapConfirmed(MotionEvent paramMotionEvent) {
		return false;
	}

	public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
		// 列数
		int i = SSView.a(mSsView, (int) paramMotionEvent.getX());
		// 排数
		int j = SSView.b(mSsView, (int) paramMotionEvent.getY());

		// if ((j >= 0 && j < SSView.b(mSsView).size())) {
		if ((j >= 0 && j < SSView.c(mSsView).size())) {
			// if (i >= 0
			// && i < ((ArrayList<Integer>) (SSView.b(mSsView).get(j)))
			// .size()) {
			if (i >= 0 && i < SSView.c(mSsView).get(j).getSeatList().size()) {
				Log.i("TAG", "排数：" + j + "列数：" + i);
				// ArrayList<Integer> localArrayList = (ArrayList<Integer>)
				// SSView
				// .b(mSsView).get(j);
				ArrayList<Seat> mSeatList = SSView.c(mSsView).get(j)
						.getSeatList();
				switch (mSeatList.get(i).getStatus()) {
				case Seat.SeatStatus.SELECTED: // 已选中
					mSeatList.get(i).setStatus(1);
					// localArrayList.set(i, Integer.valueOf(1));
					if (SSView.d(mSsView) != null) {
						SSView.d(mSsView).a(i, j, false);
					}
					SSView.delCurrentSelect(mSsView, i + 1, j + 1);
					break;
				case Seat.SeatStatus.SELECTABLE:
					// 可选
					if (SSView.getcurrentSelect(mSsView).size() == SSView
							.getImaxPay(mSsView)) {
						Toast.makeText(SSView.getContext(mSsView),
								"最多只能选" + SSView.getImaxPay(mSsView) + "张",
								Toast.LENGTH_SHORT).show();
					} else {
						// localArrayList.set(i, Integer.valueOf(3));
						mSeatList.get(i).setStatus(3);
						if (SSView.d(mSsView) != null) {
							SSView.d(mSsView).b(i, j, false);
						}
						SSView.addCurrentSelect(mSsView, new SeatSelect(i + 1,
								j + 1));
					}
					break;
				default:
					break;
				}

			}
		}

		// 显示缩略图
		SSView.a(mSsView, true);
		mSsView.invalidate();
		return false;
	}
}