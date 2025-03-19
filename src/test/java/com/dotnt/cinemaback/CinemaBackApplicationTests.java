package com.dotnt.cinemaback;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CinemaBackApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(CinemaBackApplicationTests.class);

    public static String[][] generateRowsSeats(int rows, int seatsPerRow) {
        String[][] rowsSeats = new String[rows][seatsPerRow];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seatsPerRow; j++) {
                rowsSeats[i][j] = (char) ('A' + i) + String.valueOf(j + 1);
            }
        }
        return rowsSeats;
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testGenerateRowsSeats() {
        int rows = 5;
        int seatsPerRow = 10;
        String[][] rowsSeats = generateRowsSeats(rows, seatsPerRow);
        assertEquals(rows, rowsSeats.length);
        log.info("rowsSeats: {}", rowsSeats);
//        assertEquals(seatsPerRow, rowsSeats[0].length);
//        assertEquals("A11", rowsSeats[0][0]);
//        assertEquals("A52", rowsSeats[4][9]);
    }

}
