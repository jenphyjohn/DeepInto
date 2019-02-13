package com.deepinto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepinto.R;
import com.deepinto.entity.FraMainListEntity;
import com.mincat.sample.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class FraMainListAdapter extends RecyclerView.Adapter<FraMainListAdapter.LinearViewHolder> {


    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private List<FraMainListEntity> list = new ArrayList<>();

    public FraMainListAdapter(Context mContext) {
        this.mContext = mContext;
        for (int i = 0; i < 10; i++) {

            list.add(new FraMainListEntity("", "", "", ""));
        }
    }

    @Override
    public FraMainListAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_fra_main, parent, false));
    }

    @Override
    public void onBindViewHolder(FraMainListAdapter.LinearViewHolder holder, int position) {



        if (position%2==0){

            ScreenUtils.setMargins(holder.rl_adapter_contain,0,0,10,20);

        }else {
            ScreenUtils.setMargins(holder.rl_adapter_contain,10,0,0,20);
        }

        if (position == 0) {
            holder.mImageView.setBackgroundResource(R.drawable.list_01);
            holder.tv_desc.setText("NIKE HOOPS 双肩包男学生帆布包2018新");
            holder.tv_price.setText("￥599");
            holder.tv_buyers.setText("481人已买");
        } else if (position == 1) {
            holder.mImageView.setBackgroundResource(R.drawable.list_02);
            holder.tv_desc.setText("【月销量超千笔】简约风格台灯护眼学生桌插电式家用灯泡");
            holder.tv_price.setText("￥39");
            holder.tv_buyers.setText("1881人已买");
        } else if (position == 2) {
            holder.mImageView.setBackgroundResource(R.drawable.list_03);
            holder.tv_desc.setText("【月销量超千笔】蓝山咖啡粉100条装,三合一速溶白咖啡礼盒");
            holder.tv_price.setText("￥84");
            holder.tv_buyers.setText("2266人已买");
        } else if (position == 3) {
            holder.mImageView.setBackgroundResource(R.drawable.list_04);
            holder.tv_desc.setText("【正品保证】电风扇家用遥控式强力电扇静音大风力");
            holder.tv_price.setText("￥89");
            holder.tv_buyers.setText("592人已买");
        } else if (position == 4) {
            holder.mImageView.setBackgroundResource(R.drawable.list_05);
            holder.tv_desc.setText("【月销量超万笔】正品马丁男士洗发水去头屑控油留香洗发露");
            holder.tv_price.setText("￥39.8");
            holder.tv_buyers.setText("1.9万人已买");
        } else if (position == 5) {
            holder.mImageView.setBackgroundResource(R.drawable.list_06);
            holder.tv_desc.setText("【兄弟体育正品】air jordan 3白水泥,男子篮球鞋");
            holder.tv_price.setText("￥2499");
            holder.tv_buyers.setText("13人付款");
        } else if (position == 6) {
            holder.mImageView.setBackgroundResource(R.drawable.list_07);
            holder.tv_desc.setText("NIKE Dunk sb 短袖T恤");
            holder.tv_price.setText("￥399");
            holder.tv_buyers.setText("113人已买");
        } else if (position == 7) {
            holder.mImageView.setBackgroundResource(R.drawable.list_08);
            holder.tv_desc.setText("情人节限定 兰蔻菁纯润唇膏");
            holder.tv_price.setText("￥279");
            holder.tv_buyers.setText("899人已买");
        } else if (position == 8) {
            holder.mImageView.setBackgroundResource(R.drawable.list_09);
            holder.tv_desc.setText("【月销量超千笔】官方正品兰蔻小黑瓶");
            holder.tv_price.setText("￥520");
            holder.tv_buyers.setText("1900人已买");
        } else if (position == 9) {
            holder.mImageView.setBackgroundResource(R.drawable.list_10);
            holder.tv_desc.setText("【月销量超千笔】余文乐同款近视眼镜");
            holder.tv_price.setText("￥499");
            holder.tv_buyers.setText("1083人付款");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView tv_desc, tv_price, tv_buyers;
        private RelativeLayout rl_adapter_contain;

        public LinearViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_buyers = itemView.findViewById(R.id.tv_buyers);
            rl_adapter_contain = itemView.findViewById(R.id.rl_adapter_contain);
        }
    }

}
