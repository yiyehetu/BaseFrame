package com.yita.baseframe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public abstract class BaseActivity extends AppCompatActivity {
    private ViewStub vsLoading;
    private ViewStub vsOther;
    private boolean isLoadingInflated;
    private boolean isOtherInflated;
    private View loadingView;
    private View otherView;
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);

        initTitle();
        initLCE();
        initData();
    }

    protected void initTitle() {
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        ViewParent parent = rootView.getParent();
        LinearLayout linearLayout;
        View titleView;
        if (parent != null && parent instanceof LinearLayout) {
            linearLayout = (LinearLayout) parent;
            titleView = createTitleView(linearLayout);
            int index = 0;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View child = linearLayout.getChildAt(i);
                if (child instanceof FrameLayout) {
                    index = i;
                    break;
                }
            }
            linearLayout.addView(titleView, index);
        } else {
            linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            titleView = createTitleView(linearLayout);
            linearLayout.addView(titleView);
            for (int i = 0; i < rootView.getChildCount(); i++) {
                View child = rootView.getChildAt(i);
                rootView.removeView(child);
                linearLayout.addView(child);
            }
            rootView.addView(linearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private View createTitleView(ViewGroup parent) {
        return LayoutInflater.from(this).inflate(getTitleLayoutId(), parent, false);
    }

    /**
     * 配置内容布局
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化内容布局控件
     */
    protected abstract void initContentViews(View contentView);

    /**
     * override
     * 配置标题布局
     */
    protected int getTitleLayoutId() {
        return R.layout.title_base;
    }

    /**
     * override
     * 配置加载中布局
     */
    protected int getLoadingLayoutId() {
        return R.layout.loading_base;
    }

    /**
     * override
     * 配置其他布局
     */
    protected int getOtherLayoutId() {
        return R.layout.error_base;
    }

    /**
     * override
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * override
     * 请求数据
     */
    protected void requestData() {
    }

    /**
     * 初始化ViewStub
     */
    private void initLCE() {
        // content
        ViewStub vsContent = (ViewStub) findViewById(R.id.vs_content);
        vsContent.setLayoutResource(getContentLayoutId());
        contentView = vsContent.inflate();
        initContentViews(contentView);

        // loading
        vsLoading = (ViewStub) findViewById(R.id.vs_loading);
        vsLoading.setLayoutResource(getLoadingLayoutId());
        vsLoading.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                isLoadingInflated = true;
            }
        });
        // error
        vsOther = (ViewStub) findViewById(R.id.vs_other);
        vsOther.setLayoutResource(getOtherLayoutId());
        vsOther.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                isOtherInflated = true;
            }
        });
    }

    public void showLoadingView() {
        if (!isLoadingInflated && vsLoading != null) {
            loadingView = vsLoading.inflate();
        }
        hideView(contentView, otherView);
        if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    public void showOtherView() {
        if (!isOtherInflated && vsOther != null) {
            otherView = vsOther.inflate();
        }
        hideView(loadingView, contentView);
        if (otherView != null && otherView.getVisibility() != View.VISIBLE) {
            otherView.setVisibility(View.VISIBLE);
        }
    }

    public void showContentView() {
        hideView(loadingView, otherView);
        if (contentView != null && contentView.getVisibility() != View.VISIBLE) {
            contentView.setVisibility(View.VISIBLE);
        }
    }

    public void hideView(View... views) {
        for (View view : views) {
            if (view != null && view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
        }
    }

}
