package com.cs.schoolcontentmanager.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.ActivityHomeBinding
import com.cs.schoolcontentmanager.ui.home.bottomsheet.ModalBottomSheetOptions
import com.cs.schoolcontentmanager.ui.splash.SplashActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home)
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        bottomNavigation()

        binding.fabMore.setOnClickListener {
            val options = ModalBottomSheetOptions()
            options.show(supportFragmentManager, "More_options")
        }
    }

    private fun bottomNavigation() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home)
        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_view)
        NavigationUI.setupWithNavController(bottomNav, navController)
    }
}