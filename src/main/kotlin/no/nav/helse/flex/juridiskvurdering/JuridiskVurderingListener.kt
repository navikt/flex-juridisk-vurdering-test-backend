package no.nav.helse.flex.juridiskvurdering

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

        val fnr = cr.key()
        if (fnr.erFnr()) {
            juridiskVurderingRepository.save(
                JuridiskVurderingDbRecord(
                    id = null,
                    fnr = fnr,
                    opprettet = OffsetDateTime.now(),
                    juridiskVurdering = cr.value()
                )
            )
        } else {
            log.error("Key på kafka er ikke : $fnr , hele meldingen: ${cr.value()}")
        }

        acknowledgment.acknowledge()
    }
}
