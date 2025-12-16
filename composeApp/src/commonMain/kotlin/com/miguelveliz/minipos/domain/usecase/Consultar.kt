package com.miguelveliz.minipos.domain.usecase

import com.miguelveliz.minipos.domain.repository.CuentaRepository
import com.miguelveliz.minipos.util.Result

class Consultar(
    private val cuentaRepository: CuentaRepository // Se inyectará con Koin
) : UseCase<Double, Consultar.Params>() {

    // Parámetros: ID de cuenta
    data class Params(val idCuenta: String)

    override suspend fun execute(params: Params): Double {
        // La lógica de negocios: llama al repositorio
        return when (val repoResult = cuentaRepository.cosultar(params.idCuenta)) {
            is Result.Success -> repoResult.data
            is Result.Error -> throw repoResult.exception // Lanzamos la excepción para que el UseCase.invoke la capture
        }
    }
}