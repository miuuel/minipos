package com.miguelveliz.minipos.domain.usecase

import com.miguelveliz.minipos.domain.repository.CuentaRepository

class Depositar(
    private val cuentaRepository: CuentaRepository // Se inyectará con Koin
) : UseCase<Unit, Depositar.Params>() {

    data class Params(val idCuenta: String, val monto: Double)

    override suspend fun execute(params: Params) {
        // Lógica de negocios: verifica si el monto es válido y llama al repositorio
        require(params.monto > 0.0) { "El monto a depositar debe ser positivo." }

        cuentaRepository.depositar(params.idCuenta, params.monto)

        // Opcional: Podrías ejecutar otro caso de uso aquí para actualizar el saldo localmente
    }
}