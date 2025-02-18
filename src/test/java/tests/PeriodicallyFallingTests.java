package tests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static properties.TestTags.ENTITIES;
import static properties.TestTags.SMOKE;
import static properties.TestTags.TIME;

@Execution(value = CONCURRENT)
@Tag(TIME)
public class PeriodicallyFallingTests {

    @Test
    @Tag(SMOKE)
    @AllureId("69")
    @DisplayName("Текущее время делится без остатка на 2")
    public void testTimeIsAMultipleOfTwo() {
        assertEquals(0, System.currentTimeMillis() % 2);
    }

    @Test
    @AllureId("70")
    @DisplayName("Текущее время делится без остатка на 3")
    public void testTimeIsAMultipleOfThree() {
        assertEquals(0, System.currentTimeMillis() % 3);
    }

    @Test
    @AllureId("71")
    @DisplayName("Текущее время делится без остатка на 8")
    public void testTimeIsAMultipleOfEight(){
        assertEquals(0, System.currentTimeMillis() % 8);
    }

    @Test
    @AllureId("72")
    @DisplayName("Текущее время делится без остатка на 10")
    public void testTimeIsAMultipleOfTen(){
        assertEquals(0, System.currentTimeMillis() % 10);
    }
}
