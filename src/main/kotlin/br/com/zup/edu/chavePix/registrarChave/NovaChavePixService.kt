package br.com.zup.edu.chavePix.registrarChave

import br.com.zup.edu.chavePix.ChavePix
import br.com.zup.edu.chavePix.registrarChave.repository.ChavePixRepository
import br.com.zup.edu.chavePix.registrarChave.error.ChavePixJaCadastradaException
import br.com.zup.edu.integracao.ContasItauClient
import io.micronaut.validation.Validated
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Singleton
@Validated
class NovaChavePixService (@Inject var repository: ChavePixRepository,
                           @Inject var contasItauClient: ContasItauClient) {

    @Transactional
    fun registra (@Valid novaChavePix: NovaChavePix) : ChavePix {

        if (repository.existsByChave(novaChavePix.chave)) {
            throw ChavePixJaCadastradaException("Chave Pix \"${novaChavePix.chave}\" já existe")
        }

        val response = contasItauClient.buscarContaPorTipo(novaChavePix.clienteId, novaChavePix.tipoDeConta!!.name)
        val conta = response.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado!")

        val chave = novaChavePix.toModel(conta)
        repository.save(chave)

        return chave
    }
}
