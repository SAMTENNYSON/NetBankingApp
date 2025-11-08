package com.example.netbankingapp.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiService.TransactionItem;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class MiniStatementAdapter extends RecyclerView.Adapter<MiniStatementAdapter.VH> {

    private final List<TransactionItem> items;

    public MiniStatementAdapter(List<TransactionItem> items) {
        this.items = items;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        TransactionItem t = items.get(position);
        String type = t.type != null ? t.type.toUpperCase() : "DEBIT";
        holder.tvType.setText(type);
        holder.tvAmount.setText("₹" + String.format("%.2f", t.amount));
        holder.tvDate.setText(t.date);
        holder.tvDescription.setText(t.description != null ? t.description : "");
        
        // Set badge color based on transaction type
        if (holder.badgeCard != null) {
            int badgeColor = type.equals("CREDIT") ? 
                ContextCompat.getColor(holder.itemView.getContext(), R.color.credit_green) :
                ContextCompat.getColor(holder.itemView.getContext(), R.color.debit_red);
            holder.badgeCard.setCardBackgroundColor(badgeColor);
        }
        
        // Set amount color based on transaction type
        int amountColor = type.equals("CREDIT") ?
            ContextCompat.getColor(holder.itemView.getContext(), R.color.credit_green) :
            ContextCompat.getColor(holder.itemView.getContext(), R.color.debit_red);
        holder.tvAmount.setTextColor(amountColor);
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        MaterialCardView badgeCard;
        TextView tvType, tvAmount, tvDate, tvDescription;
        VH(View itemView) {
            super(itemView);
            badgeCard = itemView.findViewById(R.id.cardTypeBadge);
            tvType = itemView.findViewById(R.id.tvType);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
