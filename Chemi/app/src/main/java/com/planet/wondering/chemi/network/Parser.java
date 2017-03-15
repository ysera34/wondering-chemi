package com.planet.wondering.chemi.network;

import android.util.Log;

import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.model.Hazard;
import com.planet.wondering.chemi.model.Pager;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
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
import static com.planet.wondering.chemi.network.Config.Chemical.Key.DATA_SCORE;
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
import static com.planet.wondering.chemi.network.Config.Product.Key.ARCHIVE;
import static com.planet.wondering.chemi.network.Config.Product.Key.BRAND;
import static com.planet.wondering.chemi.network.Config.Product.Key.CHEMICALS_SIZE;
import static com.planet.wondering.chemi.network.Config.Product.Key.IMAGE_PATH;
import static com.planet.wondering.chemi.network.Config.Product.Key.NAME;
import static com.planet.wondering.chemi.network.Config.Product.Key.PRODUCT_ID;
import static com.planet.wondering.chemi.network.Config.Product.Key.RATING;
import static com.planet.wondering.chemi.network.Config.Product.Key.RATING_COUNT;
import static com.planet.wondering.chemi.network.Config.Product.Key.WHOLE_CHEMICALS;
import static com.planet.wondering.chemi.network.Config.RESPONSE_DATA;
import static com.planet.wondering.chemi.network.Config.RESPONSE_MESSAGE;
import static com.planet.wondering.chemi.network.Config.RESPONSE_SUCCESS;
import static com.planet.wondering.chemi.network.Config.Review.Key.REVIEW_ID;
import static com.planet.wondering.chemi.network.Config.TOTAL;
import static com.planet.wondering.chemi.network.Config.Tag.Key.RANKED_TIME;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_COUNT;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_ID;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_RANK;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_RANK_DELTA;
import static com.planet.wondering.chemi.network.Config.User.Key.AGE;
import static com.planet.wondering.chemi.network.Config.User.Key.CHILD_HAS_ALLERGY;
import static com.planet.wondering.chemi.network.Config.User.Key.CHILD_HAS_DRY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.EMAIL;
import static com.planet.wondering.chemi.network.Config.User.Key.GENDER;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_ALLERGY;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_CHILD;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_DRY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_OILY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.PUSH_TOKEN;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.Key.USER;
import static com.planet.wondering.chemi.network.Config.User.Key.USER_ID;

/**
 * Created by yoon on 2017. 1. 26..
 */

public class Parser {

    public static final String TAG = Parser.class.getSimpleName();

    public static boolean parseSimpleResult(JSONObject responseObject) {

        try {
            if (responseObject.getString(RESPONSE_MESSAGE).equals(RESPONSE_SUCCESS)) {
                return true;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

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
                if (productJSONObject.getInt(WHOLE_CHEMICALS) == 1) {
                    product.setWholeChemicals(true);
                }
                product.setArchive(productJSONObject.getBoolean(ARCHIVE));
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
                        int dataScore = chemicalJSONObject.getInt(DATA_SCORE);
                        chemical.setDataScore((byte) dataScore);
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
                int dataScore = chemicalJSONObject.getInt(DATA_SCORE);
                chemical.setDataScore((byte) dataScore);
                chemical.setNameKo(chemicalJSONObject.getString(NAMEKO_ORIGIN));
                chemical.setNameEn(chemicalJSONObject.getString(NAMEEN));
                chemical.setPurpose(chemicalJSONObject.getString(PURPOSE));
                if (chemical.getPurpose().equals("null")) {
                    chemical.setPurpose("배합목적이 알려지지 않았어요");
                }
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

    public static ArrayList<Review> parseReviewList(JSONObject responseObject) {

        ArrayList<Review> reviews = new ArrayList<>();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                int reviewSize = responseObject.getInt(COUNT);
                if (reviewSize > 0) {
                    JSONArray reviewJSONArray = responseObject.getJSONArray(RESPONSE_DATA);
                    for (int i = 0; i < reviewSize; i++) {
                        JSONObject reviewJSONObject = (JSONObject) reviewJSONArray.get(i);
                        Review review = new Review();
                        review.setId(reviewJSONObject.getInt(REVIEW_ID));
                        JSONObject userJSONObject = reviewJSONObject.getJSONObject(USER);
                        review.getUser().setId(userJSONObject.getInt(USER_ID));
                        review.getUser().setName(userJSONObject.getString(NAME));
                        int gender = userJSONObject.getInt(GENDER);
                        if (gender == 0) {
                            review.getUser().setGender(true);
                        } else if (gender == 1) {
                            review.getUser().setGender(false);
                        }
                        review.getUser().setAge(userJSONObject.getString(AGE));
                        int hasDrySkin = userJSONObject.getInt(HAS_DRY_SKIN);
                        if (hasDrySkin == 1) {
                            review.getUser().setHasDrySkin(true);
                        }
                        int hasOilySkin = userJSONObject.getInt(HAS_OILY_SKIN);
                        if (hasOilySkin == 1) {
                            review.getUser().setHasOilySkin(true);
                        }
                        int hasAllergy = userJSONObject.getInt(HAS_ALLERGY);
                        if (hasAllergy == 1) {
                            review.getUser().setHasAllergy(true);
                        }
                        int hasChild = userJSONObject.getInt(HAS_CHILD);
                        if (hasChild == 1) {
                            review.getUser().setHasChild(true);
                        }
                        int childHasDrySkin = userJSONObject.getInt(CHILD_HAS_DRY_SKIN);
                        if (childHasDrySkin == 1) {
                            review.getUser().setChildHasDrySkin(true);
                        }
                        int childHasAllergy = userJSONObject.getInt(CHILD_HAS_ALLERGY);
                        if (childHasAllergy == 1) {
                            review.getUser().setChildHasAllergy(true);
                        }

                        Object ratingObject = reviewJSONObject.get(Config.Review.Key.RATING);
                        float ratingFloat = 0.0f;
                        if (ratingObject instanceof Integer && (Integer) ratingObject == -1) {
                            ratingFloat = 0.0f;
                        } else if (ratingObject instanceof Integer) {
                            ratingFloat = ((Integer) ratingObject).floatValue();
                        } else {
                            ratingFloat = ((Double) ratingObject).floatValue();
                        }
                        review.setRatingValue(ratingFloat);

                        review.setContent(reviewJSONObject.getString(Config.Review.Key.DESCRIPTION));
                        review.setDate(reviewJSONObject.getString(Config.Review.Key.DATE));

                        JSONArray jsonArray = reviewJSONObject.getJSONArray(Config.Review.Key.IMAGE_PATHS);
                        for (int j = 0; j < jsonArray.length(); j++) {
                            review.getImagePaths().add(jsonArray.getString(j));
                        }
//                        Log.i(TAG, review.toString());
                        reviews.add(review);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return reviews;
    }

    public static User parseEmailConfirm(JSONObject responseObject) {
        Log.i(TAG, responseObject.toString());
        User user = new User();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {

                JSONObject userObject = responseObject.optJSONObject(RESPONSE_DATA);
                Log.i(TAG, userObject.toString());
                if (userObject.toString().equals("{}")) {
                    user = null;
                } else {
                    JSONObject tokenObject = responseObject.getJSONObject(RESPONSE_DATA);
                    user.setToken(tokenObject.getString(TOKEN));
                    user.setPushToken(tokenObject.getString(PUSH_TOKEN));
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return user;
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
//        Log.i(TAG, user.toString());
        return user;
    }

    public static String parseNaverUser(JSONObject responseObject) {

        String email = null;
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                JSONObject naverUserJSONObject = responseObject.getJSONObject("response");
                email = naverUserJSONObject.getString(EMAIL);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return email;
    }

    public static String parseSignInUserToken(JSONObject responseObject) {

        String token = null;
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                JSONObject tokenObject = responseObject.getJSONObject(RESPONSE_DATA);
                token = tokenObject.getString(TOKEN);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return token;
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
