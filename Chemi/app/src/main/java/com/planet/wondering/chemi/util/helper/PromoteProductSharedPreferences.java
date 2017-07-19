package com.planet.wondering.chemi.util.helper;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.planet.wondering.chemi.model.home.PromoteProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yoon on 2017. 7. 7..
 */

public class PromoteProductSharedPreferences {

    private static int INDEX_OF_PROMOTE_PRODUCTS = 0;
    private static final String PREF_PROMOTE_PRODUCTS = "com.planet.wondering.chemi.promote_products";

    public static ArrayList<PromoteProduct> getStoredPromoteProducts(Context context) {
        Gson gson = new Gson();
        String jsonPromoteProducts = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_PROMOTE_PRODUCTS, null);
        if (jsonPromoteProducts == null) {
            return null;
        }
        PromoteProduct[] promoteProductArray = gson.fromJson(jsonPromoteProducts, PromoteProduct[].class);
        List<PromoteProduct> promoteProducts = Arrays.asList(promoteProductArray);
        promoteProducts = new ArrayList<>(promoteProducts);

        Iterator<PromoteProduct> iterator = promoteProducts.iterator();
        while (iterator.hasNext()) {
            PromoteProduct promoteProduct = iterator.next();
            if (promoteProduct.getId() == 0) {
                iterator.remove();
            }
        }
        return (ArrayList<PromoteProduct>) promoteProducts;
    }

    public static void setStoredPromoteProducts(Context context, List<PromoteProduct> promoteProducts) {
        Gson gson = new Gson();
        String jsonPromoteProducts = gson.toJson(promoteProducts);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_PROMOTE_PRODUCTS)
                .putString(PREF_PROMOTE_PRODUCTS, jsonPromoteProducts)
                .apply();
    }

    public static PromoteProduct getStoredPromoteProduct(Context context) {
        ArrayList<PromoteProduct> products = getStoredPromoteProducts(context);
        PromoteProduct product;

        if (products != null) {
            int size = products.size();
            if (size > 0) {
                product = products.get((INDEX_OF_PROMOTE_PRODUCTS++ % size));
                return product;
            } else {
                product = new PromoteProduct();
                product.setBrand("");
                product.setName("");
            }
        }
        return null;
    }
}
