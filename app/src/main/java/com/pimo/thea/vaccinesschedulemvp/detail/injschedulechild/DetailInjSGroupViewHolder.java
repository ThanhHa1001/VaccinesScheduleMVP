package com.pimo.thea.vaccinesschedulemvp.detail.injschedulechild;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 8/3/2017.
 */

public class DetailInjSGroupViewHolder extends RecyclerView.ViewHolder {

    TextView txtTitleGroup;

    public DetailInjSGroupViewHolder(View itemView) {
        super(itemView);
        txtTitleGroup = (TextView) itemView.findViewById(R.id.txt_title_item_detail_inj_schedule_child_group);
    }
}
