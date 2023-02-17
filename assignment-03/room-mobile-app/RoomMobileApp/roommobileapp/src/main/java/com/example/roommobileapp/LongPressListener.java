package com.example.roommobileapp;

import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

public abstract class LongPressListener implements View.OnTouchListener {
    final Handler handler;
    final int delay;

    public LongPressListener(final Handler handler, final int delay) {
        this.handler = handler;
        this.delay = delay;
    }

    protected abstract void update();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            update();
            handler.postDelayed(this, delay);
        }
    };
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handler.postDelayed(runnable, delay);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            handler.removeCallbacks(runnable);
        }
        return false;
    }
}
