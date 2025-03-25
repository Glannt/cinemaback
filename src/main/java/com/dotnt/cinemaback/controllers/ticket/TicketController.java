package com.dotnt.cinemaback.controllers.ticket;

import com.dotnt.cinemaback.dto.request.TicketRequestDTO;
import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.dto.response.TicketResponseDTO;
import com.dotnt.cinemaback.services.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {
    private final ITicketService ticketService;

    @PostMapping
    public ApiResponse<Object> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {
        TicketResponseDTO createdTicket = ticketService.createTicket(ticketRequestDTO);
        return ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message("Ticket created successfully")
                .data(createdTicket)
                .build();
    }
}
