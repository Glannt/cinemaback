package com.dotnt.cinemaback.services.tickets;

import com.dotnt.cinemaback.dto.request.TicketRequestDTO;
import com.dotnt.cinemaback.dto.response.TicketResponseDTO;
import org.springframework.data.domain.Page;

public interface ITicketService {
    TicketResponseDTO createTicket(TicketRequestDTO request);

    Page<TicketResponseDTO> getTickets(int page, int limit);

    TicketResponseDTO getTicketByBookingReference(String bookingReference);
}
