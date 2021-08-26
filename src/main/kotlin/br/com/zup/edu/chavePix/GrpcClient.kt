package br.com.zup.edu.chavePix

import br.com.zup.edu.DesafioPixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClient {

    @Singleton
    fun chavePixClientStub(@GrpcChannel("chave-pix") channel: ManagedChannel) : DesafioPixServiceGrpc.DesafioPixServiceBlockingStub? {
        return DesafioPixServiceGrpc.newBlockingStub(channel)
    }
}