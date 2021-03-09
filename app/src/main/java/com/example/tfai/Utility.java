package com.example.tfai;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class Utility {
    public static Dialog getLoading(Context context){
        Dialog d=new Dialog(context);
        d.setContentView(R.layout.safe_action_card);
//        String[] loadingtext=context.getResources().getStringArray(R.array.loading_text);
        TextView loading_txt=d.findViewById(R.id.loading_text);
//        loading_txt.setText(loadingtext[new Random().nextInt(5)]);
        d.show();
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return d;

    }
}
