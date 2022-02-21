package com.cleanerworld.ecoville.ui.authentication.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cleanerworld.ecoville.MainActivity
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerButton.setOnClickListener {
            register()
        }

    }

    fun register(){
        startActivity(Intent(this,MainActivity::class.java))
    }
}