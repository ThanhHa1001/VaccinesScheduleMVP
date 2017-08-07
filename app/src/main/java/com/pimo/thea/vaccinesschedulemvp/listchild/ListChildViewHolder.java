package com.pimo.thea.vaccinesschedulemvp.listchild;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 7/6/2017.
 */

public class ListChildViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    ImageView imgIcon;
    TextView txtName;
    TextView txtDob;
    TextView txtNextInject;
    long childId;
    long dob;

    public ListChildViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        imgIcon = (ImageView) itemView.findViewById(R.id.img_icon_item_list_child);
        txtName = (TextView) itemView.findViewById(R.id.txt_name_item_list_child);
        txtDob = (TextView) itemView.findViewById(R.id.txt_dob_item_list_child);
        txtNextInject = (TextView) itemView.findViewById(R.id.txt_next_inject_item_list_child);
    }
}
