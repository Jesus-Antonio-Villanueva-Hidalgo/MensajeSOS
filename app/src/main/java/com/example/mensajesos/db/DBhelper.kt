package com.example.mensajesos.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class DBhelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    val cont = context

   companion object{
        private var DATABASE_VERSION:Int = 1
        private var DATABASE_NAME:String = "soscontactos.db"
        var TABLE_CONTACTOS:String = "contactos"
        var TABLE_MENSAJE:String = "mensaje"
       private val ID = "id"
       private val TELEFONO = "numerotelefonico"
       private val MENSAJE = "mensaje"
       val ID_SMS = "id"
    }


    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE "+ TABLE_CONTACTOS+ "(" +
        "$ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
        "$TELEFONO TEXT NOT NULL"+")" )

        p0?.execSQL("CREATE TABLE "+ TABLE_MENSAJE+ "("+
        "$ID_SMS INTEGER PRIMARY KEY,"+
        "$MENSAJE TEXT NOT NULL" + ")" )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS" + TABLE_CONTACTOS)
        p0?.execSQL("DROP TABLE IF EXISTS" + TABLE_MENSAJE)
        onCreate(p0)
    }

}