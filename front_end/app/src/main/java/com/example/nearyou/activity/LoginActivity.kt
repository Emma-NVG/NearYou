package com.example.nearyou.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.nearyou.databinding.ActivityLoginBinding
import com.example.nearyou.model.Credential
import com.example.nearyou.model.response.ResponseCode
import com.example.nearyou.model.user.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val input_mail: EditText = binding.inputMail
        val input_password: EditText = binding.inputPassword
        val btn: Button = binding.button

        input_mail.addTextChangedListener {
            if (it.toString().isEmpty()){
                input_mail.setError("Veuillez entrer une adresse mail")
            }else{
                if (! isEmailValid( it.toString())) {
                    input_mail.setError("Adresse mail invalide")
                }
            }
        }

        btn.setOnClickListener {
            val credentials = Credential(input_mail.text.toString(),input_password.text.toString())
            CoroutineScope(Dispatchers.IO).launch{
                if(UserDAO.login(credentials).code == ResponseCode.S_SUCCESS){
                    val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(mainActivity)
                }
            }
        }

    }

    private fun isEmailValid(email: String): Boolean {
        val cs: CharSequence = email
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cs).matches()
    }

}

