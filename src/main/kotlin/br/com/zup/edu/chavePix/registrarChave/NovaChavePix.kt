package br.com.zup.edu.chavePix.registrarChave

import br.com.zup.edu.TipoDeConta
import br.com.zup.edu.chavePix.ChavePix
import br.com.zup.edu.chavePix.TipoDeChave
import br.com.zup.edu.chavePix.registrarChave.validation.ValidUUID
import br.com.zup.edu.dto.response.ContaAssociada
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
data class NovaChavePix(
    @ValidUUID
    @field:NotBlank
    val clienteId: String,

    @field:NotNull
    val tipoDeChave: TipoDeChave?,

    @field:Size(max = 77)
    val chave: String,

    @field:NotNull
    val tipoDeConta: TipoDeConta?
) {
    fun toModel(conta: ContaAssociada): ChavePix {
        return ChavePix(
            clienteId = UUID.fromString(this.clienteId),
            tipoDeChave = TipoDeChave.valueOf(this.tipoDeChave!!.name),
            chave = if (this.tipoDeChave == TipoDeChave.ALEATORIA) UUID.randomUUID().toString() else this.chave,
            tipoDeConta = TipoDeConta.valueOf(this.tipoDeConta!!.name),
            conta = conta
        )
    }

}
