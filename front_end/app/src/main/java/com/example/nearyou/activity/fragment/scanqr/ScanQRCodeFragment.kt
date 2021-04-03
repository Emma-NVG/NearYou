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
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.example.nearyou.R
import com.example.nearyou.databinding.FragmentScanQrBinding
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.member.Member
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject


class ScanQRCodeFragment : Fragment() {

    private var _binding: FragmentScanQrBinding? = null
    private val binding get() = _binding!!

    private lateinit var codeScanner: CodeScanner
    private var canScan = true

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
                    showSnackbarCameraPermission()
                }
            } else {
                startScanning()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }

    private fun startScanning() {
        val scannerView: CodeScannerView = binding.scannerView
        codeScanner = CodeScanner(context as Context, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.CONTINUOUS
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            try {
                val data = JSONObject(it.text)

                CoroutineScope(Dispatchers.IO).launch {
                    val result = Member.manager.retrieveData(data.getString("id"), data.getString("token") + "aaa")

                    withContext(Dispatchers.Main) {
                        when (result.code) {
                            ResponseCode.S_SUCCESS -> {
                                val bundle = Bundle()
                                bundle.putString("User", Json.encodeToString(result.data))
                                findNavController().navigate(R.id.action_nav_scan_qr_to_nav_profile, bundle)
                            }
                            ResponseCode.E_NO_RESOURCE -> {
                                showSnackbarErrorScan(R.string.no_user)
                            }
                            ResponseCode.E_NO_TOKEN, ResponseCode.E_UNAUTHORIZED -> {
                                showSnackbarErrorScan(R.string.invalid_data)
                            }
                            else -> {
                                showSnackbarErrorScan(R.string.unknown_error)
                            }
                        }

                        codeScanner.startPreview()
                    }
                }
            } catch (e: Exception) {
                showSnackbarErrorScan(R.string.invalid_data)
                codeScanner.startPreview()
            }
        }
        codeScanner.errorCallback = ErrorCallback { }

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
                    showSnackbarCameraPermission()
                }
            }
            REQUEST_PERMISSION_CAMERA_SETTINGS -> {
                tryStartScanning()
            }
        }
    }

    private fun showSnackbarCameraPermission() {
        Snackbar.make(binding.root, R.string.camera_needed, Snackbar.LENGTH_LONG)
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

    private fun showSnackbarErrorScan(@StringRes message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.menu_retry) {
                    if (::codeScanner.isInitialized) {
                        codeScanner.startPreview()
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
