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

    val log = logger()

    @KafkaListener(
        topics = ["flex.omrade-helse-etterlevelse"],
        idIsGroup = false,
        containerFactory = "aivenKafkaListenerContainerFactory",
    )
    fun listen(cr: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        val key = cr.key()
        val value = cr.value()

        data class FelterFraVurdering(val paragraf: String, val utfall: String)
        try {

            if (key.erFnr()) {

                val deserialisert: FelterFraVurdering = objectMapper.readValue(value)

                juridiskVurderingRepository.save(
                    JuridiskVurderingDbRecord(
                        id = null,
                        fnr = key,
                        opprettet = OffsetDateTime.now(),
                        juridiskVurdering = value,
                        paragraf = deserialisert.paragraf,
                        utfall = deserialisert.utfall,
                    )
                )
            } else {
                log.error("Key på kafka er ikke fnr: $key , hele meldingen: $value")
            }
        } catch (e: Exception) {
            log.error("Feil ved prossesering av melding på kafka. denne forkastes. $key - $value ", e)
        } finally {
            acknowledgment.acknowledge()
        }
    }
}
