package com.example.gk_crud_nafrag.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.gk_crud_nafrag.R;
import com.example.gk_crud_nafrag.Retrofit.ApiService;
import com.example.gk_crud_nafrag.model.Feature;
import com.example.gk_crud_nafrag.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Date;
import java.text.SimpleDateFormat;


public class AddProductFragment extends Fragment {
    View fragmentView;
    EditText edtNameADD,edtPriceADD,edtRemainADD,edtDescriptionADD;
    Button btnAddProduct;
    TextView tvCategoryADD,tvClickHereAdd;
    Spinner spnBrandAdd,spnCameraAdd,spnRAMAdd,spnROMAdd;
    ArrayList<String> data_Brand = new ArrayList<>();
    ArrayList<String> data_Camera = new ArrayList<>();
    ArrayList<String> data_RAM = new ArrayList<>();
    ArrayList<String> data_ROM = new ArrayList<>();
    ArrayAdapter arrAdapterBrand,arrAdapterCamera,arrAdapterRam,arrAdapterRom;
    List<Feature> featureList = new ArrayList<>();
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView =  inflater.inflate(R.layout.fragment_add_product, container, false);
        getListFeatureAPI();
        setControl();
        return fragmentView;
    }

    private void setEvent() {
        TaoDataSpiner();
        tvClickHereAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPriceADD.getText())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Bạn hãy nhập Price trước");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
                else {
                    if (Integer.parseInt(edtPriceADD.getText().toString()) < 150){
                        tvCategoryADD.setText("cheaps");
                    }else if (Integer.parseInt(edtPriceADD.getText().toString()) >= 150
                            && Integer.parseInt(edtPriceADD.getText().toString()) <= 250){
                        tvCategoryADD.setText("average");
                    } else if (Integer.parseInt(edtPriceADD.getText().toString()) > 250){
                        tvCategoryADD.setText("expensive");
                    }
                }
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNameADD.getText().toString().equals("")){
                    edtNameADD.setError("Vui lòng nhập tên sản phẩm");
                    return;
                }
                if (edtPriceADD.getText().toString().equals("")){
                    edtPriceADD.setError("Vui lòng nhập giá sản phẩm");
                    return;
                }
                if (edtRemainADD.getText().toString().equals("")){
                    edtRemainADD.setError("Vui lòng nhập số lượng sản phẩm");
                    return;
                }
                if (edtDescriptionADD.getText().toString().equals("")){
                    edtDescriptionADD.setError("Vui lòng nhập mô tả sản phẩm");
                    return;
                }
                if (tvCategoryADD.getText().toString().equals("")){
                    tvCategoryADD.setError("Click vào ClickHere");
                    return;
                }
                // Lấy ngày giờ hiện tại:
                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = formatter.format(now);
                ///
                Product newProduct = new Product();
                List<Integer> newfeatureID = new ArrayList<>();
                newProduct.setProductName(edtNameADD.getText().toString());
                newProduct.setProductPrice(Integer.parseInt(edtPriceADD.getText().toString()));
                newProduct.setProductDescription(edtDescriptionADD.getText().toString());
                newProduct.setCategoryName(tvCategoryADD.getText().toString());
                newProduct.setProductRemain(Integer.parseInt(edtRemainADD.getText().toString()));
                for (Feature feature : featureList){
                    if (feature.getFeatureTypeId() == 1
                            && feature.getFeatureSpecific() == spnBrandAdd.getSelectedItem())
                        newfeatureID.add(feature.getFeatureFeatureId());
                    else if (feature.getFeatureTypeId() == 2
                            && feature.getFeatureSpecific() == spnCameraAdd.getSelectedItem())
                        newfeatureID.add(feature.getFeatureFeatureId());
                    else if (feature.getFeatureTypeId() == 4
                            && feature.getFeatureSpecific() == spnRAMAdd.getSelectedItem())
                        newfeatureID.add(feature.getFeatureFeatureId());
                    else if (feature.getFeatureTypeId() == 7
                            && feature.getFeatureSpecific() == spnROMAdd.getSelectedItem())
                        newfeatureID.add(feature.getFeatureFeatureId());
                }
                newProduct.setFeatureIds(newfeatureID);
                if (newProduct.getCategoryName().equals("cheaps")){
                    newProduct.setCateId(1);
                }else if (newProduct.getCategoryName().equals("average")) {
                    newProduct.setCateId(2);
                }else if (newProduct.getCategoryName().equals("expensive")) {
                    newProduct.setCateId(3);
                }
                newProduct.setImageUrls(null);
                newProduct.setProductUpDate(null);
                newProduct.setProductCreateDate(null);

                if (!edtNameADD.getText().toString().equals("")
                    && !edtPriceADD.getText().toString().equals("")
                    && !edtRemainADD.getText().toString().equals("")){
                    PostProduct(newProduct);
                }
            }
        });
    }
    private void getListFeatureAPI() {
        System.out.println("Call API");
        ApiService.apiService.featureListData().enqueue(new Callback<List<Feature>>() {
            @Override
            public void onResponse(Call<List<Feature>> call, Response<List<Feature>> response) {
                featureList = response.body();
                System.out.println("FeatureList call API ok");
                setEvent();
            }
            @Override
            public void onFailure(Call<List<Feature>> call, Throwable t) {
                Toast.makeText(getActivity(), "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("FeatureList Call API Errol  " + t);
            }
        });
    }
    private void setControl() {
        edtNameADD = fragmentView.findViewById(R.id.edtProductNameADD);
        edtPriceADD = fragmentView.findViewById(R.id.edtProductPriceADD);
        edtDescriptionADD = fragmentView.findViewById(R.id.edtProductDescriptionADD);
        edtRemainADD = fragmentView.findViewById(R.id.edtProductRemainADD);
        spnBrandAdd  = fragmentView.findViewById(R.id.spnBrandAdd);
        spnCameraAdd = fragmentView.findViewById(R.id.spnCameraAdd);
        spnRAMAdd = fragmentView.findViewById(R.id.spnRAMAdd);
        spnROMAdd = fragmentView.findViewById(R.id.spnROMAdd);
        tvCategoryADD = fragmentView.findViewById(R.id.tvCategoryAdd);
        btnAddProduct = fragmentView.findViewById(R.id.btnAddProduct);
        tvClickHereAdd = fragmentView.findViewById(R.id.tvClickHereAdd);

    }
    private void TaoDataSpiner() {
        System.out.println("TaoDAtaSniper");
        for (Feature feature : featureList){
            if (feature.getFeatureTypeId() == 1) data_Brand.add(feature.getFeatureSpecific());
            else if (feature.getFeatureTypeId() == 2) data_Camera.add(feature.getFeatureSpecific());
            else if (feature.getFeatureTypeId() == 4) data_RAM.add(feature.getFeatureSpecific());
            else if (feature.getFeatureTypeId() == 7) data_ROM.add(feature.getFeatureSpecific());
        }
        arrAdapterBrand = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data_Brand);
        arrAdapterCamera = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data_Camera);
        arrAdapterRam = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data_RAM);
        arrAdapterRom = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data_ROM);
        spnBrandAdd.setAdapter(arrAdapterBrand);
        spnCameraAdd.setAdapter(arrAdapterCamera);
        spnRAMAdd.setAdapter(arrAdapterRam);
        spnROMAdd.setAdapter(arrAdapterRom);
    }
    private void PostProduct (Product product){
        ApiService.apiService.PostProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()){
                    showDeleteConfirmationDialog("Thêm sản phẩm thành công");
                    System.out.println("Thêm Thành công code =" + response.code());

                }
                else {
                    System.out.println("Không thể thêm sản phẩm vào API code =" + response.code());
//                    Toast.makeText(getActivity(), "Không thành công", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                System.out.println("Thất bại - ERROL "+t);
//                Toast.makeText(getActivity(), "Thất bại - ERROL", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void showDeleteConfirmationDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                replaceFragment();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void replaceFragment() {
        // Khởi tạo một đối tượng HomeFragment
        HomeFragment homeFragment = new HomeFragment();
        // Lấy FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        // Bắt đầu một FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Thay thế AddFragment bằng HomeFragment
        transaction.replace(R.id.fragment_container, homeFragment);
        // Đặt lại Stack và thêm HomeFragment vào Stack, để quay lại HomeFragment khi bấm nút back
        transaction.addToBackStack(null);
        // Áp dụng thay đổi và hoàn tất FragmentTransaction
        transaction.commit();
    }
}