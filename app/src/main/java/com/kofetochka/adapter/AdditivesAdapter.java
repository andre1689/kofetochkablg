package com.kofetochka.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kofetochka.dto.AdditivesDTO;
import com.kofetochka.kofetochkablg.R;

import java.util.List;

public class AdditivesAdapter extends RecyclerView.Adapter<AdditivesAdapter.ViewHolder>{

    private List<AdditivesDTO> data;
    public static OnItemClickListener listener;

    public AdditivesAdapter(List<AdditivesDTO> data){
        this.data = data;
    }


    public interface OnItemClickListener{
        void onItemClick(View itemView, int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public AdditivesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_check,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdditivesAdapter.ViewHolder holder, int position) {
        holder.tv_ID_Additives.setText(data.get(position).getID_Additives());
        holder.tv_Name_Additives.setText(data.get(position).getName_Additives());
        holder.tv_Price_Additives.setText(data.get(position).getPrice_Additives());
        holder.cb_Additives.setChecked(data.get(position).getCheckBox_Additives());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_ID_Additives, tv_Name_Additives, tv_Price_Additives;
        CheckBox cb_Additives;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_ID_Additives = (TextView)itemView.findViewById(R.id.textView_ID_Additives);
            tv_Name_Additives = (TextView)itemView.findViewById(R.id.textView_NameAdditives);
            tv_Price_Additives = (TextView)itemView.findViewById(R.id.textView_PriceAdditives);
            cb_Additives = (CheckBox)itemView.findViewById(R.id.checkBox_Additives2);
            cb_Additives.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (AdditivesAdapter.listener!=null){
                AdditivesAdapter.listener.onItemClick(itemView,getLayoutPosition(),v);
            }
        }
    }
}
