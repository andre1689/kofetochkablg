package com.kofetochka.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kofetochka.dto.ApplicationPartDTO;
import com.kofetochka.kofetochkablg.R;

import java.util.List;

public class ApplicationPartListAdapter extends RecyclerView.Adapter<ApplicationPartListAdapter.ViewHolder> {

    private List<ApplicationPartDTO> data;

    public ApplicationPartListAdapter(List<ApplicationPartDTO> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView tv_Title, tv_Subtitle, tv_Free, tv_Price;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_applicationpart);
            tv_Title = (TextView) itemView.findViewById(R.id.textView_Title);
            tv_Price = (TextView) itemView.findViewById(R.id.textView_Price);
            tv_Subtitle = (TextView) itemView.findViewById(R.id.textView_Subtitle);
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
