package com.dotnt.cinemaback.constants.init;

import com.dotnt.cinemaback.constants.enums.ESeatType;
import com.dotnt.cinemaback.constants.enums.UserType;
import com.dotnt.cinemaback.models.Genre;
import com.dotnt.cinemaback.models.Role;
import com.dotnt.cinemaback.models.SeatType;
import com.dotnt.cinemaback.repositories.GenreRepository;
import com.dotnt.cinemaback.repositories.RoleRepository;
import com.dotnt.cinemaback.repositories.SeatTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "INIT-APPLICATION")
public class InitApp {

    private final RoleRepository roleRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final GenreRepository genreRepository;

    @Bean
    @ConditionalOnProperty(prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner initApplication() {
        log.info("Initializing application.....");
        return args -> {
            Optional<Role> roleUser = roleRepository.findByName(String.valueOf(UserType.USER));
            if (roleUser.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(String.valueOf(UserType.USER))
                        .description("User role")
                        .build());
            }

            Optional<Role> roleAdmin = roleRepository.findByName(String.valueOf(UserType.ADMIN));
            if (roleAdmin.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(String.valueOf(UserType.ADMIN))
                        .description("Admin role")
                        .build());
            }

            Optional<Role> roleManager = roleRepository.findByName(String.valueOf(UserType.MANAGER));
            if (roleManager.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(String.valueOf(UserType.MANAGER))
                        .description("Manager role")
                        .build());
            }

            Optional<Role> roleStaff = roleRepository.findByName(String.valueOf(UserType.STAFF));
            if (roleStaff.isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(String.valueOf(UserType.STAFF))
                        .description("Staff role")
                        .build());
            }

            Optional<SeatType> seatTypeStandard = seatTypeRepository.findByName(ESeatType.STANDARD);
            if (seatTypeStandard.isEmpty()) {
                seatTypeRepository.save(SeatType.builder()
                        .name(ESeatType.STANDARD)
                        .description("Standard seat")
                        .build());
            }
            Optional<SeatType> seatTypeVIP = seatTypeRepository.findByName(ESeatType.VIP);
            if (seatTypeVIP.isEmpty()) {
                seatTypeRepository.save(SeatType.builder()
                        .name(ESeatType.VIP)
                        .description("Vip seat")
                        .build());
            }
            Optional<SeatType> seatTypeCouple = seatTypeRepository.findByName(ESeatType.COUPLE);
            if (seatTypeVIP.isEmpty()) {
                seatTypeRepository.save(SeatType.builder()
                        .name(ESeatType.COUPLE)
                        .description("Couple seat")
                        .build());
            }

            List<String> genres = List.of(
                    "Action",
                    "Comedy",
                    "Horror",
                    "Drama",
                    "Sci-Fi",
                    "Romance",
                    "Thriller",
                    "Adventure",
                    "Fantasy",
                    "Animation",
                    "Mystery",
                    "Documentary",
                    "Crime",
                    "Musical",
                    "Biography",
                    "War",
                    "Western",
                    "Family",
                    "History",
                    "Sport"
            );

            for (String genreName : genres) {
                Optional<Genre> genre = genreRepository.findByName(genreName);
                if (genre.isEmpty()) {
                    genreRepository.save(Genre.builder()
                            .name(genreName)
                            .build());
                }
            }

            log.info("Application initialization completed .....");
        };
    }
}
