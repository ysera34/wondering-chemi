package com.planet.wondering.chemi.network;

import android.util.Log;

import com.planet.wondering.chemi.model.CTag;
import com.planet.wondering.chemi.model.Chemical;
import com.planet.wondering.chemi.model.Hazard;
import com.planet.wondering.chemi.model.Pager;
import com.planet.wondering.chemi.model.Product;
import com.planet.wondering.chemi.model.Review;
import com.planet.wondering.chemi.model.Tag;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.model.UserConfig;
import com.planet.wondering.chemi.model.archive.ReviewProduct;
import com.planet.wondering.chemi.network.Config.Chemical.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_ARCHIVE_ID;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_ARCHIVE_IMAGE_PATH;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_ARCHIVE_KEPT;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_CONTENTS;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_CONTENTS_SIZE;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_CONTENT_TITLE;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_CONTENT_TITLE2;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_PRODUCTS;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_PRODUCTS_SIZE;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_PRODUCT_BRAND;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_PRODUCT_NAME;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_REVIEWS;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_REVIEW_DATE;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_REVIEW_PRODUCT_ID;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_REVIEW_PRODUCT_IMAGE_PATH;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_REVIEW_RATING;
import static com.planet.wondering.chemi.network.Config.Archive.Key.USER_REVIEW_SIZE;
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
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_ALTERNATIVE_NAME;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_CHEMICAL_ID;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_COUNT;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_DESCRIPTION;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_ID;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_IS_CORRECT;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_RANK;
import static com.planet.wondering.chemi.network.Config.Tag.Key.TAG_RANK_DELTA;
import static com.planet.wondering.chemi.network.Config.User.Key.AGE;
import static com.planet.wondering.chemi.network.Config.User.Key.APP_VERSION;
import static com.planet.wondering.chemi.network.Config.User.Key.BIRTH_YEAR;
import static com.planet.wondering.chemi.network.Config.User.Key.CHILD_HAS_ALLERGY;
import static com.planet.wondering.chemi.network.Config.User.Key.CHILD_HAS_DRY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.CREATE_DATE;
import static com.planet.wondering.chemi.network.Config.User.Key.EMAIL;
import static com.planet.wondering.chemi.network.Config.User.Key.GENDER;
import static com.planet.wondering.chemi.network.Config.User.Key.GET_EMAIL;
import static com.planet.wondering.chemi.network.Config.User.Key.GET_PUSH;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_ALLERGY;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_CHILD;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_DRY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_OILY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.MODIFY_DATE;
import static com.planet.wondering.chemi.network.Config.User.Key.PLATFORM;
import static com.planet.wondering.chemi.network.Config.User.Key.PUSH_TOKEN;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.Key.USER;
import static com.planet.wondering.chemi.network.Config.User.Key.USER_ID;
import static com.planet.wondering.chemi.network.Config.User.Key.USER_IMAGE_PATH;

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

    public static ArrayList<CTag> parseCTagList(JSONObject responseObject) {

        ArrayList<CTag> cTags = new ArrayList<>();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                int tagSize = responseObject.getInt(TAG_COUNT);
                if (tagSize > 0) {
                    JSONArray tagJSONArray = responseObject.getJSONArray(RESPONSE_DATA);
                    for (int i = 0; i < tagSize; i++) {
                        JSONObject tagJSONObject = tagJSONArray.getJSONObject(i);
                        CTag cTag = new CTag();
                        cTag.setId(tagJSONObject.getInt(TAG_ID));
                        cTag.setChemicalId(tagJSONObject.getInt(TAG_CHEMICAL_ID));
                        cTag.setDescription(tagJSONObject.getString(TAG_DESCRIPTION));
                        cTag.setCorrect(tagJSONObject.getInt(TAG_IS_CORRECT) == 1);
                        cTag.setAlternativeName(tagJSONObject.getString(TAG_ALTERNATIVE_NAME));
                        cTags.add(cTag);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return cTags;
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

    public static int parseTotalCount(JSONObject responseObject) {

        int total = 0;
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                total = responseObject.getInt(TOTAL);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return total;
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

    public static Pager parseListPaginationQuery(JSONObject responseObject) {

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
//                if (chemical.getPurpose().equals("null")) {
//                    chemical.setPurpose("");
//                }
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

    public static ArrayList<Chemical> parseChemicalList(JSONObject responseObject) {

        ArrayList<Chemical> chemicals = new ArrayList<>();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                int chemicalSize = responseObject.getInt(COUNT);
                if (chemicalSize > 0) {
                    JSONArray chemicalJSONArray = responseObject.getJSONArray(RESPONSE_DATA);
                    for (int i = 0; i < chemicalSize; i++) {
                        JSONObject chemicalJSONObject = (JSONObject) chemicalJSONArray.get(i);
                        Chemical chemical = new Chemical();
                        chemical.setId(chemicalJSONObject.getInt(CHEMICAL_ID));
                        chemical.setNameKo(chemicalJSONObject.getString(NAMEKO_ORIGIN));
                        chemical.setNameEn(chemicalJSONObject.getString(NAMEEN));
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
                        chemicals.add(chemical);
                    }
                }

            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return chemicals;
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
                        review.getUser().setImagePath(userJSONObject.getString(IMAGE_PATH));
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

    public static User parseSignUpForUser(JSONObject responseObject) {

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

    public static String parseMemberImagePath(JSONObject responseObject) {

        String imagePath = null;
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                JSONObject imagePathObject = responseObject.getJSONObject(RESPONSE_DATA);
                imagePath = imagePathObject.getString(USER_IMAGE_PATH);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return imagePath;
    }

    public static User parseMemberConfigUser(JSONObject responseObject) {

        User user = new User();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                JSONObject userObject = responseObject.getJSONObject(RESPONSE_DATA);
//                user.setId(userObject.getInt(USER_ID));
                user.setEmail(userObject.getString(EMAIL));
                user.setName(userObject.getString(Config.User.Key.NAME));
                user.setPlatformId((byte) userObject.getInt(PLATFORM));
                user.setImagePath(userObject.getString(USER_IMAGE_PATH));

                if (userObject.getInt(BIRTH_YEAR) == -1) {
                    user.setHasExtraInfo(false);
                } else {
                    user.setHasExtraInfo(true);
                    user.setGender(userObject.getInt(GENDER) == 0);
                    user.setAge(userObject.getString(AGE));
                    user.setBirthYear(userObject.getInt(BIRTH_YEAR));
                    user.setHasDrySkin(userObject.getInt(HAS_DRY_SKIN) == 1);
                    user.setHasOilySkin(userObject.getInt(HAS_OILY_SKIN) == 1);
                    user.setHasAllergy(userObject.getInt(HAS_ALLERGY) == 1);
                    user.setHasChild(userObject.getInt(HAS_CHILD) == 1);
                    user.setChildHasDrySkin(userObject.getInt(CHILD_HAS_DRY_SKIN) == 1);
                    user.setChildHasAllergy(userObject.getInt(CHILD_HAS_ALLERGY) == 1);
                }


                user.setCreateDate(userObject.getString(CREATE_DATE));
                user.setModifyDate(userObject.getString(MODIFY_DATE));

                int productSize = userObject.getInt(USER_PRODUCTS_SIZE);
                if (productSize > 0) {
                    user.setArchiveProducts(new ArrayList<com.planet.wondering.chemi.model.archive.Product>());
                    JSONArray productJSONArray = userObject.getJSONArray(USER_PRODUCTS);
                    for (int i = 0; i < productSize; i++) {
                        JSONObject productJSONObject = (JSONObject) productJSONArray.get(i);
                        com.planet.wondering.chemi.model.archive.Product product = new com.planet.wondering.chemi.model.archive.Product();
                        product.setProductId(productJSONObject.getInt(USER_ARCHIVE_ID));
                        product.setBrand(productJSONObject.getString(USER_PRODUCT_BRAND));
                        product.setName(productJSONObject.getString(USER_PRODUCT_NAME));
                        product.setImagePath(productJSONObject.getString(USER_ARCHIVE_IMAGE_PATH));
                        product.setKeepDate(productJSONObject.getString(USER_ARCHIVE_KEPT));
                        user.getArchiveProducts().add(product);
                    }
                }

                int contentSize = userObject.getInt(USER_CONTENTS_SIZE);
                if (contentSize > 0) {
                    user.setArchiveContents(new ArrayList<com.planet.wondering.chemi.model.archive.Content>());
                    JSONArray contentJSONArray = userObject.getJSONArray(USER_CONTENTS);
                    for (int i = 0; i < contentSize; i++) {
                        JSONObject contentJSONObject = (JSONObject) contentJSONArray.get(i);
                        com.planet.wondering.chemi.model.archive.Content content = new com.planet.wondering.chemi.model.archive.Content();
                        content.setContentId(contentJSONObject.getInt(USER_ARCHIVE_ID));
                        content.setTitle(contentJSONObject.getString(USER_CONTENT_TITLE));
                        content.setSubTitle(contentJSONObject.getString(USER_CONTENT_TITLE2));
                        content.setImagePath(contentJSONObject.getString(USER_ARCHIVE_IMAGE_PATH));
                        content.setKeepDate(contentJSONObject.getString(USER_ARCHIVE_KEPT));
                        user.getArchiveContents().add(content);
                    }
                }

                int reviewSize = userObject.getInt(USER_REVIEW_SIZE);
                if (reviewSize > 0) {
                    user.setReviewProducts(new ArrayList<ReviewProduct>());
//                    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
                    JSONArray reviewJSONArray = userObject.getJSONArray(USER_REVIEWS);
                    for (int i = 0; i < reviewSize; i++) {
                        JSONObject reviewJSONObject = (JSONObject) reviewJSONArray.get(i);
                        ReviewProduct reviewProduct = new ReviewProduct();
                        reviewProduct.setProductId(reviewJSONObject.getInt(USER_REVIEW_PRODUCT_ID));
//                        reviewProduct.setProductName(reviewJSONObject.getString(USER_REVIEW_PRODUCT_NAME));
                        reviewProduct.setProductImagePath(reviewJSONObject.getString(USER_REVIEW_PRODUCT_IMAGE_PATH));

                        Object ratingObject = reviewJSONObject.get(USER_REVIEW_RATING);
                        float ratingFloat = 0.0f;
                        if (ratingObject instanceof Integer && (Integer) ratingObject == -1) {
                            ratingFloat = 0.0f;
                        } else if (ratingObject instanceof Integer) {
                            ratingFloat = ((Integer) ratingObject).floatValue();
                        } else {
                            ratingFloat = ((Double) ratingObject).floatValue();
                        }
                        reviewProduct.setRatingValue(ratingFloat);
                        reviewProduct.setCreateDate(reviewJSONObject.getString(USER_REVIEW_DATE).substring(5, 10));
//                        reviewProduct.setWriteDate(dateFormat.parse(reviewJSONObject.getString(USER_REVIEW_DATE)));
//                        Log.i(TAG, "Date" + reviewProduct.getCreateDate());
                        user.getReviewProducts().add(reviewProduct);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return user;
    }

    public static String parseUpdatedUserName(JSONObject responseObject) {

        String userName = null;
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                JSONObject userObject = responseObject.getJSONObject(RESPONSE_DATA);
                userName = userObject.getString(Config.User.Key.NAME);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return userName;
    }

    public static UserConfig parseUserConfig(JSONObject responseObject) {

        UserConfig userConfig = new UserConfig();
        try {
            String responseMessage = responseObject.getString(RESPONSE_MESSAGE);
            if (responseMessage.equals(RESPONSE_SUCCESS)) {
                JSONObject userConfigObject = responseObject.getJSONObject(RESPONSE_DATA);
                userConfig.setGetPush(userConfigObject.getInt(GET_PUSH) == 1);
                userConfig.setGetEmail(userConfigObject.getInt(GET_EMAIL) == 1);
                userConfig.setAppVersion(userConfigObject.getString(APP_VERSION));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return userConfig;
    }
}
