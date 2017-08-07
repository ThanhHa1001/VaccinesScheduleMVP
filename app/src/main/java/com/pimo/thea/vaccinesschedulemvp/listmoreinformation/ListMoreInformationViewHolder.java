package com.pimo.thea.vaccinesschedulemvp.listmoreinformation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 8/5/2017.
 */

public class ListMoreInformationViewHolder extends RecyclerView.ViewHolder {
    View itemView;
    ImageView imgIcon;
    TextView txtTitle;
    long objectId;
    int request;

    public ListMoreInformationViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        imgIcon = (ImageView) itemView.findViewById(R.id.item_list_more_information_img_icon);
        txtTitle = (TextView) itemView.findViewById(R.id.item_list_more_information_txt_title);
    }


}
