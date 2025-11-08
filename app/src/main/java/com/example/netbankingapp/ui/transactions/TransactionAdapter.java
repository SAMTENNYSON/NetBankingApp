package com.example.netbankingapp.ui.transactions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.netbankingapp.R;
import com.example.netbankingapp.network.ApiService;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<ApiService.Transaction> transactions;

    public TransactionAdapter(List<ApiService.Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApiService.Transaction txn = transactions.get(position);
        String type = txn.getType() != null ? txn.getType().toUpperCase() : "DEBIT";
        holder.tvType.setText(type);
        holder.tvAmount.setText("₹" + txn.getAmount());
        holder.tvDate.setText(txn.getDate());
        
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
        
        // Transaction model doesn't have description field, so hide the description container
        if (holder.descriptionContainer != null) {
            holder.descriptionContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView badgeCard;
        View descriptionContainer;
        TextView tvType, tvAmount, tvDate, tvDescription;
        public ViewHolder(View itemView) {
            super(itemView);
            badgeCard = itemView.findViewById(R.id.cardTypeBadge);
            descriptionContainer = itemView.findViewById(R.id.descriptionContainer);
            tvType = itemView.findViewById(R.id.tvType);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
