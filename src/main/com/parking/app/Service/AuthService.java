package com.parking.app.service;

import com.parking.app.dto.LoginRequestDTO;
import com.parking.app.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request);

}
