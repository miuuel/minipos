package com.miguelveliz.minipos.screens.cuenta

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.miguelveliz.minipos.presentation.cuenta.CuentaViewModel
import com.miguelveliz.minipos.presentation.cuenta.CuentaIntent
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuentaScreen( viewModel: CuentaViewModel
) {
    val state by viewModel.state.collectAsState()
    //val state by viewModel.state.collectAsState(LocalLifecycleOwner.current)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gestión de Cuenta") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 2. Mostrar Saldo
            Text(
                text = "Saldo Actual:",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$ ${state.saldo.toString()}",
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Mostrar indicador de Carga
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                // 4. Botones para enviar INTENTs
                Button(
                    onClick = {
                        // Envía un INTENT para consultar el saldo
                        viewModel.processIntent(CuentaIntent.ConsultarSaldo)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Consultar Saldo")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Envía un INTENT para depositar (ejemplo: depositar 100)
                        viewModel.processIntent(CuentaIntent.Depositar(monto = 100.0))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Depositar $100.00")
                }
            }

            // 5. Mostrar Errores
            state.error?.let { errorMessage ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "ERROR: $errorMessage",
                    color = MaterialTheme.colorScheme.error
                )
            }

            // 6. Mostrar mensaje de éxito (ejemplo)
            if (state.exitoso) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Depósito realizado con éxito!",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}