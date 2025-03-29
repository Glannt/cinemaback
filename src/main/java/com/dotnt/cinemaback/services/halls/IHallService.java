package com.dotnt.cinemaback.services.halls;

import com.dotnt.cinemaback.dto.request.HallRequest;
import com.dotnt.cinemaback.dto.response.HallResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IHallService {
    HallResponse createHall(HallRequest request);

    HallResponse updateHall(UUID hallId, HallRequest request);

    String deleteHall(UUID id);

    HallResponse getHall(UUID hallId);

    Page<HallResponse> getHalls(int page, int limit);


}
