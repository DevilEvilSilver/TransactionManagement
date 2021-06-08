package com.example.projectnhom7.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectnhom7.GiaoDichActivity;
import com.example.projectnhom7.Model.GiaoDichModel;
import com.example.projectnhom7.R;
import com.example.projectnhom7.Utils.GiaoDichHandler;
import com.example.projectnhom7.Utils.UpdateGD;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GiaoDichAdapter extends RecyclerView.Adapter<GiaoDichAdapter.ViewHolder> {

    private List<GiaoDichModel> giaodichList;
    private GiaoDichHandler db;
    private GiaoDichActivity activity;

    public GiaoDichAdapter (GiaoDichHandler db, GiaoDichActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.giao_dich_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        GiaoDichModel item = giaodichList.get(position);
        String text = null;
        try {
            text = String.format("%s (%d VND - %s)", item.getName(), item.getMoney(), dateFormat(item.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.giaodich.setText(text);
    }

    public String dateFormat(String date) throws ParseException {
        SimpleDateFormat input = new SimpleDateFormat("yyyy/MM/dd");
        Date tmpDate = input.parse(date);
        DateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        return output.format(tmpDate);
    }

    public int getItemCount() {
        return giaodichList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setGiaoDich(List<GiaoDichModel> giaodichList) {
        this.giaodichList = giaodichList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        GiaoDichModel item = giaodichList.get(position);
        db.deleteGiaoDich(item.getId());
        giaodichList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        GiaoDichModel item = giaodichList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("name", item.getName());
        bundle.putInt("money", item.getMoney());
        bundle.putString("date", item.getDate());
        UpdateGD fragment = new UpdateGD();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), UpdateGD.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView giaodich;

        ViewHolder(View view) {
            super(view);
            giaodich = view.findViewById(R.id.GiaoDichTextView);
        }
    }
}
