package com.fitriutari.biblissantriapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_bayar_denda.*
import kotlinx.android.synthetic.main.activity_data_denda.*
import kotlinx.android.synthetic.main.activity_data_denda.view.*
import kotlinx.android.synthetic.main.activity_data_peminjaman.*

import java.util.*

class HalamanDendaActivity : AppCompatActivity(), View.OnClickListener {
    var tahun = 0
    var bulan = 0
    var hari = 0

    lateinit var adapter : ListAdapter
    lateinit var builder: android.app.AlertDialog.Builder
    lateinit var intentIntegrator: IntentIntegrator
    lateinit var view: View
    lateinit var db : SQLiteDatabase
    var noDenda : String = ""

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.TglKembalii -> showDialog(30)

            R.id.btnInsert4 -> {
                builder.setTitle("Konfirmasi").setMessage("Apakah data sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnInsertDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnUpdate4 -> {
                builder.setTitle("Konfirmasi").setMessage("Apakah data sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnUpdateDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnDelete4 -> {
                builder.setTitle("Konfirmasi").setMessage("Yakin akan menghapus data ini?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnDeleteDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnQrCode -> {
                var intent = Intent( this, DendaActivity::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_denda)
        intentIntegrator = IntentIntegrator(this)
        builder = android.app.AlertDialog.Builder(this)
        lvDenda.setOnItemClickListener(itemClick)
        db =DBOpenHelper(this).writableDatabase

        val cal : Calendar = Calendar.getInstance()

        bulan = cal.get(Calendar.MONTH)+1
        hari = cal.get(Calendar.DAY_OF_MONTH)
        tahun = cal.get(Calendar.YEAR)



        txKembali2.text = "$hari-$bulan-$tahun"


        dp3.init(tahun,bulan,hari,dateChange)


        TglKembalii.setOnClickListener(this)
        btnInsert4.setOnClickListener(this)
        btnUpdate4.setOnClickListener(this)
        btnDelete4.setOnClickListener(this)
        btnQrCode.setOnClickListener(this)

    }
    var dateChange = DatePicker.OnDateChangedListener{ view, year, monthOfYear, dayOfMonth ->
        txKembali2.text = "$dayOfMonth-${monthOfYear+1}-$year"
        tahun =year
        bulan = monthOfYear+1
        hari = dayOfMonth
    }
    var dateChangeDialog = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        txKembali2.text = "$dayOfMonth-${month+1}-$year"
        tahun = year
        bulan = month+1
        hari = dayOfMonth
    }
    override fun onCreateDialog(id:Int): Dialog {
        when(id){
            30 -> return DatePickerDialog(this, dateChangeDialog,tahun, bulan, hari)
        }
        return super.onCreateDialog(id)
    }
    fun showDataDenda() {
        val cursor : Cursor = db.query("denda", arrayOf("no_pinjam", "tgl_kembali", "jumlah_denda",  "no_denda as _id"),
            null, null, null, null, "no_pinjam asc")
        adapter = SimpleCursorAdapter(this, R.layout.activity_data_denda,cursor,
            arrayOf("_id","no_pinjam", "tgl_kembali", "jumlah_denda"),
            intArrayOf(R.id.edNoPeminjaman2, R.id.txKembali2, R.id.edTotal),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        lvDenda.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        showDataDenda()
    }

    val itemClick = AdapterView.OnItemClickListener { parent, view, position, id ->
        val c : Cursor = parent.adapter.getItem(position) as Cursor
        noDenda = c.getString(c.getColumnIndexOrThrow("_id"))
        edNoPeminjaman2.setText(c.getString(c.getColumnIndexOrThrow("no_peminjaman")))
        txKembali2.setText(c.getString(c.getColumnIndexOrThrow("tgl_kembali")))
        edTotal.setText(c.getString(c.getColumnIndexOrThrow("jumlah_denda")))
    }

    fun insertDataPeminjaman(noPeminjaman: String, tglKembali : String, total : String) {
        var cv : ContentValues = ContentValues()
        cv.put("no_peminjaman",noPeminjaman)
        cv.put("tglKembali",tglKembali)
        cv.put("jumlah_denda",total)
        cv.put("no_denda",noDenda)
        db.insert("denda",null,cv)
        showDataDenda()
    }

    fun updateDataPeminjaman(noPeminjaman: String, tglKembali : String, total : String) {
        var cv : ContentValues = ContentValues()
        cv.put("no_peminjaman",noPeminjaman)
        cv.put("tglKembali",tglKembali)
        cv.put("jumlah_denda",total)
        cv.put("no_denda",noDenda)
        db.update("denda",cv,"no_denda = $noDenda",null)
        showDataDenda()
    }

    fun deleteDataPeminjaman(idPengunjung : String) {
        db.delete("denda","no_denda = $noDenda",null)
        showDataDenda()
    }

    val btnInsertDialog = DialogInterface.OnClickListener { dialog, which ->
        insertDataPeminjaman(edNoPeminjaman2.text.toString(), txKembali2.text.toString(),edTotal.text.toString())
        edNoPeminjaman2.setText("")
        txKembali2.setText("")
        edTotal.setText("")
    }

    val btnUpdateDialog = DialogInterface.OnClickListener { dialog, which ->
        updateDataPeminjaman(edNoPeminjaman2.text.toString(), txKembali2.text.toString(),edTotal.text.toString())
        edNoPeminjaman2.setText("")
        txKembali2.setText("")
        edTotal.setText("")
    }

    val btnDeleteDialog = DialogInterface.OnClickListener { dialog, which ->
        deleteDataPeminjaman(noDenda)
        edNoPeminjaman2.setText("")
        txKembali2.setText("")
        edTotal.setText("")
    }


}