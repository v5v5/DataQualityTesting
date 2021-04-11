import com.epam.pop.AppConfig;
import com.epam.pop.PoetryAnalyzer;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class PoetryAnalyzerIT {

    @Autowired
    private PoetryAnalyzer analyzer;


    /**
     * Task:
     * <ul>
     * <li>
     * 1) start PoetryAnalyzer service (list of available popstars are: 2pac, gaga, perry, tool). You can
     * use your own set of lyrics, if you are curious;
     * </li>
     * <li>
     * 2) make sure it's working as expected (see corresponding Javadoc) - write some positive tests;
     * </li>
     * <li>
     * 3) try to break the service. Is there a way to avoid such breaks? How you would validate it?
     * </li>
     * <li>
     * let's say we need to do a data migration. Create a test, that 100% determines, if any text file is missing
     * after the migration.
     * </li>
     * </ul>
     */
    @Test
    public void testGaga() {
        var words = analyzer.mostPopularWords("2pac", 3);
        System.out.println(words.toString());
    }

    @Value("${rootPath}")
    private String path;

    @Test
    public void testTheResultOfMostPopularWordsIsTheSame() throws IOException {
        // arrange
//        var poetList = List.of("2pac", "gaga", "perry", "tool");
        var poetList = Files.list(Path.of(path))
            .map(d->d.getFileName().toString())
            .collect(Collectors.toList());
        System.out.println(poetList);

        for (var poet: poetList) {
            System.out.println(poet);
            // act
            var expectedWords = analyzer.mostPopularWords(poet, 5);
            System.out.println(expectedWords);
            var actualWords = analyzer.mostPopularWords(poet, 5);
            // assert
            Assertions.assertEquals(expectedWords, actualWords);
        }
    }

    @Test
    public void testTheUnexpectedDataDoesNotTotallyBreakTheService() {
        // arrange
        var unexpectedPoet = RandomStringUtils.randomAlphanumeric(10);

        // act
        try {
            var expectedWords = analyzer.mostPopularWords(unexpectedPoet, 5);
        } catch (Exception ex) {
            System.out.println("The unexpected data cause Exception");
            ex.printStackTrace();
        }

        // assert
        var poet = "perry";
        var expectedWords = analyzer.mostPopularWords(poet, 5);
        var actualWords = analyzer.mostPopularWords(poet, 5);
        Assertions.assertEquals(expectedWords, actualWords);
        System.out.println("The service is still working");
    }

    @Test
    public void testEncodingIncompatibilitiesCauseOutputResultIsNotCorrect(){
        var poet = "perry";
        byte[] bytes = poet.getBytes(StandardCharsets.UTF_8);
        poet = new String(bytes, StandardCharsets.UTF_16);

        String finalPoet = poet;
        Assertions.assertThrows(Exception.class, () -> analyzer.mostPopularWords(finalPoet, 5));
    }
}
