package com.cheweibao.liuliu.common;


import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.cheweibao.liuliu.R;

public class RatingBarUtil {

    /**
     * 难度系数 适合半颗星的整数倍&&显示
     *
     * @param difficulity 难度系数
     */
    public static void initRatingBar(Context context , double difficulity, LinearLayout llRating) {
        MyGlobal myGlobal = MyGlobal.getInstance();
        if (llRating==null){
            return;
        }
        llRating.removeAllViews();
        int width = myGlobal.SCR_WIDTH;
        for (int i = 0; i < 5; i++) {
            View rating = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 25, width / 25);
            params.leftMargin = Utils.dp2Pixel(context, 1);
            rating.setBackgroundResource(R.drawable.icon_star_blue_empty);
            rating.setLayoutParams(params);
            llRating.addView(rating);
        }

        if(difficulity>5) difficulity = 5.0;

        for (int i = 0; i < (int) difficulity; i++) {
            llRating.getChildAt(i).setBackgroundResource(R.drawable.icon_star_blue_full);
        }
        if (difficulity > (int) difficulity) {
            llRating.getChildAt((int) difficulity).setBackgroundResource(R.drawable.icon_star_blue_half);
        }
    }

}
