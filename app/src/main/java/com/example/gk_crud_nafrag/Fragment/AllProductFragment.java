package com.example.gk_crud_nafrag.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gk_crud_nafrag.Activity.ProductDetail;
import com.example.gk_crud_nafrag.Adapter.ProductAdapter;
import com.example.gk_crud_nafrag.R;
import com.example.gk_crud_nafrag.Retrofit.ApiService;
import com.example.gk_crud_nafrag.model.Feature;
import com.example.gk_crud_nafrag.model.Image;
import com.example.gk_crud_nafrag.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllProductFragment extends Fragment {
    ListView simpleList;
    ProductAdapter adapter;
    View fragmentView;
    SearchView svSearch;
    List<Product> productList = new ArrayList<>();
    List<Image> imageList = new ArrayList<>();
    List<Feature> featureList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_all_product, container, false);
        getListProductAPI();
        getListFeatureAPI();
        return fragmentView;
    }
    private void setControl() {
        simpleList = fragmentView.findViewById(R.id.simpListView);
        svSearch = fragmentView.findViewById(R.id.svSearch);
    }
    private void setEvent() {
        adapter = new ProductAdapter(productList,getActivity(),featureList);
        simpleList.setAdapter(adapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = productList.get(i);
                //Toast.makeText(getActivity(), "Bạn chọn"+ productList.get(i).getProductName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ProductDetail.class);
                intent.putExtra("keyProduct",product);
                Bundle bundle = new Bundle();
                bundle.putSerializable("keyfeatureList", (Serializable) featureList);
                System.out.println("FeatureList = " + featureList.size());
                System.out.println("ProductList = " + productList.size());
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.TimKiemProduct(newText);
                return false;
            }
        });
    }
    private void getListProductAPI() {
        ApiService.apiService.productListData().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList = response.body();
                setControl();
                setEvent();
                System.out.println("ProductList Call API ok");
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity(), "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("ProductList Call API Errol  " + t);
            }
        });
    }
    private void getListFeatureAPI() {
        ApiService.apiService.featureListData().enqueue(new Callback<List<Feature>>() {
            @Override
            public void onResponse(Call<List<Feature>> call, Response<List<Feature>> response) {
                featureList = response.body();
                System.out.println("FeatureList call API ok");
            }
            @Override
            public void onFailure(Call<List<Feature>> call, Throwable t) {
                Toast.makeText(getActivity(), "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("FeatureList Call API Errol  " + t);
            }
        });
    }

}