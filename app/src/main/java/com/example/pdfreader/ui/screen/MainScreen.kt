package com.example.pdfreader.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.pdfreader.SharedViewModel
import com.example.pdfreader.navigation.Screen

@Composable
fun MainScreen(
    sharedViewModel: SharedViewModel,
    onButtonClick: () -> Unit,
    navHostController: NavHostController
) {
    // observe navigation trigger
    val navigateTo by sharedViewModel.navigateTo.observeAsState(null)
    // set up navigation trigger
    if (navigateTo != null) {
        // temp variable
        val navigateToTemp = navigateTo!!
        sharedViewModel.setNavigateTo(null)
        navHostController.navigate(navigateToTemp)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { onButtonClick() }
        ) {
            Text(
                text = "Open PDF"
            )
        }
    }
}