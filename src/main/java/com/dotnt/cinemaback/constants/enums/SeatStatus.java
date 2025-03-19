package com.dotnt.cinemaback.constants.enums;

public enum SeatStatus {
    AVAILABLE,   // Ghế còn trống
    RESERVED,    // Ghế đã được đặt trước
    OCCUPIED,    // Ghế đang được sử dụng
    OUT_OF_ORDER, // Ghế bị hỏng, không thể sử dụng
    INACTIVE     // Ghế không hoạt động
}
