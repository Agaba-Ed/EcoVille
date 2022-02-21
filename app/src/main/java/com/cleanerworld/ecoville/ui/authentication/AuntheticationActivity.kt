package com.cleanerworld.ecoville.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.ActivityAuntheticationBinding
import com.cleanerworld.ecoville.ui.authentication.login.LoginActivity
import com.cleanerworld.ecoville.ui.authentication.register.RegisterActivity

class AuntheticationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAuntheticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuntheticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginOptionButton.setOnClickListener{
            goToLoginScreen()
        }
        binding.registerOptionButton.setOnClickListener{
            goToRegisterScreen()
        }
    }


    fun goToLoginScreen(){
        startActivity(Intent(this, LoginActivity::class.java))
    }
    fun goToRegisterScreen(){
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}