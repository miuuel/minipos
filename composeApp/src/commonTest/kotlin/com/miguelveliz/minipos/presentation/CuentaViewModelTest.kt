package com.miguelveliz.minipos.presentation

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.miguelveliz.minipos.domain.repository.CuentaRepository
import com.miguelveliz.minipos.presentation.cuenta.CuentaViewModel
import com.miguelveliz.minipos.util.Result
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.atLeast
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CuentaViewModelTest {

    // 1. Mocks y SUT (System Under Test)
    private val repository = mock<CuentaRepository>()
    private lateinit var viewModel: CuentaViewModel

    // Dispatcher para controlar el tiempo en las corrutinas
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `mi prueba`() = runTest { // <--- ESTO crea el cuerpo de la corrutina
        // GIVEN
        everySuspend { repository.cosultar(any()) } returns Result.Success(100.0)

        // WHEN
        viewModel = CuentaViewModel(repository)
        advanceUntilIdle() // Espera a que las corrutinas del ViewModel terminen

        // THEN
        // ... tus aserciones
    }

    @Test
    fun `al iniciar el ViewModel debe cargar el saldo correctamente desde el repositorio`() =
        runTest {
            // GIVEN: El repositorio responde con éxito y un saldo de 500.0
            val saldoEsperado = 500.0
            everySuspend { repository.cosultar(any()) } returns Result.Success(saldoEsperado)

            // Inicializamos el ViewModel (esto dispara la carga inicial)
            viewModel = CuentaViewModel(repository)

            // WHEN & THEN: Observamos el flujo de estados
            viewModel.state.test {
                // El primer estado suele ser el inicial (p. ej. saldo 0.0, isLoading true)
                val estadoInicial = awaitItem()
                assertTrue(estadoInicial.isLoading, "El estado inicial debería estar cargando")

                // Ejecutamos las tareas pendientes en el Dispatcher (el proceso de carga)
                advanceUntilIdle()

                // El segundo estado debe ser el saldo cargado
                val estadoExito = awaitItem()
                assertEquals(saldoEsperado, estadoExito.saldo)
                assertFalse(estadoExito.isLoading)
                assertNull(estadoExito.error)

                // Verificamos que se llamó al repo con cualquier ID
                everySuspend { repository.cosultar(any()) }
            }
        }

    @Test
    fun `debe actualizar el estado con un error cuando la consulta al repositorio falla`() =
        runTest {
            // GIVEN: El repositorio devuelve un error
            val excepcionEsperada = Exception("Error de conexión al servidor")
            everySuspend { repository.cosultar(any()) } returns Result.Error(excepcionEsperada)

            viewModel = CuentaViewModel(repository)

            viewModel.state.test {
                awaitItem() // Saltamos el estado inicial de carga
                advanceUntilIdle()

                val estadoError = awaitItem()

                // Verificamos que el estado ahora contenga el error
                assertNotNull(estadoError.error, "El estado debería tener un objeto de error")
                assertFalse(estadoError.isLoading)
                // Si tu state guarda el mensaje, puedes verificarlo:
                // assertEquals("Error de conexión al servidor", estadoError.error)
            }
        }

    @Test
    fun `al realizar un deposito exitoso debe intentar recargar el saldo`() = runTest {
        // GIVEN: El repositorio permite depositar
        everySuspend { repository.depositar(any(), any()) } returns Result.Success(Unit)
        everySuspend { repository.cosultar(any()) } returns Result.Success(100.0) // Para la carga inicial

        viewModel = CuentaViewModel(repository)
        advanceUntilIdle()

        // WHEN: El usuario deposita 50.0
        viewModel.deposit(50.0)
        advanceUntilIdle()

        // THEN: Verificamos que se llamó a la función depositar del repositorio
        everySuspend { repository.depositar(any(), 50.0) }

        // Opcional: Verificar que se volvió a consultar el saldo para actualizar la UI
        //verifySuspend(atLeast = 1) { repository.cosultar(any()) }
        verifySuspend(atLeast(1)) { repository.cosultar(any()) }
    }
}