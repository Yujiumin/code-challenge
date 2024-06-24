import org.example.ping.PingApplication
import org.example.ping.task.PingTask
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 *
 * @author Michael
 * @version 2024/6/23
 */
@SpringBootTest(classes = PingApplication.class,useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class PingApplicationTest extends Specification {

    @Autowired(required = false)
    private PingTask pingTask;

    def "test start application"() {
        expect:
        pingTask
    }

}
