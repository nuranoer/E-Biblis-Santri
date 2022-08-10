package com.fitriutari.biblissantriapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper (context: Context):SQLiteOpenHelper(context,DB_Name,null,DB_Ver) {
//Ini adalah database dari project yang kami buat
    override fun onCreate(db: SQLiteDatabase?) {
        val tAkun = "create table akun(id_user text primary key, nama text not null, username text not null, password text not null)"
        val tPengunjung = "create table pengunjung(id_pengunjung integer  primary key autoincrement, nama_pengunjung text not null, alamat_pengunjung text not null, no_hp text not null, jenis_kunjungan text not null)"
        val tBuku = "create table buku(no_buku integer primary key autoincrement, judul text not null, pengarang text not null, penerbit text not null, tahun_terbit integer not null)"
        val tPeminjaman = "create table peminjaman(no_pinjam integer primary key autoincrement, id_pengunjung integer not null, id_user text not null, no_buku text not null, tgl_pinjam text not null, tgl_kembali text not null)"
        val tDenda = "create table denda(no_denda integer primary key autoincrement, no_pinjam integer not null, tgl_kembali date not null, jumlah_denda integer not null)"
        val insAkun = "insert into akun(id_user,nama,username,password) values('A1','Admin 1','admin1','admin1')"
        db?.execSQL(tAkun)
        db?.execSQL(tPengunjung)
        db?.execSQL(tBuku)
        db?.execSQL(tPeminjaman)
        db?.execSQL(tDenda)
        db?.execSQL(insAkun)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS akun")
    }
//nama database
    companion object {
        val DB_Name = "perpus"
        val DB_Ver = 1
    }

}