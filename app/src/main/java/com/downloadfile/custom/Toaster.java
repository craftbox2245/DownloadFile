package com.downloadfile.custom;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.downloadfile.R;


public class Toaster {

    public static int INFO = 1;
    public static int SUCCESS = 2;
    public static int WARNING = 3;
    public static int DANGER = 4;
    public static void show(Context context, String text, boolean isLong, int msgType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_layout, null);
        LinearLayout ll_toast = (LinearLayout)layout.findViewById(R.id.ll_toast);

        TextView textV = (TextView) layout.findViewById(R.id.toast_text);
        textV.setText(text);

        Toast t = new Toast(context);
        t.setView(layout);
        if(isLong)
        {
            t.setDuration(Toast.LENGTH_LONG);
        }
        else
        {
            t.setDuration(Toast.LENGTH_SHORT);
        }

        if(msgType == INFO)
        {
            float radii =context.getResources().getDimension(R.dimen.toast_redius);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[] { radii, radii, radii, radii, radii, radii, radii, radii });
            shape.setColor(context.getResources().getColor(R.color.green));
            ll_toast.setBackground(shape);
        }
        if(msgType == DANGER)
        {
            float radii =context.getResources().getDimension(R.dimen.toast_redius);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[] { radii, radii, radii, radii, radii, radii, radii, radii });
            shape.setColor(context.getResources().getColor(R.color.red));
            ll_toast.setBackground(shape);
        }
        if(msgType == SUCCESS)
        {
            float radii =context.getResources().getDimension(R.dimen.toast_redius);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[] { radii, radii, radii, radii, radii, radii, radii, radii });
            shape.setColor(context.getResources().getColor(R.color.green));
            ll_toast.setBackground(shape);
        }
        if(msgType == WARNING)
        {
            float radii =context.getResources().getDimension(R.dimen.toast_redius);
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[] { radii, radii, radii, radii, radii, radii, radii, radii });
            shape.setColor(context.getResources().getColor(R.color.yellow));
            ll_toast.setBackground(shape);

        }
        t.show();
    }
}
