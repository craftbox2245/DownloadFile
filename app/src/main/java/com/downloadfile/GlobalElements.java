package com.downloadfile;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import com.gdacciaro.iOSDialog.iOSDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by CRAFT BOX on 10/6/2017.
 */

public class GlobalElements extends Application {

    public static String directory="demo";
    public static int ll_count=20;
    public static String fileprovider_path="com.downloadfile.fileprovider";
    public static int decimal_formate_dot=0;
    public static String decimal_formate_string="#.##";

    public static boolean isConnectingToInternet(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
                else
                {
                    NetworkInfo info1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (info1.isConnected()) {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static void showDialog(Context context)
    {
        final iOSDialog iOSDialog = new iOSDialog(context);
        iOSDialog.setTitle( "Internet");
        iOSDialog.setSubtitle("Please check your internet connection... ");
        iOSDialog.setPositiveLabel("Ok");
        iOSDialog.setBoldPositiveLabel(true);

        iOSDialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"OK clicked",Toast.LENGTH_SHORT).show();
                iOSDialog.dismiss();
            }
        });
        iOSDialog.show();
    }

    public static boolean getVersionCheck() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }


    public static boolean beforeDate(String fromDate, String ToDate, String formatOfDate) {
        boolean success = false;
        SimpleDateFormat sdf = new SimpleDateFormat(formatOfDate);
        try {
            if (sdf.parse(fromDate).before(sdf.parse(ToDate)) || sdf.parse(fromDate).compareTo(sdf.parse(ToDate)) == 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return success;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public static String getCurrentdate()
    {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat cu_date = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = cu_date.format(newCalendar.getTime());
        return current_date;
    }


}
