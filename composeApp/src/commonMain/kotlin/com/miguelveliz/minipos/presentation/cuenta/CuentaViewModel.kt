package com.miguelveliz.minipos.presentation.cuenta

// Módulo: shared/src/commonMain/kotlin/presentation/cuenta/CuentaViewModel.kt

import androidx.lifecycle.ViewModel
import com.miguelveliz.minipos.domain.repository.CuentaRepository
import com.miguelveliz.minipos.domain.usecase.Consultar
import com.miguelveliz.minipos.domain.usecase.Depositar
import com.miguelveliz.minipos.domain.usecase.Consultar.Params
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.miguelveliz.minipos.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class CuentaViewModel(cuentaRepository: CuentaRepository) : ViewModel(), KoinComponent {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Inyección de Dependencias de los Casos de Uso via Koin
    private val consultarSaldo: Consultar by inject()
    //private val consultarSaldo: cuentaRepo
    private val depositarSaldo: Depositar by inject()

    // Estado observable de la UI (MVI Pattern)
    private val _state = MutableStateFlow(CuentaState())
    val state: StateFlow<CuentaState> = _state.asStateFlow()

    init {
        // Ejecuta la consulta automáticamente al iniciar el ViewModel
        processIntent(CuentaIntent.ConsultarSaldo)
    }
    /**
     * Función principal que procesa las Intenciones (Intents) del usuario.
     */
    fun processIntent(intent: CuentaIntent) {
        when (intent) {
            is CuentaIntent.ConsultarSaldo -> fetchSaldo()
            is CuentaIntent.DepositarSaldo -> deposit(intent.monto)
        }
    }

     fun fetchSaldo() {
        // Ejecutamos la coroutine en el ámbito del ViewModel
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, exitoso = false) }

            when (val result = consultarSaldo(Params(idCuenta = "XYZ123"))) {
                is Result.Success -> {
                    _state.update {
                        it.copy(isLoading = false, saldo = result.data)
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.exception.message ?: "Error al consultar saldo")
                    }
                }
            }
        }
    }

     fun deposit(monto: Double) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, exitoso = false) }

            val params = Depositar.Params(idCuenta = "XYZ123", monto = monto)

            when (val result = depositarSaldo(params)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, exitoso = true) }
                    // Después de depositar, volvemos a consultar para actualizar el saldo
                    fetchSaldo()
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.exception.message ?: "Error al realizar depósito")
                    }
                }
            }
        }
    }
}