package no.nav.helse.flex.juridiskvurdering

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class TestdataResetListener(val juridiskVurderingRepository: JuridiskVurderingRepository) {

    val log = logger()

    @KafkaListener(
        topics = [TESTDATA_RESET_TOPIC],
        containerFactory = "aivenKafkaListenerContainerFactory",
        properties = ["auto.offset.reset = latest"]
    )
    fun listen(cr: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        val fnr = cr.value()
        val antall = juridiskVurderingRepository.deleteByFnr(fnr)
        log.info("Slettet $antall vurderinger på fnr $fnr - Key ${cr.key()}")
        acknowledgment.acknowledge()
    }
}

const val TESTDATA_RESET_TOPIC = "flex.testdata-reset"
