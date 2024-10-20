package com.example.notesapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.notesapp.R;

import java.io.InputStream;

public class Utility {

    private static String tag = "Utility";
    private static Utility ourInstance;

    private Activity mainActivity;

    private Utility() {

    }

    public static synchronized Utility getInstance()
    {
        if (ourInstance == null)
            ourInstance = new Utility();
        return ourInstance;
    }

    public static synchronized void resetInstance()
    {
        ourInstance = null;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * This method is useed to update the image view resource based on the input icon name
     * @param iconName
     * @param imageView
     */
    public void updateImageViewIcon(String iconName, ImageView imageView) {
        imageView.setColorFilter(GlobalVariables.getInstance().getSelectedButtonColor());
        switch (iconName) {
            case "ic_home":
                imageView.setImageResource(R.drawable.ic_home);
                break;
            case "ic_heart":
                imageView.setImageResource(R.drawable.ic_heart);
                break;
            case "ic_shield":
                imageView.setImageResource(R.drawable.ic_shield);
                break;
            case "ic_star":
                imageView.setImageResource(R.drawable.ic_star);
                break;
            case "ic_food":
                imageView.setImageResource(R.drawable.ic_food);
                break;
            case "ic_hair_cut":
                imageView.setImageResource(R.drawable.ic_hair_cut);
                break;
            case "ic_book":
                imageView.setImageResource(R.drawable.ic_book);
                break;
            case "ic_coin":
                imageView.setImageResource(R.drawable.ic_coin);
                break;
            case "ic_folder":
                imageView.setImageResource(R.drawable.ic_folder);
                break;
            case "ic_customer_service":
                imageView.setImageResource(R.drawable.ic_customer_service);
                break;
            case "ic_agreement":
                imageView.setImageResource(R.drawable.ic_agreement);
                break;
            case "ic_info_light":
                imageView.setImageResource(R.drawable.ic_info_light);
                break;
        }
    }


    /**
     * This method is used to read local JSON file to return JSON String
     * @param context
     * @param fileName
     * @return
     */
    public String readJsonFromResource(Context context, String fileName) {
        String jsonString = null;
        try {
            InputStream inputStream = context.getResources().openRawResource(
                    context.getResources().getIdentifier(
                            fileName,
                            "raw",
                            context.getPackageName()
                    )
            );
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (Exception e) {
            Log.e(tag, "readJsonFromAssets with error: "+e.getMessage());
            return null;
        }
        return jsonString;
    }


    /**
     * This method is used to open the given url in web browser
     * @param context
     * @param url
     */
    public void openBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
