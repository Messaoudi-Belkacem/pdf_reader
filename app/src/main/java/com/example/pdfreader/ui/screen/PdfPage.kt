package com.example.pdfreader.ui.screen

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.example.pdfreader.SharedViewModel
import java.io.IOException

@Composable
fun PdfPage(
    sharedViewModel: SharedViewModel
) {
    // get uri from shared view model, if it's null return
    val pdfUri: Uri = sharedViewModel.pdfUri.value ?: return
    val pageIndex = 0
    val context = LocalContext.current

    // State to store the rendered bitmap
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Render the PDF page
    LaunchedEffect(pdfUri, pageIndex) {
        try {
            context.contentResolver.openFileDescriptor(pdfUri, "r")?.use { fileDescriptor ->
                val pdfRenderer = PdfRenderer(fileDescriptor)
                val page = pdfRenderer.openPage(pageIndex)

                // Create a bitmap with the dimensions of the PDF page
                val tempBitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)

                // Render the PDF page onto the bitmap
                page.render(tempBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                // Update the bitmap state
                bitmap = tempBitmap

                // Close the page and renderer
                page.close()
                pdfRenderer.close()
            }
        } catch (e: IOException) {
            e.printStackTrace() // Log the exception
        }
    }

    // Display the rendered page as an image
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
