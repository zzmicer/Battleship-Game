package com.example.battleship.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.battleship.R
import com.example.battleship.data.firebase.FirebaseSource
import com.example.battleship.ui.login.LoginActivity
import com.example.battleship.ui.setup.SetupActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getStartedButton.setOnClickListener {
            launchLoginActivity()
        }

        logoutButton.setOnClickListener {

            AuthUI.getInstance().signOut(this@MainActivity).addOnCompleteListener {
                Toast.makeText(applicationContext, getString(R.string.user_signed_out), Toast.LENGTH_SHORT).show()
                launchLoginActivity()
            }
        }

        continueButton.setOnClickListener {
            val intent = Intent(this, SetupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun launchLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
        finish()
    }


    public override fun onStart() {
        super.onStart()
        updateUI(FirebaseSource().currentUser())
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {

            welcomeView.text = String.format("Welcome - %s", currentUser.displayName)
            getStartedButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            continueButton.visibility = View.VISIBLE

        } else {

            getStartedButton.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            continueButton.visibility = View.GONE
        }
    }
}
