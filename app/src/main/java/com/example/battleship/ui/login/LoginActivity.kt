package com.example.battleship.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import com.example.battleship.R
import com.example.battleship.data.firebase.FirebaseSource
import com.example.battleship.ui.MainActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        const val id = "id"
        const val username = "username"
        const val RC_SIGN_IN: Int = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonSignIn.setOnClickListener {

            if (FirebaseSource.currentUser() != null) {
                Toast.makeText(applicationContext, getString(R.string.user_already_in), Toast.LENGTH_SHORT).show()
                // already signed in

            } else {
                val providers = listOf(AuthUI.IdpConfig.EmailBuilder().build())

                //Really good stuff, no need to create all fields manually
                startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), RC_SIGN_IN
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseSource.currentUser()

                addPlayer(user)
            }
        } else {
            Toast.makeText(applicationContext, getString(R.string.error_sign_in), Toast.LENGTH_SHORT).show()
        }
    }

    private fun addPlayer(user: FirebaseUser?) {

        val userId: String = user!!.uid
        val reference = FirebaseDatabase.getInstance().getReference(FirebaseSource.PLAYERS_TABLE).child(userId)

        val hashMap: HashMap<String, String> = hashMapOf()
        hashMap[id] = userId
        hashMap[username] = user.displayName.toString()

        reference.setValue(hashMap).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, getString(R.string.success_sign_in), Toast.LENGTH_SHORT).show()
                launchMainActivity(user)
            } else {
                Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchMainActivity(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish();
        }
    }
}