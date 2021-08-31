package br.com.zup.edu.dto.response

import io.micronaut.data.annotation.Embeddable

@Embeddable
data class InstituicaoResponse(
    val nome: String,
    val ispb: String
)
