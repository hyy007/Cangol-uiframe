package mobi.cangol.mobile.navigation;

import mobi.cangol.mobile.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class DrawerMenuLayout extends DrawerLayout{
	private FrameLayout	mContentView;
	private FrameLayout	mMenuView;
	private float mMenuWidth=0.70f;
	private boolean isActionBarOverlay;
	
	public DrawerMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mMenuView=new FrameLayout(context);
		mContentView=new FrameLayout(context);
		
		DrawerLayout.LayoutParams lp2=new DrawerLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mContentView.setId(R.id.content_view);
		this.addView(mContentView,lp2);
		
		int width1=(int) (mMenuWidth*context.getResources().getDisplayMetrics().widthPixels);
		DrawerLayout.LayoutParams lp1=new DrawerLayout.LayoutParams(width1, LayoutParams.MATCH_PARENT);
		lp1.gravity=Gravity.LEFT;
		mMenuView.setId(R.id.menu_view);
		this.addView(mMenuView,lp1);
	}
	
	public int getMenuFrameId() {
		return mMenuView.getId();
	}

	public int getContentFrameId() {
		return mContentView.getId();
	}

	public ViewGroup getMenuView() {
		return mMenuView;
	}

	public ViewGroup getContentView() {
		return mContentView;
	}

	public void setContentView(View v) {
		mContentView.removeAllViews();
		mContentView.addView(v);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void showMenu(boolean show) {
		if (show) {
			this.openDrawer(Gravity.LEFT);
		} else {
			this.closeDrawer(Gravity.LEFT);
		}
	}
	
	public boolean isShowMenu() {
		return this.isDrawerOpen(Gravity.LEFT);
	}
	
	public void setMenuEnable(boolean enable) {
		this.setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED
				: DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
	}
	/* 
	 *  (non-Javadoc)
	 * @see android.view.ViewGroup#fitSystemWindows(android.graphics.Rect)
	 */
	@Override
	protected boolean fitSystemWindows(Rect insets) {
		int leftPadding = insets.left;
		int rightPadding = insets.right;
		int topPadding = insets.top;
		int bottomPadding = insets.bottom;
		if (isActionBarOverlay) {
			mContentView.setPadding(mContentView.getPaddingLeft()+leftPadding,
					mContentView.getPaddingTop()+topPadding,
					mContentView.getPaddingRight()+rightPadding,
					mContentView.getPaddingBottom()+bottomPadding);
			mMenuView.setPadding(mMenuView.getPaddingLeft()+leftPadding,
					mMenuView.getPaddingTop()+topPadding,
					mMenuView.getPaddingRight()+rightPadding,
					mMenuView.getPaddingBottom()+bottomPadding);
		}
		return true;
	}

	public void attachToActivity(Activity activity,boolean isActionBarOverlay) {
		// get the window background
		TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowBackground});
		int background = a.getResourceId(0, 0);
		a.recycle();	
		
		//如果已设置过颜色 则不继承theme颜色
		if(mContentView.getBackground()==null){
			mContentView.setBackgroundResource(background);
		}
		
		this.isActionBarOverlay=isActionBarOverlay;
		
		if(isActionBarOverlay){
			ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
			ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
			decor.removeView(decorChild);
			decor.addView(this);
			getContentView().addView(decorChild);
		}else{
			ViewGroup contentParent = (ViewGroup)activity.findViewById(android.R.id.content);
			ViewGroup content = (ViewGroup) contentParent.getChildAt(0);
			contentParent.removeView(content);
			contentParent.addView(this);
			getContentView().addView(content);
		}
	}
}
