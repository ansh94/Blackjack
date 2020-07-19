package com.anshdeep.blackjack.util


class Player {
    private var totalScore = 0
    private var arrayList = ArrayList<String>()

    fun getTotal(b: Int): Int {
        totalScore += b
        return totalScore
    }

    fun addCard(a: String) {
        arrayList.add(a)
    }
}