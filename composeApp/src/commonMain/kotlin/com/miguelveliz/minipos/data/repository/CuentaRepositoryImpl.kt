package com.miguelveliz.minipos.data.repository

import com.miguelveliz.minipos.domain.repository.CuentaRepository
import com.miguelveliz.minipos.data.source.CuentaDataSource
import com.miguelveliz.minipos.util.Result

class CuentaRepositoryImpl(
    private val dataSource: CuentaDataSource
) : CuentaRepository {

    override suspend fun cosultar(idCuenta: String): Result<Double> {
        return try {
            // Llama al DataSource. Si falla, va al catch.
            val saldo = dataSource.fetchSaldo(idCuenta)
            Result.Success(saldo)
        } catch (e: Exception) {
            // Convierte la excepción en un Error del Dominio
            Result.Error(e)
        }
    }

    override suspend fun depositar(idCuenta: String, monto: Double): Result<Unit> {
        return try {
            // Llama al DataSource
            dataSource.doDeposit(idCuenta, monto)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}