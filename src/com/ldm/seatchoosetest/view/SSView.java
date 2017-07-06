package com.ldm.seatchoosetest.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ldm.seatchoosetest.OnSeatClickListener;
import com.ldm.seatchoosetest.R;
import com.ldm.seatchoosetest.model.Seat;
import com.ldm.seatchoosetest.model.SeatInfo;
import com.ldm.seatchoosetest.model.SeatSelect;

public class SSView extends View {
	Context mContext;
	private String TAG = "SSView";
	/** 普通状态 */
	private Bitmap mBitMapSeatNormal = null;
	/** 已锁定 */
	private Bitmap mBitMapSeatLock = null;
	/** 已选中 */
	private Bitmap mBitMapSeatChecked = null;
	/** 缩略图画布 */
	private Canvas mCanvas = null;
	/** 是否初始化显示缩略图U */
	private boolean showThumbnail = false;
	/** 每个座位的高度 -> 57 */
	private int ss_seat_current_height = 57;
	/** 每个座位的宽度 */
	private int ss_seat_current_width = 57;
	/** 座位之间的间距L */
	private int distanceBetweenSeats = 5;
	private double T = 1.0D;
	private double preTwoPointDistance = -1.0D;
	/** 缩放比例 */
	private double zoomRatio = 1.0D;
	/** 座位最小高度 */
	private int ss_seat_min_height = 0;
	/** 座位最大高度 */
	private int ss_seat_max_height = 0;
	/** 座位最小宽度 */
	private int ss_seat_min_width = 0;
	/** 座位最大宽度 */
	private int ss_seat_max_width = 0;

	private int I = 0;
	private int ss_between_offset = 2;
	private int ss_seat_check_size = 30;
	private SSThumView mSSThumView = null;
	private int ss_seat_thum_size_w = 120;
	private int ss_seat_thum_size_h = 90;
	private int ss_seat_rect_line = 2;// 缩略图黄色框的厚度/最佳观影位置的虚线宽度
	private int center_line = 4;// 中线的宽度/屏幕轮廓宽
	/** 选座缩略图 */
	private Bitmap mBitMapThumView = null;
	private volatile int V = 1500;
	/** 排数x轴偏移量n */
	private float YaxisOffset_horizontal = 0.0F;
	/** 排数y轴偏移量o */
	private float YaxisOffset_vertical = 0.0F;
	/** 列数x轴偏移量 */
	private float XaxisOffset_horizontal = 0.0F;
	/** 列数y轴偏移量 */
	private float XaxisOffset_vertical = 0.0F;
	private float XaxisOffset_vertical_min = 0.0F;
	private float XaxisOffset_vertical_max = 0.0F;
	/** 座位距离排数的距离p */
	private int distanceBetweenSeatAndYAxis = 0;
	/** 座位距离列数的距离 */
	private int distanceBetweenSeatAndXAxis = 0;
	/** 可视座位距离顶端的距离q */
	private int distanceBetweenVisibleSeatAndTop = 0;
	/** 整个view的宽度r */
	private int view_width = 0;
	/** 整个view的高度s */
	private int view_height = 0;
	/** 顶部屏幕高度 */
	private int margin_top = 100;// 还会影响计算行和排的方法 SSView.a()和SSView.b()
	/** 缩略图顶部屏幕高度 */
	private int thum_margin_top = 30;
	/** 能否移动w */
	private boolean canMove = true;
	/** 是否可缩放v */
	private boolean canZoom = false;

	private boolean first_load_bg = true;
	private int tempX;// 缩略图实际宽
	private int tempY;// 缩略图实际高
	private ArrayList<SeatInfo> mListSeatInfos = null;
	private int iMaxPay = 0;// 最大支付座位数
	private ArrayList<SeatSelect> currentSelect = null;
	private int totalCountEachRow;
	private int rows;
	// 最佳观赏区域
	private int bestview_x_min = 0;
	private int bestview_x_max = 0;
	private int bestview_y_min = 0;
	private int bestview_y_max = 0;
	//
	private OnSeatClickListener mOnSeatClickListener = null;
	GestureDetector mGestureDetector = new GestureDetector(mContext,
			new GestureListener(this));

	public SSView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 0);
	}

	public SSView(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		this.mContext = paramContext;
	}

	/**
	 * 
	 * @param row_count列数
	 * @param rows行数
	 * @param list_seatInfos每行情况
	 * @param list_seat_condtions
	 * @param paramSSThumView缩略图
	 * @param imaxPay最大选择数
	 * @param bestview_x_min最佳观影
	 * @param bestview_x_max最佳观影
	 * @param bestview_y_min最佳观影
	 * @param bestview_y_max最佳观影
	 */
	public void init(int row_count, int rows,
			ArrayList<SeatInfo> list_seatInfos, SSThumView paramSSThumView,
			int imaxPay, int bestview_x_min, int bestview_x_max,
			int bestview_y_min, int bestview_y_max) {
		this.bestview_x_min = bestview_x_min;
		this.bestview_x_max = bestview_x_max;
		this.bestview_y_min = bestview_y_min;
		this.bestview_y_max = bestview_y_max;
		this.iMaxPay = imaxPay;
		this.currentSelect = new ArrayList<SeatSelect>();
		this.mSSThumView = paramSSThumView;
		this.totalCountEachRow = row_count;
		this.rows = rows;
		this.mListSeatInfos = list_seatInfos;
		// 普通、已锁定和已选中状态图片
		this.mBitMapSeatNormal = getBitmapFromDrawable((BitmapDrawable) mContext
				.getResources().getDrawable(R.drawable.seat_normal));
		this.mBitMapSeatLock = getBitmapFromDrawable((BitmapDrawable) mContext
				.getResources().getDrawable(R.drawable.seat_lock));
		this.mBitMapSeatChecked = getBitmapFromDrawable((BitmapDrawable) mContext
				.getResources().getDrawable(R.drawable.seat_checked));
		// 缩略图宽高
		this.ss_seat_thum_size_w = mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_seat_thum_size_w);
		this.ss_seat_thum_size_h = mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_seat_thum_size_h);
		// 座位最大最小宽高
		this.ss_seat_max_height = mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_max_height);
		this.ss_seat_max_width = mContext.getResources().getDimensionPixelSize(
				R.dimen.seat_max_width);
		this.ss_seat_min_height = mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_min_height);
		this.ss_seat_min_width = mContext.getResources().getDimensionPixelSize(
				R.dimen.seat_min_width);
		// 当前座位宽高
		this.ss_seat_current_height = mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_init_height);
		this.ss_seat_current_width = mContext.getResources()
				.getDimensionPixelSize(R.dimen.seat_init_width);

		this.ss_seat_check_size = mContext.getResources()
				.getDimensionPixelSize(R.dimen.ss_seat_check_size);// 30dp
		this.ss_between_offset = mContext.getResources().getDimensionPixelSize(
				R.dimen.ss_between_offset);// 5dp
		distanceBetweenSeats = calculateDistanceBetweenSeats();
		invalidate();
	}

	public static Bitmap getBitmapFromDrawable(
			BitmapDrawable paramBitmapDrawable) {
		return paramBitmapDrawable.getBitmap();
	}

	/**
	 * 绘制座位
	 * 
	 * @param seatNum
	 *            每排的座位顺序号
	 * @param rowNum
	 *            排号
	 * @param paramBitmap
	 *            座位对应图片
	 * @param paramCanvas1
	 *            选座控件画布
	 * @param paramCanvas2
	 *            缩略图画布
	 * @param paramPaint
	 */
	private void drawASeat(int seatNum, int rowNum, Bitmap paramBitmap,
			Canvas paramCanvas1, Canvas paramCanvas2, Paint paramPaint) {
		if (paramBitmap == null) {
			// 走道
			paramCanvas1.drawRect(getSeatRect(seatNum, rowNum), paramPaint);
			if (showThumbnail) {
				paramCanvas2.drawRect(getSeatRectForThumb(seatNum, rowNum),
						paramPaint);

			}
		} else {
			paramCanvas1.drawBitmap(paramBitmap, null,
					getSeatRect(seatNum, rowNum), paramPaint);
			if (showThumbnail) {
				paramCanvas2.drawBitmap(paramBitmap, null,
						getSeatRectForThumb(seatNum, rowNum), paramPaint);
			}
		}
	}

	/**
	 * 绘制屏幕
	 */
	private void drawcreens(Canvas paramCanvas) {
		// 屏幕区域高margin_top
		// 宽view_width
		Paint paint = new Paint();
		paint.setColor(Color.LTGRAY);//
		paint.setStrokeWidth(center_line); //
		paint.setStyle(Paint.Style.FILL);
		// 画梯形
		//
		float screen_height = margin_top * 0.6f;// 显示的屏幕的高度
		Path path2 = new Path();
		path2.reset();
		path2.moveTo(view_width / 4, 0); // 左顶点
		path2.lineTo(view_width - view_width / 4, 0); // 右顶点
		path2.lineTo(view_width - (view_width / 4 + ss_seat_current_width),
				screen_height); // 右底部
		path2.lineTo(view_width / 4 + ss_seat_current_width, screen_height); // 左底部
		paramCanvas.drawPath(path2, paint);
		//
		// 文字
		Paint textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(40);
		textPaint.setStyle(Paint.Style.FILL);
		// http://blog.csdn.net/zly921112/article/details/50401976
		// 该方法即为设置基线上那个点究竟是left,center,还是right 这里我设置为center
		textPaint.setTextAlign(Paint.Align.CENTER);// 水平居中
		FontMetricsInt fontMetrics = paint.getFontMetricsInt();
		int baseline = (int) ((screen_height - fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top);// 垂直居中
		paramCanvas.drawText("屏　　幕", view_width / 2, baseline, textPaint);
	}

	/**
	 * 座位对应矩形区域
	 * 
	 * @param seatNum
	 *            每排的座位号
	 * @param rowNum
	 *            排号
	 * @return
	 */
	private Rect getSeatRect(int seatNum, int rowNum) {
		try {
			Rect localRect = new Rect(seatNum * ss_seat_current_width
					+ distanceBetweenSeats, rowNum * ss_seat_current_height
					+ distanceBetweenSeats + margin_top, (seatNum + 1)
					* ss_seat_current_width - distanceBetweenSeats,
					(rowNum + 1) * ss_seat_current_height
							- distanceBetweenSeats + margin_top);
			return localRect;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new Rect();
	}

	/**
	 * 缩略图中座位对应矩形区域
	 * 
	 * @param seatNum
	 * @param rowNum
	 * @return
	 */
	private Rect getSeatRectForThumb(int seatNum, int rowNum) {
		try {
			Rect localRect = new Rect(
					5 + (int) (T * (seatNum * ss_seat_current_width + distanceBetweenSeats)),
					5
							+ (int) (T * (rowNum * ss_seat_current_height + distanceBetweenSeats))
							+ thum_margin_top,
					5 + (int) (T * ((seatNum + 1) * ss_seat_current_width - distanceBetweenSeats)),
					5
							+ (int) (T * ((rowNum + 1) * ss_seat_current_height - distanceBetweenSeats))
							+ thum_margin_top);
			return localRect;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new Rect();
	}

	private Rect getThumBorderRect(int paramInt1, int paramInt2) {
		int i1;
		int i3;
		try {
			if (getMeasuredWidth() < view_width) {
				i1 = getMeasuredWidth();
			} else {
				i1 = view_width;
			}
			if (getMeasuredHeight() < view_height) {
				i3 = getMeasuredHeight();
			} else {
				i3 = view_height;
			}
			// return new Rect((int) (5.0D + T * paramInt1), (int) (5.0D + T
			// * paramInt2), (int) (5.0D + T * paramInt1 + i1 * T),
			// (int) (5.0D + T * paramInt2 + i3 * T));
			return new Rect((int) (5.0D + T * paramInt1), (int) (T * paramInt2)
					+ thum_margin_top, (int) (5.0D + T * (paramInt1 + i1)),
					(int) (T * (paramInt2 + i3)) + thum_margin_top);
		} catch (Exception e) {
			e.printStackTrace();
			return new Rect();
		}
	}

	/**
	 * 最佳观赏区域对应矩形区域
	 * 
	 * @param seatNum
	 * @param rowNum
	 * @return
	 */
	private Rect getBestViewSeat() {
		try {
			Rect localRect = new Rect((bestview_x_min - 1)
					* ss_seat_current_width + distanceBetweenSeats / 2,
					(bestview_y_min - 1) * ss_seat_current_height + margin_top,
					bestview_x_max * ss_seat_current_width
							+ distanceBetweenSeats / 2, bestview_y_max
							* ss_seat_current_height + margin_top);
			return localRect;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return new Rect();
	}

	/**
	 * 缩略图最佳观影区域区域
	 * 
	 * @param seatNum
	 * @param rowNum
	 * @return
	 */
	private Rect getBestViewSeatRectForThumb() {
		try {
			Rect localRect = new Rect(
					5 + (int) (T * ((bestview_x_min - 1)
							* ss_seat_current_width + distanceBetweenSeats / 2)),
					5
							+ (int) (T * ((bestview_y_min - 1)
									* ss_seat_current_height + distanceBetweenSeats / 2))
							+ thum_margin_top,
					5 + (int) (T * (bestview_x_max * ss_seat_current_width - distanceBetweenSeats / 2)),
					5
							+ (int) (T * (bestview_y_max
									* ss_seat_current_height - distanceBetweenSeats / 2))
							+ thum_margin_top);
			return localRect;
		} catch (Exception e) {
			e.printStackTrace();
			return new Rect();
		}
	}

	@Override
	protected void onDraw(Canvas paramCanvas) {
		super.onDraw(paramCanvas);
		if (totalCountEachRow == 0 || rows == 0) {
			return;
		}
		if (YaxisOffset_horizontal + view_width < 0.0f
				|| YaxisOffset_vertical + view_height < 0.0f) {
			YaxisOffset_horizontal = 0.0f;
			YaxisOffset_vertical = 0.0f;
			distanceBetweenSeatAndYAxis = 0;
			distanceBetweenVisibleSeatAndTop = 0;
		}
		Paint localPaint2 = new Paint();
		if (ss_seat_current_width != 0 && ss_seat_current_height != 0) {
			mBitMapThumView = Bitmap.createBitmap(ss_seat_thum_size_w,
					ss_seat_thum_size_h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas();
			mCanvas.setBitmap(mBitMapThumView);
			mCanvas.save();

			Paint localPaint1 = new Paint();
			localPaint1.setXfermode(new PorterDuffXfermode(
					PorterDuff.Mode.CLEAR));
			mCanvas.drawPaint(localPaint1);

			double d1 = (ss_seat_thum_size_w - 10.0D)
					/ (ss_seat_current_width * totalCountEachRow);
			// v0/v2
			double d2 = (ss_seat_thum_size_h - 10.0D)
					/ (ss_seat_current_height * rows);
			if (d1 <= d2) {
				T = d1;
			} else {
				T = d2;
			}
			if (showThumbnail) {
				localPaint2.setColor(-16777216);// 缩略图背景色
				if (first_load_bg) {
					first_load_bg = false;
					tempX = 5 + (int) (view_width * T);
					tempY = 5 + (int) ((view_height - margin_top) * T);
				}
				mCanvas.drawRect(5.0F, 5.0F, tempX, tempY + thum_margin_top,
						localPaint2);
				//
				// 缩略图最佳观赏区域
				Paint localPaint3 = new Paint();
				localPaint3.setColor(Color.RED);
				localPaint3.setStyle(Paint.Style.FILL);
				mCanvas.drawRect(getBestViewSeatRectForThumb(), localPaint3);
				// 缩略图中线
				localPaint3.setStyle(Paint.Style.STROKE);
				localPaint3.setColor(Color.WHITE);
				localPaint3.setStrokeWidth(center_line);
				Path path = new Path();
				path.reset();
				path.moveTo(ss_seat_thum_size_w / 2, thum_margin_top);
				path.lineTo(ss_seat_thum_size_w / 2, tempY + thum_margin_top);
				DashPathEffect effects = new DashPathEffect(new float[] { 10,
						10, 10, 10 }, 1);
				localPaint3.setPathEffect(effects);
				mCanvas.drawPath(path, localPaint3);
				localPaint3.setPathEffect(null);
				// 缩略图画屏幕
				// Paint localPaint4 = new Paint();
				localPaint3.setColor(Color.LTGRAY);
				localPaint3.setStyle(Paint.Style.FILL);
				mCanvas.drawRect(new Rect(5 + tempX / 4, 5, tempX - tempX / 4,
						(int) (5 + 0.6 * thum_margin_top)), localPaint3);
				// mCanvas.drawRect(new Rect(thum_margin_top / 4, 0,
				// thum_margin_top - thum_margin_top / 4, (int) 0.6
				// * ss_seat_thum_size_h), localPaint4);
			}
		}

		// 画座位
		paramCanvas.translate(YaxisOffset_horizontal, YaxisOffset_vertical);
		// margin_left = (int) Math.round(ss_seat_current_width / 2.0D);
		view_width = ss_seat_current_width * totalCountEachRow;
		view_height = ss_seat_current_height * rows + margin_top;
		if (XaxisOffset_vertical == 0) {
			// this.getHeight()->控件高度 view_height->画布高度
			XaxisOffset_vertical_min = getHeight();
			XaxisOffset_vertical_max = view_height;
			XaxisOffset_vertical = XaxisOffset_vertical_min
					+ Math.abs(YaxisOffset_vertical);
		}
		localPaint2.setTextAlign(Paint.Align.CENTER);
		localPaint2.setAntiAlias(true);
		localPaint2.setColor(-16777216);
		for (int y = 0; y < mListSeatInfos.size(); y++) {
			// ArrayList<Integer> localArrayList = (ArrayList<Integer>)
			// mListSeatInfos.
			// .get(y);
			for (int x = 0; x < mListSeatInfos.get(y).getSeatList().size(); x++) {
				// switch (((Integer) localArrayList.get(x)).intValue()) {
				switch (mListSeatInfos.get(y).getSeatList().get(x).getStatus()) {
				case Seat.SeatStatus.WALKWAY:// 走道
					localPaint2.setColor(0);
					drawASeat(x, y, null, paramCanvas, mCanvas, localPaint2);
					localPaint2.setColor(-16777216);
					break;
				case Seat.SeatStatus.SELECTABLE:// 可选
					drawASeat(x, y, mBitMapSeatNormal, paramCanvas, mCanvas,
							localPaint2);
					break;
				case Seat.SeatStatus.LOCKED:// 锁定
					drawASeat(x, y, mBitMapSeatLock, paramCanvas, mCanvas,
							localPaint2);
					break;
				case Seat.SeatStatus.SELECTED:// 已选
					drawASeat(x, y, mBitMapSeatChecked, paramCanvas, mCanvas,
							localPaint2);
					break;
				default:
					break;
				}
			}
		}
		// 画屏幕
		drawcreens(paramCanvas);
		// 画最佳观赏区域
		localPaint2.setColor(Color.RED);
		localPaint2.setStyle(Paint.Style.STROKE);
		localPaint2.setStrokeWidth(ss_seat_rect_line);
		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		localPaint2.setPathEffect(effects);
		paramCanvas.drawRect(getBestViewSeat(), localPaint2);
		localPaint2.setStyle(Paint.Style.FILL);
		localPaint2.setPathEffect(null);
		// 画中线
		localPaint2.setStyle(Paint.Style.STROKE);
		localPaint2.setColor(Color.DKGRAY);
		localPaint2.setStrokeWidth(center_line);
		Path path = new Path();
		path.reset();
		path.moveTo(view_width / 2, margin_top);
		path.lineTo(view_width / 2, view_height + margin_top);
		effects = new DashPathEffect(new float[] { 10, 10, 10, 10 }, 1);
		localPaint2.setPathEffect(effects);
		paramCanvas.drawPath(path, localPaint2);
		localPaint2.setStyle(Paint.Style.FILL);
		localPaint2.setPathEffect(null);
		// 画排数 -> Y轴
		localPaint2.setTextSize(0.3F * ss_seat_current_height);
		// 背景颜色
		localPaint2.setColor(Color.argb(0x2e, 0x00, 0x00, 0x00));
		paramCanvas.drawRoundRect(
				new RectF(Math.abs(YaxisOffset_horizontal), 0
						* ss_seat_current_height + margin_top, Math
						.abs(YaxisOffset_horizontal)
						+ ss_seat_current_width
						/ 2, rows * ss_seat_current_height + margin_top), 99,
				99, localPaint2);
		for (int i1 = 0; i1 < mListSeatInfos.size(); i1++) {
			localPaint2.setColor(-1);
			// 文字
			paramCanvas.drawText(((SeatInfo) mListSeatInfos.get(i1)).getDes(),
					(int) Math.abs(YaxisOffset_horizontal)
							+ ss_seat_current_width / 2 / 2, i1
							* ss_seat_current_height + ss_seat_current_height
							/ 2 + distanceBetweenSeats / 2 + margin_top,
					localPaint2);
		}

		// 画列数 -> X轴
		// for (int i = 0; i < mListSeatInfos.get(0).getSeatList().size(); i++)
		// {
		// localPaint2.setColor(-1308622848);
		// paramCanvas.drawRect(
		// new Rect(margin_left + i * ss_seat_current_width,
		// (int) Math.abs(XaxisOffset_vertical), margin_left
		// + (i + 1) * ss_seat_current_width,
		// (int) Math.abs(XaxisOffset_vertical)
		// + ss_seat_current_height / 2), localPaint2);
		// localPaint2.setColor(-1);
		// // 文字
		// paramCanvas.drawText(String.valueOf(i + 1), margin_left + i
		// * ss_seat_current_width + ss_seat_current_width / 2
		// + margin_right / 2, (int) Math.abs(XaxisOffset_vertical)
		// + ss_seat_current_height / 3, localPaint2);
		// }

		if (showThumbnail) {
			// 画缩略图的黄色框
			localPaint2.setColor(-739328);
			localPaint2.setStyle(Paint.Style.STROKE);
			localPaint2.setStrokeWidth(ss_seat_rect_line);
			mCanvas.drawRect(
					getThumBorderRect((int) Math.abs(YaxisOffset_horizontal),
							(int) Math.abs(YaxisOffset_vertical)), localPaint2);
			localPaint2.setStyle(Paint.Style.FILL);
			//
			mCanvas.restore();
		}

		if (mSSThumView != null) {
			mSSThumView.a(mBitMapThumView);
			mSSThumView.invalidate();
		}
	}

	/**
	 * 获取两点的直线距离
	 * 
	 * @param paramMotionEvent
	 * @return
	 */
	private float getTwoPoiniterDistance(MotionEvent paramMotionEvent) {
		float f1 = paramMotionEvent.getX(0) - paramMotionEvent.getX(1);
		float f2 = paramMotionEvent.getY(0) - paramMotionEvent.getY(1);
		return FloatMath.sqrt(f1 * f1 + f2 * f2);
	}

	private void zoom(MotionEvent paramMotionEvent) {
		double twoPointDistance = getTwoPoiniterDistance(paramMotionEvent);
		if (preTwoPointDistance < 0.0D) {
			preTwoPointDistance = twoPointDistance;
		} else {
			try {
				// 缩放比例
				zoomRatio = (twoPointDistance / preTwoPointDistance);
				System.out.println("----zoomRatio:" + zoomRatio);
				preTwoPointDistance = twoPointDistance;
				if ((canZoom)
						&& (Math.round(zoomRatio * ss_seat_current_width) > 0L)
						&& (Math.round(zoomRatio * ss_seat_current_height) > 0L)) {
					ss_seat_current_width = (int) Math.round(zoomRatio
							* ss_seat_current_width);
					ss_seat_current_height = (int) Math.round(zoomRatio
							* ss_seat_current_height);
					{
						// 新增
						XaxisOffset_vertical = 0;
					}
					distanceBetweenSeats = (int) Math.round(zoomRatio
							* distanceBetweenSeats);
					if (distanceBetweenSeats <= 0)
						distanceBetweenSeats = 1;
				}
				invalidate();
			} catch (Exception localException) {
				localException.printStackTrace();
			}
		}
	}

	public static int m(SSView mSsView, int paramInt) {
		mSsView.V = mSsView.V - paramInt;
		return mSsView.V;
	}

	public static int x(SSView mSsView) {
		return mSsView.V;
	}

	public static void y(SSView mSsView) {
		mSsView.a();
	}

	private void a() {
		// postDelayed(new ag(this), 500L);
	}

	public static float w(SSView mSsView) {
		return mSsView.YaxisOffset_vertical;
	}

	/**
	 * 获取排数x轴偏移量
	 * 
	 * @param mSsView
	 * @return
	 */
	public static float v(SSView mSsView) {
		return mSsView.YaxisOffset_horizontal;
	}

	/**
	 * 获取整个view的高度u
	 * 
	 * @param mSsView
	 * @return
	 */
	public static int getViewHeight(SSView mSsView) {
		return mSsView.view_height;
	}

	public static int getmyMeasuredWidth(SSView mSsView) {
		return mSsView.getMeasuredWidth();
	}

	public static int getmyMeasuredHeight(SSView mSsView) {
		return mSsView.getMeasuredHeight();
	}

	/**
	 * 获取可视座位距离顶端的距离
	 * 
	 * @param mSsView
	 * @return
	 */
	public static int t(SSView mSsView) {
		return mSsView.distanceBetweenVisibleSeatAndTop;
	}

	/**
	 * 获取整个view的宽度
	 * 
	 * @param mSsView
	 * @return
	 */
	public static int getViewWidth(SSView mSsView) {
		return mSsView.view_width;
	}

	/**
	 * 获取座位距离排数的横向距离
	 * 
	 * @param mSsView
	 * @return
	 */
	public static int r(SSView mSsView) {
		return mSsView.distanceBetweenSeatAndYAxis;
	}

	public static int p(SSView mSsView) {
		return mSsView.rows;
	}

	public static int n(SSView mSsView) {
		return mSsView.totalCountEachRow;
	}

	/**
	 * 修改可见座位距离顶端的距离
	 * 
	 * @param mSsView
	 * @return
	 */
	public static int l(SSView mSsView, int paramInt) {
		mSsView.distanceBetweenVisibleSeatAndTop = mSsView.distanceBetweenVisibleSeatAndTop
				+ paramInt;
		return mSsView.distanceBetweenVisibleSeatAndTop;
	}

	public static int l(SSView mSsView) {
		return mSsView.ss_seat_current_width;
	}

	public static int k(SSView mSsView) {
		return mSsView.ss_seat_current_width;
	}

	public static int k(SSView mSsView, int paramInt) {
		mSsView.distanceBetweenSeatAndYAxis = mSsView.distanceBetweenSeatAndYAxis
				+ paramInt;
		return mSsView.distanceBetweenSeatAndYAxis;
	}

	public static int j(SSView mSsView) {
		return mSsView.ss_seat_current_height;
	}

	/**
	 * 设置可视座位距离顶端的距离
	 * 
	 * @param mSsView
	 * @param paramInt
	 * @return
	 */
	public static int j(SSView mSsView, int paramInt) {
		mSsView.distanceBetweenVisibleSeatAndTop = paramInt;
		return mSsView.distanceBetweenVisibleSeatAndTop;
	}

	/**
	 * 设置座位距离排数的横向距离
	 * 
	 * @param mSsView
	 * @return
	 */
	public static int i(SSView mSsView, int paramInt) {
		mSsView.distanceBetweenSeatAndYAxis = paramInt;
		return mSsView.distanceBetweenSeatAndYAxis;
	}

	public static boolean i(SSView mSsView) {
		return mSsView.showThumbnail;
	}

	public static int setViewHeight(SSView mSsView, int paramInt) {
		return mSsView.view_height;
	}

	public static int h(SSView mSsView) {
		return mSsView.I + 1;
	}

	public static int g(SSView mSsView, int paramInt) {
		return mSsView.view_width;
	}

	/**
	 * 获取最大支付座位数
	 * 
	 * @param mSsView
	 * @return
	 */
	public static int getImaxPay(SSView mSsView) {
		return mSsView.iMaxPay;
	}

	/**
	 * 获取已经选中的
	 * 
	 * @param mSsView
	 * @return
	 */
	public static ArrayList<SeatSelect> getcurrentSelect(SSView mSsView) {
		return mSsView.currentSelect;
	}

	/**
	 * 删除已经选中的
	 * 
	 * @param mSsView
	 * @param x
	 * @param y
	 */
	public static void delCurrentSelect(SSView mSsView, int x, int y) {
		try {
			for (int i = 0; i < mSsView.currentSelect.size(); i++) {
				if (mSsView.currentSelect.get(i).getX() == x
						&& mSsView.currentSelect.get(i).getY() == y) {
					mSsView.currentSelect.remove(i);
					return;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 添加已经选中的
	 * 
	 * @param mSsView
	 * @param x
	 * @param y
	 */
	public static void addCurrentSelect(SSView mSsView, SeatSelect select) {
		mSsView.currentSelect.add(select);
	}

	public static boolean a(SSView mSsView, boolean param) {
		mSsView.showThumbnail = param;
		return mSsView.showThumbnail;
	}

	public static Context getContext(SSView mSsView) {
		return mSsView.mContext;
	}

	/**
	 * 设置排数x轴偏移量
	 * 
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float a(SSView mSsView, float param) {
		mSsView.YaxisOffset_horizontal = param;
		return mSsView.YaxisOffset_horizontal;
	}

	/**
	 * 计算是第几列
	 * 
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static int a(SSView mSsView, int param) {
		return mSsView.a(param);
	}

	/**
	 * 计算是第几列
	 * 
	 * @param paramInt
	 * @return
	 */
	private int a(int paramInt) {
		try {
			int i1 = (paramInt + distanceBetweenSeatAndYAxis)
					/ ss_seat_current_width;
			return i1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return -1;
	}

	public static Rect a(SSView mSsView, int param1, int param2) {
		return mSsView.f(param1, param2);
	}

	private Rect f(int paramInt1, int paramInt2) {
		try {
			// int v1 = ss_seat_current_width * paramInt1
			// - distanceBetweenSeatAndYAxis - distanceBetweenSeats;
			// int v2 = ss_seat_current_height * paramInt2
			// - distanceBetweenVisibleSeatAndTop - distanceBetweenSeats;
			// int v3 = (paramInt1 + 1) * ss_seat_current_width
			// - distanceBetweenSeatAndYAxis + distanceBetweenSeats;
			// int v4 = 1 * ss_seat_current_height
			// - distanceBetweenVisibleSeatAndTop + distanceBetweenSeats;
			// return new Rect(v1, v2, v3, v4);
			try {
				int v1 = ss_seat_current_width * paramInt1
						+ distanceBetweenSeatAndYAxis - distanceBetweenSeats;
				int v2 = ss_seat_current_height * paramInt2 + margin_top
						- distanceBetweenVisibleSeatAndTop
						- distanceBetweenSeats;
				int v3 = (paramInt1 + 1) * ss_seat_current_width
						+ distanceBetweenSeatAndYAxis + distanceBetweenSeats;
				int v4 = (margin_top + 1) * ss_seat_current_height + margin_top
						- distanceBetweenVisibleSeatAndTop
						+ distanceBetweenSeats;
				return new Rect(v1, v2, v3, v4);
			} catch (Exception e) {
				e.printStackTrace();
				return new Rect();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Rect();
		}
	}

	/**
	 * 是否可以移动和点击
	 * 
	 * @param mSsView
	 * @return
	 */
	public static boolean isCanMove(SSView mSsView) {
		return mSsView.canMove;
	}

	/**
	 * 计算座位间的距离
	 * 
	 * @return
	 */
	private int calculateDistanceBetweenSeats() {
		return (int) Math.round(ss_seat_current_width / ss_seat_check_size
				* ss_between_offset);
	}

	/**
	 * 修改排数x轴的偏移量
	 * 
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float c(SSView mSsView, float param) {
		mSsView.YaxisOffset_horizontal = mSsView.YaxisOffset_horizontal - param;
		return mSsView.YaxisOffset_horizontal;
	}

	/**
	 * 设置每个座位的高度
	 * 
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float c(SSView mSsView, int param) {
		mSsView.ss_seat_current_height = param;
		return mSsView.ss_seat_current_height;
	}

	public static ArrayList<SeatInfo> c(SSView mSsView) {
		return mSsView.mListSeatInfos;
	}

	/**
	 * 修改排数y轴的偏移量
	 * 
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float d(SSView mSsView, float param) {
		mSsView.YaxisOffset_vertical = mSsView.YaxisOffset_vertical - param;
		return mSsView.YaxisOffset_vertical;
	}

	public static int d(SSView mSsView, int param) {
		mSsView.ss_seat_current_width = param;
		return mSsView.ss_seat_current_width;
	}

	public static OnSeatClickListener d(SSView mSsView) {
		return mSsView.mOnSeatClickListener;
	}

	/**
	 * 设置排数y轴偏移量
	 * 
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static float b(SSView mSsView, float param) {
		mSsView.YaxisOffset_vertical = param;
		return mSsView.YaxisOffset_vertical;
	}

	/**
	 * 计算是第几排
	 * 
	 * @param mSsView
	 * @param param
	 * @return
	 */
	public static int b(SSView mSsView, int param) {
		return mSsView.b(param);
	}

	/**
	 * 计算是第几排
	 * 
	 * @param paramInt
	 * @return
	 */
	private int b(int paramInt) {
		try {
			int i1 = (paramInt + distanceBetweenVisibleSeatAndTop - margin_top)
					/ ss_seat_current_height;
			return i1;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return -1;
	}

	public static int e(SSView mSsView) {
		mSsView.I--;
		return mSsView.I;
	}

	public static int f(SSView mSsView) {
		return mSsView.I;
	}

	/**
	 * 设置按钮点击事件
	 * 
	 * @param paramOnSeatClickLinstener
	 */
	public void setOnSeatClickListener(
			OnSeatClickListener paramOnSeatClickLinstener) {
		this.mOnSeatClickListener = paramOnSeatClickLinstener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getPointerCount() == 1) {
			if (canZoom) {
				canZoom = false;
				canMove = false;
				preTwoPointDistance = -1.0D;
				zoomRatio = 1.0D;
			} else {
				canMove = true;
			}
			// Log.e(TAG, "单点触控");
			while (ss_seat_current_width < ss_seat_min_width
					|| ss_seat_current_height < ss_seat_min_height) {
				// 超过最小尺寸时放大回至最小尺寸
				ss_seat_current_width++;
				ss_seat_current_height++;
				XaxisOffset_vertical = 0;
				distanceBetweenSeats = calculateDistanceBetweenSeats();
				// 滑到最左和最上
				SSView.i(this, 0);
				SSView.a(this, 0.0F);
				SSView.j(this, 0);
				SSView.b(this, 0.0F);
				invalidate();
			}
			while ((ss_seat_current_width > ss_seat_max_width)
					|| (ss_seat_current_height > ss_seat_max_height)) {
				// 超过最大尺寸时缩小回至最大尺寸
				ss_seat_current_width--;
				ss_seat_current_height--;
				XaxisOffset_vertical = 0;
				distanceBetweenSeats = calculateDistanceBetweenSeats();
				invalidate();
			}
			// 移动功能-点击事件
			// Log.e(TAG, "移动功能-点击事件");
			mGestureDetector.onTouchEvent(event);
		} else {
			// Log.e(TAG, "多点触控");
			canZoom = true;
			zoom(event);
		}
		return true;
	}

	// 新增加方法
	public static float setRowOffsetHorizontal(SSView mSsView, float param) {
		mSsView.XaxisOffset_horizontal = mSsView.XaxisOffset_horizontal + param;
		return mSsView.XaxisOffset_horizontal;
	}

	public static float setRowOffsetVertical(SSView mSsView, float param) {
		// param上负下正- 往下滑则减
		float offset = mSsView.XaxisOffset_vertical + param;
		if (offset >= mSsView.XaxisOffset_vertical_min) {
			mSsView.XaxisOffset_vertical = offset > mSsView.XaxisOffset_vertical_max ? mSsView.XaxisOffset_vertical_max
					: offset;
		} else {
			mSsView.XaxisOffset_vertical = mSsView.XaxisOffset_vertical_min;
		}
		return mSsView.XaxisOffset_vertical;
	}

}
