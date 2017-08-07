package com.pimo.thea.vaccinesschedulemvp.detail.injschedulechild;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.utils.DateTimeHelper;
import com.pimo.thea.vaccinesschedulemvp.utils.VaccineHelper;

import java.util.List;

/**
 * Created by thea on 8/3/2017.
 */

public class DetailInjSAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int GROUP = 0;
    private static final int CHILD = 1;

    private final View.OnClickListener injScheduleClickListener;
    private Context context;
    private List<Object> objectList;

    public DetailInjSAdapter(Context context, List<Object> objectList, View.OnClickListener injScheduleClickListener) {
        this.context = context;
        setList(objectList);
        this.injScheduleClickListener = injScheduleClickListener;
    }

    private void setList(List<Object> objectList) {
        this.objectList = objectList;
    }

    public void replaceData(List<Object> objectList) {
        setList(objectList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == GROUP) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_detail_inj_schedule_child_group, parent, false);
            return new DetailInjSGroupViewHolder(view);
        } else {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_detail_inj_schedule_child_child, parent, false);
            return new DetailInjSChildViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object object = objectList.get(position);
        if (holder.getItemViewType() == GROUP) {
            DetailInjSGroupViewHolder groupViewHolder = (DetailInjSGroupViewHolder) holder;
            groupViewHolder.txtTitleGroup.setText((CharSequence) object);
        } else {
            InjSchedule injSchedule = (InjSchedule) object;
            DetailInjSChildViewHolder childViewHolder = (DetailInjSChildViewHolder) holder;
            childViewHolder.txtTitle.setText(injSchedule.getTitle());
            childViewHolder.txtDayInjection.setText(
                    DateTimeHelper.sdfDateFull.format(injSchedule.getDayInjection()));
            String shotNumberInject = context.getString(R.string.shot_number_inject, String.valueOf(injSchedule.getShotNumber()));
            childViewHolder.txtShotNumber.setText(shotNumberInject);

            String vaccineCode = injSchedule.getVaccineCode();
            if (injSchedule.isInjected()) {
                childViewHolder.txtIsInject.setText(context.getString(R.string.is_inject_done));
                Glide.with(context)
                        .load(VaccineHelper.getIconResourcesInjectDone(vaccineCode))
                        .fitCenter()
                        .into(childViewHolder.imgIcon);
            } else {
                childViewHolder.txtIsInject.setText(context.getString(R.string.is_inject_not_done));
                Glide.with(context)
                        .load(VaccineHelper.getIconResourcesInjectNotDone(vaccineCode))
                        .fitCenter()
                        .into(childViewHolder.imgIcon);
            }
            childViewHolder.injScheduleId = injSchedule.getId();
            childViewHolder.itemView.setTag(childViewHolder);
            childViewHolder.itemView.setOnClickListener(injScheduleClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList != null) {
            if (objectList.get(position) instanceof InjSchedule) {
                return CHILD;
            }
            return GROUP;
        }
        return  -1;
    }

    @Override
    public int getItemCount() {
        if (objectList == null) {
            return  0;
        }
        return objectList.size();
    }
}
