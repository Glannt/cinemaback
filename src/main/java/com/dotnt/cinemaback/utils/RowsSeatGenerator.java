package com.dotnt.cinemaback.utils;

public class RowsSeatGenerator {
    public static String[][] generateRowsSeats(int rows, int seatsPerRow) {
        String[][] rowsSeats = new String[rows][seatsPerRow];
        for (int i = 0; i < rows; i++) {
            // Handle more than 26 rows by using AA, AB, etc. after Z
            String rowLabel;
            if (i < 26) {
                rowLabel = String.valueOf((char) ('A' + i));
            } else {
                rowLabel = String.valueOf((char) ('A' + (i / 26) - 1)) +
                        String.valueOf((char) ('A' + (i % 26)));
            }

            for (int j = 0; j < seatsPerRow; j++) {
                rowsSeats[i][j] = rowLabel + (j + 1);
            }
        }
        return rowsSeats;
    }

    public static String[] generateRowsLabels(int rows) {
        String[] rowsLabels = new String[rows];
        for (int i = 0; i < rows; i++) {
            // Handle more than 26 rows by using AA, AB, etc. after Z
            String rowLabel;
            if (i < 26) {
                rowLabel = String.valueOf((char) ('A' + i));
            } else {
                rowLabel = String.valueOf((char) ('A' + (i / 26) - 1)) +
                        String.valueOf((char) ('A' + (i % 26)));
            }
            rowsLabels[i] = rowLabel;
        }
        return rowsLabels;
    }
}
