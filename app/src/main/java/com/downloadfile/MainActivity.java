package com.downloadfile;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.downloadfile.custom.Toaster;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class MainActivity extends RuntimePermissionsActivity {

/* todo 31-10-2017 create project date */

    Button image_download,pdf_download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.super.requestAppPermissions(new
                                String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                        }, R.string.runtime_permissions_txt
                        , 20);
            }
        },500);

        image_download  = (Button) findViewById(R.id.image_download);
        pdf_download  = (Button) findViewById(R.id.pdf_download);

        image_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(GlobalElements.isConnectingToInternet(MainActivity.this))
                {

                    // todo first argument download url and second argument file name with extation
                    new DownloadFileFromURL("http://demo.phpgang.com/crop-images/demo_files/pool.jpg","pool.jpg").execute();
                }
                else
                {
                    GlobalElements.showDialog(MainActivity.this);
                }
            }
        });

        pdf_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(GlobalElements.isConnectingToInternet(MainActivity.this))
                {

                    // todo first argument download url and second argument file name with extation
                    new DownloadFileFromURL("http://www.pdf995.com/samples/pdf.pdf","demo.pdf").execute();
                }
                else
                {
                    GlobalElements.showDialog(MainActivity.this);
                }
            }
        });

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

        try {
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), ""+GlobalElements.directory);
            if (!folder.exists())
            {
                folder.mkdir();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, Integer, Integer> {
        int notificationId;
        private NotificationManager mNotifyManager;
        private NotificationCompat.Builder mBuilder;
        String file_success="";

        private String file_url,file_name;

        public DownloadFileFromURL(String file_url,String file_name) {
            super();
            this.file_url = file_url;
            this.file_name = file_name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Random r = new Random();
            notificationId = r.nextInt(80 - 65) + 65;
            Intent viewIntent = new Intent();
            viewIntent.setAction(android.content.Intent.ACTION_VIEW);
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), ""+GlobalElements.directory);
            String file_path=folder+"/"+file_name;
            String extension_file="jpg";
            try {
                String[] extension=file_name.split("\\.");
                extension_file=extension[1];
            } catch (Exception e) {
                e.printStackTrace();
            }
            Uri contentUri=null;

            if(GlobalElements.getVersionCheck())
            {
                viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                contentUri = FileProvider.getUriForFile(MainActivity.this, ""+GlobalElements.fileprovider_path, new File(""+file_path));
            }
            else
            {
                contentUri= Uri.fromFile(new File(""+file_path));
            }

            viewIntent.setDataAndType(contentUri, MimeTypeMap.getSingleton().getMimeTypeFromExtension(""+extension_file));  // you can also change jpeg to other types
            PendingIntent viewPendingIntent = PendingIntent.getActivity(MainActivity.this, notificationId, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotifyManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(MainActivity.this);
            mBuilder.setContentTitle("Download")
                    .setContentText("Download in progress")
                    .setContentIntent(viewPendingIntent)
                    .setSmallIcon(R.drawable.ic_file_download_black_24dp);
            mBuilder.setProgress(100, 0, false);
            mBuilder.setAutoCancel(true);
            mNotifyManager.notify(notificationId, mBuilder.build());
        }

        @Override
        protected Integer doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(file_url);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), ""+GlobalElements.directory);
                if (!folder.exists())
                {
                    folder.mkdir();
                }
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = new FileOutputStream(folder+"/"+file_name);

                byte data[] = new byte[1024];

                long total = 0;
                for (int i = 0; i <= 100; i += 5) {
                    // Sets the progress indicator completion percentage
                    publishProgress(Math.min(i, 100));
                    try {
                        // Sleep for 5 seconds
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
                file_success="success";
            } catch (Exception e) {
                e.printStackTrace();
                file_success="error";
            }
            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(Integer... values) {
            // setting progress percentage
            mBuilder.setProgress(100, values[0], false);
            mNotifyManager.notify(notificationId, mBuilder.build());
            super.onProgressUpdate(values);
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(Integer file_url) {
            try {
                mBuilder.setContentText("Download complete");
                mBuilder.setProgress(0, 0, false);
                mNotifyManager.notify(notificationId, mBuilder.build());
                if(file_success.equals("success"))
                {
                    Toaster.show(MainActivity.this, "Download Successfully In Your "+GlobalElements.directory+" Folder In Sdcard",true,Toaster.SUCCESS);
                }
                else
                {
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationId);
                    Toaster.show(MainActivity.this, "file not found",false,Toaster.DANGER);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
