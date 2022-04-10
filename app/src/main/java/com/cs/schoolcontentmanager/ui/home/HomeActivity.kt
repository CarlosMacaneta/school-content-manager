package com.cs.schoolcontentmanager.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.ActivityHomeBinding
import com.cs.schoolcontentmanager.ui.home.bottomsheet.ModalBottomSheetOptions
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.CameraSetup
import com.cs.schoolcontentmanager.utils.Constants.BS_OPTIONS
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject lateinit var options: ModalBottomSheetOptions
    @Inject lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home)
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)

        NavigationUI.setupWithNavController(binding.navView, navController)

        CameraSetup.cameraPermission(this)

        dbRef.keepSynced(true)

        binding.fabMore.setOnClickListener {
            options.show(supportFragmentManager, BS_OPTIONS)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}