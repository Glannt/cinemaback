package com.dotnt.cinemaback.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequestDTO {
    private String title;
    private String description;
    private Integer duration;  // Phút
    private String director;
    private List<String> cast;         // Danh sách diễn viên
    private List<String> genres;       // Danh sách genre name
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String releaseDate;        // ISO format yyyy-MM-dd
    private String status;             // ACTIVE | INACTIVE
    private Double rating;
    private Double price;
}
