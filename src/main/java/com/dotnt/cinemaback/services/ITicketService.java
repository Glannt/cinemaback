package com.dotnt.cinemaback.services;

import com.dotnt.cinemaback.dto.request.TicketRequestDTO;
import com.dotnt.cinemaback.dto.response.TicketResponseDTO;

public interface ITicketService {
    TicketResponseDTO createTicket(TicketRequestDTO request);
//    void deleteTicket(String id);
//    void deleteTicketByTransactionId(String transactionId);
//    void deleteTicketByBookedSeatId(String bookedSeatId);
//    void deleteTicketByDiscountId(String discountId);
//    void deleteTicketByTransactionIdAndBookedSeatId(String transactionId, String bookedSeatId);
//    void deleteTicketByTransactionIdAndDiscountId(String transactionId, String discountId);
//    void deleteTicketByBookedSeatIdAndDiscountId(String bookedSeatId, String discountId);
//    void deleteTicketByTransactionIdAndBookedSeatIdAndDiscountId(String transactionId, String bookedSeatId, String discountId);
//    void deleteAllTickets();
//    void deleteAllTicketsByTransactionId(String transactionId);
//    void deleteAllTicketsByBookedSeatId(String bookedSeatId);
//    void deleteAllTicketsByDiscountId(String discountId);
//    void deleteAllTicketsByTransactionIdAndBookedSeatId(String transactionId, String bookedSeatId);
//    void deleteAllTicketsByTransactionIdAndDiscountId(String transactionId, String discountId);
//    void deleteAllTicketsByBookedSeatIdAndDiscountId(String bookedSeatId, String discountId);
//    void deleteAllTicketsByTransactionIdAndBookedSeatIdAndDiscountId(String transactionId, String bookedSeatId, String discountId);
//    void deleteAllTicketsByTransactionIdIn(List<String> transactionIds);
//    void deleteAllTicketsByBookedSeatIdIn(List<String> bookedSeatIds);
//    void deleteAllTicketsByDiscountIdIn(List<String> discountIds);
//    void deleteAllTicketsByTransactionIdAndBookedSeatIdIn(List<String> transactionIds, List<String> bookedSeatIds);
//    void deleteAllTicketsByTransactionIdAndDiscountIdIn(List<String> transactionIds, List<String> discountIds);
//    void deleteAllTicketsByBookedSeatIdAndDiscountIdIn(List<String> bookedSeatIds, List<String> discountIds);
//    void deleteAllTicketsByTransactionIdAndBookedSeatIdAndDiscountIdIn(List<String> transactionIds, List<String> bookedSeatIds, List<String> discountIds);
//    void deleteAllTicketsByTransactionIdNotIn(List<String> transactionIds);
//    void deleteAllTicketsByBookedSeatIdNotIn(List<String> bookedSeatIds);
//    void deleteAllTicketsByDiscountIdNotIn(List<String> discountIds);
}
