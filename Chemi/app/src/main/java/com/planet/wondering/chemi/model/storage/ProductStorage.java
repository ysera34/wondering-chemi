package com.planet.wondering.chemi.model.storage;

import android.content.Context;

import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.model.Product;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 18..
 */

public class ProductStorage {

    private static ProductStorage sProductStorage;
    private Context mContext;

    private ArrayList<Product> mProducts;

    private ProductStorage(Context context) {
        mContext = context;
        mProducts = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Product product = new Product();
            product.setId(i);
            product.setName("product name" + i);
            product.setBrand("brand name" + i);

            for (int j = 0; j < 10; j++) {
                Chemical chemical = new Chemical();
                chemical.setNameKo("화학성분" + j);
                chemical.setNameEn("chemical" + j);
                if (j > 3 && j % 2 == 0) {
                    chemical.setMinHazard((byte)3);
                }
                chemical.setMaxHazard((byte)j);
                product.getChemicals().add(chemical);
            }
            mProducts.add(product);
        }
    }

    public static ProductStorage getStorage(Context context) {
        if (sProductStorage == null) {
            sProductStorage = new ProductStorage(context.getApplicationContext());
        }
        return sProductStorage;
    }

    public ArrayList<Product> getProducts() {
        return mProducts;
    }

//    public ArrayList<Product> getProducts(ArrayList<Integer> productIds) {
//
//    }
//
//    public ArrayList<Product> getProducts(byte categoryId) {
//
//    }

    public Product getProduct(int productId) {
        for (Product product : mProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }
}
