package com.example.documentextractionhackathon2025.data

data class RequestType(
    val name : String,
    val definition: String,
    val subtype : List<RequestSubType>? = emptyList()
)

data class RequestSubType(
    val name : String,
    val definition : String
)