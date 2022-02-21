package com.cleanerworld.ecoville.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.ActivityWalkThroughBinding
import com.cleanerworld.ecoville.ui.authentication.AuntheticationActivity

class WalkThroughActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalkThroughBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWalkThroughBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.getStartedButton.setOnClickListener{
            getStarted()
        }
    }

    fun getStarted(){
        startActivity(Intent(this, AuntheticationActivity::class.java))
    }
}