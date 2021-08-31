package br.com.zup.edu.chavePix.registrarChave

import br.com.zup.edu.DesafioPixServiceGrpc
import br.com.zup.edu.RegistrarChaveRequest
import br.com.zup.edu.RegistrarChaveResponse
import br.com.zup.edu.chavePix.registrarChave.error.ErrorHandler
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class RegistroChavePixEndpoint(
    @Inject private val service: NovaChavePixService
) : DesafioPixServiceGrpc.DesafioPixServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ErrorHandler
    override fun registrarChave(
        request: RegistrarChaveRequest?,
        responseObserver: StreamObserver<RegistrarChaveResponse>?
    ) {
        val novaChavePix = request!!.toModel()
        val chaveCriada = service.registra(novaChavePix)


        responseObserver?.onNext(RegistrarChaveResponse.newBuilder()
            .setPixId(chaveCriada.id.toString())
            .setClienteId(chaveCriada.clienteId.toString())
            .build())
        responseObserver?.onCompleted()
    }
}