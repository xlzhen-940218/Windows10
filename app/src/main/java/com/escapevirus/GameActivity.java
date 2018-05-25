package com.escapevirus;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.escapevirus.enumfolder.GameDifficulty;
import com.escapevirus.widget.GameView;
import com.xpping.windows10.BuildConfig;
import com.xpping.windows10.R;
import com.xpping.windows10.widget.ToastView;

import java.io.File;

/*
*是男人就坚持10秒 游戏activity
*/
public class GameActivity extends BaseActivity implements View.OnClickListener{
    private SurfaceView surfaceView;
    private GameDifficulty gameDifficulty;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_game);
    }

    @Override
    public boolean statusBarDarkMode() {
        return true;
    }

    @Override
    public void initData() {
        surfaceView= findViewById(R.id.surfaceView);
        findViewById(R.id.showScoreAndResetPlay).setVisibility(View.GONE);

        findViewById(R.id.superSimpleButton).setOnClickListener(this);
        findViewById(R.id.simpleButton).setOnClickListener(this);
        findViewById(R.id.easyButton).setOnClickListener(this);
        findViewById(R.id.difficultyButton).setOnClickListener(this);
        findViewById(R.id.infernalButton).setOnClickListener(this);
        findViewById(R.id.resetPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.showScoreAndResetPlay).setVisibility(View.GONE);
                findViewById(R.id.modeSelect).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.shareGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgPath=AppUtils.myShot(GameActivity.this);
                //分享APP
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Win10安卓桌面-小游戏");
                intent.putExtra(Intent.EXTRA_TEXT, "Win10安卓桌面-小游戏，是男人就坚持10秒" +
                        "，看看你能坚持几秒？下载地址：https://www.coolapk.com/apk/com.xpping.windows10");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "分享到"));
            }
        });
        ((GameView)surfaceView).setGameProgressListener(new GameView.GameProgressListener() {
            @Override
            public void onGameOver(int score) {
                ToastView.getInstaller(GameActivity.this).setText("游戏结束").show();

                String scoreGameDifficulty=gameDifficulty==GameDifficulty.superSimple?"小白模式":
                        gameDifficulty==GameDifficulty.simple?"简单模式":
                                gameDifficulty==GameDifficulty.easy?"轻松模式":
                                        gameDifficulty==GameDifficulty.difficulty?"困难模式":"地狱模式";
                ((TextView)findViewById(R.id.modeText)).setText(scoreGameDifficulty);
                ((TextView)findViewById(R.id.score)).setText(score+"");
                findViewById(R.id.showScoreAndResetPlay).setVisibility(View.VISIBLE);
                findViewById(R.id.maskView).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void initWidget() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        surfaceView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.superSimpleButton:
                gameDifficulty=GameDifficulty.superSimple;
                break;
            case R.id.simpleButton:
                gameDifficulty=GameDifficulty.simple;
                break;
            case R.id.easyButton:
                gameDifficulty=GameDifficulty.easy;
                break;
            case R.id.difficultyButton:
                gameDifficulty=GameDifficulty.difficulty;
                break;
            case R.id.infernalButton:
                gameDifficulty=GameDifficulty.infernal;
                break;
        }
        ((GameView)surfaceView).setGameDifficulty(gameDifficulty);

        findViewById(R.id.modeSelect).setVisibility(View.GONE);
        findViewById(R.id.maskView).setVisibility(View.GONE);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(findViewById(R.id.maskView).getVisibility()==View.GONE){
                ((GameView)surfaceView).setDrawing(false);
                findViewById(R.id.shareGame).setVisibility(View.VISIBLE);
                findViewById(R.id.maskView).setVisibility(View.VISIBLE);
            }
            else if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastView.getInstaller(getApplicationContext()).setText("再按一次退出游戏").show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
