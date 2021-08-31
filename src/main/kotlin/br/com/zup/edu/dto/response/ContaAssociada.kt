package br.com.zup.edu.dto.response

import io.micronaut.data.annotation.Embeddable

@Embeddable
class ContaAssociada (
    val instituicao: String = "",
    val nomeDoTitular: String = "",
    val cpfDoTitular: String = "",
    val agencia: String = "",
    val numeroDaConta: String = "",
)
