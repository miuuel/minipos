package com.miguelveliz.minipos.presentation.cuenta

/**
 * B. Intenciones del Usuario (Acciones que el usuario puede realizar)
 */
sealed class CuentaIntent {
    // 1. Acciones para Consultar
    data object ConsultarSaldo : CuentaIntent()

    // 2. Acciones para Depositar
    data class DepositarSaldo(val monto: Double) : CuentaIntent()
}

/**
 * C. Efectos Secundarios (Eventos únicos que no cambian el estado de la UI, como navegación o Toast)
 */
sealed class CuentaEffect {
    data class ShowToast(val mensaje: String) : CuentaEffect()
}