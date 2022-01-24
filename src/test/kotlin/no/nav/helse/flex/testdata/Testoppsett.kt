package no.nav.helse.flex.testdata

import no.nav.helse.flex.testdata.kafka.TESTDATA_RESET_TOPIC
import org.amshove.kluent.shouldBeEmpty
import org.apache.kafka.clients.consumer.Consumer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
abstract class Testoppsett {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var kafkaConsumer: Consumer<String, String?>

    companion object {
        init {
            KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.1.1")).also {
                it.start()
                System.setProperty("KAFKA_BROKERS", it.bootstrapServers)
            }
        }
    }

    @BeforeAll
    fun `Vi leser kafka topicet og feiler om noe eksisterer`() {
        kafkaConsumer.subscribeHvisIkkeSubscribed(TESTDATA_RESET_TOPIC)
        kafkaConsumer.hentProduserteRecords().shouldBeEmpty()
    }
    @AfterAll
    fun `Vi leser topicet og feiler hvis noe finnes og slik at subklassetestene leser alt`() {
        kafkaConsumer.hentProduserteRecords().shouldBeEmpty()
    }
}
