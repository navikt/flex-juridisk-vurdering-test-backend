package no.nav.helse.flex.juridiskvurdering

import com.fasterxml.jackson.annotation.JsonRawValue
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

@RestController
@RequestMapping(value = ["/api"])
class JuridiskVurderingApi(
    private val juridiskVurderingRepository: JuridiskVurderingRepository,
) {
    @ResponseBody
    @GetMapping(value = ["/vurderinger/"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    fun hentAlleVurderinger(
        @RequestParam paragraf: String,
    ): List<VurderingResponse> {
        return juridiskVurderingRepository.findByParagraf(paragraf).map { it.tilVurderingResponse() }
    }

    @ResponseBody
    @GetMapping(value = ["/vurderinger/{fnr}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @CrossOrigin
    fun hentVurderinger(
        @PathVariable fnr: String,
    ): List<VurderingResponse> {
        return juridiskVurderingRepository.findByFnr(fnr).map { it.tilVurderingResponse() }
    }

    private fun JuridiskVurderingDbRecord.tilVurderingResponse(): VurderingResponse =
        VurderingResponse(fnr = fnr, opprettet = opprettet, vurdering = juridiskVurdering)

    data class VurderingResponse(
        val fnr: String,
        val opprettet: OffsetDateTime,
        @JsonRawValue
        val vurdering: String,
    )
}
