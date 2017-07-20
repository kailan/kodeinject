import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import org.junit.Before
import org.junit.Test
import pw.kmp.kodeinject.annotations.OrNull
import pw.kmp.kodeinject.injected
import pw.kmp.kodeinject.injectedSingleton
import java.util.*

@Suppress("UNUSED_PARAMETER")
class InjectionTest {

    /**
     * Tests that dependencies are injected successfully into constructors.
     */
    @Test
    fun injectedDependency() {
        val client: DatabaseClient = app.instance()
        assert(client.db is MyDatabase)
    }

    /**
     * Tests that the 'Application' class only gets instantiated once.
     */
    @Test
    fun injectedSingletonDependency() {
        val app1: Application = app.instance()
        val app2: Application = app.instance()
        assert(app1 == app2)
        assert(initCount == 1)
    }

    /**
     * Tests that injection with missing dependencies works.
     */
    @Test
    fun nullableDependency() {
        try {
            assert(app.instance<ApplicationWithNullParameter>().date == null)
        } catch (ex: Throwable) {
            throw AssertionError("Injection with missing dependency failed.")
        }
    }

    // Mock Application

    lateinit var app: Kodein
    companion object {var initCount = 0}

    @Before
    fun setup() {
        app = Kodein {
            bind<Database>() with singleton { MyDatabase() }
            bind() from injected<DatabaseClient>()
            bind() from injectedSingleton<Application>()
            bind() from injectedSingleton<ApplicationWithNullParameter>()
        }
    }

    interface Database
    class MyDatabase : Database

    class DatabaseClient(val db: Database)
    class Application(client: DatabaseClient) {
        init {
            initCount++
        }
    }
    class ApplicationWithNullParameter(client: DatabaseClient, @OrNull val date: Date?)

}