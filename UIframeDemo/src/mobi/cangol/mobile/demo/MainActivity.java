package mobi.cangol.mobile.demo;

import mobi.cangol.mobile.demo.fragment.HomeFragment;
import mobi.cangol.mobile.demo.fragment.MenuFragment;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.navigation.TabNavigationFragmentActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;

@SuppressLint("ResourceAsColor")
public class MainActivity extends TabNavigationFragmentActivity  {
	private static long back_pressed;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.setStatusBarTintColor(R.color.red);
//		this.setNavigationBarTintColor(R.color.black);
		setContentView(R.layout.activity_main);
		this.getCustomActionBar().setBackgroundResource(R.color.red);
		if (savedInstanceState == null) {
			this.setMenuFragment(MenuFragment.class,null);
			this.setContentFragment(HomeFragment.class, "HomeFragment", null);
		}
		findViews();
		initViews(savedInstanceState);
		initData(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v("onStart "+System.currentTimeMillis());
	}

	@Override
	public void findViews() {
	}
	@Override
	public void initViews(Bundle savedInstanceState) {
		
	}
	@Override
	public void initData(Bundle savedInstanceState) {
		
	}

	@Override
	public void onBack() {
		if(back_pressed+2000>System.currentTimeMillis()){
			super.onBack();
			app.exit();
		}else{
			back_pressed=System.currentTimeMillis();
		}
	}
}