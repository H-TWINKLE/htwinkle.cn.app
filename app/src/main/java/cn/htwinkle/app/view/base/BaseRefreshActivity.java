package cn.htwinkle.app.view.base;

import android.graphics.Color;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.htwinkle.app.R;

public abstract class BaseRefreshActivity<T, V extends BaseQuickAdapter<T, BaseViewHolder>>
        extends BaseActivity {

    public abstract void getData();

    @ViewInject(R.id.base_swipe_refresh_layout)
    private SwipeRefreshLayout base_swipe_refresh_layout;

    @ViewInject(R.id.base_recycler_view)
    private RecyclerView base_recycler_view;

    public V adapter;

    public RecyclerView.LayoutManager layoutManager;


    public SwipeRefreshLayout getBase_swipe_refresh_layout() {
        return base_swipe_refresh_layout;
    }

    public RecyclerView getBase_recycler_view() {
        return base_recycler_view;
    }

    @Override
    public void initView() {
        setDefaultView();
    }

    public void setDefaultView() {

        base_swipe_refresh_layout.setColorSchemeColors(Color.rgb(47, 223, 189));
        base_swipe_refresh_layout.setOnRefreshListener(this::refreshData);

        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(this);
        }
        base_recycler_view.setLayoutManager(layoutManager);
        base_recycler_view.setAdapter(adapter);
    }


    public void refreshData() {
        enableRefreshData(true);
        getData();
    }

    public void enableRefreshData(boolean flag) {
        base_swipe_refresh_layout.setRefreshing(flag);
    }

    public void onSuccessGetData(List<T> data) {
        adapter.setList(data);
        enableRefreshData(false);
    }

    public void loadOneData(T data) {
        runOnUiThread(() -> {
            adapter.addData(data);
            enableRefreshData(false);
        });
    }

    public void setOneData(int position, T data) {
        runOnUiThread(() -> {
            adapter.setData(position, data);
        });
    }


    public void onFailGetData(String text) {
        enableRefreshData(false);
        setToastString(text);
    }

    public void setToastString(String text) {
        runOnUiThread(() -> Toast.makeText(this, text, Toast.LENGTH_SHORT).show());
    }

}
