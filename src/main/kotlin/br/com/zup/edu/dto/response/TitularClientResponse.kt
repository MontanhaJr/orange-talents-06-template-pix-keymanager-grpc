package br.com.zup.edu.dto.response

import io.micronaut.data.annotation.Embeddable

@Embeddable
data class TitularClientResponse (
    val id: String,
    val nome: String,
    val cpf: String
)
