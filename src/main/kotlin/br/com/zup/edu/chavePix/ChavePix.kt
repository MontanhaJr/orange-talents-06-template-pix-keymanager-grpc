package br.com.zup.edu.chavePix

import br.com.zup.edu.TipoDeChave
import br.com.zup.edu.TipoDeConta
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
data class ChavePix(
    @field:NotNull @field:NotBlank var idCliente: String,
    @field:NotNull @field:NotBlank var tipoDeChave: TipoDeChave,
    var chave: String,
    @field:NotNull @field:NotBlank var tipoDeConta: TipoDeConta
) {
    @Id
    @GeneratedValue
    var id: Long? = null
}