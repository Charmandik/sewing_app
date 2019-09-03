package fishrungames.tes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fishrungames.tes.R;
import fishrungames.tes.models.License;


public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.LicenseViewHolder> {

    private List<License> licenseList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(License license);
    }

    public void setItems(License[] licenses, OnItemClickListener listener) {
        licenseList.addAll(Arrays.asList(licenses));
        notifyDataSetChanged();
        this.listener = listener;
    }

    public void clearItems() {
        licenseList.clear();
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public LicenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.license_view_item, parent, false);
        return new LicenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LicenseViewHolder holder, int position) {
        License license = licenseList.get(position);
        holder.setData(license);
    }

    @Override
    public int getItemCount() {
        return licenseList.size();
    }

    class LicenseViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView amountTextView;
        RelativeLayout googlePaymentButton;

        public LicenseViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            googlePaymentButton = itemView.findViewById(R.id.google_payment_button);
        }

        public void setData(final License license) {
            title.setText(license.getTitle());
            String amount = "$" + license.getAmount();
            amountTextView.setText(amount);

            // On click listener for LicenseFragment
            googlePaymentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(license);
                }
            });
        }
    }
}
