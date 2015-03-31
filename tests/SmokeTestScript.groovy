import com.vkharlamov.grooviola.TestDataLoader
import ru.stroganov.hh.api.test.SmokeTest
import ru.stroganov.hh.api.test.TestDataProvider

/**
 * Created by stroganov on 27.03.2015.
 */
def dataLoader = new TestDataLoader();
include(new SmokeTest(new TestDataProvider(dataLoader.getTestData(new File('data')))))