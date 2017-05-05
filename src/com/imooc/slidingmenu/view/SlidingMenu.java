package com.imooc.slidingmenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {
	
	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;
	
	private int mMenuWidth;
	
	//dp
	private int mMenuRightPadding=50;
	
	private boolean once;
	
	private boolean isOpen;
	
	/*
	 * δʹ���Զ�������ʱ������
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics=new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);		
		mScreenWidth=outMetrics.widthPixels;
		
		//��dpת��Ϊpx
		mMenuRightPadding=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, 
				context.getResources().getDisplayMetrics());
	}
	
	/*
	 * ������View�Ŀ�͸�
	 * �����Լ��Ŀ�͸�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(!once){
			mWapper=(LinearLayout) getChildAt(0);
			mMenu=(ViewGroup) mWapper.getChildAt(0);
			mContent=(ViewGroup) mWapper.getChildAt(1);		
			mMenuWidth=mMenu.getLayoutParams().width=mScreenWidth-mMenuRightPadding;
			mContent.getLayoutParams().width=mScreenWidth;
			once=true;
		}		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	/*
	 * ͨ������ƫ��������menu����
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);		
		if (changed) {
			this.scrollTo(mMenuWidth,0);
		}		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action=ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			// ��������ߵĿ��
			int scrollX=getScrollX();
			if (scrollX>=mMenuWidth/2) {
				this.smoothScrollTo(mMenuWidth,0);	
				isOpen=false;
			}else {
				this.smoothScrollTo(0, 0);
				isOpen=true;
			}
			return true;		
		}
		
		return super.onTouchEvent(ev);
	}
	
	/*
	 * �򿪲˵�
	 */
	public void openMenu() {
		if(isOpen) return;
		this.smoothScrollTo(0, 0);
		isOpen=true;	
	}
	
	public void closeMenu() {
		if(!isOpen) return;
		this.smoothScrollTo(mMenuWidth,0);
		isOpen=false;		
	}
	
	/*
	 * �л��˵�
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		}else {
			openMenu();
		}
	}
	
		
}
