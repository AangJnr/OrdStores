package com.shop.ordstore.utilities;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shop.ordstore.R;

/**
 * Created by aangjnr on 08/12/2016.
 */

public class DotsScrollBar
{
    LinearLayout main_image_holder;
    public static void createDotScrollBar(Context context, LinearLayout main_holder, int selectedPage, int count)
    {
        for(int i=0;i<count;i++)
        {
            ImageView dot = null;
            dot= new ImageView(context);
            LinearLayout.LayoutParams vp =
                    new LinearLayout.LayoutParams(10, 10);
            dot.setLayoutParams(vp);
            if(i==selectedPage)
            {
                try {
                    //dot.setImageResource(R.drawable.page_selected);
                    dot.setBackgroundResource(R.drawable.page_not_selected);
                } catch (Exception e)
                {

                }
            }else
            {
                //dot.setImageResource(R.drawable.page_not_selected);
                dot.setBackgroundResource(R.drawable.page_not_selected);
            }
            main_holder.addView(dot);
        }
        main_holder.invalidate();
    }
}