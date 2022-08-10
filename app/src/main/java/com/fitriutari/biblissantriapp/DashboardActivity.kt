package com.fitriutari.biblissantriapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*

//import appcompat activity dan bottom navigation
class DashboardActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var db : SQLiteDatabase
    lateinit var ft : FragmentTransaction
    lateinit var fragPengunjung : FragmentPengunjung
    lateinit var fragBuku : FragmentBuku


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //layout dashboard
        setContentView(R.layout.activity_dashboard)
        bnv1.setOnNavigationItemSelectedListener(this)
        fragPengunjung = FragmentPengunjung()
        fragBuku = FragmentBuku()
        db = DBOpenHelper(this).writableDatabase
    }
//Membuat variabel getDBObject agar bisa dipanggil tiap halaman fragmen
    fun getDbObject() : SQLiteDatabase {
        return db
    }

    //create option menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var mnuInflater = menuInflater
        mnuInflater.inflate(R.menu.menu_option,menu)
        return super.onCreateOptionsMenu(menu)
    }
//isi dari option menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.itemPeminjaman ->{
                var intent = Intent( this,PeminjamanActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.itemDenda ->{
                var intent = Intent( this,HalamanDendaActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.itemEdit ->{
                var intent = Intent( this,EditProfilActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.itemLogout ->{
                var intent = Intent( this,MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //isi fragmen
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.itemPengunjung ->{
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frameLayout,fragPengunjung).commit()
                frameLayout.setBackgroundColor(Color.argb(245,255,255,225))
                frameLayout.visibility = View.VISIBLE
            }
            R.id.itemBuku ->{
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.frameLayout, fragBuku ).commit()
                frameLayout.setBackgroundColor(Color.argb(245,255,255,225))
                frameLayout.visibility = View.VISIBLE
            }
            R.id.itemHome -> frameLayout.visibility = View.GONE
        }
        return true
    }
}