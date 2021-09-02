package br.com.zup.edu.chavePix.registrarChave

import br.com.zup.edu.RegistrarChaveRequest
import br.com.zup.edu.TipoDeChave.UNKNOWN_TIPO_CHAVE
import br.com.zup.edu.TipoDeConta
import br.com.zup.edu.TipoDeConta.UNKNOWN_TIPO_CONTA
import br.com.zup.edu.chavePix.TipoDeChave

fun RegistrarChaveRequest.toModel(): NovaChavePix {
    return NovaChavePix(
        clienteId = this.clienteId,
        tipoDeChave = when (tipoDeChave) {
            UNKNOWN_TIPO_CHAVE -> null
            else -> TipoDeChave.valueOf(tipoDeChave.name)
        },
        chave = this.chave,
        tipoDeConta = when (tipoDeConta) {
            UNKNOWN_TIPO_CONTA -> null
            else -> TipoDeConta.valueOf(tipoDeConta.name)
        }
    )
}