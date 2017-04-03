package com.planet.wondering.chemi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.planet.wondering.chemi.R;

/**
 * Created by yoon on 2017. 2. 17..
 */

public class MemberAskInfoFragment extends Fragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private static final String TAG = MemberAskInfoFragment.class.getSimpleName();

    public static MemberAskInfoFragment newInstance() {

        Bundle args = new Bundle();

        MemberAskInfoFragment fragment = new MemberAskInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout mBackLayout;

    private EditText mBirthYearEditText;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_ask_info, container, false);
        mBackLayout = (LinearLayout) view.findViewById(R.id.member_config_ask_back_layout);
        mBackLayout.setOnClickListener(this);

        mBirthYearEditText = (EditText) view.findViewById(R.id.member_ask_info_birth_year_edit_text);
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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_config_ask_back_layout:
                getActivity().onBackPressed();
                break;
        }
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
                checkOffBoxState(mHaveNoChildCheckBox, new CheckBox[]{mHaveChildCheckBox});
                break;

            case R.id.member_ask_info_child_none_check_box:
                checkOffBoxState(mChildNoneCheckBox, new CheckBox[]{
                        mChildDrySkinCheckBox, mChildAllergyCheckBox});
                break;
            case R.id.member_ask_info_child_dry_skin_check_box:
//                break;
            case R.id.member_ask_info_child_allergy_check_box:
                if (mChildNoneCheckBox.isChecked()) {
                    mChildNoneCheckBox.setChecked(false);
                }
                break;
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
}
