package com.brankogeorgiev.di

import com.brankogeorgiev.navigation.Navigator
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinModule = module {
    singleOf(::Navigator)
}

fun initializeKoin(
    config: (KoinApplication. () -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(koinModule)
    }
}
