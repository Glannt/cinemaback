package com.dotnt.cinemaback.services.token;

import com.dotnt.cinemaback.models.Token;
import com.dotnt.cinemaback.models.User;

public interface ITokenService {
     Token addToken(User user, String token, boolean isMobileDevice);
     Token refreshToken(String refreshToken, User user) throws Exception;
}
