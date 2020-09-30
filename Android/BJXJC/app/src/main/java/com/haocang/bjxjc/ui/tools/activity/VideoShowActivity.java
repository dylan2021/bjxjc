package com.haocang.bjxjc.ui.tools.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;

import com.haocang.bjxjc.R;
import com.haocang.bjxjc.activity.comm.CommFinalActivity;
import com.haocang.bjxjc.activity.tool.CacheActivityUtil;
import com.haocang.bjxjc.ui.tools.view.VideoFullScreenView;
import com.haocang.bjxjc.utils.tools.MyUtils;

//视频播放
public class VideoShowActivity extends CommFinalActivity {
    private VideoFullScreenView mVideoView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheActivityUtil.addActivity(VideoShowActivity.this);
        Intent i = getIntent();
        url = i.getStringExtra("URL");
        mVideoView = findViewById(R.id.video_view);
        MediaController mc = new MediaController(mContext);
        mVideoView.setMediaController(mc);
        initEvent();
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_video_show;
    }

    @Override
    protected boolean showNavigation() {
        return true;
    }

    @Override
    protected int showMoreIcon() {
        return 0;
    }

    @Override
    protected String showTitleName() {
        return "视频播放";
    }

    private void initEvent() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });


        //设置循环播放
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //stopPlaybackVideo();
                mp.start();
                mp.setLooping(true);
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stopPlaybackVideo();
                return true;
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        try {

            mVideoView.setVideoURI(MyUtils.GetFileProviderUri(VideoShowActivity.this, url));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying()) {
            mVideoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView.canPause()) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaybackVideo();
        CacheActivityUtil.finishSingleActivity(VideoShowActivity.this);
    }

    private void stopPlaybackVideo() {
        try {
            mVideoView.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return super.onKeyDown(keyCode, event);
    }



}
