package com.example.myapplication.ui

import androidx.test.core.app.ActivityScenario
import org.junit.Before
import org.junit.Test

class MainActivityTest {
    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testApp() {
    }
}