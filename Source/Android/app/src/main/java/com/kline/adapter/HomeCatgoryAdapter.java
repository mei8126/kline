package com.kline.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kline.bean.Campaign;
import com.kline.bean.HomeCampaign;
import com.kline.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mei on 2016/3/10.
 */
public class HomeCatgoryAdapter extends RecyclerView.Adapter<HomeCatgoryAdapter.ViewHodler> {

    private final int VIEW_TYPE_L = 0;
    private final int VIEW_TYPE_R = 1;

    private List<HomeCampaign> mDatas;
    private LayoutInflater mInflater;
    private OnCampainClickListener mListener;
    private Context mContext;

    public HomeCatgoryAdapter(List<HomeCampaign> datas, Context context) {
        mDatas = datas;
        mContext = context;
    }

    public void setOnCampaignClickListener(OnCampainClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        mInflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_R) {
            view = mInflater.inflate(R.layout.template_home_cardview2, parent, false);
        } else {
            view = mInflater.inflate(R.layout.template_home_cardview, parent, false);
        }
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {
        HomeCampaign homeCampaign = mDatas.get(position);
        holder.textTitle.setText(homeCampaign.getTitle());

//        holder.imageViewBig.setImageResource(homeCampaign.getImgBig());
//        holder.imageViewSmallTop.setImageResource(category.getImgSmallTop());
//        holder.imageViewSmallBottom.setImageResource(category.getImgSmallBottom());

        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        }
        return VIEW_TYPE_L;
    }

    class ViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textTitle;
        private ImageView imageViewBig;
        private ImageView imageViewSmallTop;
        private ImageView imageViewSmallBottom;

        public ViewHodler(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imageview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            anim(view);

        }

        private void anim(final View v) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    HomeCampaign campaign = mDatas.get(getLayoutPosition());
                    switch (v.getId()) {
                        case R.id.imageview_big:
                            mListener.onClick(v, campaign.getCpOne());
                            break;
                        case R.id.imgview_small_top:
                            mListener.onClick(v, campaign.getCpTwo());
                            break;
                        case R.id.imgview_small_bottom:
                            mListener.onClick(v, campaign.getCpThree());
                            break;
                    }
                }
            });
            animator.start();
        }
    }


    public interface OnCampainClickListener {
        public void onClick(View view, Campaign campaign);
    }

}
