package com.fitriutari.biblissantriapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var db : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Mengambil variabel username,password, btn login .. username dan password ambil dari database
        var Username = findViewById(R.id.Username) as EditText
        var Password = findViewById(R.id.Password) as EditText
        var btnLogin = findViewById(R.id.btnLogin) as Button

        btnLogin.setOnClickListener {
            val username = Username.text;
            val password = Password.text;

            //intent ke halaman dashboard activity
            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
        }

    }
}