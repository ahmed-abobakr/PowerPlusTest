package com.powerplus.powerplustest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.powerplus.powerplustest.util.IabHelper;
import com.powerplus.powerplustest.util.IabResult;
import com.powerplus.powerplustest.util.Purchase;

public class MainActivity extends AppCompatActivity {

    public  final String BASE64_PUBLIC_KEY = "";
    final String PURCHASE_ID = "";
    final int PURCHASE_REQUEST = 1001;

    private AdView mAdView;
    TextView txtPurchase, purchasedTxt;

    IabHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adView);
        txtPurchase = (TextView) findViewById(R.id.txt_purchase);
        purchasedTxt = (TextView) findViewById(R.id.purchased_txt);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("81CFE94B10A5436D0D6CE894B9510312").build();
        mAdView.loadAd(adRequest);

        txtPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePurchase(PURCHASE_ID, PURCHASE_REQUEST);
            }
        });

        helper = new IabHelper(MainActivity.this, BASE64_PUBLIC_KEY);

        helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == PURCHASE_REQUEST){
                if (mAdView != null) {
                    mAdView.destroy();
                    purchasedTxt.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void updatePurchase(String appID, int requestCode){
        try {
            helper.flagEndAsync();
            helper.launchPurchaseFlow(MainActivity.this, appID, requestCode, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (result.isFailure()) {
                        Log.d("TEST", "Fail " + result.getMessage());
                        Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, "");

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
