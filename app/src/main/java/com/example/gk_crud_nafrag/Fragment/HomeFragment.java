package com.example.gk_crud_nafrag.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gk_crud_nafrag.R;
import com.example.gk_crud_nafrag.Retrofit.ApiService;
import com.example.gk_crud_nafrag.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    TextView tvProductNumber;
    List<Product> productList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tvProductNumber = (TextView) view.findViewById(R.id.tvProductNumber);
        getListProductAPI();

        return view;

    }
    private void getListProductAPI() {
        ApiService.apiService.productListData().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList = response.body();
                tvProductNumber.setText(String.valueOf(productList.size()));
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity(), "Call API Errol  " + t, Toast.LENGTH_SHORT).show();
                System.out.println("Call API Errol  " + t);
            }
        });
    }
}