package com.example.mensajesos

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class ConnectThread:Thread() {
    private val DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val TAG = "Connect Thread"

    private var mmAdapter: BluetoothAdapter? = null
    var mmSocket: BluetoothSocket? = null
    private var mmDevice: BluetoothDevice? = null

    @SuppressLint("MissingPermission")
    fun ConnectThread(device: BluetoothDevice, adapter: BluetoothAdapter?) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        var tmp: BluetoothSocket? = null
        mmAdapter = adapter
        mmDevice = device

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            if (mmDevice != null) {
                Log.i(TAG, "Device Name: " + mmDevice!!.getName())
                Log.i(TAG, "Device UUID: " + mmDevice!!.getUuids()[0].uuid)
                tmp = device.createRfcommSocketToServiceRecord(mmDevice!!.getUuids()[0].uuid)
            } else Log.d(TAG, "Device is null.")
        } catch (e: NullPointerException) {
            Log.d(TAG, " UUID from device is null, Using Default UUID, Device name: " + device.name)
            try {
                tmp = device.createRfcommSocketToServiceRecord(DEFAULT_UUID)
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        } catch (e: IOException) {
        }
        mmSocket = tmp
    }

    @SuppressLint("MissingPermission")
    override fun run() {
        // Cancel discovery because it will slow down the connection
        if (mmAdapter!!.isDiscovering) {
            mmAdapter!!.cancelDiscovery()
        }
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket!!.connect()
        } catch (connectException: IOException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket!!.close()
            } catch (closeException: IOException) {
            }
            return
        }
    }

    /** Will cancel an in-progress connection, and close the socket  */
    fun cancel() {
        try {
            mmSocket!!.close()
        } catch (e: IOException) {
        }
    }
}