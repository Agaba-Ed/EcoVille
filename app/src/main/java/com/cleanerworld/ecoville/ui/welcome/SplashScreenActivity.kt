package com.cleanerworld.ecoville.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.ActivityMainBinding
import com.cleanerworld.ecoville.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.splashScreenButton.setOnClickListener{
            goToWalkThroughScreen()
        }

    }
    fun goToWalkThroughScreen(){
        startActivity(Intent(this,WalkThroughActivity::class.java))
    }
}