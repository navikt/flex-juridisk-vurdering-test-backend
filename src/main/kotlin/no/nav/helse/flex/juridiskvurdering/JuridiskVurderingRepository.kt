package no.nav.helse.flex.juridiskvurdering

import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
interface JuridiskVurderingRepository : CrudRepository<JuridiskVurderingDbRecord, String> {
    fun findByFnr(fnr: String): List<JuridiskVurderingDbRecord>
    @Modifying
    @Query("delete from Syketilfellebit s where s.fnr = :fnr")
    fun deleteByFnr(fnr: String): Long
}

@Table("syketilfellebit")
data class JuridiskVurderingDbRecord(
    @Id
    val id: String? = null,
    val fnr: String,
    val opprettet: OffsetDateTime,
    val juridiskVurdering: String,
)
