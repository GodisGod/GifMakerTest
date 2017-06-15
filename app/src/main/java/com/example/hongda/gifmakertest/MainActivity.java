package com.example.hongda.gifmakertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hongda.gifmakertest.gifmaker.GifMakePresenter;
import com.example.hongda.gifmakertest.gifmaker.IGifMakeView;
import com.example.hongda.gifmakertest.util.GlideLoader;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IGifMakeView {

    private GifMakePresenter presenter;

    //图片集合
    private List<String> imgPaths;

    String img1 = "http://imgsrc.baidu.com/forum/w%3D580/sign=9e4c5f0ed72a283443a636036bb5c92e/8dd202e93901213f461b01cf57e736d12f2e953f.jpg";
    String img2 = "http://imgsrc.baidu.com/forum/w%3D580/sign=554e18251dd8bc3ec60806c2b28aa6c8/77b09e16fdfaaf51ca112ca0885494eef11f7a6e.jpg";
    String img3 = "http://imgsrc.baidu.com/forum/w%3D580/sign=907442957fd98d1076d40c39113eb807/8af1a4e736d12f2e07917a094cc2d562843568e6.jpg";

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activity = this;
        presenter = new GifMakePresenter(this);
        imgPaths = new ArrayList<>();

        findViewById(R.id.btn_choose_imgs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        findViewById(R.id.btn_make_gif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getGifImages();
                presenter.solveImages(imgPaths);
            }
        });
    }


    @Override
    public void finishPaths() {
        Log.i("LHD", "finishPaths");
        presenter.createGif(300, 480, 640);
    }

    @Override
    public void finishCreate(boolean b) {
        Log.i("LHD", "finishCreate: " + b);
//        Toast.makeText(this, "生成成功", Toast.LENGTH_SHORT).show();
        if (b) {
            Toast.makeText(this, "生成成功", Toast.LENGTH_SHORT).show();
            Log.i("LHD", "gif path:" + presenter.getPreViewFile());
        } else {
            Toast.makeText(this, "生成失败", Toast.LENGTH_SHORT).show();
        }

    }

    private void chooseImage() {
        ImageConfig imageConfig =
                new ImageConfig.Builder(new GlideLoader())
                        // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                        .steepToolBarColor(getResources().getColor(R.color.black))
                        // 标题的背景颜色 （默认黑色）
                        .titleBgColor(getResources().getColor(R.color.black))
                        // 提交按钮字体的颜色  （默认白色）
                        .titleSubmitTextColor(getResources().getColor(R.color.white))
                        // 标题颜色 （默认白色）
                        .titleTextColor(getResources().getColor(R.color.white))
//                                .crop()
                        .mutiSelectMaxSize(6)
                        .showCamera()
                        .filePath("/GifMaker/Pictures")
                        .build();
        ImageSelector.open(activity, imageConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get Image Path List
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            Log.i("LHD", "=======onActivityResult======" + pathList.size());
            imgPaths.clear();
            for (String s :
                    pathList) {
                imgPaths.add(s);
                Log.i("LHD", "选择的图片路径： " + s);
            }
        }
    }

}
