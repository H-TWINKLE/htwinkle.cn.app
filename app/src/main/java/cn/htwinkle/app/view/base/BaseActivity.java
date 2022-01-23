package cn.htwinkle.app.view.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.htwinkle.app.R;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";

    public abstract void initData();

    public abstract void initView();

    protected Toolbar baseToolBar;

    private String toolBarTitle;

    public Bundle savedInstanceState;

    @ViewInject(R.id.base_ll_layout)
    private LinearLayout base_ll_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        this.savedInstanceState = savedInstanceState;
        initData();
        setBaseToolBar();
        initView();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void startActivity(Class c) {
        startActivity(new Intent(this, c));
    }

    protected void setToastString(String text) {
        runOnUiThread(() -> Toast.makeText(this, text, Toast.LENGTH_SHORT).show());
    }

    public String getToolBarTitle() {
        return toolBarTitle;
    }

    public void setToolBarTitle(String toolBarTitle) {
        this.toolBarTitle = toolBarTitle;
    }

    private void setBaseToolBar() {
        if (TextUtils.isEmpty(getToolBarTitle()))
            return;
        initToolbar();
    }

    private void initToolbar() {
        baseToolBar = findViewById(R.id.base_tool_bar);
        baseToolBar.setTitle(getToolBarTitle());
        baseToolBar.setNavigationOnClickListener((e) -> {
            this.finish();
        });
    }

    public void onFailDefaultToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void loadDialogView() {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        resizeLayout();
    }

    public int getDisplayHeight() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        return heightPixels / 2;
    }

    public void resizeLayout() {
        if (null != base_ll_layout) {
            ViewGroup.LayoutParams lp = base_ll_layout.getLayoutParams();
            lp.height = getDisplayHeight();
            base_ll_layout.setLayoutParams(lp);
        }
    }

    public void loadAnim(int p, int p2) {
        overridePendingTransition(p, p2);
    }

    public LinearLayout getBase_ll_layout() {
        return base_ll_layout;
    }

    public Toolbar getBaseToolBar() {
        return baseToolBar;
    }
}
