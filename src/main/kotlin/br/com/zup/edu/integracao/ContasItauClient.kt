package br.com.zup.edu.integracao

import br.com.zup.edu.dto.response.DadosContaResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:9091")
interface ContasItauClient {

    @Get("/api/v1/clientes/{clienteId}/contas{?tipo}")
    fun buscarContaPorTipo(@PathVariable clienteId: String, @QueryValue tipo: String): HttpResponse<DadosContaResponse>

}