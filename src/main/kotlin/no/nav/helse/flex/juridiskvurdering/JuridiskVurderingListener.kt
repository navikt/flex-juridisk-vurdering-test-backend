package no.nav.helse.flex.juridiskvurdering

import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class JuridiskVurderingListener(

    private val juridiskVurderingRepository: JuridiskVurderingRepository,
) {

    @KafkaListener(
        topics = ["flex.juridisk-vurdering-test"],
        idIsGroup = false,
        containerFactory = "aivenKafkaListenerContainerFactory",
    )
    fun listen(cr: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {

        val fnr = cr.value().tilMedFnr().fødselsnummer
        juridiskVurderingRepository.save(
            JuridiskVurderingDbRecord(
                id = null,
                fnr = fnr,
                opprettet = OffsetDateTime.now(),
                juridiskVurdering = cr.value()
            )
        )

        acknowledgment.acknowledge()
    }

    data class MedFnr(val fødselsnummer: String)

    fun String.tilMedFnr(): MedFnr = objectMapper.readValue(this)
}
