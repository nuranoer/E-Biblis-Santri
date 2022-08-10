package com.fitriutari.biblissantriapp

import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_bayar_denda.*

class DendaActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bayar_denda)
        intentIntegrator = IntentIntegrator(this)
        btnGenerateDenda.setOnClickListener(this)
        builder = AlertDialog.Builder(this)
        db =DBOpenHelper(this).writableDatabase
    }

    lateinit var intentIntegrator: IntentIntegrator
    lateinit var view: View
    lateinit var builder: AlertDialog.Builder
    lateinit var db : SQLiteDatabase

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnGenerateDenda -> {
                val barCodeEncoder = BarcodeEncoder()
                val bitmap = barCodeEncoder.encodeBitmap(
                    edDenda.text.toString(),
                    BarcodeFormat.QR_CODE, 400, 400
                )
                imv.setImageBitmap(bitmap)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult =IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(intentResult!=null) {
            if (intentResult.contents != null) {
                edDenda.setText(intentResult.contents)
            } else {
                Toast.makeText(this, "Dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    val itemClick = AdapterView.OnItemClickListener { parent, view, position, id ->
        val c: Cursor = parent.adapter.getItem(position) as Cursor
        val barCodeEncoder = BarcodeEncoder()
        val bitmap = barCodeEncoder.encodeBitmap(edDenda.text.toString(),
            BarcodeFormat.QR_CODE, 400, 400)
        imv.setImageBitmap(bitmap)
    }
}