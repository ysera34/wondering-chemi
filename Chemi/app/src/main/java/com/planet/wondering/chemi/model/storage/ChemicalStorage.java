package com.planet.wondering.chemi.model.storage;

import android.content.Context;

import com.planet.wondering.chemi.model.Chemical;

import java.util.ArrayList;

/**
 * Created by yoon on 2017. 1. 22..
 */

public class ChemicalStorage {

    private static ChemicalStorage sChemicalStorage;
    private Context mContext;

    private ArrayList<Chemical> mChemicals;

    public ChemicalStorage(Context context) {
        mContext = context;
        mChemicals = new ArrayList<>();

        for (int j = 0; j < 10; j++) {
            Chemical chemical = new Chemical();
            chemical.setId(j);
            chemical.setNameKo("화학성분" + j);
            chemical.setNameEn("chemical" + j);
            if (j > 3 && j % 2 == 0) {
                chemical.setMinHazard((byte)3);
            }
            chemical.setMaxHazard((byte)j);
            mChemicals.add(chemical);
        }
    }

    public static ChemicalStorage getStorage(Context context) {
        if (sChemicalStorage == null) {
            sChemicalStorage = new ChemicalStorage(context.getApplicationContext());
        }
        return sChemicalStorage;
    }

    public ArrayList<Chemical> getChemicals() {
        return mChemicals;
    }

    public Chemical getChemical(int chemicalId) {
        for (Chemical chemical : mChemicals) {
            if (chemical.getId() == chemicalId) {
                return chemical;
            }
        }
        return null;
    }
}
