package com.planet.wondering.chemi.util.helper;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.planet.wondering.chemi.model.Chemical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yoon on 2017. 3. 17..
 */

public class ChemicalSharedPreferences {

    private static final int MAX_NUMBER_OF_CHEMICALS = 10;
    private static final String PREF_CHEMICALS = "com.planet.wondering.chemi.latest.chemicals";

    public static ArrayList<Chemical> getStoredChemicals(Context context) {
        Gson gson = new Gson();
        String jsonChemicals = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_CHEMICALS, null);
        if (jsonChemicals == null) {
            return null;
        }
        Chemical[] chemicalArray = gson.fromJson(jsonChemicals, Chemical[].class);
        List<Chemical> chemicals = Arrays.asList(chemicalArray);
        chemicals = new ArrayList<>(chemicals);
        return (ArrayList<Chemical>) chemicals;
    }

    public static void setStoreChemicals(Context context, List<Chemical> chemicals) {
        Gson gson = new Gson();
        String jsonChemicals = gson.toJson(chemicals);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CHEMICALS, jsonChemicals)
                .apply();
    }

    public static void addStoreChemical(Context context, Chemical chemical) {
        List<Chemical> chemicals = getStoredChemicals(context);
        if (chemicals == null) {
            chemicals = new ArrayList<>();
        }
        for (Chemical c : chemicals) {
            if (c.getNameKo().equals(chemical.getNameKo())) {
                chemicals.remove(c);
                break;
            }
        }
        chemicals.add(chemical);

        if (chemicals.size() > MAX_NUMBER_OF_CHEMICALS) {
            chemicals.remove(0);
        }
        setStoreChemicals(context, chemicals);
    }

    public static void removeStoredChemical(Context context, Chemical chemical) {
        ArrayList<Chemical> chemicals = getStoredChemicals(context);
        if (chemicals != null) {
            chemicals.remove(findChemicalsIndex(context, chemical));
            setStoreChemicals(context, chemicals);
        }
    }

    public static void removeAllStoredChemical(Context context) {
        ArrayList<Chemical> chemicals = getStoredChemicals(context);
        if (chemicals != null) {
            chemicals.clear();
            setStoreChemicals(context, chemicals);
        }
    }

    public static int findChemicalsIndex(Context context, Chemical chemical) {
        ArrayList<Chemical> chemicals = getStoredChemicals(context);
        for (int i = 0; i < chemicals.size(); i++) {
            if (chemicals.get(i).getNameKo().equals(chemical.getNameKo())) {
                return i;
            }
        }
        return -1;
    }
}
