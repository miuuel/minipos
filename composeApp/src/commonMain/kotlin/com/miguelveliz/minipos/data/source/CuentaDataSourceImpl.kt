package com.miguelveliz.minipos.data.source

import com.miguelveliz.minipos.data.remote.BASE_URL
import com.miguelveliz.minipos.data.remote.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class CuentaDataSourceImpl : CuentaDataSource {

    private val client = httpClient
    private val endpoint = "$BASE_URL/api/cuenta"

    override suspend fun fetchSaldo(idCuenta: String): Double {
        // Mockoon Endpoint: GET http://localhost:3000/api/cuenta/saldo?id={idCuenta}

        val response: SaldoResponseDto = client.get("$endpoint/saldo") {
            // Envía el ID de la cuenta como un parámetro de consulta (query parameter)
            parameter("id", idCuenta)
        }.body()

        return response.saldo
    }

    override suspend fun doDeposit(idCuenta: String, monto: Double) {
        // Mockoon Endpoint: POST http://localhost:3000/api/cuenta/depositar

        client.post("$endpoint/depositar") {
            // Define que el cuerpo de la petición es JSON
            contentType(ContentType.Application.Json)
            // Envía el DTO con la data
            setBody(DepositoRequestDto(idCuenta = idCuenta, monto = monto))
        }.body<DepositoResponseDto>() // Opcionalmente, lee la respuesta si necesitas el mensaje

        // El Unit se devuelve implícitamente si la llamada es exitosa
    }
}