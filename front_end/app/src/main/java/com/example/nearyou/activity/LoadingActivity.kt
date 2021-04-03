package com.example.nearyou.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.nearyou.R
import com.example.nearyou.databinding.ActivityLoadingBinding
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.UserDAO
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoadingActivity : Activity() {
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                tryLoginWithCacheCredential()
            }
        })
        binding.loadingLogo.startAnimation(fadeIn)
    }

    private fun tryLoginWithCacheCredential() {
        val credential = UserDAO.getCacheCredential(this@LoadingActivity)
        if (credential != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val response = UserDAO.login(credential)
                when (response.code) {
                    ResponseCode.S_SUCCESS -> {
                        startActivity(
                            Intent(
                                this@LoadingActivity,
                                MainActivity::class.java
                            )
                        )
                    }
                    ResponseCode.E_NO_INTERNET -> {
                        val snack = Snackbar.make(binding.root, R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                        snack.setAction(R.string.menu_retry) {
                            snack.dismiss()
                            tryLoginWithCacheCredential()
                        }
                        snack.show()
                    }
                    else -> {
                        UserDAO.removeCredential(this@LoadingActivity)
                        val loginActivity =
                            Intent(this@LoadingActivity, LoginActivity::class.java)
                        startActivity(loginActivity)
                    }
                }
            }
        } else {
            val loginActivity = Intent(this@LoadingActivity, LoginActivity::class.java)
            startActivity(loginActivity)
        }
    }
}