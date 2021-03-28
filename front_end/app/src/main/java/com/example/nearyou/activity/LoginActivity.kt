package com.example.nearyou.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.nearyou.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val input_mail: EditText = binding.inputMail
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
            //TODO compléter
        }

    }

    private fun isEmailValid(email: String): Boolean {
        val cs: CharSequence = email
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cs).matches()
    }

}

