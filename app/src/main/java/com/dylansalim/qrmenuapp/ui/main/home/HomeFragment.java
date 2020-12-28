package com.dylansalim.qrmenuapp.ui.main.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.QRResult;
import com.dylansalim.qrmenuapp.models.dao.Shop;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.ui.merchant.MerchantActivity;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements IShopView {

    public static final String TAG = "HomeFragment";

    private ShopPresenter shopPresenter;

    private RecShopListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopPresenter = new ShopPresenter(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        CardView qrScanCardView = root.findViewById(R.id.qrCard);
        qrScanCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setPrompt("Scan a QrCode to navigate to Menu");
                integrator.setOrientationLocked(false);
                integrator.setCameraId(0);
                integrator.initiateScan();
            }
        });

        initView(root);
        initData();
        return root;
    }

    private void initView(View root) {
        adapter = new RecShopListAdapter(getContext());
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //创建并设置Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickListener((view, position) -> Log.d(TAG, "click---" + position));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopPresenter != null) {
            shopPresenter.onDestory();
        }
    }

    private void initData() {
        String userId = getUserId();
        shopPresenter.getRecentList(userId);
    }

    @Override
    public void onGetShopList(List<Shop> shopList) {
        if (shopList != null) {
            adapter.fillData(shopList);
        }
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getContext().getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getContext().getString(R.string.token), "");
        if (!token.equals("")) {
            String dataString;
            try {
                dataString = JWTUtils.getDataString(token);
                UserDetailDao userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);
                Log.d(TAG, userDetailDao.toString());
                return userDetailDao.getId() + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shopPresenter != null) {
            Log.d(TAG, "home initData");
            initData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(getActivity(), "Scan Cancelled", Toast.LENGTH_LONG).show();
            }else{
                try{
                    QRResult qrResult = new Gson().fromJson(result.getContents(), QRResult.class);
                    Intent navigateToMerchant = new Intent(getActivity(), MerchantActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(getResources().getString(R.string.store_id), qrResult.getStoreId());
                    navigateToMerchant.putExtras(bundle);
                    startActivity(navigateToMerchant);
                }catch(Exception e){
                    e.printStackTrace();
                    // show invalid qr
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
            Toast.makeText(getActivity(), "Failed to get Scan result. Scan the correct Qr Code", Toast.LENGTH_LONG).show();
        }
    }
}