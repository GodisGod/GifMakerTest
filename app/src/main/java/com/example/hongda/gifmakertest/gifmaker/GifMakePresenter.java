package com.example.hongda.gifmakertest.gifmaker;

import android.content.Context;
import android.util.Log;

import com.example.hongda.gifmakertest.bean.GifImageFrame;
import com.example.hongda.gifmakertest.util.GifMakeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenliu on 2016/7/1.<br/>
 * 描述：
 * </br>
 */
public class GifMakePresenter {
    IGifMakeView mView;

    int MAX_COUNT = 20;

    List<GifImageFrame> gifImages;

    private String previewFile;

    private boolean hasPreview;

    public GifMakePresenter(Context mContext) {
        mView = (IGifMakeView) mContext;
    }

    public List<GifImageFrame> getGifImages() {
        if (gifImages == null) {
            gifImages = new ArrayList<>();
            GifImageFrame gif = new GifImageFrame();
            gif.setType(GifImageFrame.TYPE_ICON);
            gifImages.add(gif);
        }
        return gifImages;
    }

    public void solveImages(List<String> paths) {
        int size = gifImages.size() - 1;
        int count = MAX_COUNT - size;
        int sizeP = paths.size();
        GifImageFrame gif = null;
        if (count < sizeP) {
            for (int i = 0; i < count; i++) {
                gif = new GifImageFrame();
                gif.setPath(paths.get(i));
                gifImages.add(gif);
            }
        } else {
            for (int i = 0; i < sizeP; i++) {
                gif = new GifImageFrame();
                gif.setPath(paths.get(i));
                gifImages.add(gif);
            }
        }
        mView.finishPaths();
    }


    private List<String> getPaths() {
        List<String> paths = new ArrayList<>();
        int size = gifImages.size();
        for (int i = 1; i < size; i++) {
            paths.add(gifImages.get(i).getPath());
        }
        return paths;
    }


    /**
     * 生成gif图
     */
    public void createGif(final int fps, final int width, final int height) {
        previewFile = "";
        hasPreview = false;
        final String filename = String.valueOf(System.currentTimeMillis());
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    previewFile = GifMakeUtil.createGif(filename, getPaths(), fps, width, height);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e.getCause());
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        hasPreview = true;
                        mView.finishCreate(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("LHD", "onError: " + e.getMessage());
                        hasPreview = false;
                        mView.finishCreate(false);
                    }

                    @Override
                    public void onNext(String s) {
                    }
                });
    }


    public void clear() {
        if (gifImages != null) {
            gifImages.clear();
            GifImageFrame gif = new GifImageFrame();
            gif.setType(GifImageFrame.TYPE_ICON);
            gifImages.add(gif);
        }
    }

    public String getPreViewFile() {
        return previewFile;
    }

    public boolean isHasPreview() {
        return hasPreview;
    }
}




