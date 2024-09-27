package com.example.pdfreader.ui.screen

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.graphicsLayer
import com.example.pdfreader.SharedViewModel
import java.io.IOException
import androidx.compose.foundation.gestures.detectHorizontalDragGestures

@Composable
fun PdfPage(
    sharedViewModel: SharedViewModel
) {
    // Get URI from shared view model, if it's null return
    val pdfUri: Uri = sharedViewModel.pdfUri.value ?: return
    val context = LocalContext.current

    // State to track the current page index and total page count
    var pageIndex by remember { mutableIntStateOf(0) }
    var totalPages by remember { mutableIntStateOf(1) }

    // State to store the rendered bitmap
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // State for zoom, panning, and drag offset
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var dragOffsetX by remember { mutableFloatStateOf(0f) }

    // Render the PDF page whenever the URI or pageIndex changes
    LaunchedEffect(pdfUri, pageIndex) {
        try {
            context.contentResolver.openFileDescriptor(pdfUri, "r")?.use { fileDescriptor ->
                val pdfRenderer = PdfRenderer(fileDescriptor)
                totalPages = pdfRenderer.pageCount
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

    // Detect swipe gestures for page flipping
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX + dragOffsetX,
                    translationY = offsetY
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            // If the drag offset exceeds a threshold, flip pages
                            if (dragOffsetX > 100 && pageIndex > 0) {
                                pageIndex--
                            } else if (dragOffsetX < -100 && pageIndex < totalPages - 1) {
                                pageIndex++
                            }
                            // Reset drag offset after page flip
                            dragOffsetX = 0f
                        }
                    ) { change, dragAmount ->
                        change.consume()  // Consume the gesture event to handle drag
                        dragOffsetX += dragAmount
                    }
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        // Update the scale based on pinch-to-zoom gesture
                        scale = (scale * zoom).coerceIn(0.5f, 5f)

                        // Calculate the max panning values based on the image size and scale
                        val maxX = if (scale > 1) (bitmap!!.width * scale - bitmap!!.width) / 2 else 0f
                        val maxY = if (scale > 1) (bitmap!!.height * scale - bitmap!!.height) / 2 else 0f

                        // Update the offsets for panning
                        offsetX = (offsetX + pan.x * scale).coerceIn(-maxX, maxX)
                        offsetY = (offsetY + pan.y * scale).coerceIn(-maxY, maxY)
                    }
                }
        )
    }
}
