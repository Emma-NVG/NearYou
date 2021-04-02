package com.example.nearyou.activity.fragment.scanqr

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.example.nearyou.R
import com.example.nearyou.databinding.FragmentScanQrBinding
import com.google.android.material.snackbar.Snackbar


class ScanQRCodeFragment : Fragment() {

    private var _binding: FragmentScanQrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var codeScanner: CodeScanner

    private var snackbarCameraPermission: Snackbar? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanQrBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)

        return root
    }

    override fun onStart() {
        super.onStart()

        tryStartScanning()
    }

    private fun tryStartScanning() {
        if (context != null && activity != null) {
            if (checkSelfPermission(context as Context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
                } else {
                    showSnackbar()
                }
            } else {
                startScanning()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if(::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }

    private fun startScanning() {
        // Parameters (default values)
        val scannerView: CodeScannerView = binding.scannerView
        codeScanner = CodeScanner(context as Context, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            Toast.makeText(context, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanning()
                } else {
                    showSnackbar()
                }
            }
            REQUEST_PERMISSION_CAMERA_SETTINGS -> {
                tryStartScanning()
            }
        }
    }

    private fun showSnackbar() {
        Snackbar.make(binding.root, R.string.camera_needed, Snackbar.LENGTH_SHORT)
            .setAction(R.string.menu_settings) {
                if (activity != null) {
                    val uri = Uri.fromParts("package", activity?.packageName, null)

                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_CAMERA_SETTINGS)
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.nav_scan_qr).isVisible = false
    }

    companion object {
        private const val REQUEST_PERMISSION_CAMERA = 200
        private const val REQUEST_PERMISSION_CAMERA_SETTINGS = 201
    }
}
