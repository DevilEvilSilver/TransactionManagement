package com.example.projectnhom7.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectnhom7.Model.KhoanDongModel;
import com.example.projectnhom7.R;
import com.example.projectnhom7.SavingActivity;
import com.example.projectnhom7.Utils.KhoanDongHandler;
import com.example.projectnhom7.Utils.UpdateKD;

import java.util.List;

public class KhoanDongAdapter extends RecyclerView.Adapter<KhoanDongAdapter.ViewHolder> {

    private List<KhoanDongModel> khoanDongList;
    private KhoanDongHandler db;
    private SavingActivity activity;

    public KhoanDongAdapter (KhoanDongHandler db, SavingActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    public KhoanDongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.khoan_dong_layout, parent, false);
        return new KhoanDongAdapter.ViewHolder(itemView);
    }

    public void onBindViewHolder(KhoanDongAdapter.ViewHolder holder, int position) {
        KhoanDongModel item = khoanDongList.get(position);
        String text = String.format("%s (%d VND)", item.getName(), item.getMoney());
        holder.khoandong.setText(text);
    }

    public int getItemCount() {
        return khoanDongList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setKhoanDong(List<KhoanDongModel> khoanDongList) {
        this.khoanDongList = khoanDongList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        KhoanDongModel item = khoanDongList.get(position);
        db.deleteKhoanDong(item.getId());
        khoanDongList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        KhoanDongModel item = khoanDongList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("name", item.getName());
        bundle.putInt("money", item.getMoney());
        UpdateKD fragment = new UpdateKD();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), UpdateKD.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView khoandong;

        ViewHolder(View view) {
            super(view);
            khoandong = view.findViewById(R.id.KhoanDongTextView);
        }
    }
}
