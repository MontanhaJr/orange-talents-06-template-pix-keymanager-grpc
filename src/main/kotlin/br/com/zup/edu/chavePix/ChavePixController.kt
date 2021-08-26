package br.com.zup.edu.chavePix

import br.com.zup.edu.DesafioPixServiceGrpc
import br.com.zup.edu.RegistrarChaveRequest
import br.com.zup.edu.chavePix.registrarChave.NovaChavePixRequest
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
class ChavePixController (@Inject val grpcClient: DesafioPixServiceGrpc.DesafioPixServiceBlockingStub){

    @Post("/api/chave-pix/")
    fun registrarChavePix(@Body @Valid request: NovaChavePixRequest) : HttpResponse<Any> {
        val novaChave = RegistrarChaveRequest.newBuilder()
            .setChave(request.chave)
            .setIdCliente(request.idCliente)
            .setTipoDeChave(request.tipoDeChave)
            .setTipoDeConta(request.tipoDeConta)
            .build()

        try {
            val responseNovaChave = grpcClient.registrarChave(novaChave)
            val uriBuilder = UriBuilder.of("api/chave-pix/{id}").expand(mutableMapOf(Pair("id", responseNovaChave.idChaveCriada)))
            return HttpResponse.created<Any?>(uriBuilder).body(responseNovaChave.idChaveCriada)
        } catch (e: StatusRuntimeException) {
//            e.printStackTrace()
            throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }

    }
}