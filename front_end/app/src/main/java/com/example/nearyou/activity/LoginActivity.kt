package com.example.nearyou.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.nearyou.R
import com.example.nearyou.databinding.ActivityLoginBinding
import com.example.nearyou.model.credential.LoginCredential
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : Activity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inputMail: EditText = binding.inputMail
        val inputPassword: EditText = binding.inputPassword
        val btnC: Button = binding.buttonConnection
        val btnI: Button = binding.buttonInscription

        btnC.setOnClickListener {
            if (isEmailValid(inputMail.text.toString())) {
                val credentials =
                    LoginCredential(inputMail.text.toString(), inputPassword.text.toString())

                CoroutineScope(Dispatchers.Main).launch {
                    val response = UserDAO.login(credentials)

                    when (response.code) {
                        ResponseCode.S_SUCCESS -> {
                            UserDAO.saveCredentialCache(credentials, applicationContext)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                        ResponseCode.E_NO_INTERNET -> {
                            inputMail.error = getString(R.string.no_internet)
                        }
                        ResponseCode.E_EMAIL_TOO_LONG -> {
                            inputMail.error = getString(R.string.alert_email_too_long)
                        }
                        ResponseCode.E_BAD_EMAIL_FORMAT -> {
                            inputMail.error = getString(R.string.alert_invalid_email)
                        }
                        ResponseCode.E_WRONG_CREDENTIALS -> {
                            inputMail.error = getString(R.string.alert_wrong_credential)
                        }
                        else -> inputMail.error = getString(R.string.unknown_error)
                    }
                }
            } else {
                inputMail.error = getString(R.string.alert_invalid_email)
            }
        }

        btnI.setOnClickListener {
            val inscriptionActivity = Intent(this@LoginActivity, InscriptionActivity::class.java)
            startActivity(inscriptionActivity)
        }
    }


    override fun onResume() {
        super.onResume()

        if (UserDAO.user != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val cs: CharSequence = email
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cs).matches()
    }

}

//TODO rename files in signin signup