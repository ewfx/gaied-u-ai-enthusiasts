package com.example.documentextractionhackathon2025.data

import android.content.Context
import com.example.documentextractionhackathon2025.file.FileManager
import com.example.documentextractionhackathon2025.file.FileManagerInterface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RequestTypeParser {
    fun getRequestData(context: Context?): List<RequestType>? {
        val fileManager: FileManagerInterface = FileManager.getInstance(context)

        try {
            val jsonData = fileManager.read("RequestData.json")
            val jsonString = String(jsonData)
            println("Request String $jsonString")
            val listType = object : TypeToken<List<RequestType?>?>() {}.type
            val reqData = Gson().fromJson<List<RequestType>>(jsonString, listType)
            return reqData
        } catch (e: Exception) {
            println("Error occurred during parsing  String ${e.message}")
            e.printStackTrace()
        }

        return null
    }
}
