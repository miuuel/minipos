package com.miguelveliz.minipos.presentation.cuenta

import com.miguelveliz.minipos.util.Result

/**
 * A. Estado de la UI (Qué mostrar en la pantalla)
 */
data class CuentaState(
    val saldo: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val exitoso: Boolean = false
)
