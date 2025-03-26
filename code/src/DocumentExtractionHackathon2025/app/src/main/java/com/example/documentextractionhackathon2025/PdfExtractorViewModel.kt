package com.example.documentextractionhackathon2025

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.documentextractionhackathon2025.data.PromptBuilder
import com.example.documentextractionhackathon2025.data.RequestType
import com.example.documentextractionhackathon2025.data.RequestTypeParser
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PdfExtractorViewModel(val applicationCntxt: Application) :
    AndroidViewModel(applicationCntxt) {

    val extractedText = MutableLiveData("Extracted text will appear here...")
    val aiResponse = MutableLiveData("Ai Response will appear here...")
    private lateinit var pdfPickerLauncher: ActivityResultLauncher<Intent>
    val inputText = MutableLiveData("")


    init {
        getRequestData()
    }


    private fun getRequestData() {
        viewModelScope.launch {
            val listOfRequestData = withContext(Dispatchers.IO) {
                getParseAndGetTheData()
            }
            listOfRequestData?.let {
                setRequestData(listOfRequestData)
            }

        }
    }

    private fun getParseAndGetTheData(): List<RequestType>? {
        val parser = RequestTypeParser()
        return parser.getRequestData(applicationCntxt.applicationContext)

    }


    private val requestDataList: MutableList<RequestType> = mutableListOf()

    private fun setRequestData(requestData: List<RequestType>) {
        requestDataList.clear()
        requestDataList.addAll(requestData)
    }

    fun setPdfPickerLauncher(launcher: ActivityResultLauncher<Intent>) {
        pdfPickerLauncher = launcher
    }

    fun pickPdf() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pdfPickerLauncher.launch(intent)
    }

    fun submitInput() {
        if(!inputText.value.isNullOrEmpty()){
            try {
                val prompt = PromptBuilder.createPrompt(requestDataList, inputText.value!!)
                println("Request prompt : $prompt")
                getAiResFromGemini(prompt)
                return
            }catch (ex: Exception){
                println("Exception occurred while creating the prompt : ${ex.message}")
                return
            }
        }
    }

    fun clearInput() {
        inputText.value = ""
    }

    fun processSelectedPdf(uri: Uri) {
        val file = getFileFromUri(uri)
        file?.let { pdfFile ->
            val textFromPdfBox = extractTextFromPdf(pdfFile)
            if (textFromPdfBox.isNotBlank()) {
                try {
                    val prompt = PromptBuilder.createPrompt(requestDataList, textFromPdfBox)
                    println("Request prompt : $prompt")
                    getAiResFromGemini(prompt)
                    return
                }catch (ex: Exception){
                    println("Exception occured while creating the prompt : ${ex.message}")
                    return
                }
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver = getApplication<Application>().contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            val name = it.getString(nameIndex)
            val file = File(getApplication<Application>().cacheDir, name)
            contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return file
        }
        return null
    }


    private fun extractTextFromPdf(pdfFile: File): String {
        return try {
            PDDocument.load(pdfFile).use { document ->
                PDFTextStripper().getText(document)
            }
        } catch (e: Exception) {
            Log.e("PDFBox", "Error extracting text from PDF: ${e.message}")
            ""
        }
    }

    private fun getAiResFromGemini(prompt: String) {
        val config = generationConfig { temperature = 0.7f }

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash-latest",
            apiKey = "AIzaSyDl-tboW9TsIhw626MXVY6zQyNrT96zzBA",
            generationConfig = config
        )

        viewModelScope.launch {    // Non-streaming
            try {
                val response =
                    generativeModel.generateContent(prompt)
                response.text?.let { outputContent ->
                    aiResponse.value = outputContent
                }
            } catch (e: Exception) {
                aiResponse.value = e.message
            }
        }
    }

}
