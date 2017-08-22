package com.kofetochka.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kofetochka.dto.ApplicationPartDTO;
import com.kofetochka.kofetochkablg.R;

import java.util.List;

public class ApplicationPartListAdapter extends RecyclerView.Adapter<ApplicationPartListAdapter.ViewHolder> {

    private List<ApplicationPartDTO> data;
    public static OnItemClickListener listener;

    public ApplicationPartListAdapter(List<ApplicationPartDTO> data) {
        this.data = data;
    }

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void addItem(int position, ApplicationPartDTO applicationPartDTO){
        this.data.add(position, applicationPartDTO);
    }

    public void deleteItem(int position){
        this.data.remove(position);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView tv_Title, tv_Subtitle, tv_Free, tv_Price, tv_ID_AP;
        ImageView iv_Edit, iv_Copy, iv_Delete, iv_Free;

        public ViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_applicationpart);
            tv_Title = (TextView) itemView.findViewById(R.id.textView_Title);
            tv_Free = (TextView) itemView.findViewById(R.id.textView_Free);
            tv_Price = (TextView) itemView.findViewById(R.id.textView_Price);
            tv_Subtitle = (TextView) itemView.findViewById(R.id.textView_Subtitle);
            tv_ID_AP = (TextView) itemView.findViewById(R.id.textView_ID_AP);
            iv_Copy = (ImageView) itemView.findViewById(R.id.imageView_Copy);
            iv_Copy.setOnClickListener(this);
            iv_Edit = (ImageView) itemView.findViewById(R.id.imageView_Edit);
            iv_Edit.setOnClickListener(this);
            iv_Delete = (ImageView) itemView.findViewById(R.id.imageView_Delete);
            iv_Delete.setOnClickListener(this);
            iv_Free = (ImageView) itemView.findViewById(R.id.imageView_Free);
            iv_Free.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(ApplicationPartListAdapter.listener!=null){
                ApplicationPartListAdapter.listener.onItemClick(itemView,getLayoutPosition(),v);
            }
        }
    }


    @Override
    public ApplicationPartListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applicationpart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_Title.setText(data.get(position).getTitle());
        holder.tv_Price.setText(data.get(position).getPrice());
        holder.tv_Subtitle.setText(data.get(position).getSubtitle());
        holder.tv_ID_AP.setText(data.get(position).getID_AP());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
