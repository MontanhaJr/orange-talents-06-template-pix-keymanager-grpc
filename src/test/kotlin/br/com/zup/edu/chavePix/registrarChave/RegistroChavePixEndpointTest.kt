package br.com.zup.edu.chavePix.registrarChave

import br.com.zup.edu.DesafioPixServiceGrpc
import br.com.zup.edu.RegistrarChaveRequest
import br.com.zup.edu.TipoDeChave
import br.com.zup.edu.TipoDeConta
import br.com.zup.edu.chavePix.ChavePix
import br.com.zup.edu.chavePix.GrpcClient
import br.com.zup.edu.chavePix.registrarChave.error.ChavePixJaCadastradaException
import br.com.zup.edu.chavePix.registrarChave.repository.ChavePixRepository
import br.com.zup.edu.dto.response.ContaAssociada
import br.com.zup.edu.dto.response.DadosContaResponse
import br.com.zup.edu.dto.response.InstituicaoResponse
import br.com.zup.edu.dto.response.TitularClientResponse
import br.com.zup.edu.integracao.ContasItauClient
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.lang.RuntimeException
import java.util.*
import javax.inject.Inject
import javax.validation.ConstraintViolationException

@MicronautTest(transactional = false)
internal class RegistroChavePixEndpointTest (val repository: ChavePixRepository, val grpcClient: DesafioPixServiceGrpc.DesafioPixServiceBlockingStub){

    @Inject
    lateinit var itauClient: ContasItauClient

    companion object {
        val CLIENT_ID = UUID.randomUUID()
    }

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    internal fun `deve cadastrar nova chave pix e retornar o id do cliente e da chave`() {
        // cenário
        `when`(itauClient.buscarContaPorTipo(clienteId = CLIENT_ID.toString(), tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.ok(dadosContaResponse()))

        // ação
        val response = grpcClient.registrarChave(RegistrarChaveRequest.newBuilder()
                                                            .setClienteId(CLIENT_ID.toString())
                                                            .setTipoDeChave(TipoDeChave.EMAIL)
                                                            .setChave("mauricio@gmail.com")
                                                            .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
                                                            .build())

        // validação

        with(response) {
            assertEquals(CLIENT_ID.toString(), clienteId)
            assertNotNull(pixId)
        }
    }

    @Test
    internal fun `nao deve registrar chave pix quando ela ja existe`() {
        // cenário
            repository.save(ChavePix(
                tipoDeChave = br.com.zup.edu.chavePix.TipoDeChave.CPF,
                chave = "47095526907",
                clienteId = CLIENT_ID,
                tipoDeConta = TipoDeConta.CONTA_CORRENTE,
                conta = ContaAssociada()
            ))

        // ação
        val assertThrows = assertThrows<RuntimeException> {
            grpcClient.registrarChave(
                RegistrarChaveRequest.newBuilder()
                    .setClienteId(CLIENT_ID.toString())
                    .setTipoDeChave(TipoDeChave.CPF)
                    .setChave("47095526907")
                    .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
                    .build()
            )
        }

        // validação
        with(assertThrows) {
            assertEquals("ALREADY_EXISTS: Chave Pix \"47095526907\" já existe", this.message)
        }
    }

    @Test
    internal fun `nao deve registrar chave pix se nao encontrar a conta`() {
        // cenário
        `when`(itauClient.buscarContaPorTipo(clienteId = CLIENT_ID.toString(), TipoDeConta.CONTA_CORRENTE.name))
            .thenReturn(HttpResponse.notFound())

        // ação
        val assertThrows = assertThrows<StatusRuntimeException> {
            grpcClient.registrarChave(
                RegistrarChaveRequest.newBuilder()
                    .setClienteId(CLIENT_ID.toString())
                    .setTipoDeChave(TipoDeChave.TELEFONE)
                    .setChave("+5547999999999")
                    .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
                    .build()
            )
        }

        // validação
        with(assertThrows) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Cliente não encontrado!", status.description)
        }
    }

    @Test
    internal fun `nao deve registrar chave pix se nao tiver o tipo de conta`() {
        // ação
        val assertThrows = assertThrows<StatusRuntimeException> {
            grpcClient.registrarChave(RegistrarChaveRequest.newBuilder()
                .setClienteId(CLIENT_ID.toString())
                .setTipoDeChave(TipoDeChave.TELEFONE)
                .setChave("+5547999999999")
                .build())
        }

        // validação
        with(assertThrows) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Parâmetros de entrada inválidos", status.description)
        }
    }

    @MockBean(ContasItauClient::class)
    fun itauClient(): ContasItauClient? {
        return Mockito.mock(ContasItauClient::class.java)
    }

    private fun dadosContaResponse(): DadosContaResponse {
        return DadosContaResponse(
            tipo = "CONTA_CORRENTE",
            instituicao = InstituicaoResponse("UNIBANCO ITAU SA", "ITAU_UNIBANCO_ISBP"),
            agencia = "1218",
            numero = "291900",
            titular = TitularClientResponse("c56dfef4-7901-44fb-84e2-a2cefb157890", "Rafael M C Ponte", "02467781054")
        )
    }

}