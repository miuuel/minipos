package com.miguelveliz.minipos.domain.repository

import com.miguelveliz.minipos.util.Result

interface CuentaRepository {

    /**
     * Consulta el saldo de una cuenta.
     * @param idCuenta ID de la cuenta a consultar.
     * @return Result<Double> que contiene el saldo o un error.
     */
    suspend fun cosultar(idCuenta: String): Result<Double>

    /**
     * Realiza un depósito en una cuenta.
     * @param idCuenta ID de la cuenta.
     * @param monto Monto a depositar.
     * @return Result<Unit> para éxito o un error.
     */
    suspend fun depositar(idCuenta: String, monto: Double): Result<Unit>
}