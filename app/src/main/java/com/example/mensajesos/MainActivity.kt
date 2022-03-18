package com.example.mensajesos

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import android.provider.Settings
import android.telephony.SmsManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mensajesos.databinding.ActivityMainBinding
import com.example.mensajesos.db.dbcontactos
import com.google.android.gms.location.*


class MainActivity : AppCompatActivity() {
    //private val locationPermissionCode = 2

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PERMISSION_ID = 42
    var REQUEST_READ_CONTACT = 79
    //var listContact = arrayListOf<String>()
    var listContact = mutableListOf<String>()
    var arraylistcontacts = ArrayList<String>()
    var flag = 0

    // Iniciación tardía del viewBinding
    lateinit var binding : ActivityMainBinding

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
       binding = ActivityMainBinding.inflate(layoutInflater)
       setContentView(binding.root)

      if (allPermissionsGrantedGPS()){
           mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
       } else {
           // Si no hay permisos solicitarlos al usuario.
           ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
       }

        var btnSend=findViewById<Button>(R.id.btnSend)
        var etmMessage=findViewById<EditText>(R.id.etmMessage)
        var txtlatitude=findViewById<TextView>(R.id.lbllatitud)
        var txtlongitude=findViewById<TextView>(R.id.lbllongitud)
        var btnSelectContact=findViewById<Button>(R.id.btnSelectContact)
        var spinner=findViewById<Spinner>(R.id.spinnerShow)
        var dataAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listContact)
        var btnSave = findViewById<Button>(R.id.btnSave)

       var spinner2=findViewById<Spinner>(R.id.spinnerinsert)
       var dataAdapter2 = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listContact)


       fun myMessage() {
            val myMsg: String = etmMessage.text.toString().trim()

            if (myMsg == "" || arraylistcontacts.size == 0) {//arraylistcontacts.size == 0
                Toast.makeText(this, "Seleccione al menos un contacto y no deje vacio el campo de mensaje", Toast.LENGTH_SHORT).show()
            }
            /*else if(arraylistcontacts == null){
                Toast.makeText(this, "Por favor seleccione al menos un contacto", Toast.LENGTH_SHORT).show()
            }*/
            else {
                for(i in arraylistcontacts){//listcontact
                    var aux:String = "https://www.google.com/maps/place/"+txtlatitude.text+","+txtlongitude.text
                    val smsManager:SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage( i, null, myMsg +" "+aux, null, null)
                }
                Toast.makeText(this, "Mensaje Enviado", Toast.LENGTH_SHORT).show()
            }
        }

       fun actualizar(){
           var dbcontacts = dbcontactos(this)
           var listcontactAdaptaer = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbcontacts.readcontacts())

           var aux2 = ""
           arraylistcontacts.clear()
           for(i in 0 until listcontactAdaptaer.count){
               aux2 = listcontactAdaptaer.getItem(i) as String
               arraylistcontacts.add(aux2)
               println(aux2)
           }
           dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
           spinner.setAdapter(listcontactAdaptaer)
           dbcontacts.close()
       }

        btnSend.setOnClickListener {
            //Se verifica que se cuente con el permiso de envio de SMS
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                leerubicacionactual()
                myMessage()
            } else {
                val permissionrequest=101
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.SEND_SMS),
                    permissionrequest)
                Toast.makeText(this,"SE NECESITA EL PERMISO DE SMS PARA ENVIAR EL MENSAJE VIA SMS",Toast.LENGTH_LONG).show()
            }
            /*val dbhelper = DBhelper(this)
            val db = dbhelper.writableDatabase
            if(db != null){
                Toast.makeText(this, "BASE DE DATOS CREADA", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "ERROR ENCONTRADO", Toast.LENGTH_SHORT).show()
            }*/
        }

        btnSelectContact.setOnClickListener{
           // var int:Intent = Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI)
           val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
           if(permissionCheck == PackageManager.PERMISSION_GRANTED){
               var int=Intent(Intent.ACTION_PICK)
               int.setType(CommonDataKinds.Phone.CONTENT_TYPE)
               if (intent.resolveActivity(getPackageManager()) != null) {
                   startActivityForResult(int, 1)
               }
               //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
               //spinner.setAdapter(dataAdapter)

               dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
               spinner2.setAdapter(dataAdapter2)
           }
           else{
               ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_READ_CONTACT)
               Toast.makeText(this,"SE NECESITA EL PERMISO DE CONTACTOS PARA ELEGIR A QUIENES ENVIAR EL MENSAJE",Toast.LENGTH_LONG).show()
           }
       }

        btnSave.setOnClickListener{
            var dbcontacto = dbcontactos(this)
            var idContact:Long = 0
            var idSms: Long
            var verificationUpdate:Long
            if(listContact.size > 0){
                for(i in listContact){
                    idContact = dbcontacto.insertcontacts(i)
                }
                if(idContact > 0){
                    Toast.makeText(this,"CONTACTO GUARDADO",Toast.LENGTH_SHORT).show()
                    actualizar()
                   //istContact.clear()
                    //spinner2.adapter = dataAdapter2
                }else{
                    //Toast.makeText(this,"ERROR AL GUARDAR CONTACTO",Toast.LENGTH_SHORT).show()
                }
            }
            if(etmMessage.text.toString() != ""){
                /*if(flag == 1){
                    verificationUpdate = dbcontacto.updatemessage(1,etmMessage.text.toString())
                    if(verificationUpdate > 0){
                        Toast.makeText(this,"ACTUALIZACION DEL MENSAJE HECHA",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"ERROR AL ACTUALIZAR MENSAJE",Toast.LENGTH_SHORT).show()
                    }
                }
                if(flag == 0){
                    flag = 1
                    idSms = dbcontacto.insertsms(1, etmMessage.text.toString())
                    if(idSms > 0){
                        Toast.makeText(this,"MENSAJE GUARDADO",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"ERROR AL GUARDAR MENSAJE",Toast.LENGTH_SHORT).show()
                    }
                }*/

                var dbsms = dbcontactos(this)
                var verification = dbsms.readmessage()
                if(verification.size > 0){
                    verificationUpdate = dbcontacto.updatemessage(1,etmMessage.text.toString())
                    if(verificationUpdate > 0){
                        Toast.makeText(this,"ACTUALIZACION DEL MENSAJE HECHA",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"ERROR AL ACTUALIZAR MENSAJE",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    idSms = dbcontacto.insertsms(1, etmMessage.text.toString())
                    if(idSms > 0){
                        Toast.makeText(this,"MENSAJE GUARDADO",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"ERROR AL GUARDAR MENSAJE",Toast.LENGTH_SHORT).show()
                    }
                }
                dbsms.close()
            }else{
                Toast.makeText(this,"NO DEJE EL CAMPO DE MENSAJE VACIO POR FAVOR",Toast.LENGTH_SHORT).show()
            }
            //loadelements()
            //listContact.clear()
            listContact.removeAll(listContact)
        }

       var dbcontacts = dbcontactos(this)
       var listcontactAdaptaer = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbcontacts.readcontacts())
       dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
       spinner.setAdapter(listcontactAdaptaer)

       var aux2 = ""
       arraylistcontacts.clear()
       for(i in 0 until listcontactAdaptaer.count){
           aux2 = listcontactAdaptaer.getItem(i) as String
           //arraylistcontacts.set(i,aux2)

           arraylistcontacts.add(aux2)
           println(aux2)
           //listContact[i] = aux2
           //listContact.[i] = listcontactAdaptaer.getItem(i).toString()
           //listContact.set(i,listcontactAdaptaer.getItem(i).toString())
           //println(listContact[i])
       }

       var listsmsadapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbcontacts.readmessage())
       var aux = ""
       for(i in 0 .. listsmsadapter.count-1){
           aux += listsmsadapter.getItem(i)
       }
       etmMessage.setText(aux)
    }

    private fun allPermissionsGrantedGPS() = REQUIRED_PERMISSIONS_GPS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun leerubicacionactual(){
        if (checkPermissions()){
            if (isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.lastLocation.addOnCompleteListener(this){ task ->
                        var location: Location? = task.result
                        if (location == null){
                            requestNewLocationData()
                        } else {
                            binding.lbllatitud.text = location.latitude.toString()
                            binding.lbllongitud.text = location.longitude.toString()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Activar ubicación", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                this.finish()
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(){
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, Looper.myLooper())
    }

    private val mLocationCallBack = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation : Location = locationResult.lastLocation

            binding.lbllatitud.text = mLastLocation.latitude.toString()//Latitud
            binding.lbllongitud.text = mLastLocation.longitude.toString()//Longitud

        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    companion object{
        private val REQUIRED_PERMISSIONS_GPS= arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode== RESULT_OK){
            var uri = data?.data
            //intent.type = CommonDataKinds.Phone.CONTENT_TYPE
            val projection = arrayOf(CommonDataKinds.Phone.NUMBER)
            val cursor = contentResolver.query(uri!!, projection, null, null, null)

            if(cursor != null && cursor.moveToFirst()){
                //var indiceName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                var indiceNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                //var nombre:String = cursor.getString(indiceName)
                var number:String = cursor.getString(indiceNumber)
                number = number.replace("(","").replace(")","").replace(" ","").replace("-","")

                /*for(i in listContact){
                    if(i == number){

                    }else{
                        listContact.add(number)
                    }
                }*/

                listContact.add(number)

            }
            else{
                Toast.makeText(this, "PROBLEMA 3", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "NO SE SELECCICONO NINGUN CONTACTO", Toast.LENGTH_SHORT).show()
            loadelements()
        }
    }
    private fun loadelements(){
        var spinner=findViewById<Spinner>(R.id.spinnerShow)
        //var dataAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listContact)
        var dataAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arraylistcontacts)
        var dbcontacts = dbcontactos(this)
        var listcontactAdaptaer = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dbcontacts.readcontacts())
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(listcontactAdaptaer)
    }
}