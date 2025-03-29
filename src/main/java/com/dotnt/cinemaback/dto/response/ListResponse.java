package com.dotnt.cinemaback.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Getter
@Setter
@Builder
public class ListResponse<T> {
    List<T> data;
    int totalPages;
    int currentPage;
    int pageSize;
    long totalElements;
    boolean isLast;
    boolean isFirst;

    public void setIsLast(boolean last) {
    }
}
