package br.com.zup.edu.chavePix

import br.com.zup.edu.TipoDeConta
import br.com.zup.edu.dto.response.ContaAssociada
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
data class ChavePix(
    @field:NotNull
    @Column(nullable = false)
    var clienteId: UUID,

    @field:NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var tipoDeChave: TipoDeChave,

    @field:Size(max = 77)
    var chave: String,

    @field:NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var tipoDeConta: TipoDeConta,

    @Embedded
    val conta: ContaAssociada
) {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    var id: UUID? = null
}