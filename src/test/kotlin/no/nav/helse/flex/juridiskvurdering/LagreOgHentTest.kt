package no.nav.helse.flex.juridiskvurdering

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBeEmpty
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class LagreOgHentTest : FellesTestOppsett() {
    @Autowired
    lateinit var juridiskVurderingListener: JuridiskVurderingListener

    @Autowired
    lateinit var juridiskVurderingRepository: JuridiskVurderingRepository

    @Test
    fun contextLoads() {
        val json = """{ "fodselsnummer": "12345", "paragraf": "1203", "utfall": "OK" }"""
        juridiskVurderingListener.listen(ConsumerRecord("topic", 0, 0L, "12345678987", json)) { }

        juridiskVurderingRepository.findByFnr("12345678987").first().juridiskVurdering `should be equal to` json

        juridiskVurderingRepository.deleteByFnr("12345678987")

        juridiskVurderingRepository.findByFnr("12345678987").shouldBeEmpty()
    }
}
