package com.planet.wondering.chemi.view.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.network.Parser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.planet.wondering.chemi.network.Config.NUMBER_OF_RETRIES;
import static com.planet.wondering.chemi.network.Config.Product.PATH;
import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;

/**
 * Created by yoon on 2017. 2. 5..
 */

public class ProductPagerFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_PRODUCT_IDS = "product_ids";

    public static ProductPagerFragment newInstance() {

        Bundle args = new Bundle();

        ProductPagerFragment fragment = new ProductPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductPagerFragment newInstance(ArrayList<Integer> productIds, int productId) {

        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PRODUCT_IDS, productIds);
        args.putInt(ARG_PRODUCT_ID, productId);

        ProductPagerFragment fragment = new ProductPagerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private ViewPager mProductViewPager;
    private ArrayList<Integer> mProductIds;
    private int mProductId;
    private Product mProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductIds = getArguments().getIntegerArrayList(ARG_PRODUCT_IDS);
        mProductId = getArguments().getInt(ARG_PRODUCT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_pager, container, false);
        mProductViewPager = (ViewPager) view.findViewById(R.id.fragment_product_pager_view_pager);
        mProductViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
//                return ProductFragment.newInstance(mProductIds.get(position));
//                requestProduct(mProductIds.get(position));
                try {
                    mProduct = new ProductRequestTask().execute(mProductIds.get(position)).get();
                } catch (Exception e) {
                    e.getMessage();
                }
                return ProductFragment.newInstance(mProduct);
            }

            @Override
            public int getCount() {
                return mProductIds.size();
            }


//            @Override
//            public void setPrimaryItem(ViewGroup container, int position, Object object) {
//                super.setPrimaryItem(container, position, object);
//
//                Fragment currentItem = getItem(position);
//                if (currentItem != null) {
//                    currentItem.setMenuVisibility(true);
//                    currentItem.setUserVisibleHint(true);
//                }
//            }
        });
        for (int i = 0; i < mProductIds.size(); i++) {
            if (mProductIds.get(i) == mProductId) {
                mProductViewPager.setCurrentItem(i);
                break;
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void requestProduct(int productId) {

//        Product product = new Product();
//        product.setName("product" + mProductId);
//        product.setBrand("brand" + mProductId);
//
//        bindProduct(product);
        final ProgressDialog progressDialog =
                ProgressDialog.show(getActivity(), getString(R.string.progress_dialog_title_product),
                        getString(R.string.progress_dialog_message_wait), false, false);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_HOST + PATH + productId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mProduct = Parser.parseProduct(response);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e(TAG, error.toString());
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                NUMBER_OF_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    private class ProductRequestTask extends AsyncTask<Integer, Void, Product> {

        @Override
        protected Product doInBackground(Integer... params) {
            try {
                String url = URL_HOST + PATH + params[0];
                Log.i("url", url);
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                in.close();
                connection.disconnect();
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                return Parser.parseProduct(jsonObject);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }
    }
}
