package no.nav.helse.flex.juridiskvurdering

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api"])
class JuridiskVurderingApi(
    private val juridiskVurderingRepository: JuridiskVurderingRepository,
) {

    @ResponseBody
    @GetMapping(value = ["/vurderinger/{fnr}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hentVurderinger(@PathVariable fnr: String): List<JuridiskVurderingDbRecord> {
        return juridiskVurderingRepository.findByFnr(fnr)
    }
}
