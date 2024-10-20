package com.example.notesapp.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.notesapp.R;

public class GlobalVariables {
    private static String tag = "Global";
    private static GlobalVariables ourInstance;

    private static Handler handlerUI;
    private static Handler handlerWorker;

    private Activity mainActivity;

    private int defaultButtonColor;
    private int selectedButtonColor;

    private GlobalVariables() {
    }

    public int getSelectedButtonColor() {
        return ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.orange_light);
    }

    public void setSelectedButtonColor(int selectedButtonColor) {
        this.selectedButtonColor = selectedButtonColor;
    }

    public int getDefaultButtonColor() {
        return ContextCompat.getColor(mainActivity.getApplicationContext(), R.color.white);
    }

    public void setDefaultButtonColor(int defaultButtonColor) {
        this.defaultButtonColor = defaultButtonColor;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static synchronized GlobalVariables getInstance()
    {
        if (ourInstance == null)
            ourInstance = new GlobalVariables();
        return ourInstance;
    }

    public static synchronized void resetInstance() {
        ourInstance = null;
    }

    public static synchronized Handler getHandlerWorker()
    {
        if (handlerWorker == null)
        {
            HandlerThread thread = new HandlerThread("Handler Worker Thread");
            thread.start();
            handlerWorker = new Handler(thread.getLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    Runnable runnable = (Runnable) msg.obj;
                    runnable.run();
                }
            };
        }
        return handlerWorker;
    }

    public static synchronized Handler getHandlerUI()
    {
        if (handlerUI == null)
        {
            handlerUI = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    Runnable runnable = (Runnable) msg.obj;
                    runnable.run();
                }
            };
        }
        return handlerUI;
    }
}
