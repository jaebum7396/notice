package user.controller;

import common.model.Response;
import common.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.model.LoginRequest;
import user.service.AuthService;

@Slf4j
@Tag(name = "UserController", description = "회원가입, 유저정보")
@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    CommonUtils commonUtils;
    @Autowired
    AuthService authService;
    @PostMapping(value = "/login")
    @Operation(summary="로그인 엔드포인트", description="가입한 회원을 로그인 하는 API")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return commonUtils.okResponsePackaging(authService.generateToken(loginRequest));
    }
}