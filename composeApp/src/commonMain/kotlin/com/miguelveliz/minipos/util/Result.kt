package com.miguelveliz.minipos.util

/**
 * Clase Result multiplataforma simple para envolver resultados.
 *
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

// Función auxiliar para mapear el error fácilmente
inline fun <T> Result<T>.onFailure(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(exception)
    }
    return this
}