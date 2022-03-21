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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.thread


class ActBluethooth : AppCompatActivity() {

   /*var mmSocket: BluetoothSocket? = null
    var connectedThread: ConnectedThread? = null
    var createConnectThread: CreateConnectThread? = null*/
    lateinit var handler:Handler

   private val CONNECTING_STATUS: Int = 1 // used in bluetooth handler to identify message status

    private val MESSAGE_READ = 2 // used in bluetooth handler to identify message update

    lateinit var mmSocket2: BluetoothSocket

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothDevice: BluetoothDevice
    private lateinit var connectBluetooth:ConnectThread

    private var btPairedDevice: ArrayAdapter<String>? = null
    private var btArrayAdapter: ArrayAdapter<String>? = null

    private val enableBluetooth: Button? = null
    private  var connectSystem:android.widget.Button? = null


    lateinit var blue :BluetoothJhr
    var initConexion = false
    var offHilo = false


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_bluethooth)

        //var btnON = findViewById<Button>(R.id.btnON)
        //var btnOFF = findViewById<Button>(R.id.btnOFF)
        //var btnSelectDispositive = findViewById<Button>(R.id.btnSelectDispositive)
        //var btnConectar = findViewById<Button>(R.id.btnConectar)
        var btnSalir = findViewById<Button>(R.id.btnSalir)
        //var lstvBluetooth = findViewById<ListView>(R.id.lvDispositives)
        //var arraylDispositives = arrayListOf<String>()
        var arraylDispositives = mutableListOf<String>()
        var arrayadapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraylDispositives)
        var txtShow = findViewById<TextView>(R.id.txtShowMessage)
        val btnApplication = findViewById<Button>(R.id.btnGoApplication)

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
                if (mensaje!=""){
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


        /*handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    CONNECTING_STATUS -> when (msg.arg1) {
                        1 -> {
                        }
                        -1 -> {
                        }
                    }
                    MESSAGE_READ -> {
                        val arduinoMsg: String = msg.obj.toString() // Read message from Arduino
                        when (arduinoMsg.lowercase()) {
                            "led is turned on" -> {
                                //imageView.setBackgroundColor(resources.getColor(R.color.colorOn))
                                txtShow.setText("Arduino Message : $arduinoMsg")
                            }
                            "led is turned off" -> {
                                //imageView.setBackgroundColor(resources.getColor(R.color.colorOff))
                                txtShow.setText("Arduino Message : $arduinoMsg")
                            }
                        }
                    }
                }
            }
        }*/


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


   /*@SuppressLint("MissingPermission")
   private class CreateConnectThread(bluetoothAdapter: BluetoothAdapter, address: String?) : Thread() {
        @SuppressLint("MissingPermission")
        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            bluetoothAdapter.cancelDiscovery()
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect()
                Log.e("Status", "Device connected")
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget()
            } catch (connectException: IOException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close()
                    Log.e("Status", "Cannot connect to device")
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget()
                } catch (closeException: IOException) {
                    Log.e(TAG, "Could not close the client socket", closeException)
                }
                return
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = ConnectedThread(mmSocket)
            connectedThread.run()
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }

        init {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            val bluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
            var tmp: BluetoothSocket? = null
            val uuid = bluetoothDevice.uuids[0].uuid
            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid)
            } catch (e: IOException) {
                Log.e(TAG, "Socket's create() method failed", e)
            }
            mmSocket = tmp
        }
    }*/


    private val ActionFoundReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                btArrayAdapter?.add(
                    """
                    ${device!!.name}
                    ${device.address}
                    """.trimIndent()
                )
                btArrayAdapter?.notifyDataSetChanged()

                if (device?.name?.matches("HC 05" as Regex) == true) {
                    bluetoothDevice = device
                    connectSystem?.setEnabled(true)
                    bluetoothAdapter.cancelDiscovery()
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                //scanBluetooth.setChecked(false)
                //statusText.setText("Bluetooth has ended discovery process.")
                Toast.makeText(applicationContext, "Bluetooth has ended discovery process.", Toast.LENGTH_SHORT).show()
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                if (bluetoothAdapter.isEnabled) {
                    enableBluetooth?.setEnabled(false)
                    //scanBluetooth.setEnabled(true)
                   //statusText.setText("Bluetooth is Enabled.")
                    Toast.makeText(applicationContext, "Bluetooth is Enabled.", Toast.LENGTH_SHORT).show()
                } else {
                    enableBluetooth?.setEnabled(true)
                    //scanBluetooth.setEnabled(false)
                    connectSystem?.setEnabled(false)
                    //statusText.setText("Bluetooth is Disabled.")
                    Toast.makeText(applicationContext, "Bluetooth is Disabled.", Toast.LENGTH_SHORT).show()
                }
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
                //statusText.setText("Connected to Plant Watering System")
                if (connectBluetooth.mmSocket != null) {
                    Log.d("Socket", "Successfully created!")
                    //SocketSingleton.setSocket(connectBluetooth.mmSocket)
                    //startActivity(Intent(this, PlantWateringActivity::class.java))
                    Toast.makeText(applicationContext, "Bluetooth is Enabled.", Toast.LENGTH_SHORT).show()
                }
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {
                //statusText.setText("Disconnected from Plant Watering System")
                Toast.makeText(applicationContext, "Disconnected from Plant Watering System", Toast.LENGTH_SHORT).show()
            }
        }
    }



   /* @SuppressLint("MissingPermission")
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

       private val mmSocket2: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            val UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            device.createRfcommSocketToServiceRecord(UUID)
        }
        lateinit var bluetoothAdapter2: BluetoothAdapter

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter2?.cancelDiscovery()

            try {
                mmSocket2?.connect()
            } catch (connectException: IOException) {
                try {
                    mmSocket2?.close();
                } catch (closeException: IOException) {

                }
                return;
            }
            // Closes the client socket and causes the thread to finish.
            fun cancel() {
                try {
                    mmSocket2?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Could not close the client socket", e)
                }
            }
        }

    }*/

}