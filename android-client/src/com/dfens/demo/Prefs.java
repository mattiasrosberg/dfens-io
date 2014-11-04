package com.dfens.demo;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by mattias on 2014-11-04.
 */
@SharedPref
public interface Prefs {

    @DefaultInt(0)
    int maxSeqNo();
}
