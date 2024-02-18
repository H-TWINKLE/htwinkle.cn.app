package cn.htwinkle.app.view.app.screen.share;

import android.graphics.Point;
import android.view.Display;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.pedro.vlc.VlcListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.htwinkle.app.R;
import cn.htwinkle.app.constants.Constants;
import cn.htwinkle.app.entity.OnlineStream;
import cn.htwinkle.app.view.base.BaseActivity;
import cn.htwinkle.app.wrapper.VlcVideoLibraryWrapper;

@ContentView(R.layout.activity_share_view)
public class ShareViewActivity extends BaseActivity implements VlcListener {

    private VlcVideoLibraryWrapper wrapper = null;

    @ViewInject(R.id.share_view_sv)
    private SurfaceView share_view_sv;

    @ViewInject(R.id.share_view_cl)
    private CoordinatorLayout share_view_cl;

    @Override
    public void initData() {
        setToolBarTitle("详情");

        OnlineStream.StreamsDTO shareView = getContent(Constants.SHARE_VIEW);
        if (shareView != null) {
            setToolBarTitle(shareView.getName());
        }
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
    }

    @Override
    public void initView() {
        OnlineStream.StreamsDTO shareView = getContent(Constants.SHARE_VIEW);

        Point outSize = getPoint();
        // 从Point对象中获取宽、高
        int width = outSize.x;
        int height = outSize.y;

        wrapper = new VlcVideoLibraryWrapper(this, this, share_view_sv, width, height);

        share_view_sv.getHolder().setFixedSize(width, height);

        String tcUrl = shareView.getTcUrl();
        String substring = tcUrl.substring(0, tcUrl.lastIndexOf("/"));
        String endPoint = substring + shareView.getUrl();
        wrapper.play(endPoint);
    }

    @NonNull
    private Point getPoint() {
        Display display = getWindowManager().getDefaultDisplay();
        // 方法一(推荐使用)使用Point来保存屏幕宽、高两个数据
        Point outSize = new Point();
        // 通过Display对象获取屏幕宽、高数据并保存到Point对象中
        display.getSize(outSize);
        return outSize;
    }

    @Override
    public void finish() {
        super.finish();
        loadAnim(R.anim.no_anim, R.anim.anim_slide_out_bottom);
    }

    private OnlineStream.StreamsDTO getContent(String key) {
        return (OnlineStream.StreamsDTO) getIntent().getSerializableExtra(key);
    }


    @Override
    public void onComplete() {
        Toast.makeText(this, "正在播放", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        Toast.makeText(this, "播放失败，请重试", Toast.LENGTH_SHORT).show();
        if (wrapper != null) {
            wrapper.stop();
        }
    }
}
