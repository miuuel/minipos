package com.miguelveliz.minipos.di


// Importaciones de Dominio
import com.miguelveliz.minipos.domain.usecase.Consultar
import com.miguelveliz.minipos.domain.usecase.Depositar
import com.miguelveliz.minipos.domain.repository.CuentaRepository
// Importaciones de Datos
import com.miguelveliz.minipos.data.repository.CuentaRepositoryImpl
import com.miguelveliz.minipos.data.source.CuentaDataSource
import com.miguelveliz.minipos.data.source.CuentaDataSourceImpl
import com.miguelveliz.minipos.presentation.cuenta.CuentaViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


// 1. Módulo de Dominio: Declara los Casos de Uso
val domainModule = module {
    // 'factory' crea una nueva instancia cada vez que se pide.
    factory { Consultar(get()) }
    factory { Depositar(get()) }
}

// 2. Módulo de Datos: Declara Repositorios e Data Sources
val dataModule = module {
    // 'single' crea una única instancia (Singleton) de la fuente de datos.
    single<CuentaDataSource> { CuentaDataSourceImpl() }

    // 'single' o 'factory' para el Repositorio, dependiendo de la necesidad.
    // Usamos 'single' para el Repositorio que maneja el saldo simulado.
    single<CuentaRepository> { CuentaRepositoryImpl(get()) }

    single<CuentaRepository> { CuentaRepositoryImpl(get()) }
}

val presentationModule = module {
    // Usamos 'factory' sin parámetros, ya que eliminamos el CoroutineScope del constructor
    factoryOf (::CuentaViewModel)
    //viewModelOf(::CounterViewModel)
    //factoryOf(::ListViewModel)
    //factoryOf(::DetailViewModel)
}

val sharedModules = listOf(domainModule, dataModule, presentationModule)
/**
 * Inicializa Koin con todos los módulos compartidos.
 * Esta función debe llamarse una vez al inicio de la aplicación
 * (e.g., en la clase Application de Android o en iOS al iniciar).
 */
/*fun initKoin() {
    startKoin {
        modules(sharedModules)
    }
}*/

// Asegúrate de que initKoin acepte un bloque de configuración
fun initKoin(appDeclaration: (KoinApplication) -> Unit = {}) {
    startKoin {
        appDeclaration(this) // Ejecuta el bloque de configuración (donde se llamará androidContext)
        modules(sharedModules)
    }
}

