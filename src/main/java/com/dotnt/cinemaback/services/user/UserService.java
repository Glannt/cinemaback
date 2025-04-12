package com.dotnt.cinemaback.services.user;


import com.dotnt.cinemaback.dto.UserLoginDTO;
import com.dotnt.cinemaback.dto.request.UserCreationRequest;
import com.dotnt.cinemaback.dto.response.SignupResponse;

public interface UserService {
    SignupResponse createUser(UserCreationRequest request);
    String login(UserLoginDTO dto) throws Exception;
}
