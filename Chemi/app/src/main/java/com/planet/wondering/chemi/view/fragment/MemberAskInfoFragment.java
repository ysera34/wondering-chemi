package com.planet.wondering.chemi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.planet.wondering.chemi.R;
import com.planet.wondering.chemi.model.User;
import com.planet.wondering.chemi.network.AppSingleton;
import com.planet.wondering.chemi.util.helper.TextValidator;
import com.planet.wondering.chemi.util.helper.UserSharedPreferences;
import com.planet.wondering.chemi.util.listener.OnUserInfoUpdateListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.planet.wondering.chemi.network.Config.SOCKET_TIMEOUT_GET_REQ;
import static com.planet.wondering.chemi.network.Config.URL_HOST;
import static com.planet.wondering.chemi.network.Config.User.Key.BIRTH_YEAR;
import static com.planet.wondering.chemi.network.Config.User.Key.CHILD_HAS_ALLERGY;
import static com.planet.wondering.chemi.network.Config.User.Key.CHILD_HAS_DRY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.GENDER;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_ALLERGY;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_CHILD;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_DRY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.HAS_OILY_SKIN;
import static com.planet.wondering.chemi.network.Config.User.Key.TOKEN;
import static com.planet.wondering.chemi.network.Config.User.PATH;

/**
 * Created by yoon on 2017. 2. 17..
 */

public class MemberAskInfoFragment extends Fragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private static final String TAG = MemberAskInfoFragment.class.getSimpleName();

    private static final String ARG_CONFIG_USER = "config_user";

    public static MemberAskInfoFragment newInstance() {

        Bundle args = new Bundle();

        MemberAskInfoFragment fragment = new MemberAskInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberAskInfoFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIG_USER, user);

        MemberAskInfoFragment fragment = new MemberAskInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;

    private EditText mBirthYearEditText;
    private TextView mBirthYearValidationTextView;

    private CheckBox mFemaleCheckBox;
    private CheckBox mMaleCheckBox;

    private CheckBox mParentNoneCheckBox;
    private CheckBox mParentDrySkinCheckBox;
    private CheckBox mParentOilySkinCheckBox;
    private CheckBox mParentAllergyCheckBox;

    private CheckBox mHaveChildCheckBox;
    private CheckBox mHaveNoChildCheckBox;

    private CheckBox mChildNoneCheckBox;
    private CheckBox mChildDrySkinCheckBox;
    private CheckBox mChildAllergyCheckBox;

    private TextView mAskInfoSubmitTextView;

    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = (User) getArguments().getSerializable(ARG_CONFIG_USER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_ask_info, container, false);
        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_ask_back_layout);
        mBackLayout.setOnClickListener(this);

        mBirthYearEditText = (EditText) view.findViewById(R.id.member_ask_info_birth_year_edit_text);
        mBirthYearValidationTextView = (TextView) view.findViewById(R.id.member_ask_info_birth_year_validation_text_view);

        mFemaleCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_female_check_box);
        mFemaleCheckBox.setOnCheckedChangeListener(this);
        mMaleCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_male_check_box);
        mMaleCheckBox.setOnCheckedChangeListener(this);
        mParentNoneCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_parent_none_check_box);
        mParentNoneCheckBox.setOnCheckedChangeListener(this);
        mParentDrySkinCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_parent_dry_skin_check_box);
        mParentDrySkinCheckBox.setOnCheckedChangeListener(this);
        mParentOilySkinCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_parent_oily_skin_check_box);
        mParentOilySkinCheckBox.setOnCheckedChangeListener(this);
        mParentAllergyCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_parent_allergy_check_box);
        mParentAllergyCheckBox.setOnCheckedChangeListener(this);
        mHaveChildCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_have_child_check_box);
        mHaveChildCheckBox.setOnCheckedChangeListener(this);
        mHaveNoChildCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_have_no_child_check_box);
        mHaveNoChildCheckBox.setOnCheckedChangeListener(this);
        mChildNoneCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_child_none_check_box);
        mChildNoneCheckBox.setOnCheckedChangeListener(this);
        mChildDrySkinCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_child_dry_skin_check_box);
        mChildDrySkinCheckBox.setOnCheckedChangeListener(this);
        mChildAllergyCheckBox = (CheckBox) view.findViewById(R.id.member_ask_info_child_allergy_check_box);
        mChildAllergyCheckBox.setOnCheckedChangeListener(this);

        mAskInfoSubmitTextView = (TextView) view.findViewById(R.id.member_ask_info_submit_button_text_view);
        mAskInfoSubmitTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validateBirthYearEditText();
        bindUserInfo(mUser);
    }

    private void bindUserInfo(User user) {

        if (user.isHasExtraInfo()) {
            mBirthYearEditText.setText(String.valueOf(user.getBirthYear()));

            if (user.isGender()) {
                mFemaleCheckBox.setChecked(true);
            } else {
                mMaleCheckBox.setChecked(true);
            }

            if (!user.isHasDrySkin() && !user.isHasOilySkin() && !user.isHasAllergy()) {
                mParentNoneCheckBox.setChecked(true);
            } else {
                if (user.isHasDrySkin()) {
                    mParentDrySkinCheckBox.setChecked(true);
                }
                if (user.isHasOilySkin()) {
                    mParentOilySkinCheckBox.setChecked(true);
                }
                if (user.isHasAllergy()) {
                    mParentAllergyCheckBox.setChecked(true);
                }
            }

            if (user.isHasChild()) {
                mHaveChildCheckBox.setChecked(true);

                if (!user.isChildHasDrySkin() && !user.isChildHasAllergy()) {
                    mChildNoneCheckBox.setChecked(true);
                } else {
                    if (user.isChildHasDrySkin()) {
                        mChildDrySkinCheckBox.setChecked(true);
                    }
                    if (user.isChildHasAllergy()) {
                        mChildAllergyCheckBox.setChecked(true);
                    }
                }

            } else {
                mHaveNoChildCheckBox.setChecked(true);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_ask_back_layout:
                getActivity().onBackPressed();
                break;
            case R.id.member_ask_info_submit_button_text_view:
                if (isBirthYearValidate && isFormValidate) {
                    requestSubmitUserAskInfo(findInfoValueUser());
                } else {
                    if (!isBirthYearValidate){
                        Toast.makeText(getActivity(), "출생년도 입력란을 확인해주세요", Toast.LENGTH_SHORT).show();
                    } else if (!isFormValidate) {
                        Toast.makeText(getActivity(), "부가정보 입력란을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private boolean isBirthYearValidate = false;
    private boolean isFormValidate = false;

    private void validateBirthYearEditText() {
        mBirthYearEditText.addTextChangedListener(
                new TextValidator(mBirthYearEditText) {
                    @Override
                    public void validate(TextView textView, String text) {
                        if (validateBirthYear(text)) {
                            mBirthYearEditText.setBackgroundResource(R.drawable.edit_text_under_line_correct);
                            mBirthYearValidationTextView.setText("");
                            isBirthYearValidate = true;

                            if (isFormValidate) {
                                mAskInfoSubmitTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                                mAskInfoSubmitTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
                            }
                        } else {
                            if (text.length() > 3) {
                                mBirthYearValidationTextView.setText("출생년도 입력란을 확인해주세요.");
                            }
                            mBirthYearEditText.setBackgroundResource(R.drawable.edit_text_under_line_focus_true_accent);
                            isBirthYearValidate = false;

                            mAskInfoSubmitTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                            mAskInfoSubmitTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
                        }
                    }
                }
        );
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.member_ask_info_female_check_box:
                checkOffBoxState(mFemaleCheckBox, new CheckBox[]{mMaleCheckBox});
                break;
            case R.id.member_ask_info_male_check_box:
                checkOffBoxState(mMaleCheckBox, new CheckBox[]{mFemaleCheckBox});
                break;
            case R.id.member_ask_info_parent_none_check_box:
                checkOffBoxState(mParentNoneCheckBox, new CheckBox[]{
                        mParentDrySkinCheckBox, mParentOilySkinCheckBox, mParentAllergyCheckBox});
                break;
            case R.id.member_ask_info_parent_dry_skin_check_box:
//                break;
            case R.id.member_ask_info_parent_oily_skin_check_box:
//                break;
            case R.id.member_ask_info_parent_allergy_check_box:
                if (mParentNoneCheckBox.isChecked()) {
                    mParentNoneCheckBox.setChecked(false);
                }
                break;
            case R.id.member_ask_info_have_child_check_box:
                checkOffBoxState(mHaveChildCheckBox, new CheckBox[]{mHaveNoChildCheckBox});
                break;
            case R.id.member_ask_info_have_no_child_check_box:
                checkOffBoxState(mHaveNoChildCheckBox, new CheckBox[]{mHaveChildCheckBox,
                        mChildNoneCheckBox, mChildDrySkinCheckBox, mChildAllergyCheckBox});
                if (mHaveChildCheckBox.isChecked()) {
                    mHaveChildCheckBox.setChecked(false);
                }
                break;
            case R.id.member_ask_info_child_none_check_box:
                checkOffBoxState(mChildNoneCheckBox, new CheckBox[]{mChildDrySkinCheckBox, mChildAllergyCheckBox});
            case R.id.member_ask_info_child_dry_skin_check_box:
                checkOffBoxState(mChildDrySkinCheckBox, new CheckBox[]{mChildNoneCheckBox});
            case R.id.member_ask_info_child_allergy_check_box:
                checkOffBoxState(mChildAllergyCheckBox, new CheckBox[]{mChildNoneCheckBox});
                if (!mHaveChildCheckBox.isChecked()) {
                    mHaveChildCheckBox.setChecked(true);
                }

                checkOffBoxState(mChildNoneCheckBox, new CheckBox[]{mChildDrySkinCheckBox, mChildAllergyCheckBox});

                break;
        }

//        findInfoValue();
        if (validateForms()) {
            isFormValidate = true;
            if (isBirthYearValidate) {
                mAskInfoSubmitTextView.setTextColor(getResources().getColorStateList(R.color.color_selector_button_white_primary));
                mAskInfoSubmitTextView.setBackgroundResource(R.drawable.selector_opaque_primary);
            }
        } else {
            mAskInfoSubmitTextView.setTextColor(getResources().getColor(R.color.colorWhite));
            mAskInfoSubmitTextView.setBackgroundResource(R.drawable.widget_solid_oval_rectangle_iron);
            isFormValidate = false;
        }
    }

    private void checkOffBoxState(CheckBox checkBox1, CheckBox... checkBoxes) {

        if (checkBox1.isChecked()) {
            for (CheckBox checkBox : checkBoxes) {
                checkBox.setChecked(false);
            }
            checkBox1.setChecked(true);
        }
    }

    private void checkInBoxState(CheckBox checkBox1, CheckBox... checkBoxes) {

        boolean isCheck = false;
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                isCheck = true;
                break;
            }
        }

        if (isCheck) {
            checkBox1.setChecked(false);
        }
    }

    private boolean validateBirthYear(String birthYearString) {
        if (birthYearString == null || birthYearString.equals("")) {
            return false;
        }

        int birthYear = Integer.valueOf(birthYearString);
        return birthYear >= 1950 && birthYear < 2015;
    }

    private User findInfoValueUser() {

        User infoValueUser = new User();
        infoValueUser.setBirthYear(Integer.valueOf(mBirthYearEditText.getText().toString()));

        infoValueUser.setGender(mFemaleCheckBox.isChecked());
        if (!mParentNoneCheckBox.isChecked()) {
            infoValueUser.setHasDrySkin(mParentDrySkinCheckBox.isChecked());
            infoValueUser.setHasOilySkin(mParentOilySkinCheckBox.isChecked());
            infoValueUser.setHasAllergy(mParentAllergyCheckBox.isChecked());
        }

        infoValueUser.setHasChild(mHaveChildCheckBox.isChecked());
        if (!mChildNoneCheckBox.isChecked()) {
            infoValueUser.setChildHasDrySkin(mChildDrySkinCheckBox.isChecked());
            infoValueUser.setChildHasAllergy(mChildAllergyCheckBox.isChecked());
        }

        Log.i("info user : ", infoValueUser.toString());
        return infoValueUser;
    }

    private boolean validateForms() {

        boolean isValidate1 = false;
        boolean isValidate2 = false;
        boolean isValidate3 = false;
        boolean isValidate4 = false;

        if (mFemaleCheckBox.isChecked() || mMaleCheckBox.isChecked()) {
            isValidate1 = true;
        }
        if (mParentNoneCheckBox.isChecked() || mParentDrySkinCheckBox.isChecked()
                || mParentOilySkinCheckBox.isChecked() || mParentAllergyCheckBox.isChecked()) {
            isValidate2 = true;
        }
//        if (mHaveChildCheckBox.isChecked() || mHaveNoChildCheckBox.isChecked()) {
//            isValidate3 = true;
//        }
//        if (mChildNoneCheckBox.isChecked()
//                || mChildDrySkinCheckBox.isChecked() || mChildAllergyCheckBox.isChecked()) {
//            isValidate4 = true;
//        }

        if (mHaveChildCheckBox.isChecked()) {
            isValidate3 = true;
            if (mChildNoneCheckBox.isChecked()
                    || mChildDrySkinCheckBox.isChecked() || mChildAllergyCheckBox.isChecked()) {
                isValidate4 = true;
            }
        } else if (mHaveNoChildCheckBox.isChecked()) {
            isValidate3 = true;
            isValidate4 = true;
        }

        return isValidate1 && isValidate2 && isValidate3 && isValidate4;
    }

    private void requestSubmitUserAskInfo(final User user) {

        Map<String, String> params = new HashMap<>();
        params.put(GENDER, String.valueOf(user.getGender()));
        params.put(BIRTH_YEAR, String.valueOf(user.getBirthYear()));
        params.put(HAS_DRY_SKIN, String.valueOf(user.getHasDrySkin()));
        params.put(HAS_OILY_SKIN, String.valueOf(user.getHasOilySkin()));
        params.put(HAS_ALLERGY, String.valueOf(user.getHasAllergy()));
        params.put(HAS_CHILD, String.valueOf(user.getHasChild()));
        params.put(CHILD_HAS_DRY_SKIN, String.valueOf(user.getChildHasDrySkin()));
        params.put(CHILD_HAS_ALLERGY, String.valueOf(user.getChildHasAllergy()));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, URL_HOST + PATH, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        Toast.makeText(getActivity(), "회원정보가 업데이트 되었습니다.", Toast.LENGTH_SHORT).show();
                        mUserInfoUpdateListener.onUserInfoValueUpdate();
//                        mUserInfoUpdateListener.onUserInfoValueUpdate(user);
//                        getActivity().onBackPressed();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        Toast.makeText(getActivity(), R.string.progress_dialog_message_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(TOKEN, UserSharedPreferences.getStoredToken(getActivity()));
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(SOCKET_TIMEOUT_GET_REQ,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, TAG);
    }

    OnUserInfoUpdateListener mUserInfoUpdateListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mUserInfoUpdateListener = (OnUserInfoUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnUserInfoUpdateListener");
        }
    }
}
