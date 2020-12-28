package com.dylansalim.qrmenuapp.ui.store_qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.Store;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class StoreQRActivity extends AppCompatActivity implements StoreQRViewInterface {
    private ImageView mQRImage;
    private TextView mStoreTitle, mStoreDesc;
    private StoreQRPresenterInterface storeQRPresenterInterface;
    private static final String TAG = "sqra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_qr);
        Toolbar toolbar = findViewById(R.id.store_qr_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("QR code");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        mQRImage = findViewById(R.id.iv_store_qr);
        mStoreTitle = findViewById(R.id.tv_store_qr_store_title);
        mStoreDesc = findViewById(R.id.tv_store_qr_store_desc);


        setupMVP();

        Bundle bundle = getIntent().getExtras();

        if (null != bundle.getParcelable(getResources().getString(R.string.store_result))) {
            Store store = bundle.getParcelable(getResources().getString(R.string.store_result));
            storeQRPresenterInterface.setStoreDetail(store);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_qr_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_share:
                storeQRPresenterInterface.onShareBtnClick();
                return true;
        }

        return true;
    }

    private void setupMVP() {
        storeQRPresenterInterface = new StoreQRPresenter(this);
    }


    @Override
    public void setQRDetail(Bitmap bitmap, String title, String desc, String imageUrl) {
        Log.d(TAG, "empty SET QR DETAIL");
        if (null != imageUrl) {
            ImageView mProfileImg = findViewById(R.id.iv_store_qr_profile);
            Picasso.get().load(imageUrl)
                    .placeholder(R.drawable.sample)
                    .into(mProfileImg);
        }

        if (null != bitmap && null != title && null != desc) {
            Log.d(TAG, "SET QR DETAIL");
            mQRImage.setImageBitmap(bitmap);
            mStoreTitle.setText(title);
            mStoreDesc.setText(desc);
        }
    }

    @Override
    public void shareQR(Bitmap bitmap, String desc) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, desc);
        String url = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", "description");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
        sendIntent.setType("image/*");
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sendIntent, "Share Image"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storeQRPresenterInterface.disposeObserver();
    }
}