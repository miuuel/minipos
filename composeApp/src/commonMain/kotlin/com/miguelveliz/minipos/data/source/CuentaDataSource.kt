package com.miguelveliz.minipos.data.source

interface CuentaDataSource {
    suspend fun fetchSaldo(idCuenta: String): Double
    suspend fun doDeposit(idCuenta: String, monto: Double)
}