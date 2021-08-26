package br.com.zup.edu.chavePix.registrarChave

import br.com.zup.edu.chavePix.ChavePixRepository
import br.com.zup.edu.DesafioPixServiceGrpc
import br.com.zup.edu.RegistrarChaveRequest
import br.com.zup.edu.RegistrarChaveResponse
import br.com.zup.edu.chavePix.registrarChave.error.ErrorHandler
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class RegistroChavePixEndpoint(@Inject var repository: ChavePixRepository) : DesafioPixServiceGrpc.DesafioPixServiceImplBase() {

    @ErrorHandler
    override fun registrarChave(
        request: RegistrarChaveRequest?,
        responseObserver: StreamObserver<RegistrarChaveResponse>?
    ) {
        val novaChavePix = request?.toModel()

        repository.save(novaChavePix)

        responseObserver?.onNext(RegistrarChaveResponse.newBuilder().setIdChaveCriada(novaChavePix?.id.toString()).build())
        responseObserver?.onCompleted()
    }
}