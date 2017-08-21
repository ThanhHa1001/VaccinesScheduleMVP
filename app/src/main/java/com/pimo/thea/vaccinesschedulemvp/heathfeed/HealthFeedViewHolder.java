package com.pimo.thea.vaccinesschedulemvp.heathfeed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 8/10/2017.
 */

public class HealthFeedViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    TextView txtTitleAsk;
    TextView txtContentAnswerNotFull;
    ImageView imgShare;
    ImageView imgBookmark;
    String urlHealthFeed;

    public HealthFeedViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        txtTitleAsk = (TextView) itemView.findViewById(R.id.item_list_health_feed_txt_title);
        txtContentAnswerNotFull = (TextView) itemView.findViewById(R.id.item_list_health_feed_txt_content);
        imgShare = (ImageView) itemView.findViewById(R.id.item_list_health_feed_img_share);
        imgBookmark = (ImageView) itemView.findViewById(R.id.item_list_health_feed_img_bookmark);
    }
}
