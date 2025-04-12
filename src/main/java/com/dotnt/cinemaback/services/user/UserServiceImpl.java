package com.dotnt.cinemaback.services.user;


import com.dotnt.cinemaback.constants.enums.UserStatus;
import com.dotnt.cinemaback.constants.enums.UserType;
import com.dotnt.cinemaback.dto.UserLoginDTO;
import com.dotnt.cinemaback.dto.request.UserCreationRequest;
import com.dotnt.cinemaback.dto.response.SignupResponse;
import com.dotnt.cinemaback.exception.AppException;
import com.dotnt.cinemaback.exception.ErrorCode;
import com.dotnt.cinemaback.models.Role;
import com.dotnt.cinemaback.models.Token;
import com.dotnt.cinemaback.models.User;
import com.dotnt.cinemaback.models.UserHasRole;
import com.dotnt.cinemaback.repositories.RoleRepository;
import com.dotnt.cinemaback.repositories.TokenRepository;
import com.dotnt.cinemaback.repositories.UserRepository;
import com.dotnt.cinemaback.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SignupResponse createUser(UserCreationRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            log.info("User already existed ", request.getEmail());
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Role role = roleRepository.findByName(String.valueOf(UserType.USER))
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        User user = User
                .builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .status(UserStatus.ACTIVE)
                .build();
        user.setCreatedBy(request.getEmail());

        UserHasRole userHasRole = UserHasRole
                .builder()
                .role(role)
                .user(user)
                .build();
        user.setUserHasRoles(Set.of(userHasRole));
        return SignupResponse
                .builder()
                .fullName(String.format("%s %s", user.getFirstName(), user.getLastName()))
                .email(user.getEmail())
                .dob(user.getDob())
                .roles(user.getUserHasRoles().stream().map(UserHasRole::getRole).map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
    @Override
    public String login(UserLoginDTO dto) throws Exception {
        Optional<User> optionalUser = Optional.empty();
        if(dto.getEmail() != null){
            optionalUser = userRepository.findByEmail(dto.getEmail());
        }
        User user = optionalUser.get();
        if(user.getStatus() == UserStatus.INACTIVE){
//            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_IS_LOCKED));
            throw new AppException(ErrorCode.USER_IS_LOCKED);
        }
        return jwtTokenUtils.generateToken(user);
    }

    public User getUserDetailFromToken(String token) throws Exception {
        if(jwtTokenUtils.isTokenExpired(token)) {
//            throw new ExpiredTokenException("Token is expired");
            throw new Exception("Token is expired");
        }
        String subject = jwtTokenUtils.getSubject(token);
        Optional<User> user;
        user = userRepository.findByEmail(subject);
        if (user.isEmpty()) {
            user = userRepository.findByEmail(subject);
        }
        return user.orElseThrow(() -> new Exception("User not found"));

    }

    public User getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailFromToken(existingToken.getToken());
    }

}
