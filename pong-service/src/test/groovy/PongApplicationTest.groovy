import org.example.pong.PongApplication
import org.example.pong.controller.HelloController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper
import org.springframework.test.context.BootstrapWith
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

/**
 *
 * @author Michael
 * @version 2024/6/24
 */
@BootstrapWith(SpringBootTestContextBootstrapper)
@AutoConfigureWebTestClient
@SpringBootTest(classes = PongApplication.class, useMainMethod = SpringBootTest.UseMainMethod.ALWAYS, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PongApplicationTest extends Specification {

    @Autowired(required = false)
    private HelloController helloController;


    def "when context is loaded then all expected beans are created"() {
        expect: "the HelloController is created"
        helloController
    }

    @Autowired
    private WebTestClient webTestClient;

    def "test sayHello"() {
        expect:
        webTestClient.post()
                .uri("/hello")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("World")
    }


}
