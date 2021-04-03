package com.example.nearyou.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.nearyou.R
import com.example.nearyou.activity.fragment.profile.ProfileFragment
import com.example.nearyou.databinding.ActivityCatchLinkProfileBinding
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.member.Member
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class CatchLinkProfile : AppCompatActivity() {
    private lateinit var binding: ActivityCatchLinkProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove notification bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityCatchLinkProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val appLinkIntent = intent
        val appLinkData = appLinkIntent.data

        val segments = appLinkData?.pathSegments
        val token = segments?.get(segments.size - 2).toString()
        val id = segments?.get(segments.size - 3).toString()



        CoroutineScope(Dispatchers.IO).launch {
            val result = Member.manager.retrieveData(id, token)

            withContext(Dispatchers.Main) {
                when (result.code) {
                    ResponseCode.S_SUCCESS -> {
                        binding.fragment.visibility = View.VISIBLE
                        binding.error.visibility = View.GONE

                        val bundle = Bundle()
                        bundle.putString("User", Json.encodeToString(result.data))

                        val profileFragment = ProfileFragment()
                        profileFragment.arguments = bundle
                        supportFragmentManager.beginTransaction()
                            .add(binding.fragment.id, ProfileFragment())
                            .commit()
                    }
                    ResponseCode.E_NO_INTERNET -> {
                        binding.fragment.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE

                        binding.error.text = getString(R.string.no_internet)
                    }
                    ResponseCode.E_NO_RESOURCE -> {
                        binding.fragment.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE

                        binding.error.text = getString(R.string.no_user)
                    }
                    else -> {
                        binding.fragment.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE

                        binding.error.text = getString(R.string.unknown_error)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.catch_url_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                if (isTaskRoot) {
                    startActivity(Intent(this@CatchLinkProfile, LoadingActivity::class.java))
                } else {
                    onBackPressed()
                }
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}