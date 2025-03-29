package com.dotnt.cinemaback.controllers.ticket;

import com.dotnt.cinemaback.dto.request.TicketRequestDTO;
import com.dotnt.cinemaback.dto.response.ApiResponse;
import com.dotnt.cinemaback.dto.response.ListResponse;
import com.dotnt.cinemaback.dto.response.TicketResponseDTO;
import com.dotnt.cinemaback.services.tickets.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {
    private final ITicketService ticketService;


    /**
     * Tạo mới ticket.
     *
     * @param ticketRequestDTO the ticket request data transfer object
     * @return ResponseEntity containing the API response with the created ticket details
     */
    @PostMapping
    public ResponseEntity<
            ApiResponse<
                    TicketResponseDTO
                    >
            > createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {
        TicketResponseDTO createdTicket = ticketService.createTicket(ticketRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<TicketResponseDTO>builder()
                                .code(HttpStatus.CREATED.value())
                                .message("Ticket created successfully")
                                .data(createdTicket)
                                .build()
                );
    }

    /**
     * Lấy tất cả ticket.
     *
     * @return ResponseEntity containing the API response with the list of all tickets
     */
    @GetMapping
    public ResponseEntity<
            ApiResponse<
                    ListResponse<
                            TicketResponseDTO
                            >
                    >
            > getAllTickets(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        Page<TicketResponseDTO> tickets = ticketService.getTickets(page, limit);
        ListResponse<TicketResponseDTO> response = ListResponse.<TicketResponseDTO>builder()
                .currentPage(tickets.getNumber() + 1)
                .pageSize(tickets.getSize())
                .totalPages(tickets.getTotalPages())
                .totalElements(tickets.getTotalElements())
                .isFirst(tickets.isFirst())
                .isLast(tickets.isLast())
                .data(tickets.getContent())
                .build();

        return ResponseEntity.ok(
                ApiResponse.<ListResponse<TicketResponseDTO>>builder()
                        .code(HttpStatus.OK.value())
                        .message("Tickets retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    /**
     * Lấy ticket qua booking reference.
     *
     * @param bookingReference the booking reference of the ticket
     * @return ResponseEntity containing the API response with the ticket details
     */
    @GetMapping("/{bookingReference}")
    public ResponseEntity<
            ApiResponse<
                    TicketResponseDTO
                    >
            > getTicketByBookingReference(@PathVariable String bookingReference) {
        TicketResponseDTO ticket = ticketService.getTicketByBookingReference(bookingReference);
        return ResponseEntity.ok(
                ApiResponse.<TicketResponseDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("Ticket retrieved successfully")
                        .data(ticket)
                        .build()
        );
    }

}
