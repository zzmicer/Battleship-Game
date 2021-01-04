package com.example.battleship.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.battleship.R
import com.example.battleship.data.firebase.FirebaseSource
import com.example.battleship.data.models.Player
import com.example.battleship.internal.getViewModel
import com.example.battleship.ui.login.LoginActivity
import com.example.battleship.ui.setup.SetupActivity
import com.example.battleship.ui.stat.ProfileActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ME_PLAYER = "ME_PLAYER"
        const val ROOM_NAME = "ROOM_NAME"
        const val ROLE_NAME = "ROLE_NAME"
        const val VS_PLAYER = "VS_PLAYER"
    }

    private val viewModel by lazy {
        getViewModel { MainViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutButton.setOnClickListener {

            AuthUI.getInstance().signOut(this@MainActivity).addOnCompleteListener {
                // user is now signed out
                Toast.makeText(applicationContext, getString(R.string.user_signed_out), Toast.LENGTH_SHORT).show()
                launchLoginActivity()
            }
        }

        selectRoomButton.setOnClickListener {
            val intent = Intent(this, RoomActivity::class.java)
            intent.putExtra(ME_PLAYER, viewModel.getMyPlayer())
            startActivity(intent)
        }

        getStartedButton.setOnClickListener {
            launchLoginActivity()
        }
        watchStatButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
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
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(FirebaseSource.currentUser())
    }

    private fun updateUI(currentUser: FirebaseUser?) {

        if (currentUser != null) {

            welcomeView.text = String.format("Welcome - %s", currentUser.displayName)
            getStartedButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            selectRoomButton.visibility = View.VISIBLE
            watchStatButton.visibility = View.VISIBLE

        } else {

            getStartedButton.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            selectRoomButton.visibility = View.GONE
            watchStatButton.visibility = View.GONE
        }
    }
}