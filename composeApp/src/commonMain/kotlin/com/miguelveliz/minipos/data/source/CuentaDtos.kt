package com.miguelveliz.minipos.data.source

import kotlinx.serialization.Serializable

@Serializable
data class SaldoResponseDto(
    val idCuenta: String,
    val saldo: Double, // Debe coincidir con el JSON de Mockoon
)

@Serializable
// Clase simple para la respuesta de depósito exitoso
data class DepositoResponseDto(
    val mensaje: String
)

// DTO para enviar la información del depósito
@Serializable
data class DepositoRequestDto(
    val idCuenta: String,
    val monto: Double
)