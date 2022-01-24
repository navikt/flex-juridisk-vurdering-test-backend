package no.nav.helse.flex.juridiskvurdering

import org.amshove.kluent.`should be equal to`
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class LagreOgHentTest : Testoppsett() {

    @Autowired
    lateinit var juridiskVurderingListener: JuridiskVurderingListener

    @Autowired
    lateinit var juridiskVurderingRepository: JuridiskVurderingRepository

    @Test
    fun contextLoads() {
        val json = """{ "fødselsnummer": "12345", "annenKey": 123 }"""
        juridiskVurderingListener.listen(ConsumerRecord("topic", 0, 0L, "fg", json)) { }

        juridiskVurderingRepository.findByFnr("12345").first().juridiskVurdering `should be equal to` json
    }
}
