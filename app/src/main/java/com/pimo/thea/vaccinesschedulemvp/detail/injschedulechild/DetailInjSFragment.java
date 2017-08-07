package com.pimo.thea.vaccinesschedulemvp.detail.injschedulechild;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.detail.injchild.DetailInjCActivity;
import com.pimo.thea.vaccinesschedulemvp.editorchilds.EditorChildActivity;
import com.pimo.thea.vaccinesschedulemvp.editorchilds.EditorChildFragment;
import com.pimo.thea.vaccinesschedulemvp.utils.DateTimeHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thea on 8/3/2017.
 */

public class DetailInjSFragment extends Fragment implements DetailInjSContract.View{
    public static final String TAG = "DetailINJSFragment";
    public static final String CHILD_ID_ARG = "childId";
    public static final String CHILD_DOB_ARG = "childDOB";
    public static final int REQUEST_EDIT_CHILD = 100;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView imgAvatar;
    private TextView txtDOB;
    private TextView txtGender;
    private TextView txtPresentWeight;
    private TextView txtPresentHeight;
    private RecyclerView rvContent;
    private FloatingActionButton fabEditChild;

    private long childId;
    private long childDob;

    private DetailInjSContract.Presenter presenter;
    private DetailInjSAdapter detailInjSAdapter;

    public DetailInjSFragment() {

    }

    public static DetailInjSFragment newInstance() {
        return new DetailInjSFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        childId = bundle.getLong(CHILD_ID_ARG);
        childDob = bundle.getLong(CHILD_DOB_ARG);

        Log.d(TAG, "child id: " + childId + " -- child dob: " + childDob);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_detail_inj_schedule_child, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new DetailInjSPresenter(childId,
                childDob,
                VaccinesScheduleRepository.getInstance(VaccinesScheduleLocalDataSource.getInstance(getContext())),
                this,
                getContext());

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.detail_inj_schedule_child_collapsing_toolbar);
        toolbar = (Toolbar) view.findViewById(R.id.detail_inj_schedule_child_toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        imgAvatar = (ImageView) view.findViewById(R.id.detail_inj_schedule_child_img_avatar);
        txtDOB = (TextView) view.findViewById(R.id.detail_inj_schedule_child_txt_dob);
        txtGender = (TextView) view.findViewById(R.id.detail_inj_schedule_child_txt_gender);
        txtPresentWeight = (TextView) view.findViewById(R.id.detail_inj_schedule_child_txt_present_weight);
        txtPresentHeight = (TextView) view.findViewById(R.id.detail_inj_schedule_child_txt_present_height);

        fabEditChild = (FloatingActionButton) view.findViewById(R.id.detail_inj_schedule_child_fab_edit_child);
        fabEditChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditorChildActivity.class);
                intent.putExtra(EditorChildFragment.CHILD_ID_ARG, childId);
                startActivityForResult(intent, REQUEST_EDIT_CHILD);
            }
        });

        detailInjSAdapter = new DetailInjSAdapter(getContext(), new ArrayList<Object>(0), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailInjSChildViewHolder childViewHolder = (DetailInjSChildViewHolder) view.getTag();
                showDetailInjection(childViewHolder.injScheduleId);
            }
        });

        rvContent = (RecyclerView) view.findViewById(R.id.detail_inj_schedule_child_recycler_view);
        rvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvContent.setNestedScrollingEnabled(false);
        rvContent.setAdapter(detailInjSAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Override
    public void showFullnameChildInAppbarLayout(String fullname) {
        collapsingToolbarLayout.setTitle(fullname);
    }

    @Override
    public void showInformationChildInAppBarLayout(Child child) {
        String avatarUrl = child.getAvatarUrl();
        int gender = child.getGender();
        if (avatarUrl == null) {
            int avatarDrawable;
            if (gender == VaccinesScheduleLocalContract.ChildEntry.GENDER_MALE) {
                avatarDrawable = R.drawable.ic_face_black_male_24dp;
            } else {
                avatarDrawable = R.drawable.ic_face_black_24dp;
            }
            Glide.with(getContext())
                    .load(avatarDrawable)
                    .fitCenter()
                    .into(imgAvatar);
        } else {
            Glide.with(getContext())
                    .load(avatarUrl)
                    .fitCenter()
                    .into(imgAvatar);
        }

        String dobChild = DateTimeHelper.sdfDateFull.format(child.getDateOfBirth());
        txtDOB.setText(dobChild);
        if (gender == VaccinesScheduleLocalContract.ChildEntry.GENDER_MALE) {
            txtGender.setText(getString(R.string.gender_male_1));
        } else {
            txtGender.setText(getString(R.string.gender_female_1));
        }

        float presentWeight = child.getPresentWeight();
        if (presentWeight == 0F) {
            txtPresentWeight.setText("--");
        } else {
            String strPresentWeight = String.valueOf(presentWeight) + " kg";
            txtPresentWeight.setText(strPresentWeight);
        }

        int presentHeight = child.getPresentHeight();
        if (presentHeight == 0) {
            txtPresentHeight.setText("--");
        } else {
            String strPresentHeight = String.valueOf(presentHeight) + " cm";
            txtPresentHeight.setText(strPresentHeight);
        }
    }

    @Override
    public void showNoInformationChildInAppBarLayout() {

    }

    @Override
    public void showListInjScheduleByChildID(List<Object> objects) {
        detailInjSAdapter.replaceData(objects);
    }

    @Override
    public void showNoInjScheduleByChildID() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void showDetailInjection(long injScheduleId) {
        Intent intent = new Intent(getActivity(), DetailInjCActivity.class);
        intent.putExtra(DetailInjCActivity.INJECTION_ID_ARGS, injScheduleId);
        startActivity(intent);
    }
}
