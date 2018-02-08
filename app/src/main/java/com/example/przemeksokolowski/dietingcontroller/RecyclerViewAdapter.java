package com.example.przemeksokolowski.dietingcontroller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.przemeksokolowski.dietingcontroller.model.ChoosenProducts;
import com.example.przemeksokolowski.dietingcontroller.model.Product;
import com.example.przemeksokolowski.dietingcontroller.model.ProductListItem;

import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AdapterViewHolder> {

    private Context mContext;

    private ArrayList<ProductListItem> mChoosenProductsList;

    RecyclerViewAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_list_item, parent, false);
        view.setFocusable(true);

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position) {
        holder.nameTextView.setText(mChoosenProductsList.get(position).getName());
        holder.weightTextView.setText(mChoosenProductsList.get(position).getWeight());

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (mChoosenProductsList == null)
            return 0;
        else
            return mChoosenProductsList.size();
    }

    void swapChoosenProductsList(ArrayList<ProductListItem> newList) {
        mChoosenProductsList = newList;
        notifyDataSetChanged();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView nameTextView, weightTextView;

        AdapterViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.product_name_tv);
            weightTextView = itemView.findViewById(R.id.product_weight_tv);
        }
    }
}
