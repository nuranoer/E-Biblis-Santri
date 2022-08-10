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
import kotlinx.android.synthetic.main.frag_data_buku.view.*
import java.time.Year

class FragmentBuku : Fragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnInsert2 -> {
                builder.setTitle("Konfirmasi").setMessage("Apakah data sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnInsertDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnUpdate2 -> {
                builder.setTitle("Konfirmasi").setMessage("Apakah data sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnUpdateDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
            R.id.btnDelete2 -> {
                builder.setTitle("Konfirmasi").setMessage("Yakin akan menghapus data ini?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya",btnDeleteDialog)
                    .setNegativeButton("Tidak",null)
                builder.show()
            }
        }
    }

    lateinit var thisParent : DashboardActivity
    lateinit var db : SQLiteDatabase
    lateinit var adapter : ListAdapter
    lateinit var v : View
    lateinit var builder: AlertDialog.Builder
    var noBuku : String = " "

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as DashboardActivity
        db = thisParent.getDbObject()

        v = inflater.inflate(R.layout.frag_data_buku,container,false)
        v.btnInsert2.setOnClickListener(this)
        v.btnUpdate2.setOnClickListener(this)
        v.btnDelete2.setOnClickListener(this)
        builder = AlertDialog.Builder(thisParent)
        v.lvBuku.setOnItemClickListener(itemClick)
        return v
    }

    fun showDataBuku() {
        val cursor : Cursor = db.query("buku", arrayOf("judul", "pengarang", "penerbit", "tahun_terbit", "no_buku as _id"),
            null, null, null, null, "judul asc")
        adapter = SimpleCursorAdapter(thisParent, R.layout.item_data_buku,cursor,
            arrayOf("_id","judul","pengarang","penerbit","tahun_terbit"), intArrayOf(R.id.itemNoBuku,R.id.itemJudul,R.id.itemPengarang, R.id.itemPenerbit, R.id.itemTahun),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        v.lvBuku.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        showDataBuku()
    }

    val itemClick = AdapterView.OnItemClickListener { parent, view, position, id ->
        val c : Cursor = parent.adapter.getItem(position) as Cursor
        noBuku = c.getString(c.getColumnIndexOrThrow("_id"))
        v.edNamaBuku.setText(c.getString(c.getColumnIndexOrThrow("judul")))
        v.edPengarang.setText(c.getString(c.getColumnIndexOrThrow("pengarang")))
        v.edPenerbit.setText(c.getString(c.getColumnIndexOrThrow("penerbit")))
        v.edTahunTerbit.setText(c.getString(c.getColumnIndexOrThrow("tahun_terbit")))
    }

    fun insertDataBuku(namaBuku : String, pengarang : String, penerbit : String, tahunTerbit : String) {
        var cv : ContentValues = ContentValues()
        cv.put("judul",namaBuku)
        cv.put("pengarang",pengarang)
        cv.put("penerbit",penerbit)
        cv.put("tahun_terbit",tahunTerbit)
        db.insert("buku",null,cv)
        showDataBuku()
    }

    fun updateDataBuku(namaBuku : String, pengarang : String, penerbit : String, tahunTerbit: String) {
        var cv : ContentValues = ContentValues()
        cv.put("judul",namaBuku)
        cv.put("pengarang",pengarang)
        cv.put("penerbit",penerbit)
        cv.put("tahun_terbit",tahunTerbit)
        db.update("buku",cv,"no_buku = $noBuku",null)
        showDataBuku()
    }

    fun deleteDataBuku(noBuku: String) {
        db.delete("buku","no_buku = $noBuku",null)
        showDataBuku()
    }

    val btnInsertDialog = DialogInterface.OnClickListener { dialog, which ->
        insertDataBuku(v.edNamaBuku.text.toString(), v.edPengarang.text.toString(), v.edPenerbit.text.toString(),v.edTahunTerbit.text.toString())
        v.edNamaBuku.setText("")
        v.edPengarang.setText("")
        v.edPenerbit.setText("")
        v.edTahunTerbit.setText("")
    }

    val btnUpdateDialog = DialogInterface.OnClickListener { dialog, which ->
        updateDataBuku(v.edNamaBuku.text.toString(), v.edPengarang.text.toString(), v.edPenerbit.text.toString(),v.edTahunTerbit.text.toString())
        v.edNamaBuku.setText("")
        v.edPengarang.setText("")
        v.edPenerbit.setText("")
        v.edTahunTerbit.setText("")

    }

    val btnDeleteDialog = DialogInterface.OnClickListener { dialog, which ->
        deleteDataBuku(noBuku)
        v.edNamaBuku.setText("")
        v.edPengarang.setText("")
        v.edPenerbit.setText("")
        v.edTahunTerbit.setText("")
    }
}