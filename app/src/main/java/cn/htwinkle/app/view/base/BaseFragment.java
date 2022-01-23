package cn.htwinkle.app.view.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.xutils.x;

import java.util.Objects;


public abstract class BaseFragment extends Fragment {


    public abstract void initData();

    public abstract void initView();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    protected void startActivity(Class c) {
        Objects.requireNonNull(getContext()).startActivity(new Intent(getActivity(), c));
    }

    protected void setToastString(String text) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show());
    }

}
