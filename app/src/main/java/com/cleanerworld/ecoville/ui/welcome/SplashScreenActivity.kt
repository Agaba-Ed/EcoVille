package com.cleanerworld.ecoville.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cleanerworld.ecoville.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //wait for 3 seconds and then start the next activity
        Thread(Runnable {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            goToWalkThroughScreen()
        }).start()
    }
    fun goToWalkThroughScreen(){
        startActivity(Intent(this,WalkThroughActivity::class.java))
    }
}