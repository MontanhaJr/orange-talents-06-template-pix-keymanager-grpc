package br.com.zup.edu.chavePix

import br.com.zup.edu.DesafioPixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import javax.inject.Singleton

@Factory
class GrpcClient {

    @Bean
    fun chavePixClientStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel) : DesafioPixServiceGrpc.DesafioPixServiceBlockingStub? {
        return DesafioPixServiceGrpc.newBlockingStub(channel)
    }
}