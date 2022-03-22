package com.example.mensajesos

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import java.util.*
import kotlin.concurrent.thread


class ActBluethooth : AppCompatActivity() {

    lateinit var blue :BluetoothJhr
    var initConexion = false
    var offHilo = false


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_bluethooth)

        //var btnConectar = findViewById<Button>(R.id.btnConectar)
        //var btnSalir = findViewById<Button>(R.id.btnSalir)
        //var arraylDispositives = arrayListOf<String>()
        var arraylDispositives = mutableListOf<String>()
        var arrayadapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraylDispositives)
        var txtShow = findViewById<TextView>(R.id.txtShowMessage)
        val btnApplication = findViewById<Button>(R.id.btnGoApplication)

        var btnSend = findViewById<Button>(R.id.btnSend)

        //val bAdapter = BluetoothAdapter.getDefaultAdapter()

        //val blue = BluetoothJhr(this, lstvBluetooth, ActBluethooth::class.java)
        //blue.onBluetooth()
        blue = BluetoothJhr(this, device_name::class.java)
        //si se pierde conexion no sale sino que avisa con un mensaje  error
        blue.exitErrorOk(true)
        //mensaje conectado
        blue.mensajeConexion("Conectado jhr")
        //mensaje de error
        blue.mensajeErrorTx("algo salio mal")

        thread(start = true){
            while (!initConexion && !offHilo){
                Thread.sleep(500)
            }

            while (!offHilo){
                var mensaje = blue.mRx()
                if (mensaje != ""){
                    txtShow.post {

                        txtShow.text = mensaje

                    }
                }
                Thread.sleep(100)
            }

        }

        btnApplication.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        /*txtShow.doOnTextChanged { text, start, before, count ->
            var btnSend = findViewById<Button>(R.id.btnSend)
            btnSend.performClick()
            //btnSend.callOnClick()
        }*/
        txtShow.doAfterTextChanged {
            btnSend.performClick()
        }

       /* btnSelectDispositive.setOnClickListener {
            arraylDispositives.removeAll(arraylDispositives)
            // Checks if Bluetooth Adapter is present
            if (bAdapter == null) {
                Toast.makeText(applicationContext, "Bluetooth no soportado en este dispositivo", Toast.LENGTH_SHORT).show()
            } else {
                if(!bAdapter.isEnabled){
                    var enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                    startActivity(enableIntent)

                    var enable = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enable,0)
                }else{
                    // Arraylist of all the bonded (paired) devices
                    var pairedDevices = bAdapter.bondedDevices
                    if (pairedDevices.size > 0) {
                        for (device in pairedDevices) {
                            // get the device name
                            var deviceName:String = device.name
                            // get the mac address
                            var macAddress = device.address
                            arraylDispositives.add(macAddress+"   "+deviceName)
                            //arraylDispositives.add(ui.toString())
                        }
                        //var arrayadapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arraylDispositives)
                        lstvBluetooth.adapter = arrayadapter
                    }
                    else {
                        // In case no device is found
                        Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }*/

        //lstvBluetooth.setOnItemClickListener { adapterView, view, i, l ->
            //var m = arraylDispositives[i]
            //txtShow.text = arraylDispositives[i]

            //txtShow.text = lstvBluetooth.getItemAtPosition(i) as String
            //blue.bluetoothSeleccion(i)
        //}

        //btnConectar.setOnClickListener {
            //var aux = txtShow.text.toString().substring(0,16)
            //lateinit var aux2:BluetoothDevice
            /*for(device in bAdapter.bondedDevices){
                if(aux == device.address){
                    aux2 = device
                }
            }*/


            //txtShow.setText("Establishing connection...")
            //lateinit var conect:ConnectThread
            //conect.ConnectThread(bluetoothDevice, bluetoothAdapter)
            //var connectBluetooth = ConnectThread(bluetoothDevice, bluetoothAdapter)
            //conect.start()
            //connectBluetooth.start()
            //var thread: ConnectThread = ConnectThread(aux2)
            //thread.start()
            //txtShow.text = "CONECTADO"


            //mmSocket2

        //}

    }

    override fun onResume() {
        initConexion =  blue.conectaBluetooth()
        super.onResume()
    }

    override fun onPause() {
        offHilo = true
        blue.exitConexion()
        super.onPause()
    }
    @Override
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            var builder = AlertDialog.Builder(this)
            builder.setMessage("¿Desea salir de Be Safe?")
            builder.setPositiveButton(android.R.string.ok){
                    dialog, which ->
                var intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            builder.setNegativeButton("Cancelar",null)
            builder.show()
        }
        return super.onKeyDown(keyCode, event)
    }
}