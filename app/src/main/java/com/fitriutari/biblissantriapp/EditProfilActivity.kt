package com.fitriutari.biblissantriapp

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import kotlinx.android.synthetic.main.frag_data_buku.view.*

class EditProfilActivity: AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnKembalii -> {
                var intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
            R.id.btnLogoutt -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.btnEdtProfil -> {
                builder.setTitle("Konfirmasi").setMessage("Yakin akan mengubah Data ini?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya", btnUpdateDialog)
                    .setNegativeButton("Tidak", null)
                builder.show()
            }
        }
    }

    lateinit var thisParent : DashboardActivity
    lateinit var db : SQLiteDatabase
    lateinit var v : View
    lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var idUser = findViewById(R.id.edidUser) as EditText
        var Nama = findViewById(R.id.edNamaUser) as EditText
        var Username = findViewById(R.id.edUsername) as EditText
        var Password = findViewById(R.id.Password) as EditText
        var btnEdit = findViewById(R.id.btnEdtProfil) as Button

    }

    fun showDataUser() {
        val cursor : Cursor = db.query("user", arrayOf("nama", "username", "password", "id_user as _id"),
            null, null, null, null, "nama asc")
    }

    override fun onStart() {
        super.onStart()
        showDataUser()
    }

    fun updateDataUser(nama : String, username : String, password : String, idUser : String) {
        var cv : ContentValues = ContentValues()
        cv.put("nama",nama)
        cv.put("username", username)
        cv.put("password", password)
        db.update("user",cv,"id_user = $idUser",null)
        showDataUser()
    }

    val btnUpdateDialog = DialogInterface.OnClickListener { dialog, which ->
        updateDataUser(v.edNamaUser.text.toString(), v.edUsername.text.toString(), v.edPass.text.toString(), v.edidUser.text.toString())
        v.edNamaUser.setText("")
        v.edUsername.setText("")
        v.edPass.setText("")
    }
}