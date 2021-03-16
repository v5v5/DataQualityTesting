import static java.util.Arrays.asList;

import com.epam.pop.AppConfig;
import com.epam.pop.PopService;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class PopServiceImplTest {

  @Autowired
  private JavaSparkContext cxt;

  @Autowired
  private PopService ps;

  /**
   * Task: Make sure {@link PopService#topWords} works as expected. Create tests for each test case
   * (as many as you think is enough). Check corresponding javadoc to get acceptance criteria.
   * <p>
   * All required beans (class instances) are already injected into this class.
   * <p>
   * Suppose, you should get familiar with SparkContext API. Find all the required information in
   * javadocs
   *
   * @see <a href="https://spark.apache.org/docs/2.1.0/api/java/index.html?org/apache/spark/api/java/JavaSparkContext.html">
   * JavaSparkContext</a>
   * @see PopService#topWords
   */
  @Test
  public void testThisThing() {
    // arrange
    var inputData = asList("mouse", "cat", "cheese", "mouse", "mouse", "cat");
    var rddLines = cxt.parallelize(inputData);

    // act
    var actualResult = ps.topWords(rddLines, 3);

    // assert
    Assertions.assertEquals(3, actualResult.size());
    Assertions.assertEquals("mouse", actualResult.get(0));
  }

  @Test
  public void testSize() {
    // arrange
    var inputData = asList("mouse", "cat", "cheese", "mouse", "mouse", "cat");
    var rddLines = cxt.parallelize(inputData);

    // act
    var actualResult = ps.topWords(rddLines, 2);

    // assert
    Assertions.assertEquals(2, actualResult.size());
    Assertions.assertEquals("mouse", actualResult.get(0));
  }
}
