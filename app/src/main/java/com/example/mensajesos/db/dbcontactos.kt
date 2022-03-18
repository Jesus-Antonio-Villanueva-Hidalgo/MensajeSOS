package com.example.mensajesos.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import java.lang.Exception
import java.util.ArrayList

class dbcontactos(context: Context?): DBhelper(context) {

    //val cont = context

    companion object{
        private val id = "id"
        private val numerotelefonico = "numerotelefonico"
        private val mensaje = "mensaje"
    }

   open fun insertcontacts(numero: String):Long{
        var id:Long = 0
       try{
           val dbhelper:DBhelper = DBhelper(cont)
           val db:SQLiteDatabase = dbhelper.writableDatabase

           var cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_CONTACTOS WHERE numerotelefonico=$numero",null)
           if(cursor.moveToNext()){
               Toast.makeText(cont,"El numero: $numero ya esta guardado",Toast.LENGTH_SHORT).show()
           }else{
               var content:ContentValues = ContentValues()
               content.put("numerotelefonico",numero)
               id = db.insert(TABLE_CONTACTOS,null, content)
           }
           cursor.close() //Cerramos nuestro cursor
           db.close() //Cerramos nuestro db
       }catch (ex: Exception){
           ex.toString()
       }

       return id
    }

    open fun insertsms(idsms:Int, mensaje: String):Long {
        var id:Long = 0
        val dbhelper:DBhelper = DBhelper(cont)
        val db:SQLiteDatabase = dbhelper.writableDatabase
        try{

            var content:ContentValues = ContentValues()
            content.put("id", idsms)
            content.put("mensaje", mensaje)

            id= db.insert(TABLE_MENSAJE ,null,content)
        }catch (ex: Exception){
            ex.toString()
        }
        db.close()

        return id
    }

    @SuppressLint("Range")
    open fun readcontacts():ArrayList<String>{
        val dbhelper = DBhelper(cont)
        val db:SQLiteDatabase = dbhelper.writableDatabase
        var listContacts = arrayListOf<String>()
        //var contact:Contactos?
        var cursor:Cursor?
        var phone:String
        try{
            cursor = db.rawQuery("SELECT numerotelefonico FROM $TABLE_CONTACTOS",null)
            if(cursor.moveToFirst()){
               do{
                   //contact = Contactos()
                   //contact.setphone(cursor.getString(0))
                   //listContacts.add(contact)
                   phone = cursor.getString(cursor.getColumnIndex("numerotelefonico"))
                   listContacts.add(phone)
                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch(ex: Exception){
            ex.toString()
        }
        db.close()
        return listContacts
    }


    @SuppressLint("Range")
    open fun readmessage():ArrayList<String>{
        val dbhelper = DBhelper(cont)
        val db:SQLiteDatabase = dbhelper.writableDatabase
        var listmessage = arrayListOf<String>()
        //var contact:Contactos?
        var cursor: Cursor?
        var sms:String
        try{
            cursor = db.rawQuery("SELECT mensaje FROM $TABLE_MENSAJE WHERE id=1",null)
            if(cursor.moveToFirst()){
                do{
                    sms = cursor.getString(cursor.getColumnIndex("mensaje"))
                    listmessage.add(sms)
                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch(ex: Exception){
            ex.toString()
        }
        db.close()
        return listmessage
    }

    @SuppressLint("Range")
    fun updatemessage(idsms: Int, mensaje: String): Long{
        var id:Long = 0
        val dbhelper = DBhelper(cont)
        val db:SQLiteDatabase = dbhelper.writableDatabase
        var content:ContentValues = ContentValues()
        content.put("id",idsms)
        content.put("mensaje",mensaje)
        id = db.update(TABLE_MENSAJE, content, "id=1",null).toLong()
        db.close()

        return id
        /*val cursor = db.rawQuery("SELECT mensaje FROM $TABLE_MENSAJE WHERE id=1 LIMIT 1",null)
        var sms = ""
        if(cursor.moveToFirst()){
            sms = cursor.getString(cursor.getColumnIndex("mensaje"))
        }
        cursor.close()
        return sms*/
    }

}