package com.pimo.thea.vaccinesschedulemvp.editorchilds;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pimo.thea.vaccinesschedulemvp.R;

import java.util.ArrayList;

/**
 * Created by thea on 7/23/2017.
 */

public class CroppingOptionAdapter extends ArrayAdapter {

    private ArrayList options;
    private LayoutInflater inflater;

    public CroppingOptionAdapter(@NonNull Context context, ArrayList options) {
        super(context, R.layout.selector_cropping_option, options);
        this.options = options;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.selector_cropping_option, null);
        }
        CroppingOption croppingOptionItem = (CroppingOption) options.get(position);

        if (croppingOptionItem != null) {
            ((ImageView) convertView.findViewById(R.id.img_selector_cropping_option))
                    .setImageDrawable(croppingOptionItem.getIcon());
            ((TextView) convertView.findViewById(R.id.txt_selector_cropping_option))
                    .setText(croppingOptionItem.getTitle());
            return convertView;
        }
        return null;
    }
}
