package com.example.projetogremiologin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Item> mUploads;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference pasta = storageReference.child("imagens");

    public ImageAdapter(Context context, List<Item> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Item currentItem = mUploads.get(position);
        StorageReference fotoItem = pasta.child(currentItem.pathFoto + ".jpeg");

        holder.priceValue.setText(currentItem.getPreco().toString());
        holder.boughPrice.setText(currentItem.getBoughtPrice().toString());
        holder.description.setText(currentItem.getDescription());
        holder.category.setText(currentItem.getCategory());
        GlideApp.with(mContext)
                .load(fotoItem)
                .fitCenter()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView priceValue, boughPrice, category, description;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_item_view);
            priceValue = itemView.findViewById(R.id.value_item_text);
            boughPrice = itemView.findViewById(R.id.bought_price_item_text);
            category = itemView.findViewById(R.id.category_item_text);
            description = itemView.findViewById(R.id.description_item_text);
        }
    }
}
