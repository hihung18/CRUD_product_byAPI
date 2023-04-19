package com.example.gk_crud_nafrag.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gk_crud_nafrag.R;
import com.example.gk_crud_nafrag.model.Feature;
import com.example.gk_crud_nafrag.model.Product;

import java.util.List;

public class ProductDetail extends AppCompatActivity {
    ImageView ivImageProducDetail,ivPreviousProductDetail;
    TextView tvProductNameDetail,tvPriceDetail,tvDescriptionDetail;
    TextView tvBrandDetail, tvCameraDetail,tvRAMDetail,tvROMDetail,tvCategoryDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setControl();
        setEvent();

    }
    private void setEvent() {
        Product product = (Product) getIntent().getSerializableExtra("keyProduct");
        Bundle bundle = getIntent().getExtras();
        List<Feature> featureList = (List<Feature>) bundle.getSerializable("keyfeatureList");
        tvProductNameDetail.setText(product.getProductName());
        tvPriceDetail.setText(String.valueOf(product.getProductPrice()));
        tvDescriptionDetail.setText(product.getProductDescription());
        tvCategoryDetail.setText(product.getCategoryName());
        for (Integer n : product.getFeatureIds())
            for (Feature feature: featureList){
                if (n == feature.getFeatureFeatureId()){
                    if (feature.getFeatureTypeId() == 1){
                        tvBrandDetail.setText(feature.getFeatureSpecific());
                    }else if (feature.getFeatureTypeId() == 2){
                        tvCameraDetail.setText(feature.getFeatureSpecific());
                    }
                    else if (feature.getFeatureTypeId() == 4){
                        tvRAMDetail.setText(feature.getFeatureSpecific());
                    }else if (feature.getFeatureTypeId() == 7){
                        tvROMDetail.setText(feature.getFeatureSpecific());
                    }
                    break;
                }
            }


        if (product.getProductId() < 35 ){
            Glide.with(this)
                    .load(product.getImageUrls().get(0))
                    .into(ivImageProducDetail);
        }
        ivPreviousProductDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void setControl() {
        ivImageProducDetail = findViewById(R.id.ivImgDetail);
        tvProductNameDetail = findViewById(R.id.tvProductNameDetail);
        tvPriceDetail = findViewById(R.id.tvPriceDetail);
        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        tvBrandDetail = findViewById(R.id.tvBrandDetail);
        tvRAMDetail = findViewById(R.id.tvRAMDetail);
        tvROMDetail = findViewById(R.id.tvROMDetail);
        tvCameraDetail = findViewById(R.id.tvCameraDetail);
        tvCategoryDetail = findViewById(R.id.tvCategoryDetail);
        ivPreviousProductDetail = findViewById(R.id.ivPreviousProductDetail);

    }
}