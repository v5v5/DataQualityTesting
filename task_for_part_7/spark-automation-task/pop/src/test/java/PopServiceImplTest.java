import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.pop.AppConfig;
import com.epam.pop.PopService;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.SparkException;
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
    assertEquals(3, actualResult.size());
    assertEquals("mouse", actualResult.get(0));
  }

  @Test
  public void testSize() {
    // arrange
    var inputData = asList("mouse", "cat", "cheese", "mouse", "mouse", "cat");
    var rddLines = cxt.parallelize(inputData);

    // act
    var actualResult = ps.topWords(rddLines, 2);

    // assert
    assertEquals(2, actualResult.size());
    assertEquals("mouse", actualResult.get(0));
  }

  @Test
  public void testDataContainsNull() {
    // arrange
    var inputData = asList(
        null, "кошка", "кошка", null, "кошка", "cat", "cat", "狗", "狗", "狗");
    var rddLines = cxt.parallelize(inputData);

    // act-assert
    assertThrows(SparkException.class, () -> ps.topWords(rddLines, 1));
  }

  @Test
  public void testDataDoesNotContainNull() {
    // arrange
    var inputData = asList(
        "animal", "dog", "кошка", "кошка", "кошка", "cat", "cat", "狗", "狗", "狗");
    var rddLines = cxt.parallelize(inputData);

    // act-assert
    assertDoesNotThrow(() -> ps.topWords(rddLines, 1));
  }

  @Test
  public void testDataContainsSeveralWords() {
    var inputData = asList(
        "dog", "dog", "dog", "dog",
        "cat", "cat",
        "cat and dog",
        "dog and cat", "dog and cat");

    var countDog = inputData.stream()
        .flatMap(s -> List.of(s.split("\\s")).stream())
        .filter(f -> f.equals("dog")).count();
    System.out.println("Count dog = " + countDog);

    var countCat = inputData.stream()
        .flatMap(s -> List.of(s.split("\\s")).stream())
        .filter(f -> f.equals("cat")).count();
    System.out.println("Count cat = " + countCat);

    var rddLines = cxt.parallelize(inputData);

    // act
    var result = ps.topWords(rddLines, 4);
    System.out.println(result);

    // assert
    Assertions.assertEquals("dog", result.get(0));
  }

  @Test
  public void testDataContainsNotPrintedSymbols() {
    // arrange
    var inputData = asList(
        "dog", "dog", "dog\t", "dog",
        "cat", "cat",
        "cat\tdog",
        "dog\ncat", "dog\ncat",
        "cat\ncat", "cat\tmouse");

    var countDog = inputData.stream()
        .flatMap(s -> List.of(s.split("\\s")).stream())
        .filter(f -> f.equals("dog")).count();
    System.out.println("Count dog = " + countDog);

    var countCat = inputData.stream()
        .flatMap(s -> List.of(s.split("\\s")).stream())
        .filter(f -> f.equals("cat")).count();
    System.out.println("Count cat = " + countCat);

    var rddLines = cxt.parallelize(inputData);

    // act
    var result = ps.topWords(rddLines, 5);

    // assert
    assertEquals("cat", result.get(0));
  }
}


