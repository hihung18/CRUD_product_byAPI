package com.example.gk_crud_nafrag.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.gk_crud_nafrag.Activity.ProductDetail;
import com.example.gk_crud_nafrag.Activity.ProductUpdate;
import com.example.gk_crud_nafrag.R;
import com.example.gk_crud_nafrag.Retrofit.ApiService;
import com.example.gk_crud_nafrag.model.Feature;
import com.example.gk_crud_nafrag.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends BaseAdapter {
    List<Product> productList;
    List<Product> temp_productList = new ArrayList<>();
    List<Feature> featureList = new ArrayList<>();
    Context context;
    Bitmap bitmap;
    public ProductAdapter(List<Product> productList, Context context,List<Feature>  featureList) {
        this.productList = productList;
        this.context = context;
        this.featureList = featureList;
        temp_productList.addAll(productList);
    }
    @NonNull
    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return productList.get(i).getProductId();
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.product_listview_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product product = this.productList.get(position);
        ///Ánh xạ
        //// setText - setImg
        viewHolder.tvName.setText(product.getProductName());
        viewHolder.tvPrice.setText(String.valueOf(product.getProductPrice())+ "  $");
        if (product.getProductId() < 35) {
            //System.out.println("URL product " + product.getImageUrls().get(0));
            Glide.with(context)
                    .load(product.getImageUrls().get(0))
                    .into(viewHolder.ivIMG);
        }
        viewHolder.ivDetele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog(position);
            }
        });
        viewHolder.ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductUpdate.class);
                intent.putExtra("keyProduct",product);
                Bundle bundle = new Bundle();
                bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                System.out.println("FeatureList = " + featureList.size());
                System.out.println("ProductList = " + productList.size());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder{
        TextView tvName,tvPrice;
        ImageView ivIMG;
        ImageView ivDetele, ivUpdate;
        public ViewHolder(View itemView){

            tvName = ((TextView) itemView.findViewById(R.id.tvName_List_item));
            ivIMG = (ImageView) itemView.findViewById(R.id.ivImage_List_item);
            tvPrice = ((TextView) itemView.findViewById(R.id.tvPrice_List_item));
            ivDetele = itemView.findViewById(R.id.ivDelete);
            ivUpdate = itemView.findViewById(R.id.ivUpdate);
        }
    }
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có chắc muốn xóa sản phẩm này?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteProductID(productList.get(position).getProductId());
                productList.remove(position);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Đóng cửa sổ cảnh báo và không làm gì cả
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void DeleteProductID(int productID) {
        ApiService.apiService.DeleteProductID(productID).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("response = " + response);
                if (response.isSuccessful()) {
//                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    showDeleteSuccessDialog("Xóa thành công");
                } else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void TimKiemProduct(String msg){
        System.out.println("temp_productList = " + temp_productList.size());
        productList.clear();
        if ( msg == ""){
            productList.addAll(temp_productList);
        }
        for (Product product: temp_productList){
            if (product.getProductName().toLowerCase().contains(msg.toLowerCase())){
                productList.add(product);
            }
        }
        notifyDataSetChanged();
    }
    private void showDeleteSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
