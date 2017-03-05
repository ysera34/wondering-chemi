package com.planet.wondering.chemi.network;

import android.util.Log;

import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.model.Hazard;
import com.planet.wondering.chemi.model.Pager;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Tag;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.network.Config.Chemical.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.planet.wondering.chemi.network.Config.COUNT;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.ALLERGY_DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.CHEMICALS;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.CHEMICAL_ID;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.HAZARDS;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.HAZARD_SIZE;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.MAX_VALUE;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.MIN_VALUE;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.NAMEEN;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.NAMEKO_ORIGIN;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.NAMEKO_PRODUCT;
import static com.planet.wondering.chemi.network.Config.Chemical.Key.PURPOSE;
import static com.planet.wondering.chemi.network.Config.Hazard.Key.CLASS;
import static com.planet.wondering.chemi.network.Config.Hazard.Key.CODE;
import static com.planet.wondering.chemi.network.Config.Hazard.Key.DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Hazard.Key.HAZARD_ID;
import static com.planet.wondering.chemi.network.Config.Hazard.Key.SOURCE;
import static com.planet.wondering.chemi.network.Config.Hazard.Key.TYPE;
import static com.planet.wondering.chemi.network.Config.PAGE;
import static com.planet.wondering.chemi.network.Config.PAGE_NEXT;
import static com.planet.wondering.chemi.network.Config.PAGE_PREV;
import static com.planet.wondering.chemi.network.Config.Product.Key.ALLERGY;
import static com.planet.wondering.chemi.network.Config.Product.Key.BRAND;
import static com.planet.wondering.chemi.network.Config.Product.Key.CHEMICALS_SIZE;
import static com.planet.wondering.chemi.network.Config.Product.Key.IMAGE_PATH;
import static com.planet.wondering.chemi.network.Config.Product.Key.NAME;
import static com.planet.wondering.chemi.network.Config.Product.Key.PRODUCT_ID;
import static com.planet.wondering.chemi.network.Config.Product.Key.RATING;
import static com.planet.wondering.chemi.network.Config.Product.Key.RATING_COUNT;
import static com.planet.wondering.chemi.network.Config.RESPONSE_DATA;
import static com.planet.wondering.chemi.network.Config.RESPONSE_MESSAGE;
import static com.planet.wondering.chemi.network.Config.RESPONSE_SUCCESS;
import static com.planet.wondering.chemi.network.Config.TOTAL;
import static com.planet.wondering.chemi.network.Config.Tag.Key.RANKED_TIME;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_COUNT;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_ID;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_RANK;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_RANK_DELTA;
import static com.planet.wondering.chemi.network.Config.User.Key.EMAIL;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.Key.USER_ID;

/**
 * Created by yoon on 2017. 1. 26..
 */

public class Parser {

    public static final String TAG = Parser.class.getSimpleName();

    public static ArrayList<Tag> parseTagPopularList(JSONObject responseObject) {

        ArrayList<Tag> tags = new ArrayList<>();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                int tagSize = responseObject.getInt(TAG_COUNT);
                if (tagSize > 0) {
                JSONArray tagJSONArray = responseObject.getJSONArray(RESPONSE_DATA);

                    for (int i = 0; i < tagSize; i++) {
                        JSONObject tagJSONObject = tagJSONArray.getJSONObject(i);
                        Tag tag = new Tag();
                        tag.setId(tagJSONObject.getInt(TAG_ID));
                        tag.setName(tagJSONObject.getString(TAG_DESCRIPTION));
                        tag.setRank(tagJSONObject.getInt(TAG_RANK));
                        tag.setVariation(tagJSONObject.getInt(TAG_RANK_DELTA));
                        if (i == 0) {
                            Date rankDate = dateFormat.parse(responseObject.getString(RANKED_TIME));
                            tag.setRankDate(rankDate);
                        }
                        tags.add(tag);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return tags;
    }

    public static ArrayList<String> parseTagStringList(JSONObject responseObject) {

        ArrayList<String> tagStrings = new ArrayList<>();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                int tagSize = responseObject.getInt(TAG_COUNT);
                if (tagSize > 0) {
                JSONArray tagJSONArray = responseObject.getJSONArray(RESPONSE_DATA);
                    for (int i = 0; i < tagSize; i++) {
                        JSONObject tagJSONObject = tagJSONArray.getJSONObject(i);
                        tagStrings.add(tagJSONObject.getString(TAG_DESCRIPTION));
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return tagStrings;
    }

    public static ArrayList<Tag> parseTagList(JSONObject responseObject) {

        ArrayList<Tag> tags = new ArrayList<>();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                int tagSize = responseObject.getInt(TAG_COUNT);
                JSONArray tagJSONArray = responseObject.getJSONArray(RESPONSE_DATA);
                if (tagSize > 0) {
                    for (int i = 0; i < tagSize; i++) {
                        JSONObject tagJSONObject = tagJSONArray.getJSONObject(i);
                        Tag tag = new Tag();
                        tag.setId(tagJSONObject.getInt(TAG_ID));
                        tag.setName(tagJSONObject.getString(TAG_DESCRIPTION));
                        tags.add(tag);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return tags;
    }

    public static ArrayList<Product> parseProductList(JSONObject responseObject) {

        ArrayList<Product> products = new ArrayList<>();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                int productSize = responseObject.getInt(COUNT);
                if (productSize > 0) {
                    JSONArray productJSONArray = responseObject.getJSONArray(RESPONSE_DATA);
                    for (int i = 0; i < productSize; i++) {
                        JSONObject productJSONObject = (JSONObject) productJSONArray.get(i);
                        Product product = new Product();
                        product.setId(productJSONObject.getInt(PRODUCT_ID));
                        product.setBrand(productJSONObject.getString(BRAND));
                        product.setName(productJSONObject.getString(NAME));
                        product.setImagePath(productJSONObject.getString(IMAGE_PATH));

                        Object ratingObject = productJSONObject.get(RATING);
//                        Log.i(TAG, String.valueOf(ratingObject));
                        float ratingFloat = 0.0f;
//                        if (ratingObject instanceof Integer) {
//                            ratingFloat = ((Integer) ratingObject).floatValue();
//                        } else if (ratingObject instanceof Double) {
//                            ratingFloat = ((Double) ratingObject).floatValue();
//                        } else if (ratingObject == null) {
//                            ratingFloat = 0.0f;
//                        }
                        if (ratingObject instanceof Integer && (Integer) ratingObject == -1) {
                            ratingFloat = 0.0f;
                        } else if (ratingObject instanceof Integer) {
                            ratingFloat = ((Integer) ratingObject).floatValue();
                        } else {
                            ratingFloat = ((Double) ratingObject).floatValue();
                        }
                        product.setRatingValue(ratingFloat);
                        product.setRatingCount(productJSONObject.getInt(RATING_COUNT));
                        products.add(product);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return products;
    }

    public static Pager parseProductListPagingQuery(JSONObject responseObject) {

        Pager pager = new Pager();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                pager.setTotal(responseObject.getInt(TOTAL));
                JSONObject skippingJSONObject = responseObject.getJSONObject(PAGE);
                pager.setPrevQuery(skippingJSONObject.getString(PAGE_PREV));
                pager.setNextQuery(skippingJSONObject.getString(PAGE_NEXT));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return pager;
    }

    public static Product parseProduct(JSONObject responseObject) {

        Product product = new Product();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                JSONObject productJSONObject = responseObject.getJSONObject(RESPONSE_DATA);
                product.setId(productJSONObject.getInt(PRODUCT_ID));
                product.setBrand(productJSONObject.getString(BRAND));
                product.setName(productJSONObject.getString(NAME));
                product.setImagePath(productJSONObject.getString(IMAGE_PATH));
//                product.setRatingValue((float) productJSONObject.getDouble(RATING));
                Object ratingObject = productJSONObject.get(RATING);
                float ratingFloat = 0.0f;
                if (ratingObject instanceof Integer && (Integer) ratingObject == -1) {
                    ratingFloat = 0.0f;
                } else if (ratingObject instanceof Integer) {
                    ratingFloat = ((Integer) ratingObject).floatValue();
                } else {
                    ratingFloat = ((Double) ratingObject).floatValue();
                }
                product.setRatingValue(ratingFloat);
                product.setRatingCount(productJSONObject.getInt(RATING_COUNT));
                product.setAllergyCount(productJSONObject.getInt(ALLERGY));
                int chemicalSize = productJSONObject.getInt(CHEMICALS_SIZE);
                if (chemicalSize > 0) {
                    JSONArray chemicalJSONArray = productJSONObject.getJSONArray(CHEMICALS);
                    for (int i = 0; i < chemicalSize; i++) {
                        JSONObject chemicalJSONObject = (JSONObject) chemicalJSONArray.get(i);
                        Chemical chemical = new Chemical();
                        chemical.setId(chemicalJSONObject.getInt(CHEMICAL_ID));
                        chemical.setNameKo(chemicalJSONObject.getString(NAMEKO_PRODUCT));
                        chemical.setNameEn(chemicalJSONObject.getString(NAMEEN));
                        chemical.setPurpose(chemicalJSONObject.getString(PURPOSE));
//                        chemical.setMaxHazard((byte) chemicalJSONObject.getInt(MAX_VALUE));
//                        Object minValueObject = chemicalJSONObject.get(MIN_VALUE);
//                        if (minValueObject != null) {
//                            chemical.setMinHazard((byte)chemicalJSONObject.getInt(MIN_VALUE));
//                        }
                        int maxValue = chemicalJSONObject.getInt(MAX_VALUE);
                        if (maxValue != -1) {
                            chemical.setMaxHazard((byte) maxValue);
                        }
                        int minValue = chemicalJSONObject.getInt(MIN_VALUE);
                        if (minValue != -1) {
                            chemical.setMinHazard((byte) minValue);
                        }
                        int allergyState = chemicalJSONObject.getInt(Key.ALLERGY);
                        if (allergyState > 0) {
                            chemical.setAllergy(true);
                        }
                        product.getChemicals().add(chemical);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
//        Log.i(TAG, product.toString());
        return product;
    }

    public static Chemical parseChemical(JSONObject responseObject) {

        Chemical chemical = new Chemical();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                JSONObject chemicalJSONObject = responseObject.getJSONObject(RESPONSE_DATA);
                chemical.setId(chemicalJSONObject.getInt(CHEMICAL_ID));
//                chemical.setMaxHazard((byte)chemicalJSONObject.getInt(MAX_VALUE));
                int maxValue = chemicalJSONObject.getInt(MAX_VALUE);
                if (maxValue != -1) {
                    chemical.setMaxHazard((byte) maxValue);
                }
//                Object minValueObject = chemicalJSONObject.get(MIN_VALUE);
//                if (minValueObject != null) {
//                    chemical.setMinHazard((byte)chemicalJSONObject.getInt(MIN_VALUE));
//                }
                int minValue = chemicalJSONObject.getInt(MIN_VALUE);
                if (minValue != -1) {
                    chemical.setMinHazard((byte) minValue);
                }
                chemical.setNameKo(chemicalJSONObject.getString(NAMEKO_ORIGIN));
                chemical.setNameEn(chemicalJSONObject.getString(NAMEEN));
                chemical.setPurpose(chemicalJSONObject.getString(PURPOSE));
                chemical.setAllergy(chemicalJSONObject.getBoolean(Key.ALLERGY));
                chemical.setAllergyDescription(chemicalJSONObject.getString(ALLERGY_DESCRIPTION));
                int hazardSize = chemicalJSONObject.getInt(HAZARD_SIZE);
                JSONArray hazardJSONArray = chemicalJSONObject.getJSONArray(HAZARDS);
                if (hazardSize > 0) {
                    for (int i = 0; i < hazardSize; i++) {
                        JSONObject hazardJSONObject = (JSONObject) hazardJSONArray.get(i);
                        Hazard hazard = new Hazard();
                        hazard.setId(hazardJSONObject.getInt(HAZARD_ID));
                        hazard.setSource(hazardJSONObject.getString(SOURCE));
                        hazard.setCode(hazardJSONObject.getString(CODE));
                        hazard.setName(hazardJSONObject.getString(Config.Hazard.Key.NAME));
                        hazard.setDescription(hazardJSONObject.getString(DESCRIPTION));
                        hazard.setClassName(hazardJSONObject.getString(CLASS));
                        hazard.setType((byte)hazardJSONObject.getInt(TYPE));
                        hazard.setIconResId(hazard.getType());
                        chemical.getHazards().add(hazard);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
//        Log.i(TAG, chemical.toString());
        return chemical;
    }

    public static User parseUser(JSONObject responseObject) {

        User user = new User();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                JSONObject userJSONObject = responseObject.getJSONObject(RESPONSE_DATA);
                user.setId(userJSONObject.getInt(USER_ID));
                user.setEmail(userJSONObject.getString(EMAIL));
                user.setName(userJSONObject.getString(Config.User.Key.NAME));
                user.setToken(userJSONObject.getString(TOKEN));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        Log.i(TAG, user.toString());
        return user;
    }

    public static ArrayList<Chemical> parseChemicalList(JSONObject responseObject) {

        ArrayList<Chemical> chemicals = new ArrayList<>();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {

            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return chemicals;
    }
}
