package com.example.matrixdbms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerView_Config {
    FirebaseAuth mAuth;
    private static FirebaseUser user;
    private Context mContext;
    private PartsAdapter mPartsAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<MatrixDBMS> matrixDBMS, List<String> keys) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mContext = context;
        mPartsAdapter = new PartsAdapter(matrixDBMS, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mPartsAdapter);
    }

    class PartItemView extends RecyclerView.ViewHolder {
        private TextView mPart;
        private TextView mManufacturer;
        private TextView mCategory;
        private TextView mInstallation;
        private TextView mModification;
        private TextView mOperator;

        private String key;

        public PartItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.part_list_item, parent, false));

            mPart = (TextView) itemView.findViewById(R.id.part_txtView);
            mManufacturer = (TextView) itemView.findViewById(R.id.manufacturer_txtView);
            mCategory = (TextView) itemView.findViewById(R.id.category_txtView);
            mInstallation = (TextView) itemView.findViewById(R.id.installation_txtView);
            mModification = (TextView) itemView.findViewById(R.id.modification_txtView);
            mOperator = (TextView) itemView.findViewById(R.id.operator_txtView);
            mManufacturer.setVisibility(View.INVISIBLE);
            mCategory.setVisibility(View.INVISIBLE);
            mInstallation.setVisibility(View.INVISIBLE);
            mModification.setVisibility(View.INVISIBLE);
            mOperator.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(mContext, PartDetailsActivity.class);
                        intent.putExtra("key",key);
                        intent.putExtra("part", mPart.getText().toString());
                        intent.putExtra("manufacturer",mManufacturer.getText().toString());
                        intent.putExtra("category",mCategory.getText().toString());
                        intent.putExtra("installation",mInstallation.getText().toString());
                        intent.putExtra("modification",mModification.getText().toString());
                        intent.putExtra("operator",mOperator.getText().toString());

                        mContext.startActivity(intent);

                }
            });

        }

        public void bind(MatrixDBMS matrixDBMS, String key) {
            mPart.setText(matrixDBMS.getPart());
            mManufacturer.setText(matrixDBMS.getManufacturer());
            mCategory.setText(matrixDBMS.getCategory());
            mInstallation.setText(matrixDBMS.getInstallation_date());
            mModification.setText(matrixDBMS.getModification_date());
            mOperator.setText(matrixDBMS.getOperator_name());
            this.key = key;
        }
    }
    class PartsAdapter extends RecyclerView.Adapter<PartItemView>{
        private List<MatrixDBMS> mPartList;
        private List<String> mKeys;

        public PartsAdapter(List<MatrixDBMS> mPartList, List<String> mKeys) {
            this.mPartList = mPartList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public PartItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PartItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PartItemView holder, int position) {
            holder.bind(mPartList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mPartList.size();
        }
    }

    public static void logout() {
        user=null;
    }
}
