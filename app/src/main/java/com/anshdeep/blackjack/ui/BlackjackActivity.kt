package com.anshdeep.blackjack.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anshdeep.blackjack.util.CardIdMapper
import com.anshdeep.blackjack.util.Player
import com.anshdeep.blackjack.R
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*
import kotlin.collections.ArrayList

class BlackjackActivity : AppCompatActivity() {

    private var dealerCards = ArrayList<ImageView>()
    private var playerCards = ArrayList<ImageView>()
    private var player = Player()
    private var dealer = Player()
    private var map = CardIdMapper()
    private lateinit var cardList: ArrayList<Int>

    private val rand = Random()
    private var r: Int = 0
    private var id: Int = 0
    private var index = 0
    private var dealerIndex = 0
    private var store = 0

    private var moneyAvailable = 0
    private var userBet = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        userBet = intent.getIntExtra("USER_BET", 250)

        val sharedPref = getSharedPreferences("blackjack", Context.MODE_PRIVATE)

        moneyAvailable = sharedPref.getInt("MONEY_AVAILABLE", 0)


        cardList = map.getAllCardIDs(this)
        dealerCards = arrayListOf(dealerCard1, dealerCard2, dealerCard3, dealerCard4, dealerCard5)
        playerCards = arrayListOf(playerCard1, playerCard2, playerCard3, playerCard4, playerCard5)

        setupInitialCards()
        money.text = "Money Left: $moneyAvailable"


        // click listener for pass button
        passBtn.setOnClickListener {
            while (dealer.getTotal(0) < 17) {
                val name = addRandomCard(dealer)
                val cardValue = findCardValue(name)
                dealer.getTotal(cardValue)
                dealerCards[dealerIndex].setImageResource(id)
                dealerIndex++
            }
            dealerCards[0].setImageResource(store)
            declareWinner()
            showEndGameButtons()
        }

        // click listener for hit button
        hitBtn.setOnClickListener {
            val name = addRandomCard(player)
            val cardValue = findCardValue(name)
            score.text = "Score: " + player.getTotal(cardValue)
            playerCards[index].setImageResource(id)
            index++
            if (player.getTotal(0) > 21) {
                score.text = "Busted!"
                showEndGameButtons()
                moneyAvailable -= userBet
                money.text = "Money Left: $moneyAvailable"
                with(sharedPref.edit()) {
                    putInt("MONEY_AVAILABLE", moneyAvailable)
                    apply()
                }
            }
        }

        newGame.setOnClickListener {
            when {
                moneyAvailable <= 0 -> {
                    Toast.makeText(
                        this, "Sorry, you don't have any money left",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
                moneyAvailable < userBet -> {
                    Toast.makeText(
                        this, "Not enough money. Change the betting amount!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
                else -> {
                    restartGame()
                }
            }
        }
    }

    private fun setupInitialCards() {
        for (i in 0 until 4) {
            r = rand.nextInt(cardList.size)
            id = cardList[r]
            val name = resources.getResourceEntryName(id)

            if (i % 2 == 0) {
                player.addCard(name)
                val cardValue = findCardValue(name)
                score.text = "Score: " + player.getTotal(cardValue)
                playerCards[index].setImageResource(id)
            } else {
                dealer.addCard(name)

                val cardValue = findCardValue(name)
                dealer.getTotal(cardValue)

                if (index == 0) {
                    dealerCards[index].setImageResource(R.drawable.back)
                    store = id
                } else dealerCards[index].setImageResource(id)
                index++

                dealerIndex++
            }
            cardList.remove(id)
        }
    }

    private fun addRandomCard(person: Player): String {
        r = rand.nextInt(cardList.size)
        id = cardList[r]
        val name = resources.getResourceEntryName(id)
        person.addCard(name)
        cardList.remove(id)
        return name
    }


    private fun findCardValue(a: String): Int {
        when (a) {
            "clubs10", "clubs_jack", "clubs_queen", "clubs_king",
            "diamonds10", "diamonds_jack", "diamonds_queen", "diamonds_king",
            "hearts10", "hearts_jack", "hearts_queen", "hearts_king",
            "spades10", "spades_jack", "spades_queen", "spades_king" -> return 10

            "clubs2", "diamonds2", "hearts2", "spades2" -> return 2
            "clubs3", "diamonds3", "hearts3", "spades3" -> return 3
            "clubs4", "diamonds4", "hearts4", "spades4" -> return 4
            "clubs5", "diamonds5", "hearts5", "spades5" -> return 5
            "clubs6", "diamonds6", "hearts6", "spades6" -> return 6
            "clubs7", "diamonds7", "hearts7", "spades7" -> return 7
            "clubs8", "diamonds8", "hearts8", "spades8" -> return 8
            "clubs9", "diamonds9", "hearts9", "spades9" -> return 9
            "clubs_ace", "diamonds_ace", "hearts_ace", "spades_ace" -> return 1
        }
        return 0
    }


    private fun declareWinner() {
        if (player.getTotal(0) == 21) showWinnerText("Player")
        else if (dealer.getTotal(0) == 21) showWinnerText("Dealer")
        else if (player.getTotal(0) < 21 &&
            player.getTotal(0) > dealer.getTotal(0)
        ) showWinnerText("Player")
        else if (dealer.getTotal(0) < 21 &&
            dealer.getTotal(0) > player.getTotal(0)
        ) showWinnerText("Dealer")
        else if (dealer.getTotal(0) == 21 &&
            player.getTotal(0) == 21
        ) showWinnerText("Dealer")
        else if (player.getTotal(0) == dealer.getTotal(0)) score.text = "Tie!"
        else if (player.getTotal(0) > 21) showWinnerText("Dealer")
        else if (dealer.getTotal(0) > 21) showWinnerText("Player")


    }

    private fun showWinnerText(person: String) {
        if (person == "Player") {
            score.text = "You Win!"
            moneyAvailable += (userBet * 2)
        } else {
            score.text = "Dealer Wins!"
            moneyAvailable -= userBet


        }
        money.text = "Money Left: $moneyAvailable"

        val sharedPref = getSharedPreferences("blackjack", Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putInt("MONEY_AVAILABLE", moneyAvailable)
            apply()
        }

    }

    private fun restartGame() {
        finish()
        startActivity(intent)
    }

    private fun showEndGameButtons() {
        newGame.visibility = View.VISIBLE
        buttons.visibility = View.INVISIBLE
    }
}