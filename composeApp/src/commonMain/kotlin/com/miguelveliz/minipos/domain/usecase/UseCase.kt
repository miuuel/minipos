package com.miguelveliz.minipos.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.miguelveliz.minipos.util.Result
import kotlinx.coroutines.withContext

/**
 * Clase base para todos los Casos de Uso.
 * Ejecuta la lógica de negocios de forma asíncrona y devuelve un resultado.
 * R: Tipo de retorno del caso de uso.
 * P: Tipo de parámetros de entrada (Unit si no se necesitan).
 */
abstract class UseCase<out R, in P> {

    // Define un Dispatcher por defecto (generalmente IO o Default en la capa de Dominio)
    // El 'Dispatchers.Default' es adecuado para el cálculo pesado en KMP.
    // Para el consumo de APIs (en la capa de datos), 'Dispatchers.IO' se usa a menudo,
    // pero en commonMain, se mapea a un dispatcher apropiado.
    protected val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    // Método que se debe implementar con la lógica de negocios.
    protected abstract suspend fun execute(params: P): R

    /**
     * Llama al caso de uso, envolviendo la ejecución en un try/catch
     * y asegurando que se ejecute en el Dispatcher definido.
     */

    suspend operator fun invoke(params: P): Result<R> {
        return try {
            val result = withContext(defaultDispatcher) {
                execute(params) // Ejecuta y obtiene R
            }
            Result.Success(result) // Envuelve el éxito en tu Result personalizado
        } catch (e: Exception) {
            // Maneja la excepción y la envuelve en tu Result personalizado
            Result.Error(e)
        }
    }
}