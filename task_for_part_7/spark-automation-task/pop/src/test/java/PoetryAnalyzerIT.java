import static org.apache.commons.io.FileUtils.copyDirectory;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.pop.AppConfig;
import com.epam.pop.PoetryAnalyzer;
import com.epam.pop.WordData;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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
//        var popStarsList = List.of("2pac", "gaga", "perry", "tool");
        var popStarsList = Files.list(Path.of(path))
            .map(d->d.getFileName().toString())
            .collect(Collectors.toList());
        System.out.println(popStarsList);

        // act
        var assertionList = new ArrayList<Executable>();
        popStarsList.forEach(popStar -> {
            System.out.println(popStar);
            var expectedWords = analyzer.mostPopularWords(popStar, 5);
            System.out.println(expectedWords);
            var actualWords = analyzer.mostPopularWords(popStar, 5);

            assertionList.add(() -> assertEquals(expectedWords, actualWords));
        });

        // assert
        assertAll(assertionList);
    }

    @Test
    public void testTheUnexpectedDataDoesNotTotallyBreakTheService() {
        // arrange
        var unexpectedPopStar = RandomStringUtils.randomAlphanumeric(10);

        // act
        try {
            var expectedWords = analyzer.mostPopularWords(unexpectedPopStar, 5);
        } catch (Exception ex) {
            System.out.println("The unexpected data cause Exception");
            ex.printStackTrace();
        }

        // assert
        var popStar = "perry";
        var expectedWords = analyzer.mostPopularWords(popStar, 5);
        var actualWords = analyzer.mostPopularWords(popStar, 5);
        assertEquals(expectedWords, actualWords);
        System.out.println("The service is still working");
    }

    @Test
    public void testEncodingIncompatibilitiesCauseOutputResultIsNotCorrect(){
        var popStar = "perry";
        byte[] bytes = popStar.getBytes(StandardCharsets.UTF_8);
        popStar = new String(bytes, StandardCharsets.UTF_16);

        String finalPopStar = popStar;
        assertThrows(Exception.class, () -> analyzer.mostPopularWords(finalPopStar, 5));
    }

    @Autowired
    private WordData wordData;

    @Test
    public void testDataIsMissingAfterDataMigration() throws IOException {
        // arrange
        System.out.println("Migration with losses is started...");
        var migrationPath = ".\\migration\\";
        File srcDirectory = new File(path);
        File dstDirectory = new File(migrationPath);

        deleteDirectory(dstDirectory);
        copyDirectory(srcDirectory, dstDirectory);

        for (var popstarDir: dstDirectory.listFiles()) {
            System.out.println(
                Arrays.stream(popstarDir.listFiles())
                    .map(f -> f.getName())
                    .collect(Collectors.toList()));
            var popstarFiles = popstarDir.listFiles();
            var randomIndex = new Random()
                .ints(0, popstarFiles.length)
                .findFirst()
                .getAsInt();
            FileUtils.forceDelete(popstarFiles[randomIndex]);
            System.out.println(
                Arrays.stream(popstarDir.listFiles())
                    .map(f -> f.getName())
                    .collect(Collectors.toList()));
        }
        System.out.println("Migration with losses is done.");

        // act
        Dataset<Row> dataBeforeMigration = wordData.create(path + "*");
        Dataset<Row> dataAfterMigration = wordData.create(migrationPath + "*");

        // assert
        assertAll(
            () -> assertEquals(dataBeforeMigration.count(), dataAfterMigration.count()),
            () -> {
                deleteDirectory(dstDirectory);
                System.out.println(String.format("After test: directory '%s' is deleted", dstDirectory));
            }
        );
    }
}
