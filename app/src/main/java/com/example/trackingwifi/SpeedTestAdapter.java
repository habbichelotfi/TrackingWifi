package com.example.trackingwifi;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.internet_speed_testing.ProgressionModel;
import com.example.trackingwifi.ui.frag2.ListwifiAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpeedTestAdapter extends RecyclerView.Adapter<SpeedTestAdapter.ViewHolder> {
    private List<SpeedTest_> list;
    private Cursor cursor;
    private LayoutInflater mInflater;
    private List<ProgressionModel> dataList = new ArrayList<>();

    private ListwifiAdapter.ItemClickListener mClickListener;

    public SpeedTestAdapter(Context context, List<SpeedTest_> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }
    public SpeedTestAdapter(Context context, Cursor cursor) {
        this.mInflater = LayoutInflater.from(context);
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public SpeedTestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.wifi_test, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeedTestAdapter.ViewHolder holder, int position) {
        if (list!=null) {
            SpeedTest_ speedTest_ = list.get(position);

            holder.date.setText(list.get(position).getDate());
            holder.upload.setText(list.get(position).getUpload());
            holder.download.setText(list.get(position).getDownload());
            holder.namewifi.setText(speedTest_.getNamewifi());
            holder.number.setText(""+position);
        }
        else{

            while(cursor.moveToNext()) {
                int index;

                index = cursor.getColumnIndexOrThrow(SampleSQLiteDBHelper.PERSON_COLUMN_NAME);
                String name = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow(SampleSQLiteDBHelper.PERSON_COLUMN_D);
                String d = cursor.getString(index);

                index = cursor.getColumnIndexOrThrow("Upload");
                String u = cursor.getString(index);
                index = cursor.getColumnIndexOrThrow(SampleSQLiteDBHelper.PERSON_COLUMN_DATE);
                String date = cursor.getString(index);
                holder.date.setText(date);
                holder.upload.setText(u);
                holder.download.setText(d);
                holder.namewifi.setText(name);
                //... do something with data
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list==null){
            return cursor.getCount();
        }
        return list.size();
    }

    public void setDataList(int count, ProgressionModel progressModel) {
        if (dataList.size() <= count) {
            dataList.add(progressModel);

        } else {
            dataList.set(count, progressModel);

        }

        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView download,upload, date, namewifi,number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
number=itemView.findViewById(R.id.number);
            download = itemView.findViewById(R.id.download);
            namewifi = itemView.findViewById(R.id.wifi_name);
            upload = itemView.findViewById(R.id.upload);
            date = itemView.findViewById(R.id.date);

        }
    }
}
