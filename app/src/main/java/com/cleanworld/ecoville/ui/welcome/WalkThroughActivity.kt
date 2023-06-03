package com.cleanerworld.ecoville.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.ActivityWalkThroughBinding
import com.cleanerworld.ecoville.ui.authentication.AuntheticationActivity
import com.cleanworld.ecoville.adapter.OnBoardingItemsAdapter
import com.cleanworld.ecoville.data.DataSource.onBoardingItems


class WalkThroughActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWalkThroughBinding


    private lateinit var onBoardingItemsAdapter: OnBoardingItemsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWalkThroughBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupOnBoardingItems()
        setUpIndicators()
        setCurrentIndicator(0)
        binding.getStartedButton.setOnClickListener{
            getStarted()
        }
    }

    fun getStarted(){
        startActivity(Intent(this, AuntheticationActivity::class.java))
        finish()
    }


    private fun setupOnBoardingItems() {

        onBoardingItemsAdapter = OnBoardingItemsAdapter(this, onBoardingItems)
        binding.walkthroughViewpager.adapter = onBoardingItemsAdapter
        binding.walkthroughViewpager.registerOnPageChangeCallback(
            object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            }
        )
        (binding.walkthroughViewpager.getChildAt(0) as RecyclerView).overScrollMode=RecyclerView.OVER_SCROLL_NEVER
    }

    private fun setUpIndicators(){
        val indicators= arrayOfNulls<ImageView>(onBoardingItemsAdapter.itemCount)
        val layoutParams:LinearLayout.LayoutParams=LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for(i in indicators.indices){
            indicators[i]= ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.circular_shape
                    )
                )
                it.layoutParams=layoutParams
                binding.indicatorContainer.addView(it)
            }
        }

    }

    private fun setCurrentIndicator(position: Int){
        val childCount=binding.indicatorContainer.childCount

        for(i in 0 until childCount){
            val imageView=binding.indicatorContainer.getChildAt(i) as ImageView
            if(i==position)
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext,
                    R.drawable.rounded_rect)
                )
            else
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext,
                        R.drawable.circular_shape)
                )

        }

    }

    override fun onResume() {
        super.onResume()
        displayActivityOnce()
    }
    private fun displayActivityOnce(){
        val sharedPreferences=getSharedPreferences("DISPLAY_ONCE",0)
        val isFirstTime=sharedPreferences.getBoolean("isFirstTime",true)
        if(isFirstTime){
            val editor=sharedPreferences.edit()
            editor.putBoolean("isFirstTime",false)
            editor.apply()
        }
        else{
            getStarted()
        }
    }


}