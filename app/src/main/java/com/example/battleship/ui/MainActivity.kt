package com.example.battleship.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.battleship.R
import com.example.battleship.ui.setup.SetupActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getStartedButton.setOnClickListener {
            val intent = Intent(this, SetupActivity::class.java)
            this.startActivity(intent)
            finish()
        }
    }
}