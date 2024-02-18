package cn.htwinkle.app.view.app.screen.share;

import android.view.SurfaceView;
import android.widget.Toast;

import com.pedro.vlc.VlcListener;
import com.pedro.vlc.VlcVideoLibrary;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;

import cn.htwinkle.app.R;
import cn.htwinkle.app.constants.Constants;
import cn.htwinkle.app.entity.OnlineStream;
import cn.htwinkle.app.view.base.BaseActivity;

@ContentView(R.layout.activity_share_view)
public class ShareViewActivity extends BaseActivity implements VlcListener {

    private VlcVideoLibrary vlcVideoLibrary = null;

    private String[] options = new String[]{":fullscreen"};

    @ViewInject(R.id.share_view_sv)
    private SurfaceView share_view_sv;

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
        vlcVideoLibrary = new VlcVideoLibrary(this, this, share_view_sv);
        vlcVideoLibrary.setOptions(Arrays.asList(options));

        String tcUrl = shareView.getTcUrl();
        String substring = tcUrl.substring(0, tcUrl.lastIndexOf("/"));
        String endPoint = substring + shareView.getUrl();
        vlcVideoLibrary.play(endPoint);
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
        if (vlcVideoLibrary != null) {
            vlcVideoLibrary.stop();
        }
    }
}
