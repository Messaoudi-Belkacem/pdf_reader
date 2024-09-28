package com.example.pdfreader.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.pdfreader.SharedViewModel
import com.example.pdfreader.util.PdfBitmapConverter

@Composable
fun PdfViewerScreen(
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val pdfBitmapConverter = remember {
        PdfBitmapConverter(context)
    }
    var pdfUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var renderedPages by remember {
        mutableStateOf<List<Bitmap>>(emptyList())
    }
    LaunchedEffect(pdfUri) {
        pdfUri?.let { uri ->
            renderedPages = pdfBitmapConverter.pdfToBitmaps(uri)
        }
    }
    LaunchedEffect(Unit) {
        sharedViewModel.pdfUri.observeForever { uri ->
            pdfUri = uri
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(renderedPages) { _, page ->
            PdfPage(
                page = page,
            )
        }
    }
}

@Composable
fun PdfPage(
    page: Bitmap,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = page,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat())
            .drawWithContent {
                drawContent()
            }
    )
}