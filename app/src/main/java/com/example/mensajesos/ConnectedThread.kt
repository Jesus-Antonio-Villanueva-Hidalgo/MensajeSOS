package com.example.mensajesos

import android.bluetooth.BluetoothSocket
import android.os.Handler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectedThread:Thread() {
    private val MESSAGE_READ = 1
    private var parentHandler: Handler? = null

    private var mmSocket: BluetoothSocket? = null
    private var mmInStream: InputStream? = null
    private var mmOutStream: OutputStream? = null

    fun ConnectedThread(mHandler: Handler?, socket: BluetoothSocket) {
        parentHandler = mHandler
        mmSocket = socket
        var tmpIn: InputStream? = null
        var tmpOut: OutputStream? = null

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.inputStream
            tmpOut = socket.outputStream
        } catch (e: IOException) {
        }
        mmInStream = tmpIn
        mmOutStream = tmpOut
    }

    override fun run() {
        val buffer = ByteArray(1024) // buffer store for the stream
        var bytes = 0 // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                // Do it in this manner so no bytes go missing
                // Arduino serial monitor appends /n or /r so we can make use of those
                // Otherwise, append a special character available in ACSII to do the same
                buffer[bytes] = mmInStream!!.read().toByte()
                //if (buffer[bytes] == "\n" || buffer[bytes] == '\r')
                if(buffer[bytes].equals('\n') || buffer[bytes].equals('\r')){
                    // Send the obtained bytes to the UI activity
                    parentHandler!!.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget()
                    bytes = 0
                } else {
                    bytes++
                }
            } catch (e: IOException) {
                break
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    fun write(bytes: ByteArray?) {
        try {
            mmOutStream!!.write(bytes)
            mmOutStream!!.flush()
        } catch (e: IOException) {
        }
    }

    /* Call this from the main activity to shutdown the connection */
    fun cancel() {
        try {
            mmSocket!!.close()
        } catch (e: IOException) {
        }
    }
}