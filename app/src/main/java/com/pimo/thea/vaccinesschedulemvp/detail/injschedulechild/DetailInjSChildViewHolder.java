package com.pimo.thea.vaccinesschedulemvp.detail.injschedulechild;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 8/3/2017.
 */

public class DetailInjSChildViewHolder extends RecyclerView.ViewHolder{
    View itemView;
    ImageView imgIcon;
    TextView txtTitle;
    TextView txtShotNumber;
    TextView txtDayInjection;
    TextView txtIsInject;
    long injScheduleId;

    public DetailInjSChildViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        imgIcon = (ImageView) itemView.findViewById(R.id.img_icon_item_detail_inj_schedule_child);
        txtTitle = (TextView) itemView.findViewById(R.id.txt_title_item_detail_inj_schedule_child);
        txtShotNumber = (TextView) itemView.findViewById(R.id.txt_shot_number_item_detail_inj_schedule_child);
        txtDayInjection = (TextView) itemView.findViewById(R.id.txt_day_injection_item_detail_inj_schedule_child);
        txtIsInject = (TextView) itemView.findViewById(R.id.txt_is_inject_item_detail_inj_schedule_child);
    }


}
