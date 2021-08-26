package br.com.zup.edu.chavePix.registrarChave.error

import io.micronaut.aop.Around
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

@Around
@MustBeDocumented
@Retention(RUNTIME)
@Target(CLASS, FUNCTION)
annotation class ErrorHandler
