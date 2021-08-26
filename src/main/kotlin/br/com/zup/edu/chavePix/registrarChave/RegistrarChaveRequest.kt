package br.com.zup.edu.chavePix.registrarChave

import br.com.zup.edu.RegistrarChaveRequest
import br.com.zup.edu.chavePix.ChavePix

fun RegistrarChaveRequest.toModel() : ChavePix {
    return ChavePix(this.idCliente, this.tipoDeChave, this.chave, this.tipoDeConta)
}