package com.example.documentextractionhackathon2025

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.viewModels
import com.example.documentextractionhackathon2025.databinding.FragmentPdfExtractorBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PdfExtractorFragment : Fragment() {

    private var _binding: FragmentPdfExtractorBinding? = null
    private val pdfExtractorViewModel: PdfExtractorViewModel by viewModels()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result.data?.data?.let { uri: Uri ->
            pdfExtractorViewModel.processSelectedPdf(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPdfExtractorBinding.inflate(inflater, container, false)
        binding.viewModel = pdfExtractorViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        pdfExtractorViewModel.setPdfPickerLauncher(pdfPickerLauncher)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            hideKeyboard(requireContext(), binding.inputText)
            pdfExtractorViewModel.submitInput()
        }
        /*pdfExtractorViewModel.aiResponse.observe(viewLifecycleOwner){ outputContent->
            binding.aiResponseTv.text = Html.fromHtml(outputContent, Html.FROM_HTML_MODE_LEGACY)
        }*/
    }

    private fun hideKeyboard(context: Context, editText: AppCompatEditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}