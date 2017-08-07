package com.pimo.thea.vaccinesschedulemvp.listmoreinformation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.Childcare;
import com.pimo.thea.vaccinesschedulemvp.data.Disease;
import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;
import com.pimo.thea.vaccinesschedulemvp.utils.ChildcareHelper;
import com.pimo.thea.vaccinesschedulemvp.utils.DiseaseHelper;
import com.pimo.thea.vaccinesschedulemvp.utils.VaccineHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thea on 8/5/2017.
 */

public class ListMoreInformationAdapter extends RecyclerView.Adapter<ListMoreInformationViewHolder> {

    public static final int DISEASE = 1;
    public static final int VACCINE = 2;
    public static final int CHILDCARE = 3;

    private View.OnClickListener onClickListener;
    private Context context;
    private List<Object> objects;

    public ListMoreInformationAdapter(Context context, List<Object> objects, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        replaceData(objects);
    }

    public void replaceData(List<Object> objects) {
        setList(objects);
        notifyDataSetChanged();
    }

    private void setList(List<Object> objects) {
        this.objects = objects;
    }

    @Override
    public ListMoreInformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_more_information, parent, false);
        return new ListMoreInformationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListMoreInformationViewHolder holder, int position) {
        if (holder.getItemViewType() == DISEASE) {
            Disease disease = (Disease) objects.get(position);
            Glide.with(context)
                    .load(DiseaseHelper.getIconResourcesDiseaseCode(disease.getCode()))
                    .fitCenter()
                    .into(holder.imgIcon);
            String title = context.getString(R.string.item_list_more_information_title, disease.getName());
            holder.txtTitle.setText(title);
            holder.objectId = disease.getDiseaseId();
            holder.request = DISEASE;
        } else if (holder.getItemViewType() == VACCINE) {
            Vaccine vaccine = (Vaccine) objects.get(position);
            Glide.with(context)
                    .load(VaccineHelper.getIconResourcesInjectNotDone(vaccine.getCode()))
                    .fitCenter()
                    .into(holder.imgIcon);
            holder.txtTitle.setText(vaccine.getName());
            holder.objectId = vaccine.getVaccineId();
            holder.request = VACCINE;
        } else {
            Childcare childcare = (Childcare) objects.get(position);
            Glide.with(context)
                    .load(ChildcareHelper.getChildcareDrawableId((int) childcare.getChildcareId()))
                    .fitCenter()
                    .into(holder.imgIcon);
            holder.txtTitle.setText(childcare.getTitle());
            holder.objectId = childcare.getChildcareId();
            holder.request = CHILDCARE;
        }
        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        if (objects == null) {
            return 0;
        }
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (objects.get(position) instanceof Disease) {
            return DISEASE;
        } else if (objects.get(position) instanceof Vaccine) {
            return VACCINE;
        } else {
            return CHILDCARE;
        }
    }
}
