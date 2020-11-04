package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trainstationapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}
