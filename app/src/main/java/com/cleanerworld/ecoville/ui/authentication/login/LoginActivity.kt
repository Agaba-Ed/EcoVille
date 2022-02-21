package com.cleanerworld.ecoville.ui.authentication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cleanerworld.ecoville.MainActivity
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.ActivityLoginBinding
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            login()
        }
    }
    fun login(){
        startActivity(Intent(this,MainActivity::class.java))
    }
}