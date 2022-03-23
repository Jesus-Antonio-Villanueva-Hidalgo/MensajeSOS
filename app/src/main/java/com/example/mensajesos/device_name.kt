package com.example.mensajesos

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class device_name : AppCompatActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_name)

        var listview = findViewById<ListView>(R.id.disp)
        var arraylDispositives = mutableListOf<String>()

        val blue = BluetoothJhr(this, listview, MainActivity::class.java)//ActBluethooth::class.java


        blue.onBluetooth()

        val bAdapter = BluetoothAdapter.getDefaultAdapter()

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
                    var arrayadapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arraylDispositives)
                    listview.adapter = arrayadapter
                }
                else {
                    // In case no device is found
                    Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
                }
            }
        }

        listview.setOnItemClickListener{adapterView, view, i, l ->
            blue.bluetoothSeleccion(i)
        }

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