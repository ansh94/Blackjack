package com.anshdeep.blackjack.util

import android.R.drawable
import android.content.Context
import android.util.Log
import com.anshdeep.blackjack.R


class CardIdMapper {

    fun getAllCardIDs(context: Context): ArrayList<Int> {
        val drawableResources = drawable()
        val res = ArrayList<Int>()
        val fields = R.drawable::class.java.declaredFields

        for (element in fields) {
            try {
                val resourceId = element.getInt(drawableResources)
                val name = context.resources.getResourceEntryName(resourceId)

                if (name.matches(Regex("(clubs|joker|spades|diamonds|hearts).*"))) {
                    res.add(resourceId)
                }
            } catch (e: Exception) {
                Log.d("CardToIdMapper", "Error message: " + e.localizedMessage)
                continue
            }
        }

        return res
    }
}