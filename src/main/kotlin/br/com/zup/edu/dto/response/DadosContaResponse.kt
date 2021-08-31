package br.com.zup.edu.dto.response

import io.micronaut.data.annotation.Embeddable

@Embeddable
data class DadosContaResponse(
    val tipo: String,
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularClientResponse
) {
    fun toModel() : ContaAssociada {
        return ContaAssociada(
            this.instituicao.nome,
            this.titular.nome,
            this.titular.cpf,
            this.agencia,
            this.numero
        )
    }
}
