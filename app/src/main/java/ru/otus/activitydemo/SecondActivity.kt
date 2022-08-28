package ru.otus.activitydemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class SecondActivity : AppCompatActivity() {

    companion object {
        const val EXT_RESULT_KEY = "ext_result_key"
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            listenLocation()
        } else {
            requestPermissionWithRationale(findViewById<Button>(R.id.request_permission_btn))
        }
    }

    private val launcherMultiple = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.all { it }) {
            listenLocation()
        } else {
            requestPermissionWithRationale(findViewById<Button>(R.id.request_permission_btn))
        }

        when {
            map[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                // do with location
            }
            map[Manifest.permission.READ_CONTACTS] == true -> {
                // do with contacts
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        findViewById<Button>(R.id.ok_btn).setOnClickListener {
            returnResult()
        }
        findViewById<Button>(R.id.request_permission_btn).setOnClickListener {
            requestPermissionWithRationale(it)
        }

    }

    private fun returnResult() {
        val result = Intent()
            .putExtra(EXT_RESULT_KEY, "This is result")

        setResult(RESULT_OK, result)
    }

    private fun listenLocation() {
        Toast.makeText(this, "Now I see you", Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission() {
        launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        launcherMultiple.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun requestPermissionWithRationale(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                listenLocation()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                // show dialog, invoke requestPermission() when confirm
                showInfoDialog(view)
            }
            else -> requestPermission()
        }
    }

    private fun showInfoDialog(view: View) {
        Snackbar.make(view, "Please, I need permission", Snackbar.LENGTH_LONG)
            .setAction("OKAY") { requestPermission() }
            .show()
    }
}