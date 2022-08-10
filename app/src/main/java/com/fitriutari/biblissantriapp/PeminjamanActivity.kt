package com.fitriutari.biblissantriapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_data_denda.*
import kotlinx.android.synthetic.main.activity_data_peminjaman.*
import java.util.*

class PeminjamanActivity : AppCompatActivity(), View.OnClickListener {
    var tahun = 0
    var bulan = 0
    var hari = 0

    lateinit var adapter : ListAdapter
    lateinit var builder: android.app.AlertDialog.Builder
    lateinit var intentIntegrator: IntentIntegrator
    lateinit var view: View
    lateinit var db : SQLiteDatabase
    var noPeminjaman : String = " "


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.TglPinjam -> showDialog(20)
            R.id.TglKembali -> showDialog(30)

            R.id.btnInsert3 -> {
                var id_pengunjung = edIdpengunjung2.text.toString()
                var idUser = edidUser.text.toString()
                var noBuku = edNoBuku2.text.toString()
                var tglPinjam = TglPinjam.text.toString()
                var tglKembali = TglKembali.text.toString()

                var cv : ContentValues = ContentValues()
                cv.put("id_pengunjung",id_pengunjung.toString())
                cv.put("id_user",idUser.toString())
                cv.put("no_buku",noBuku.toString())
                cv.put("tgl_pinjam",tglPinjam.toString())
                cv.put("tgl_pinjam",tglKembali.toString())
                db.insert("peminjaman",null,cv)
                showDataPeminjaman()

            }
            R.id.btnUpdate3 -> {
                builder.setTitle("Konfirmasi").setMessage("Apakah data sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnUpdateDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnDelete3 -> {

            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_peminjaman)
        intentIntegrator = IntentIntegrator(this)
        builder = android.app.AlertDialog.Builder(this)
        lvPinjam.setOnItemClickListener(itemClick)
        db =DBOpenHelper(this).writableDatabase

        val cal : Calendar = Calendar.getInstance()

        bulan = cal.get(Calendar.MONTH)+1
        hari = cal.get(Calendar.DAY_OF_MONTH)
        tahun = cal.get(Calendar.YEAR)


        txPinjam.text = "$hari-$bulan-$tahun"
        txKembali.text = "$hari-$bulan-$tahun"


        dp1.init(tahun,bulan,hari,dateChange)
        dp2.init(tahun,bulan,hari,dateChange)

        TglPinjam.setOnClickListener(this)
        TglKembali.setOnClickListener(this)
        btnInsert3.setOnClickListener(this)
        btnDelete3.setOnClickListener(this)
        btnUpdate3.setOnClickListener(this)



    }
    var dateChange = DatePicker.OnDateChangedListener{ view, year, monthOfYear, dayOfMonth ->
        txPinjam.text = "$dayOfMonth-${monthOfYear+1}-$year"
        tahun =year
        bulan = monthOfYear+1
        hari = dayOfMonth
    }
    var dateChange2 = DatePicker.OnDateChangedListener{ view, year, monthOfYear, dayOfMonth ->
        txKembali.text = "$dayOfMonth-${monthOfYear+1}-$year"
        tahun =year
        bulan = monthOfYear+1
        hari = dayOfMonth
    }
    var dateChangeDialog = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        txPinjam.text = "$dayOfMonth-${month+1}-$year"
        tahun = year
        bulan = month+1
        hari = dayOfMonth
    }
    var dateChangeDialog2 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        txKembali.text = "$dayOfMonth-${month+1}-$year"
        tahun = year
        bulan = month+1
        hari = dayOfMonth
    }

    override fun onCreateDialog(id:Int): Dialog {
        when(id){
            20 -> return DatePickerDialog(this, dateChangeDialog,tahun, bulan, hari)
            30 -> return DatePickerDialog(this, dateChangeDialog2,tahun, bulan, hari)
        }
        return super.onCreateDialog(id)
    }

    fun showDataPeminjaman() {
        val cursor : Cursor = db.query("peminjaman", arrayOf("id_pengunjung", "id_user", "no_buku", "tgl_pinjam", "tgl_kembali", "no_pinjam as _id"),
            null, null, null, null, "id_pengunjung asc")
        adapter = SimpleCursorAdapter(this, R.layout.activity_data_peminjaman,cursor,
            arrayOf("_id","id_pengunjung", "id_user", "no_buku", "tgl_pinjam","tgl_kembali"),
            intArrayOf(R.id.edIdpengunjung2, R.id.edidUser, R.id.edNoBuku2, R.id.txPinjam, R.id.txKembali),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        lvPinjam.adapter = adapter
    }
    override fun onStart() {
        super.onStart()
        showDataPeminjaman()
    }

    val itemClick = AdapterView.OnItemClickListener { parent, view, position, id ->
        val c : Cursor = parent.adapter.getItem(position) as Cursor
        noPeminjaman = c.getString(c.getColumnIndexOrThrow("_id"))
        edIdpengunjung2.setText(c.getString(c.getColumnIndexOrThrow("id_pengunjung")))
        edidUser.setText(c.getString(c.getColumnIndexOrThrow("id_user")))
        edNoBuku2.setText(c.getString(c.getColumnIndexOrThrow("no_buku")))
        txPinjam.setText(c.getString(c.getColumnIndexOrThrow("tgl_pinjam")))
        txKembali.setText(c.getString(c.getColumnIndexOrThrow("tgl_kembali")))
    }

    fun updateDataPeminjaman(idPengunjung : String, idUser: String, noBuku: String, tglPinjam: String, tglKembali: String, noPeminjaman : String) {
        var cv : ContentValues = ContentValues()
        cv.put("id_pengunjung",idPengunjung)
        cv.put("id_user",idUser)
        cv.put("no_buku",noBuku)
        cv.put("tgl_pinjam",tglPinjam)
        cv.put("tgl_pinjam",tglKembali)
        db.update("peminjaman",cv,"no_peminjaman = $noPeminjaman",null)
        showDataPeminjaman()
    }

    fun deleteDataPeminjaman(idPengunjung : String) {
        db.delete("peminjaman","no_peminjaman = $noPeminjaman",null)
        showDataPeminjaman()
    }
    
    val btnUpdateDialog = DialogInterface.OnClickListener { dialog, which ->
        updateDataPeminjaman(edIdpengunjung2.text.toString(), edidUser.text.toString(), edNoBuku2.text.toString(), txPinjam.text.toString(), txKembali.text.toString(), edNoPinjam.toString())
        edIdpengunjung2.setText("")
        edidUser.setText("")
        edNoBuku2.setText("")
        txPinjam.setText("")
        txKembali.setText("")
    }

    val btnDeleteDialog = DialogInterface.OnClickListener { dialog, which ->
        deleteDataPeminjaman(noPeminjaman)
        edIdpengunjung2.setText("")
        edidUser.setText("")
        edNoBuku2.setText("")
        txPinjam.setText("")
        txKembali.setText("")
    }


}