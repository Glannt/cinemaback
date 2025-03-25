package com.dotnt.cinemaback.utils;

import java.util.UUID;

public class BookingReferenceGenearator {
    public static String generateBookingReference() {
        return "BOOK-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();
    }
}
