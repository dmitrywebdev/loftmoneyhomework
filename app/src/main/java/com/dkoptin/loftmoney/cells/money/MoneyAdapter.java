package com.dkoptin.loftmoney.cells.money;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dkoptin.loftmoney.MoneyAdapterListener;
import com.dkoptin.loftmoney.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.MoneyViewHolder> {

    private List<MoneyCellModel> moneyCellModels = new ArrayList<>();
    private MoneyAdapterListener moneyAdapterListener;

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();

    public void clearSelections() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItem(final int position) {
        mSelectedItems.put(position, !mSelectedItems.get(position));
        notifyDataSetChanged();
    }

    public void clearItem(final int position) {
        mSelectedItems.put(position, false);
        notifyDataSetChanged();
    }

    public int getSelectedSize() {
        int result = 0;
        for (int i = 0; i < moneyCellModels.size(); i++) {
            if (mSelectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }


    public List<Integer> getSelectedItemIds() {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        for (MoneyCellModel moneyCellModel : moneyCellModels) {
            if (mSelectedItems.get(i)) {
                result.add(Integer.valueOf(moneyCellModel.getId()));
            }
            i++;
        }
        return result;
    }

    public void setMoneyAdapterListener(MoneyAdapterListener moneyAdapterListener) {
        this.moneyAdapterListener = moneyAdapterListener;
    }

    public void setData(List<MoneyCellModel> moneyCellModels) {
        this.moneyCellModels.clear();
        this.moneyCellModels.addAll(moneyCellModels);
        notifyDataSetChanged();
    }

    public void addData(List<MoneyCellModel> moneyCellModels) {
        this.moneyCellModels.addAll(moneyCellModels);
        notifyDataSetChanged();
    }

   public void sortArrayList() {
        Collections.sort(moneyCellModels, new Comparator<MoneyCellModel>(){
            @Override
            public int compare(MoneyCellModel item1, MoneyCellModel item2) {
                return item1.getDate().compareTo(item2.getDate());
            }
        });
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MoneyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_money, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MoneyViewHolder holder, int position) {
        holder.bind(moneyCellModels.get(position), mSelectedItems.get(position));
        holder.setListener(moneyAdapterListener, moneyCellModels.get(position), position);
    }

    @Override
    public int getItemCount() {
        return moneyCellModels.size();
    }

    static class MoneyViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        private TextView nameView;
        private TextView valueView;

        public MoneyViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemView = itemView;
            nameView = itemView.findViewById(R.id.cellMoneyNameView);
            valueView = itemView.findViewById(R.id.cellMoneyValueView);

        }

        public void bind(MoneyCellModel moneyCellModel, final boolean isSelected) {
            mItemView.setSelected(isSelected);
            nameView.setText(moneyCellModel.getName());
            valueView.setText(moneyCellModel.getValue());
            valueView.setTextColor(ContextCompat.getColor(valueView.getContext(), moneyCellModel.getColor()));
        }

        public void setListener(final MoneyAdapterListener listener, final MoneyCellModel moneyCellModel, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(moneyCellModel, position);
                }
            });
            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(moneyCellModel, position);
                    return false;
                }
            });
        }
    }
}
