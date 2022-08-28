package ru.otus.activitydemo

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.Toast

import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible


private const val PERMISSION_REQUEST_CODE = 101
private const val RESULT_REQUEST_CODE = 102

class MainActivity : AppCompatActivity() {

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        object : ActivityResultCallback<ActivityResult?> {
            override fun onActivityResult(result: ActivityResult?) {
                result ?: return
                if (result.resultCode == Activity.RESULT_OK) {
                    handleResult(result.data)
                    Snackbar.make(
                        findViewById<Button>(R.id.next2_btn),
                        "Превосходно",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.request_permission_btn).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showContacts()
            } else {
                requestPermissionWithRationale(it)
            }
        }

        findViewById<Button>(R.id.settings_btn).setOnClickListener {
            openApplicationSettings()
        }

        findViewById<Button>(R.id.next_btn).setOnClickListener {
            startActivityForResult(
                Intent(this, SecondActivity::class.java),
                RESULT_REQUEST_CODE
            )
        }

        findViewById<Button>(R.id.next2_btn).setOnClickListener {
            launcher.launch(Intent(this, SecondActivity::class.java))
        }
    }

    private fun showContacts() {
        Toast.makeText(this, "Show Contacts", Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun requestPermissionWithRationale(view: View) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_CONTACTS
            )
        ) {
            // show dialog, invoke requestPermission() when confirm
            Snackbar.make(view, "Please, I need permission", Snackbar.LENGTH_LONG)
                .setAction("OKAY") { requestPermission() }
                .show()
        } else {
            requestPermission()
        }
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isEmpty()) {
                // Request is canceled
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                handleResult(data)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleResult(data: Intent?) {
        findViewById<Button>(R.id.next2_btn).isVisible = true
    }

}