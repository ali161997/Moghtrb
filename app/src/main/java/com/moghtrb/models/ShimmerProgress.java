package com.moghtrb.models;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

public class ShimmerProgress extends Drawable {
    MutableLiveData<Boolean> finished;

    public ShimmerProgress() {
        finished = new MutableLiveData<>(false);
    }

    public MutableLiveData<Boolean> getFinished() {
        return finished;
    }

    @Override
    protected boolean onLevelChange(int level) {
        if (level == 10000) {
            finished.setValue(true);

        }
        // level is on a scale of 0-10,000
        // where 10,000 means fully downloaded

        // your app's logic to change the drawable's
        // appearance here based on progress
        return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
