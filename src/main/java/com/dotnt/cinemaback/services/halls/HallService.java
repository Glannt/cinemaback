package com.dotnt.cinemaback.services.halls;

import com.dotnt.cinemaback.constants.enums.CinemaStatus;
import com.dotnt.cinemaback.constants.enums.ESeatType;
import com.dotnt.cinemaback.constants.enums.ProjectionType;
import com.dotnt.cinemaback.constants.enums.SeatStatus;
import com.dotnt.cinemaback.dto.request.HallRequest;
import com.dotnt.cinemaback.dto.response.HallResponse;
import com.dotnt.cinemaback.exception.AppException;
import com.dotnt.cinemaback.exception.ErrorCode;
import com.dotnt.cinemaback.models.*;
import com.dotnt.cinemaback.repositories.*;
import com.dotnt.cinemaback.utils.RowsSeatGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "HALL-SERVICE")
public class HallService implements IHallService {
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final CinemaRepository cinemaRepository;
    private final HallHasSeatRepository hallHasSeatRepository;
    private final SeatTypeRepository seatTypeRepository;

    @Override
    @Transactional
    public HallResponse createHall(HallRequest request) {
        //find cinema with id
        Cinema cinema = cinemaRepository.findCinemaById(request.getCinemaId())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        //build hall
        Hall hall = Hall
                .builder()
                .cinema(cinema)
                .name(request.getName())
                .projectionType(ProjectionType.valueOf(request.getProjectionType()))
                .seatCount(request.getSeatCount())
                .status(CinemaStatus.valueOf(request.getStatus()))
                .build();

        //Add hall into cinema
        cinema.getHalls().add(hall);

        //save to db
        hallRepository.save(hall);
        cinemaRepository.save(cinema);



        List<HallHasSeat> hallSeats = createHallSeats(hall, request.getSeatCount(), request.getSeatsPerRow());
        hallHasSeatRepository.saveAll(hallSeats);


        return HallResponse.builder()
                .id(hall.getId())
                .name(hall.getName())
                .status(request.getStatus())
                .seats(hallSeats
                        .stream()
                        .map(hallSeat -> hallSeat
                                .getSeat()
                                .getId()
                                .toString())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public HallResponse updateHall(UUID hallId, HallRequest request) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        if (hall.getStatus() == CinemaStatus.INACTIVE) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }

        if (request.getProjectionType() != null && !request.getProjectionType().isEmpty()) {
            hall.setProjectionType(ProjectionType.valueOf(request.getProjectionType()));
        }

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            hall.setStatus(CinemaStatus.valueOf(request.getStatus()));
        }

        hallRepository.save(hall);
        if (hall.getHallHasSeats() != null && !hall.getHallHasSeats().isEmpty()) {
            hallHasSeatRepository.deleteAll(hall.getHallHasSeats());
        }
        List<HallHasSeat> hallSeats = createHallSeats(hall, request.getSeatCount(), request.getSeatsPerRow());
        hallHasSeatRepository.saveAll(hallSeats);


        return HallResponse.builder()
                .id(hall.getId())
                .name(hall.getName())
                .status(hall.getStatus().name())
                .seats(hallSeats
                        .stream()
                        .map(hallSeat -> hallSeat
                                .getSeat()
                                .getId()
                                .toString())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public String deleteHall(UUID id) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        hall.setStatus(CinemaStatus.INACTIVE);
        hallRepository.save(hall);
        hall.getHallHasSeats().forEach(hallHasSeat -> {
            hallHasSeat.setStatus(SeatStatus.INACTIVE);
            hallHasSeat.getSeat().setStatus(SeatStatus.INACTIVE);
            hallHasSeatRepository.save(hallHasSeat);
        });

        return "Delete Hall Success";
    }

    @Override
    public HallResponse getHall(UUID hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        if (hall.getStatus() == CinemaStatus.INACTIVE) {
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        }
        return HallResponse
                .builder()
                .id(hall.getId())
                .name(hall.getName())
                .status(String.valueOf(hall.getStatus()))
                .seats(hall.getHallHasSeats().stream()
                        .map(hallSeat -> hallSeat.getSeat().getId().toString())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Page<HallResponse> getHalls(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit); // page - 1 vì Pageable bắt đầu từ 0
        Page<Hall> hallPage = hallRepository.findAll(pageable);

        return hallPage.map(hall -> HallResponse.builder()
                .id(hall.getId())
                .name(hall.getName())
                .status(String.valueOf(hall.getStatus()))
                .seats(hall.getHallHasSeats().stream()
                        .map(hallSeat -> hallSeat.getSeat().getId().toString())
                        .collect(Collectors.toList()))
                .build());
    }


    private List<HallHasSeat> createHallSeats(Hall hall, int seatCount, int seatsPerRow) {
        List<HallHasSeat> hallSeats = new ArrayList<>();
        int rows = (int) Math.ceil((double) seatCount / seatsPerRow);
        String[] rowLabel = RowsSeatGenerator.generateRowsLabels(rows);
//        String[] rowLabel = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        SeatType seatType = seatTypeRepository.findByName(ESeatType.STANDARD).orElseThrow();
        for (int rowIndex = 0; rowIndex < rowLabel.length && hallSeats.size() < seatCount; rowIndex++) {
            String currentRow = rowLabel[rowIndex];
            for (int seatNum = 1; seatNum <= seatsPerRow && hallSeats.size() < seatCount; seatNum++) {
                Seat seat = Seat.builder()
                        .row(currentRow)
                        .number(seatNum)
                        .seatType(seatType)
                        .build();

                seat = seatRepository.save(seat);

                HallHasSeat hallSeat = HallHasSeat.builder()
                        .hall(hall)
                        .seat(seat)
                        .status(SeatStatus.AVAILABLE)
                        .build();

                hallSeats.add(hallSeat);
            }
        }

        return hallSeats;
    }


}