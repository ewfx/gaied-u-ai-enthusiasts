package com.example.documentextractionhackathon2025

import android.app.Application
import android.util.Log
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize PDFBox to load necessary resources
        PDFBoxResourceLoader.init(this)
        Log.d("PDFBox", "PDFBox initialized successfully")
    }
}