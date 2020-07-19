package com.anshdeep.blackjack.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.anshdeep.blackjack.R
import com.anshdeep.blackjack.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var moneyAvailable = 0
    private var minimumBet = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("blackjack", Context.MODE_PRIVATE)

        startGameButton.setOnClickListener {
            if (bettingAmount.text.toString().isNotEmpty()) {
                val userBetAmount = Integer.parseInt(bettingAmount.text.toString())
                if (userBetAmount < minimumBet || userBetAmount > moneyAvailable) {
                    Toast.makeText(
                        this, "Please enter a valid bet amount!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    with(sharedPref.edit()) {
                        putInt("MONEY_AVAILABLE", moneyAvailable)
                        apply()
                    }

                    val intent = Intent(this, BlackjackActivity::class.java)
                    intent.putExtra("USER_BET", userBetAmount)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(
                    this, "Please enter a bet amount!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        resetDataButton.setOnClickListener {
            with(sharedPref.edit()) {
                clear()
                apply()
            }

            moneyAvailable = 1000
            minimumBet = 300
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            with(prefs.edit()) {
                putString("amount", "1000")
                putString("bet", "300")
                apply()
            }

            moneyAvailableText.text = "Money Available: $moneyAvailable"
            minimumBetText.text = "Minimum Bet: $minimumBet"

            Toast.makeText(
                this, "All data back to default values",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.blackjack_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        minimumBet = Integer.parseInt(prefs.getString("bet", "300")!!)
        val initialAmount = prefs.getString("amount", "1000")
        minimumBetText.text = "Minimum Bet: " + minimumBet


        val sharedPref = getSharedPreferences("blackjack", Context.MODE_PRIVATE)
        moneyAvailable = sharedPref.getInt("MONEY_AVAILABLE", Integer.parseInt(initialAmount!!))
        moneyAvailableText.text = "Money Available: " + moneyAvailable

    }
}