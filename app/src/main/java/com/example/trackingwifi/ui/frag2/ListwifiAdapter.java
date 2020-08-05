package com.example.trackingwifi.ui.frag2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trackingwifi.R;
import com.example.trackingwifi.WifiInfo_;

import java.util.List;

public class ListwifiAdapter extends RecyclerView.Adapter<ListwifiAdapter.ViewHolder> {
    private List<WifiInfo_> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    public ListwifiAdapter(Context context,List<WifiInfo_> list){
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.wifi_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WifiInfo_ t = list.get(position);
      holder.channel.setText("Channel: "+t.getChannel());
      holder.namewifi.setText(t.getName());
      holder.number.setText(""+position);
      holder.dbm.setText(""+t.getDmb()+" dbm");
      holder.mac.setText("Mac: "+t.getBssid());
        holder.couleur.setBackground(t.getColor());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView number,namewifi,mac,dbm,channel;
                ImageView couleur;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.number);
            namewifi=itemView.findViewById(R.id.name_wifi);
            channel=itemView.findViewById(R.id.channel);
            mac=itemView.findViewById(R.id.mac);
            dbm=itemView.findViewById(R.id.dbm);
            couleur=itemView.findViewById(R.id.colour_wifi);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());

        }
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
