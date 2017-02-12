package com.planet.wondering.chemi.network;

import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by yoon on 2017. 1. 26..
 */

public class Config {

    public static final String URL_HOST = "http://52.79.127.149:3000";
    public static final String IMAGE_URL_HOST = "https://s3.ap-northeast-2.amazonaws.com/chemistaticfiles02";
    public static final String RESPONSE_MESSAGE = "message";
    public static final String RESPONSE_SUCCESS = "success";
    public static final String RESPONSE_ERROR = "error";
    public static final String RESPONSE_DATA = "data";
    public static final String TOTAL = "totaled";
    public static final String COUNT = "counted";
    public static final String PAGE = "skipping";
    public static final String PAGE_PREV = "prev";
    public static final String PAGE_NEXT = "next";
    public static final String QUERY_START = "?";

    public static final int SOCKET_TIMEOUT_GET_REQ = 5000;
    public static final int SOCKET_TIMEOUT_POST_REQ = 10000;

    public static final class Tag {
        public static final String PATH = File.separator + "tags?";

        public static final class Key {
            public static final String CHARACTER_QUERY = "character=";
            public static final String LOWEST_QUEST_DEFAULT = "lowest=10";
            public static final String RANKED_TIME = "ranked";
            public static final String TAG_COUNT = "counted";
            public static final String TAG_ID = "id";
            public static final String TAG_DESCRIPTION = "description";
            public static final String TAG_RANK = "rank";
            public static final String TAG_RANK_DELTA = "rankDelta";
        }
    }

    public static final class Product {
        public static final String PATH = File.separator + "products" + File.separator;
        public static final String QUERY_PATH = File.separator + "products?";
        public static final String QUERY_TAG = "&tag=";
        public static final String QUERY_CATEGORY = "?categoryid=";
        public static final String QUERY_ORDER = "&orderBy=";
        public static final String ORDER_ACCURACY = QUERY_ORDER + 0;
        public static final String ORDER_RATING_VALUE = QUERY_ORDER + 1;
        public static final String ORDER_RATING_COUNT = QUERY_ORDER + 2;

        public static final class Key {
            public static final String PRODUCTS = "products";
            public static final String PRODUCT_ID = "id";
//            public static final String CATEGORY_ID = "categoryid";
//            public static final String MAKER = "maker";
            public static final String BRAND = "brand";
            public static final String NAME = "name";
//            public static final String TYPE = "type";
//            public static final String PURPOSE = "purpose";
//            public static final String RELEASED = "released";
            public static final String RATING = "rated";
            public static final String RATING_COUNT = "reviewed";
            public static final String IMAGE_PATH = "imagePath";
            public static final String ALLERGY = "allergied";
            public static final String CHEMICALS_SIZE = "chemicaled";
        }
    }

    public static final class Chemical {
        public static final String PATH = File.separator + "chemicals";

        public static final class Key {
            public static final String CHEMICALS = "chemicals";
            public static final String CHEMICAL_ID = "id";
            public static final String NAMEKO_PRODUCT = "nameForProduct";
            public static final String NAMEKO_ORIGIN = "koreanName";
            public static final String NAMEEN = "englishName";
//            public static final String ABBR = "abbr";
            public static final String PURPOSE = "purpose";
            public static final String MAX_VALUE = "ewglevel";
            public static final String MIN_VALUE = "ewglevel2";
            public static final String ALLERGY = "isAllergy";
            public static final String HAZARD_SIZE = "hazardes";
            public static final String HAZARDS = "hazards";
//            public static final String KEYWORD = "keyword";
//            public static final String EFFECTS = "effects";
        }
    }

    public static final class Hazard {

        public static final class Key {
            public static final String HAZARD_ID = "id";
            public static final String SOURCE = "source";
            public static final String CODE = "code";
            public static final String NAME = "name";
            public static final String DESCRIPTION = "description";
            public static final String TYPE = "type";
            public static final String ALLERGY = "isAllergy";
        }
    }

    public static String encodeUTF8(String string) {
        try {
            return URLEncoder.encode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "UnsupportedEncodingException : " + e.toString());
        }
        return null;
    }
}
