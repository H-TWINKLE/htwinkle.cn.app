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
import java.util.Objects;

import cn.htwinkle.app.R;

public abstract class BaseRefreshFragment<T, V extends BaseQuickAdapter<T, BaseViewHolder>>
        extends BaseFragment {

    public abstract void getData();

    @ViewInject(R.id.base_swipe_refresh_layout)
    private SwipeRefreshLayout base_swipe_refresh_layout;

    @ViewInject(R.id.base_recycler_view)
    private RecyclerView base_recycler_view;

    public V adapter;

    @Override
    public void initView() {
        setDefaultView();
    }

    public SwipeRefreshLayout getBase_swipe_refresh_layout() {
        return base_swipe_refresh_layout;
    }

    public RecyclerView getBase_recycler_view() {
        return base_recycler_view;
    }


    public void setDefaultView() {
        base_swipe_refresh_layout.setColorSchemeColors(Color.rgb(47, 223, 189));
        base_swipe_refresh_layout.setOnRefreshListener(this::refreshData);

        base_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            adapter.addData(data);
            enableRefreshData(false);
        });
    }

    public void setOneData(int position, T data) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            adapter.setData(position, data);
        });
    }


    public void onFailGetData(String text) {
        enableRefreshData(false);
        setToastString(text);
    }

    public void setToastString(String text) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        });
    }
}
