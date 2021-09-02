package br.com.zup.edu.dto.request

import br.com.zup.edu.TipoDeChave
import br.com.zup.edu.TipoDeConta
import io.micronaut.core.annotation.Introspected

@Introspected
data class NovaChavePixRequest(
    val idCliente: String,
    val tipoDeChave: TipoDeChave,
    val chave: String,
    val tipoDeConta: TipoDeConta
)
