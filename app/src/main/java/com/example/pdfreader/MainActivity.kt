package com.example.pdfreader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.example.pdfreader.constant.Constants.PICK_PDF_REQUEST
import com.example.pdfreader.navigation.RootNavigationGraph
import com.example.pdfreader.navigation.Screen
import com.example.pdfreader.ui.theme.PDFReaderTheme
import java.io.File

class MainActivity : ComponentActivity() {
    private val sharedViewModel = SharedViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PDFReaderTheme {
                RootNavigationGraph(
                    sharedViewModel = sharedViewModel,
                    onButtonClick = {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/pdf"
                        }
                        ActivityCompat.startActivityForResult(
                            this,
                            intent,
                            PICK_PDF_REQUEST,
                            null
                        )
                    }
                )
            }
        }
    }

    // Add this to handle result from file picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            val pdfUri: Uri? = data.data
            // call pdfPage screen on this pdf uri
            pdfUri?.let {
                val file = it.path?.let { it1 -> File(it1) }
                if (file != null) {
                    sharedViewModel.setPdfUri(pdfUri)
                    sharedViewModel.setNavigateTo(Screen.Reader.route)
                } else {
                    Log.d("MainActivity", "File is null")
                }
            }
        }
    }
}