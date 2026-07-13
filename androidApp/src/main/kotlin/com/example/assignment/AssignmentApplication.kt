package com.example.assignment

import android.app.Application
import com.example.assignment.di.initKoin

class AssignmentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
