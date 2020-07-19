package com.anshdeep.blackjack.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.anshdeep.blackjack.R


class MySettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val sp = preferenceScreen.sharedPreferences
        val amountPref: ListPreference? = findPreference("amount") as ListPreference?
        amountPref?.summary = sp.getString("amount", "Click to show a list to choose from")

        val betPref: ListPreference? = findPreference("bet") as ListPreference?
        betPref?.summary = sp.getString("bet", "Click to show a list to choose from")
    }


    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        if (key.equals("amount")) {
            val amountPref: ListPreference? = key?.let { findPreference(it) }
            amountPref?.summary = preferences!!.getString(key, "")
        } else if (key.equals("bet")) {
            val betPref: ListPreference? = key?.let { findPreference(it) }
            betPref?.summary = preferences!!.getString(key, "")
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceManager.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }
}