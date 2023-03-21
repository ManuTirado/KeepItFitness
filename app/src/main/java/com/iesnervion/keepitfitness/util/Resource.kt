package com.iesnervion.keepitfitness.util

/**
 * Una clase genérica que contiene un valor con su estado de la operación.
 * [Success] contiene el valor de la operación (true si fue correcta).
 * [Error] contiene el mensaje de error.
 * [Loading] indica que la operación está en curso.
 * [Finished] indica que la operación ha finalizado.
 * @param <R> El tipo de dato que contiene.
 */
sealed class Resource<out R> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object Finished : Resource<Nothing>()
}