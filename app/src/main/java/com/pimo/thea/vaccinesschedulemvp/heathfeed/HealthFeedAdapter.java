package com.pimo.thea.vaccinesschedulemvp.heathfeed;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.HealthFeed;

import java.util.List;

/**
 * Created by thea on 8/10/2017.
 */

public class HealthFeedAdapter extends RecyclerView.Adapter<HealthFeedViewHolder> {

    private HealthFeedItemClickListener healthFeedItemClickListener;
    private List<HealthFeed> healthFeeds;
    private Context context;

    public HealthFeedAdapter(Context context, List<HealthFeed> healthFeeds) {
        this.context = context;
        replaceData(healthFeeds);
    }

    public void replaceData(List<HealthFeed> healthFeeds) {
        setList(healthFeeds);
        notifyDataSetChanged();
    }

    private void setList(List<HealthFeed> healthFeeds) {
        this.healthFeeds = healthFeeds;
    }

    @Override
    public HealthFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_list_health_feed, parent, false);
        return new HealthFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HealthFeedViewHolder holder, int position) {
        final HealthFeed healthFeed = healthFeeds.get(position);
        holder.txtTitleAsk.setText(healthFeed.getTitleAsk());
        holder.txtContentAnswerNotFull.setText(healthFeed.getContentAnswer());
        final boolean isBookmark = healthFeed.isBookmark();
        if (isBookmark) {
            Glide.with(context)
                    .load(R.drawable.ic_bookmark_blue_24dp)
                    .into(holder.imgBookmark);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_bookmark_outline_grey_24dp)
                    .into(holder.imgBookmark);
        }
        final String url = healthFeed.getUrl();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthFeedItemClickListener.onHealthFeedClick(url);
            }
        });

        holder.imgBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthFeedItemClickListener.onAddBookmark(healthFeed);
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                healthFeedItemClickListener.onShareHealthFeed(url);
            }
        });

        if (position == healthFeeds.size() - 1) {
            healthFeedItemClickListener.onLoadMore(position);
        }

        holder.itemView.setTag(holder);

    }

    @Override
    public int getItemCount() {
        if (healthFeeds == null) {
            return 0;
        }
        return healthFeeds.size();
    }

    public void setHealthyFeedItemClickListener(HealthFeedItemClickListener healthyFeedItemClickListenner) {
        this.healthFeedItemClickListener = healthyFeedItemClickListenner;
    }

    public interface HealthFeedItemClickListener {

        void onShareHealthFeed(String url);

        void onAddBookmark(HealthFeed healthFeed);

        void onHealthFeedClick(String url);

        void onLoadMore(int position);
    }


}
