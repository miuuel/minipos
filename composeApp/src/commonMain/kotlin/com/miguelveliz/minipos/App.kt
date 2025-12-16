package com.miguelveliz.minipos

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.miguelveliz.minipos.presentation.cuenta.CuentaViewModel
import com.miguelveliz.minipos.screens.cuenta.CuentaScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Surface {
            val cuentaViewModel = koinViewModel<CuentaViewModel>()

            CuentaScreen(viewModel = cuentaViewModel)
        }
    }
}
