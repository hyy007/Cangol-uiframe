
package mobi.cangol.mobile.uiframe.demo.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mobi.cangol.mobile.base.BaseContentFragment;
import mobi.cangol.mobile.base.FragmentInfo;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.uiframe.demo.R;

public class LeakFragment extends BaseContentFragment {
	
	private TextView mTextView1;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_item, container,false);
		return v;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		findViews(view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews(savedInstanceState);
		initData(savedInstanceState);
	}
	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void findViews(View view) {
		this.setTitle(this.getClass().getSimpleName());
		mTextView1=(TextView) view.findViewById(R.id.textView1);
		mTextView1.setText("Click me Sleep 10000");
	}

	@Override
	protected void initViews(Bundle savedInstanceState) {
		mTextView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				postRunnable(new StaticInnerRunnable(){
					public void run(){
						Log.d("sleep start "+this);
						SystemClock.sleep(10000);
						Log.d("sleep stop "+this);
					}
				});
			}
		});
	}

	@Override
	protected FragmentInfo getNavigtionUpToFragment() {
		return null;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public boolean isCleanStack() {
		return false;
	}

}