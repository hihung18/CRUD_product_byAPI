package com.example.gk_crud_nafrag.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gk_crud_nafrag.R;
import com.example.gk_crud_nafrag.Retrofit.ApiService;
import com.example.gk_crud_nafrag.model.Feature;
import com.example.gk_crud_nafrag.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductUpdate extends AppCompatActivity {
    EditText edtNameUpdate,edtPriceUpdate,edtRemainUpdate,edtDescriptionUpdate;
    Spinner spnBrandUpdate,spnCameraUpdate,spnRAMUpdate,spnROMUpdate;
    ImageView ivPreviousProductUpdate;
    Button btnUpdate;
    TextView tvCategoryUpdate, tvClickHereUpdate;
    ArrayList<String> data_Brand = new ArrayList<>();
    ArrayList<String> data_Camera = new ArrayList<>();
    ArrayList<String> data_RAM = new ArrayList<>();
    ArrayList<String> data_ROM = new ArrayList<>();
    ArrayAdapter arrAdapterBrand,arrAdapterCamera,arrAdapterRam,arrAdapterRom;

    List<Feature> featureList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);
        setControl();
        setEvent();
    }
    private void setControl() {
        edtNameUpdate = findViewById(R.id.edtNameUpdate);
        edtPriceUpdate = findViewById(R.id.edtPriceUpdate);
        edtDescriptionUpdate = findViewById(R.id.edtDescriptionUpdate);
        edtRemainUpdate = findViewById(R.id.edtRemainUpdate);
        spnBrandUpdate = findViewById(R.id.spnBrandUpdate);
        spnCameraUpdate = findViewById(R.id.spnCameraUpdate);
        spnRAMUpdate = findViewById(R.id.spnRAMUpdate);
        spnROMUpdate = findViewById(R.id.spnROMUpdate);
        btnUpdate = findViewById(R.id.btnUpdateProduct);
        tvCategoryUpdate = findViewById(R.id.tvCategoryUpdate);
        tvClickHereUpdate = findViewById(R.id.tvClickHereUpdate);
        ivPreviousProductUpdate = findViewById(R.id.ivPreviousProductUpdate);
    }
    private void setEvent() {
        Product product = (Product) getIntent().getSerializableExtra("keyProduct");
        Bundle bundle = getIntent().getExtras();
        featureList = (List<Feature>) bundle.getSerializable("keyfeatureList");
        System.out.println("FeatureList = " + featureList.size());
        System.out.println("Product = " +product);

        edtNameUpdate.setText(product.getProductName());
        edtPriceUpdate.setText(String.valueOf(product.getProductPrice()));
        tvCategoryUpdate.setText(product.getCategoryName());
        edtDescriptionUpdate.setText(product.getProductDescription());
        edtRemainUpdate.setText(String.valueOf(product.getProductRemain()));

        //TaoDataSpiner();
        for (Feature feature : featureList){
            if (feature.getFeatureTypeId() == 1) data_Brand.add(feature.getFeatureSpecific());
            else if (feature.getFeatureTypeId() == 2) data_Camera.add(feature.getFeatureSpecific());
            else if (feature.getFeatureTypeId() == 4) data_RAM.add(feature.getFeatureSpecific());
            else if (feature.getFeatureTypeId() == 7) data_ROM.add(feature.getFeatureSpecific());
        }
        arrAdapterBrand = new ArrayAdapter(ProductUpdate.this, R.layout.custom_spinner_item,data_Brand);
        arrAdapterCamera = new ArrayAdapter(ProductUpdate.this, R.layout.custom_spinner_item,data_Camera);
        arrAdapterRam = new ArrayAdapter(ProductUpdate.this, R.layout.custom_spinner_item,data_RAM);
        arrAdapterRom = new ArrayAdapter(ProductUpdate.this, R.layout.custom_spinner_item,data_ROM);
        spnBrandUpdate.setAdapter(arrAdapterBrand);
        spnCameraUpdate.setAdapter(arrAdapterCamera);
        spnRAMUpdate.setAdapter(arrAdapterRam);
        spnROMUpdate.setAdapter(arrAdapterRom);
        //set spinner setSelection

        String temp_brand = "",temp_Camera = "",temp_Ram = "",temp_Rom = "";
        for(Integer Fid : product.getFeatureIds()){
            for (Feature feature: featureList){
                if (Fid == feature.getFeatureFeatureId() && feature.getFeatureTypeId() == 1)
                    temp_brand = feature.getFeatureSpecific();
                else if (Fid == feature.getFeatureFeatureId() && feature.getFeatureTypeId() == 2)
                    temp_Camera = feature.getFeatureSpecific();
                else if (Fid == feature.getFeatureFeatureId() && feature.getFeatureTypeId() == 4)
                    temp_Ram = feature.getFeatureSpecific();
                else if (Fid == feature.getFeatureFeatureId() && feature.getFeatureTypeId() == 7)
                    temp_Rom = feature.getFeatureSpecific();
            }
        }
        spnBrandUpdate.setSelection(arrAdapterBrand.getPosition(temp_brand));
        spnCameraUpdate.setSelection(arrAdapterCamera.getPosition(temp_Camera));
        spnRAMUpdate.setSelection(arrAdapterRam.getPosition(temp_Ram));
        spnROMUpdate.setSelection(arrAdapterRom.getPosition(temp_Rom));

        tvClickHereUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(edtPriceUpdate.getText().toString()) != product.getProductPrice()){
                    if (Integer.parseInt(edtPriceUpdate.getText().toString()) < 150){
                        tvCategoryUpdate.setText("cheaps");
                    }else if (Integer.parseInt(edtPriceUpdate.getText().toString()) >= 150
                            && Integer.parseInt(edtPriceUpdate.getText().toString()) <= 250){
                        tvCategoryUpdate.setText("average");
                    } else if (Integer.parseInt(edtPriceUpdate.getText().toString()) > 250){
                        tvCategoryUpdate.setText("expensive");
                    }
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set các giá trị trong EditText cho new Product
                Product newProduct = new Product();
                newProduct.setProductName(edtNameUpdate.getText().toString()) ;
                newProduct.setProductPrice(Integer.parseInt(edtPriceUpdate.getText().toString())); ;
                newProduct.setProductRemain(Integer.parseInt(edtRemainUpdate.getText().toString()));
                newProduct.setProductDescription(edtDescriptionUpdate.getText().toString()); ;
                newProduct.setCategoryName(tvCategoryUpdate.getText().toString());

                /// set lại Feature khi có sự thay đổi. Dù có thay đổi hay không cũng chạy để set lại
                /// như khi add new products
                List<Integer> newfeatureID = new ArrayList<>();
                for (Feature feature : featureList){
                    if (feature.getFeatureTypeId() == 1
                            && feature.getFeatureSpecific() == spnBrandUpdate.getSelectedItem())
                        newfeatureID.add(feature.getFeatureFeatureId());
                    else if (feature.getFeatureTypeId() == 2
                            && feature.getFeatureSpecific() == spnCameraUpdate.getSelectedItem())
                        newfeatureID.add(feature.getFeatureFeatureId());
                    else if (feature.getFeatureTypeId() == 4
                            && feature.getFeatureSpecific() == spnRAMUpdate.getSelectedItem())
                        newfeatureID.add(feature.getFeatureFeatureId());
                    else if (feature.getFeatureTypeId() == 7
                            && feature.getFeatureSpecific() == spnROMUpdate.getSelectedItem())
                        newfeatureID.add(feature.getFeatureFeatureId());
                }
                /// set CateID cho produc // hầu như k dùng cateID
                if (newProduct.getCategoryName().equals("cheaps")){
                    newProduct.setCateId(1);
                }else if (newProduct.getCategoryName().equals("average")) {
                    newProduct.setCateId(2);
                }else if (newProduct.getCategoryName().equals("expensive")) {
                    newProduct.setCateId(3);
                }
                /// các giá trị còn lại cho newProduct
                newProduct.setImageUrls(product.getImageUrls());
                newProduct.setProductUpDate(null);
                newProduct.setProductCreateDate(product.getProductCreateDate());
                newProduct.setFeatureIds(newfeatureID);
                UpdateProduct(product.getProductId(),newProduct);

            }
        });
        ivPreviousProductUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void TaoDataSpiner() {
        System.out.println("TaoDAtaSniper");
        // tạo data cho FeatureType
        for (Feature feature : featureList){
            if (feature.getFeatureTypeId() == 1) data_Brand.add(feature.getFeatureSpecific());
            else if (feature.getFeatureTypeId() == 2) data_Camera.add(feature.getFeatureSpecific());
            else if (feature.getFeatureTypeId() == 4) data_RAM.add(feature.getFeatureSpecific());
            else if (feature.getFeatureTypeId() == 7) data_ROM.add(feature.getFeatureSpecific());
        }
        // new ArrayAdapter cho FeatureType để hiển thị lên Spinner
        arrAdapterBrand = new ArrayAdapter(this, android.R.layout.simple_list_item_1,data_Brand);
        arrAdapterCamera = new ArrayAdapter(this, android.R.layout.simple_list_item_1,data_Camera);
        arrAdapterRam = new ArrayAdapter(this, android.R.layout.simple_list_item_1,data_RAM);
        arrAdapterRom = new ArrayAdapter(this, android.R.layout.simple_list_item_1,data_ROM);
        /// set Adapter hiển thị lên Spinner
        spnBrandUpdate.setAdapter(arrAdapterBrand);
        spnCameraUpdate.setAdapter(arrAdapterCamera);
        spnRAMUpdate.setAdapter(arrAdapterRam);
        spnROMUpdate.setAdapter(arrAdapterRom);
    }
    private void UpdateProduct(int productID, Product product){
        ApiService.apiService.UpdateProduct(productID,product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product UpdateProduct = response.body();
                if (response.isSuccessful()){
                    System.out.println("Update Thành công code =" + response.code());
                    showUpdateSuccessDialog();
                }
                else {
                    System.out.println("Không thể Update sản phẩm vào API code =" + response.code());
//                    Toast.makeText(ProductUpdate.this, "Không Update thành công", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                System.out.println("UpdateThất bại - ERROL "+t);
//                Toast.makeText(ProductUpdate.this, " Update Thất bại - ERROL", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showUpdateSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductUpdate.this);
        builder.setMessage("Cập nhật thành công");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent order = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(order);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}