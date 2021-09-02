package br.com.zup.edu.chavePix.registrarChave.repository

import br.com.zup.edu.chavePix.ChavePix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository : JpaRepository<ChavePix, UUID> {
    fun existsByChave(chave: String): Boolean
}