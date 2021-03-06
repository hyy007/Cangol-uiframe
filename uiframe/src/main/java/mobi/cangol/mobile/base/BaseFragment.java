/*
 * Copyright (c) 2013 Cangol
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.session.SessionService;

public abstract class BaseFragment extends Fragment {
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    protected final String TAG = Log.makeLogTag(this.getClass());
    protected CoreApplication app;
    private long startTime;
    private int resultCode = RESULT_CANCELED;
    private Bundle resultData;
    private CustomFragmentManager stack;
    private InternalHandler handler;
    protected HandlerThread handlerThread;

    /**
     * 查找view
     *
     * @param view
     */
    abstract protected void findViews(View view);

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    abstract protected void initViews(Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    abstract protected void initData(Bundle savedInstanceState);

    /**
     * 返回上级导航fragment
     *
     * @return
     */
    abstract protected FragmentInfo getNavigtionUpToFragment();

    /**
     * 初始化子fragment管理栈
     *
     * @param containerId
     */
    protected void initFragmentStack(int containerId) {
        if (null == stack)
            stack = CustomFragmentManager.forContainer(this.getActivity(), containerId, this.getChildFragmentManager());
    }

    /**
     * 获取子fragment管理栈
     *
     * @return
     */
    final public CustomFragmentManager getCustomFragmentManager() {
        return stack;
    }

    /**
     * 返回当前fragment是否是单例的，如果是在当前的自定栈里，他只能存在一个
     *
     * @return
     */
    public boolean isSingleton() {
        return false;
    }

    /**
     * 返回是否清除栈,一级fragment建议设置为true,二三级fragment建议设置为false
     *
     * @return
     */
    public boolean isCleanStack() {
        return false;
    }

    /**
     * 获取AppService
     *
     * @param name
     * @return
     */
    public AppService getAppService(String name) {
        return app.getAppService(name);
    }

    /**
     * 获取Session
     *
     * @return
     */
    public SessionService getSession() {
        return app.getSession();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(TAG, "onAttach");
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.v(TAG, "onAttachFragment");
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Log.v(TAG, "onCreateAnimation");
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        Log.v(TAG, "onMultiWindowModeChanged");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        handler = new InternalHandler(this, handlerThread.getLooper());
        app = (CoreApplication) this.getActivity().getApplication();
        if (savedInstanceState == null) {

        } else {
            if (null != stack) stack.restoreState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        {
            Log.v(TAG, "onCreateView");
            startTime = System.currentTimeMillis();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume " + getIdleTime() + "s");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG, "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "onDestroyView " + getIdleTime() + "s");
    }

    @Override
    public void onDestroy() {
        getHandler().getLooper().quit();
        handlerThread.quit();
        if (null != stack)stack.destroy();
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    public void onDrawerSlide(float slideOffset) {
        Log.v(TAG, "onDrawerSlide");
    }

    public void onDrawerOpened() {
        Log.v(TAG, "onDrawerOpened");
    }

    public void onDrawerClosed() {
        Log.v(TAG, "onDrawerClosed");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != stack) stack.saveState(outState);
        Log.v(TAG, "onSaveInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.v(TAG, "onConfigurationChanged");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "onActivityResult");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.v(TAG, "onHiddenChanged " + hidden);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.v(TAG, "onLowMemory");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(TAG, "onViewStateRestored");
    }

    /**
     * fragment之间的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        Log.v(TAG, "onFragmentResult");
    }

    /**
     * 设置回调的状态码
     *
     * @param resultCode
     */
    final public void setResult(int resultCode) {
        this.setResult(resultCode, null);
    }

    /**
     * 设置回调的状态码和参数
     *
     * @param resultCode
     * @param resultData
     */
    final public void setResult(int resultCode, Bundle resultData) {
        this.resultCode = resultCode;
        this.resultData = resultData;
    }

    /**
     * 通知返回回调
     */
    final public void notifyResult() {
        BaseFragment target = (BaseFragment) getTargetFragment();
        if (target != null) {
            target.onFragmentResult(getTargetRequestCode(), resultCode, resultData);
        } else {
            throw new IllegalStateException("Target Fragment is null");
        }
    }

    /**
     * 返回左上角导航图标的事件处理结果
     *
     * @return
     */
    public boolean onSupportNavigateUp() {
        hideSoftInput();
        return false;
    }

    /**
     * back键相应
     *
     * @return
     */

    public boolean onBackPressed() {

        if (null == stack) return false;
        if (stack.size() <= 1||stack.peek()==null) {
            return false;
        } else {
            if (stack.peek().onBackPressed()) {
                return true;
            } else {
                stack.popBackStack();
                return true;
            }
        }
    }

    /**
     * 返回view
     *
     * @param id
     * @return
     */
    public final View findViewById(int id) {
        if(getView()==null)
            return null;
        else
            return this.getView().findViewById(id);
    }

    /**
     * 显示toast
     *
     * @param resId
     */
    public void showToast(int resId) {
        if (isEnable()) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.showToast(resId);
        } else {
            Log.e("IllegalStateException  Fragment isEnable=false");
        }
    }

    /**
     * 显示toast
     *
     * @param resId
     * @param duration
     */
    public void showToast(int resId, int duration) {
        if (isEnable()) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.showToast(resId, duration);
        } else {
            Log.e("IllegalStateException  Fragment isEnable=false");
        }
    }

    /**
     * 显示toast
     *
     * @param str
     */
    public void showToast(String str) {
        if (isEnable()) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.showToast(str);
        } else {
            Log.e("IllegalStateException  Fragment isEnable=false");
        }
    }

    /**
     * 显示toast
     *
     * @param str
     * @param duration
     */
    public void showToast(String str, int duration) {
        if (isEnable()) {
            CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
            bfActivity.showToast(str);
        } else {
            Log.e("IllegalStateException  Fragment isEnable=false");
        }
    }

    /**
     * 返回当前fragment是否有效
     *
     * @return
     */
    public boolean isEnable() {
        if (null == getActivity() || !isAdded() || isRemoving() || isDetached()) {
            return false;
        }
        return true;
    }

    public void showSoftInput(EditText editText) {
        if (isEnable()) {
            BaseActivityDelegate bfActivity = (BaseActivityDelegate) this.getActivity();
            bfActivity.showSoftInput(editText);
        } else {
            Log.e("IllegalStateException  Fragment isEnable=false");
        }
    }

    public void hideSoftInput() {
        if (isEnable()) {
            BaseActivityDelegate bfActivity = (BaseActivityDelegate) this.getActivity();
            bfActivity.hideSoftInput();
        } else {
            Log.e("IllegalStateException  Fragment isEnable=false");
        }
    }

    /**
     * 获取相应时间
     *
     * @return
     */

    public float getIdleTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }

    /**
     * 获取回调码
     *
     * @return
     */
    protected int getResultCode() {
        return resultCode;
    }

    /**
     * 替换fragment
     *
     * @param fragmentClass
     */
    final public void replaceFragment(Class<? extends BaseFragment> fragmentClass) {
        replaceFragment(fragmentClass, fragmentClass.getSimpleName(), null);
    }

    /**
     * 替换fragment
     *
     * @param fragmentClass
     * @param args
     */
    final public void replaceFragment(Class<? extends BaseFragment> fragmentClass, Bundle args) {
        replaceFragment(fragmentClass, fragmentClass.getSimpleName(), args);
    }

    /**
     * 替换fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    final public void replaceFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args) {
        this.replaceFragment(fragmentClass, tag, args, null);
    }

    /**
     * 替换fragment,并要求请求回调
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param args
     */
    final public void replaceFragmentForResult(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, int requestCode) {
        if (requestCode != -1) {
            this.replaceFragment(fragmentClass, tag, args, new CustomFragmentTransaction().setTargetFragment(this, requestCode));
        } else {
            throw new IllegalStateException("requestCode!=-1");
        }
    }

    /**
     * 替换父类级fragment 带自定义动画
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param customFragmentTransaction
     */
    final public void replaceFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, CustomFragmentTransaction customFragmentTransaction) {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        CustomFragmentManager stack = null;
        if (parent != null) {
            stack = parent.getCustomFragmentManager();
        } else {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                stack = bfActivity.getCustomFragmentManager();
            }
        }
        if (null != stack && !stack.isStateSaved()) {
            stack.replace(fragmentClass, tag, args, customFragmentTransaction);
            stack.commit();
        } else {
            Log.e(TAG, "Can not perform this action after onSaveInstanceState");
        }
    }

    /**
     * 判断是否执行了onSaveInstanceState
     * Support 26.0.0-alpha1 之后才有isStateSaved方法
     * 这里用反射直接读取mStateSaved字段，兼容旧的版本
     * 也为了与之后的版本兼容修改了方法名
     *
     * @return
     */
    public boolean isSavedState() {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        CustomFragmentManager stack = null;
        if (parent != null) {
            stack = parent.getCustomFragmentManager();
        } else {
            if (getActivity() == null) {
                return false;
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                stack = bfActivity.getCustomFragmentManager();
            }
        }
        if (stack != null) {
            return stack.isStateSaved();
        } else {
            return false;
        }
    }

    /**
     * 替换父类级fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    final public void replaceParentFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args) {
        replaceParentFragment(fragmentClass, tag, args, null);
    }

    /**
     * 替换父类级fragment,并要求请求回调
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param requestCode
     */
    final public void replaceParentFragmentForResult(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, int requestCode) {
        if (requestCode != -1) {
            this.replaceParentFragment(fragmentClass, tag, args, new CustomFragmentTransaction().setTargetFragment(this, requestCode));
        } else {
            throw new IllegalStateException("requestCode!=-1");
        }
    }

    /**
     * 替换父类级fragment 带自定义动画
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param customFragmentTransaction
     */
    final public void replaceParentFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, CustomFragmentTransaction customFragmentTransaction) {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        if (parent != null) {
            parent.replaceFragment(fragmentClass, tag, args, customFragmentTransaction);
        } else {
            throw new IllegalStateException("ParentFragment is null");
        }
    }

    /**
     * 替换子类级fragment
     *
     * @param fragmentClass
     * @param tag
     * @param args
     */
    final public void replaceChildFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args) {
        replaceChildFragment(fragmentClass, tag, args, null);
    }

    /**
     * 替换子fragment 带自定义动画
     *
     * @param fragmentClass
     * @param tag
     * @param args
     * @param customFragmentTransaction
     */
    final public void replaceChildFragment(Class<? extends BaseFragment> fragmentClass, String tag, Bundle args, CustomFragmentTransaction customFragmentTransaction) {
        if (stack != null) {
            if (!stack.isStateSaved()) {
                stack.replace(fragmentClass, tag, args, customFragmentTransaction);
                stack.commit();
            } else {
                Log.e(TAG, "Can not perform this action after onSaveInstanceState");
            }
        } else {
            throw new IllegalStateException("fragment'CustomFragmentManager is null, Please initFragmentStack");
        }
    }

    /**
     * 将当前fragment弹出栈
     */
    final public void popBackStack() {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        if (parent != null) {
            parent.getCustomFragmentManager().popBackStack();
        } else {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                bfActivity.getCustomFragmentManager().popBackStack();
            }
        }
    }

    /**
     * 将所有fragment弹出栈
     */
    final public void popBackStackAll() {
        BaseFragment parent = (BaseFragment) this.getParentFragment();
        if (parent != null) {
            parent.getCustomFragmentManager().popBackStackAll();
        } else {
            if (getActivity() == null) {
                throw new IllegalStateException("getActivity is null");
            } else {
                CustomFragmentActivityDelegate bfActivity = (CustomFragmentActivityDelegate) this.getActivity();
                bfActivity.getCustomFragmentManager().popBackStackAll();
            }
        }
    }

    protected Handler getHandler() {
        return handler;
    }

    protected void handleMessage(Message msg) {

    }

    protected void postRunnable(StaticInnerRunnable runnable) {
        if (handler != null && runnable != null)
            handler.post(runnable);
    }

    protected void postRunnable(Runnable runnable) {
        if (handler != null && runnable != null)
            handler.post(runnable);
    }

    protected static class StaticInnerRunnable implements Runnable {
        @Override
        public void run() {
        }
    }

    protected final static class InternalHandler extends Handler {
        private final WeakReference<BaseFragment> mFragmentRef;

        public InternalHandler(BaseFragment fragment, Looper looper) {
            super(looper);
            mFragmentRef = new WeakReference<>(fragment);
        }

        public void handleMessage(Message msg) {
            BaseFragment fragment = mFragmentRef.get();
            if (fragment != null && fragment.isEnable()) {
                fragment.handleMessage(msg);
            }
        }
    }

    @ColorInt
    public  int getThemeAttrColor(@AttrRes int colorAttr) {
        if(getActivity()==null){
            throw new IllegalStateException("getActivity is null");
        }else{
            TypedArray array = getActivity().obtainStyledAttributes(null, new int[]{colorAttr});
            try {
                return array.getColor(0, 0);
            } finally {
                array.recycle();
            }
        }
    }

    public TypedValue getAttrTypedValue(@AttrRes int attr){
        if(getActivity()==null){
            throw new IllegalStateException("getActivity is null");
        }else{
            TypedValue typedValue = new TypedValue();
            getActivity().getTheme().resolveAttribute(attr, typedValue, true);
            return typedValue;
        }
    }
}