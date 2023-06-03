package com.cleanerworld.ecoville

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.cleanerworld.ecoville.databinding.ActivityMainBinding
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val menuItems= arrayOf(
            CbnMenuItem(
                R.drawable.ic_location,
                R.drawable.ic_avd_location,
                R.id.navigation_location
            ),
            CbnMenuItem(
                R.drawable.ic_recycle,
                R.drawable.ic_avd_recycle,
                R.id.navigation_recycle
            ),
            CbnMenuItem(
                R.drawable.ic_post,
                R.drawable.ic_avd_post,
                R.id.navigation_add
            ),
            CbnMenuItem(
                R.drawable.ic_notifications,
                R.drawable.ic_avd_notifications,
                R.id.navigation_notifications
            ),
            CbnMenuItem(
                R.drawable.ic_profile,
                R.drawable.ic_avd_profile,
                R.id.navigation_profile
            )
        )

        binding.bottomNav.setMenuItems(menuItems)



        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController=navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)


    }




}