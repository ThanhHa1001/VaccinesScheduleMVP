package com.pimo.thea.vaccinesschedulemvp.listchild;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract;
import com.pimo.thea.vaccinesschedulemvp.utils.DateTimeHelper;

import java.util.List;

/**
 * Created by thea on 7/1/2017.
 */

public class ListChildAdapter extends RecyclerView.Adapter<ListChildViewHolder> {

    private final View.OnClickListener childClickListener;
    private Context context;
    private List<Child> childList;

    public ListChildAdapter(Context context, List<Child> childList, View.OnClickListener childClickListener) {
        this.context = context;
        setList(childList);
        this.childClickListener = childClickListener;
    }

    public void replaceData(List<Child> childList) {
        setList(childList);
        notifyDataSetChanged();
    }

    private void setList(List<Child> childList) {
        this.childList = childList;
    }

    @Override
    public ListChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_child, parent, false);

        return new ListChildViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListChildViewHolder holder, int position) {
        Glide.with(context)
                .load(childList.get(position).getAvatarUrl())
                .fitCenter()
                .into(holder.imgIcon);

        long dobChild = childList.get(position).getDateOfBirth();

        holder.childId = childList.get(position).getId();
        holder.dob = dobChild;
        holder.txtName.setText(childList.get(position).getFullName());
        holder.txtDob.setText(DateTimeHelper.sdfDateFull.format(dobChild));
        String longToDate = DateTimeHelper.sdfDateFull.format(childList.get(position).getNextInject());
        String nextDayInjection = context.getString(R.string.item_child_list_next_day_injection, longToDate);
        holder.txtNextInject.setText(nextDayInjection);
        String avatarUrl = childList.get(position).getAvatarUrl();
        int gender = childList.get(position).getGender();
        if (avatarUrl == null) {
            int avatarDrawable;
            if (gender == VaccinesScheduleLocalContract.ChildEntry.GENDER_MALE) {
                avatarDrawable = R.drawable.ic_face_black_male_24dp;
            } else {
                avatarDrawable = R.drawable.ic_face_black_24dp;
            }
            Glide.with(context)
                    .load(avatarDrawable)
                    .fitCenter()
                    .into(holder.imgIcon);
        } else {
            Glide.with(context)
                    .load(avatarUrl)
                    .fitCenter()
                    .into(holder.imgIcon);
        }
        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(childClickListener);
    }

    @Override
    public int getItemCount() {
        if (childList == null) {
            return 0;
        }
        return childList.size();
    }
}
