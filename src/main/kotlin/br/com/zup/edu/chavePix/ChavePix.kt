package br.com.zup.edu.chavePix

import br.com.zup.edu.TipoDeChave
import br.com.zup.edu.TipoDeConta
import br.com.zup.edu.dto.response.ContaAssociada
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
data class ChavePix(
    @field:NotNull @field:NotBlank
    @Column(nullable = false)
    var clienteId: UUID,

    @field:NotNull @field:NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var tipoDeChave: br.com.zup.edu.chavePix.TipoDeChave,

    @field:Size(max = 77)
    var chave: String,

    @field:NotNull @field:NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var tipoDeConta: TipoDeConta,

    @field:Valid
    @Embedded
    val conta: ContaAssociada
) {
    @Id
    @GeneratedValue
    var id: Long? = null
}