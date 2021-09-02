package br.com.zup.edu.chavePix

import br.com.zup.edu.DesafioPixServiceGrpc
import br.com.zup.edu.RegistrarChaveRequest
import br.com.zup.edu.dto.request.NovaChavePixRequest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/")
class ChavePixController(@Inject val grpcClient: DesafioPixServiceGrpc.DesafioPixServiceBlockingStub) {

    @Post("/api/chave-pix/")
    fun registrarChavePix(@Body @Valid request: NovaChavePixRequest): HttpResponse<Any> {
        val novaChave = RegistrarChaveRequest.newBuilder()
            .setChave(request.chave)
            .setClienteId(request.idCliente)
            .setTipoDeChave(request.tipoDeChave)
            .setTipoDeConta(request.tipoDeConta)
            .build()

        try {
            val responseNovaChave = grpcClient.registrarChave(novaChave)
            val uriBuilder =
                UriBuilder.of("api/chave-pix/{id}").expand(mutableMapOf(Pair("id", responseNovaChave.pixId)))
            return HttpResponse.created<Any?>(uriBuilder).body(responseNovaChave.pixId)
        } catch (e: StatusRuntimeException) {
//            e.printStackTrace()
            val status = e.status
            val statusCode = status.code
            val description = status.description

            if (statusCode == Status.Code.INVALID_ARGUMENT) {
                throw HttpStatusException(HttpStatus.BAD_REQUEST, description)
            }

            throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }

    }
}