package com.example.flashlight

import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.getSystemService
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var cameraFlash = false
    var flashOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toggelButton = findViewById<ToggleButton>(R.id.tbOnOff)
        cameraFlash = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        try {
            // Get the first camera id (usually rear camera)
            val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
            var cameraId : String
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            showMessage("Error accessing camera: ${e.message}")
            toggelButton.isEnabled = false
            return
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
            showMessage("No camera available.")
            toggelButton.isEnabled = false
            return
        }

        toggelButton.setOnClickListener {
            if(cameraFlash){
                if(flashOn){
                    flashOn = false
                    flashLightOff()
                }
                else{
                    flashOn = true
                    flashLightOn()
                }
            }
        }
    }

    private fun flashLightOff() {
        try{
            val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
            var cameraId : String
            cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, false)
        }catch(e: CameraAccessException) {
            e.printStackTrace()
        }
        catch (e: Exception) {
            e.printStackTrace()
            showMessage("Error toggling flashlight: ${e.message}")
        }
    }

    private fun flashLightOn() {
        try{
            val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
            var cameraId : String
            cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, true)
        }catch(e: CameraAccessException) {
            e.printStackTrace()
            showMessage("Error accessing camera: ${e.message}")
        }
        catch (e: Exception) {
            e.printStackTrace()
            showMessage("Error toggling flashlight: ${e.message}")
        }
    }

    override fun onPause() {
        super.onPause()
        if (flashOn) {
            try{
                val toggelButton = findViewById<ToggleButton>(R.id.tbOnOff)
                val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                var cameraId : String
                cameraId = cameraManager.cameraIdList[0]
                cameraManager.setTorchMode(cameraId, false)
            }catch(e: Exception) {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (flashOn) {
            try{
                val toggelButton = findViewById<ToggleButton>(R.id.tbOnOff)
                val cameraManager: CameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                var cameraId : String
                cameraId = cameraManager.cameraIdList[0]
                cameraManager.setTorchMode(cameraId, false)
            }catch(e: Exception) {}
        }
    }

    private fun showMessage(message: String) {
        val tvMessage = findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = message
    }
}