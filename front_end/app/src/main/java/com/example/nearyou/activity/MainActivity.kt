package com.example.nearyou.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.nearyou.R
import com.example.nearyou.databinding.ActivityMainBinding
import com.example.nearyou.model.Permission
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.UserDAO
import com.example.nearyou.service.LocationBackgroundService
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navView.menu.findItem(R.id.nav_profile_logout).setOnMenuItemClickListener {
            UserDAO.logout(this@MainActivity)
            false
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_scan_qr,
                R.id.nav_profile,
                R.id.nav_profile_edit,
                R.id.nav_profile_access,
                R.id.nav_settings
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Add a button to close menu
        navView.getHeaderView(0).findViewById<ImageButton>(R.id.close_menu).setOnClickListener {
            drawerLayout.close()
        }

        tryStartLocationService()
        refreshUserDataEachMinute()
    }

    private fun refreshUserDataEachMinute() {
        Timer().scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    if (UserDAO.user != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val credentials = UserDAO.getCacheCredential(applicationContext)

                            if (credentials != null) {
                                val result = UserDAO.login(credentials)

                                if (result.code == ResponseCode.E_WRONG_CREDENTIALS) {
                                    UserDAO.logout(applicationContext)
                                    UserDAO.removeCredential(this@MainActivity)
                                    Toast.makeText(this@MainActivity, R.string.force_logout, Toast.LENGTH_LONG).show()
                                }
                            } else {
                                UserDAO.logout(applicationContext)
                                UserDAO.removeCredential(this@MainActivity)
                                Toast.makeText(this@MainActivity, R.string.force_logout, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

            },1000 * 60, 1000 * 60
        )
    }

    override fun onResume() {
        super.onResume()

        UserDAO.userStateListener.observe(this@MainActivity, {
            if (it == null) {
                stopService(Intent(this, LocationBackgroundService::class.java))

                val intent = Intent(this, LoadingActivity::class.java)
                this.startActivity(intent)
                finishAffinity()
            }
        })

        supportActionBar?.setDisplayShowTitleEnabled(false)
    }


    // This function add the menu to the application
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // This method allows to go to the fragment corresponding to the ID of the clicked menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    // This method allows to go to the fragment corresponding to the ID of the clicked navigation item
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tryStartLocationService()
                } else {
                    showSnackBar()
                }
                return
            }
            REQUEST_PERMISSION_LOCATION_SETTINGS -> {
                tryStartLocationService()
            }
        }
    }

    private fun tryStartLocationService() {
        if (!LocationBackgroundService.isRunning) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (Permission.isPermissionsAllowed(permissions, this)) {
                val serviceIntent = Intent(this, LocationBackgroundService::class.java)
                startService(serviceIntent)
            } else {
                if (Permission.shouldShowRequestPermissionRationale(permissions, this)) {
                    showSnackBar()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        REQUEST_PERMISSION_LOCATION
                    )
                }
            }
        }
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, R.string.location_needed, Snackbar.LENGTH_SHORT)
            .setAction(R.string.menu_settings) {
                val uri = Uri.fromParts("package", packageName, null)

                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = uri
                startActivityForResult(intent, REQUEST_PERMISSION_LOCATION_SETTINGS)
            }
            .show()
    }

    companion object {
        private const val REQUEST_PERMISSION_LOCATION = 200
        private const val REQUEST_PERMISSION_LOCATION_SETTINGS = 201
    }
}
