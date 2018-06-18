package com.qinlong275.android.picu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinlong275.android.picu.R;
import com.qinlong275.android.picu.bean.PicUItem;
import com.qinlong275.android.picu.ui.activity.myshow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PicUlistAdapter extends RecyclerView.Adapter<PicUlistAdapter.ViewHolder> implements View.OnClickListener{

    private List<PicUItem> mPicUItemList=new ArrayList<>();
    private int mState;
    private Context mContext;

    //state:1:收到的 2：制作的
    public PicUlistAdapter(int state, Context context,List<PicUItem> uItems) {
        mState = state;
        mContext=context;
        mPicUItemList=uItems;
    }

//    public void setDate(ArrayList<PicUItem> list){
//        mPicUItemList=list;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reclycle_item_common, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PicUItem picUItem = mPicUItemList.get(position);
        holder.mIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,myshow.class);
                intent.putExtra("secretType",picUItem.getSecretType());
                mContext.startActivity(intent);
            }
        });

        if (mState == 1) {
            holder.mPublishDate.setVisibility(View.INVISIBLE);
        } else {
            holder.mMyreceivephotolock.setVisibility(View.INVISIBLE);
            holder.mMyreceivetipname.setVisibility(View.INVISIBLE);
            holder.mMyreceivetiptime.setVisibility(View.INVISIBLE);
        }

        //根据picUItem的具体内容来绑定头像，图片，水印样式
        if (picUItem.getUnBlockType().equals("2")){
            holder.mMyreceivephotowatermark.setImageResource(R.drawable.btn_watermark_sketch);
        }else if (picUItem.getUnBlockType().equals("3")){
            holder.mMyreceivephotowatermark.setImageResource(R.drawable.btn_watermark_click);
        }

        if (picUItem.getPictureUrl().equals("a")){
            holder.mIconImage.setImageResource(R.drawable.orange);
        }

        if (picUItem.getUserid().equals("q")){
            holder.mUserShow.setImageResource(R.drawable.strawberry);
        }

        if (picUItem.getIsBlock().equals("true")){
            holder.mMyreceivephotolock.setImageResource(R.drawable.bt_lock);
        }
    }

    @Override
    public int getItemCount() {
        return mPicUItemList.size();
    }

    @Override
    public void onClick(View view) {

    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.icon_image)
        ImageView mIconImage;
        @BindView(R.id.draw_image)
        RelativeLayout mDrawImage;
        @BindView(R.id.drawCirViewCon)
        RelativeLayout mDrawCirViewCon;
        @BindView(R.id.user_show)
        CircleImageView mUserShow;
        @BindView(R.id.myreceivephotolock)
        ImageView mMyreceivephotolock;
        @BindView(R.id.myreceivephotowatermark)
        ImageView mMyreceivephotowatermark;
        @BindView(R.id.pic_card)
        CardView mPicCard;
        @BindView(R.id.myreceivetipname)
        TextView mMyreceivetipname;
        @BindView(R.id.myreceivetiptime)
        TextView mMyreceivetiptime;
        @BindView(R.id.publish_date)
        TextView mPublishDate;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
