package tests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static properties.TestTags.SMOKE;

@Execution(value = CONCURRENT)
@Tag(SMOKE)
public class CheckDateTests {

    @Test
    @Tag(SMOKE)
    @AllureId("")
    @DisplayName("Текущее число месяца делится без остатка на 2")
    public void testDateIsAMultipleOfTwo() {
        assertEquals(0, LocalDate.now().getDayOfMonth() % 2);
    }
}
