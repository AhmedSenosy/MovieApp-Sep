package com.senosy.evamovieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.senosy.evamovieapp.R
import com.senosy.evamovieapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        linkNavBottomToNavigation()
    }

    private fun linkNavBottomToNavigation(){
        binding.navigationBnv.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }


}