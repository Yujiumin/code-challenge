import org.example.ping.lock.RateLimitLock
import org.example.ping.task.PingTask
import org.springframework.test.annotation.Repeat
import spock.lang.Specification

/**
 *
 * @author Michael
 * @version 2024/6/24
 */
class PingTaskTest extends Specification {

    @Repeat(100)
    def "test sayHello"() {
        when:
        RateLimitLock.tryLock(lock -> {
            PingTask.sayHello(lock)
        }, ex -> { })
        then:
        noExceptionThrown()
    }
}
