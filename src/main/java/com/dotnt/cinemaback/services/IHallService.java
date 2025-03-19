package com.dotnt.cinemaback.services;

import com.dotnt.cinemaback.dto.request.HallRequest;
import com.dotnt.cinemaback.dto.response.HallResponse;

import java.util.List;
import java.util.UUID;

public interface IHallService {
    HallResponse createHall(HallRequest request);

    HallResponse updateHall(UUID hallId, HallRequest request);

    String deleteHall(UUID id);

    HallResponse getHall(UUID hallId);

    List<HallResponse> getHalls(int page, int limit);
}
