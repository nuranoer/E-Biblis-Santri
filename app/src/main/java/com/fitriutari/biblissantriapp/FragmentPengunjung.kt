package com.fitriutari.biblissantriapp

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CursorAdapter
import android.widget.ListAdapter
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.frag_data_pengunjung.*
import kotlinx.android.synthetic.main.frag_data_pengunjung.view.*

class FragmentPengunjung : Fragment(), View.OnClickListener {

    //Mengatur action on click pada button
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnInsert -> {
                builder.setTitle("Konfirmasi").setMessage("Apakah data sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnInsertDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnUpdate -> {
                builder.setTitle("Konfirmasi").setMessage("Apakah data sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnUpdateDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnHapus -> {
                builder.setTitle("Konfirmasi").setMessage("Yakin akan menghapus data ini?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnDeleteDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
        }
    }

    //1. Mmebuat variabel lateinit var, var
    lateinit var thisParent : DashboardActivity
    lateinit var db : SQLiteDatabase
    lateinit var adapter : ListAdapter
    lateinit var v : View
    lateinit var builder: AlertDialog.Builder
    var idPengunjung : String = " "
    var var1 : String = ""
    var var2 : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as DashboardActivity
        db = thisParent.getDbObject()

        v = inflater.inflate(R.layout.frag_data_pengunjung,container,false)
        v.btnInsert.setOnClickListener(this)
        v.btnUpdate.setOnClickListener(this)
        v.btnHapus.setOnClickListener(this)
        builder = AlertDialog.Builder(thisParent)
        v.lvPengunjung.setOnItemClickListener(itemClick)
        return v

        rg1.setOnCheckedChangeListener {group, checkId ->
            when(checkId) {
                R.id.rbSantri -> var1 = "SD"
                R.id.rbUmum -> var1 = "SMP"

            }
        }
    }
//Menampilkan data pengunjung
    fun showDataPengunjung() {
        val cursor : Cursor = db.query("pengunjung", arrayOf("nama_pengunjung", "alamat_pengunjung", "no_hp", "jenis_kunjungan", "id_pengunjung as _id"),
            null, null, null, null, "nama_pengunjung asc")
        adapter = SimpleCursorAdapter(thisParent, R.layout.item_data_pengunjung,cursor,
            arrayOf("_id","nama_pengunjung","alamat_pengunjung","no_hp","jenis_kunjungan"), intArrayOf(R.id.itemIdPengunjung,R.id.itemNama,R.id.itemAlamat, R.id.itemNoHp, R.id.itemJenis),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        v.lvPengunjung.adapter = adapter
    }
//Menjalankan function showdatapengunjung
    override fun onStart() {
        super.onStart()
        showDataPengunjung()
    }

    val itemClick = AdapterView.OnItemClickListener { parent, view, position, id ->
        val c : Cursor = parent.adapter.getItem(position) as Cursor
        idPengunjung = c.getString(c.getColumnIndexOrThrow("_id"))
        v.edNamaPengunjung.setText(c.getString(c.getColumnIndexOrThrow("nama_pengunjung")))
        v.edAlamat.setText(c.getString(c.getColumnIndexOrThrow("alamat_pengunjung")))
        v.edNoHP.setText(c.getString(c.getColumnIndexOrThrow("no_hp")))
    }

    fun insertDataPengunjung(namaPengunjung : String, alamat : String, noHp : String, jenisKunjungan : String) {
        var cv : ContentValues = ContentValues()
        cv.put("nama_pengunjung",namaPengunjung)
        cv.put("alamat_pengunjung",alamat)
        cv.put("no_hp",noHp)
        cv.put("jenis_kunjungan",jenisKunjungan)
        db.insert("pengunjung",null,cv)
        showDataPengunjung()
    }

    fun updateDataPengunjung(noHp: String, idPengunjung : String) {
        var cv : ContentValues = ContentValues()
        cv.put("no_hp",noHp)
        db.update("pengunjung",cv,"id_pengunjung = $idPengunjung",null)
        showDataPengunjung()
    }

    fun deleteDataPengunjung(idPengunjung : String) {
        db.delete("pengunjung","id_pengunjung = $idPengunjung",null)
        showDataPengunjung()
    }

    val btnInsertDialog = DialogInterface.OnClickListener { dialog, which ->
        insertDataPengunjung(v.edNamaPengunjung.text.toString(), v.edAlamat.text.toString(), v.edNoHP.text.toString(),v.rg1.toString())
        v.edNamaPengunjung.setText("")
        v.edAlamat.setText("")
        v.edNoHP.setText("")

    }

    val btnUpdateDialog = DialogInterface.OnClickListener { dialog, which ->
        updateDataPengunjung(v.edNoHP.text.toString(),v.edIdPengunjung.text.toString())
        v.edIdPengunjung.setText("")
        v.edNoHP.setText("")

    }

    val btnDeleteDialog = DialogInterface.OnClickListener { dialog, which ->
        deleteDataPengunjung(idPengunjung)
        v.edNamaPengunjung.setText("")
        v.edAlamat.setText("")
        v.edNoHP.setText("")
    }
}