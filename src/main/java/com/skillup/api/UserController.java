package com.skillup.api;

import com.skillup.api.dto.in.UserInDto;
import com.skillup.api.dto.in.UserPin;
import com.skillup.api.dto.out.UserOutDto;
import com.skillup.api.util.SkillUpCommon;
import com.skillup.api.util.SkillUpResponse;
import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserService;
import com.skillup.infrastructure.jooq.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class UserController {

    // 单例模式：new一个对象，不用每次都new
    // 替代：UserService userService = new UserService();
    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto) {
        UserDomain userDomain;
        // function: insert data into user table
        try {
            userDomain = userService.createUser(toDomain(userInDto));
            return ResponseEntity
                    .status(SkillUpCommon.SUCCESS)
                    .body(
                            SkillUpResponse
                                    .builder()
                                    .result(toOutDto(userDomain))
                                    .msg(null)
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(
                            SkillUpResponse
                                    .builder()
                                    .result(null)
                                    .msg(String.format(SkillUpCommon.USER_EXISTS, userInDto.getUserName()))
                                    .build()
                    );
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> readUserById(@PathVariable("id") String accountId) {
        UserDomain userDomain = userService.readUserById(accountId);
        // handle userDomain is null
        if (Objects.isNull(userDomain)) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(
                            SkillUpResponse
                                    .builder()
                                    .result(null)
                                    .msg(String.format(SkillUpCommon.USER_ID_WRONG, accountId))
                                    .build()
                    );
        }
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(
                        SkillUpResponse
                                .builder()
                                .result(toOutDto(userDomain))
                                .msg(null)
                                .build()
                );
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillUpResponse> readUserByName(@PathVariable("name") String accountName) {
        UserDomain userDomain = userService.readUserByName(accountName);
        // handle userDomain is null
        if (Objects.isNull(userDomain)) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(
                            SkillUpResponse
                                    .builder()
                                    .result(null)
                                    .msg(String.format(SkillUpCommon.USER_NAME_WRONG, accountName))
                                    .build()
                    );
        }
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(
                        SkillUpResponse
                                .builder()
                                .result(toOutDto(userDomain))
                                .msg(null)
                                .build()
                );
    }

    @PostMapping("/login")
    public ResponseEntity<SkillUpResponse> login(@RequestBody UserInDto userInDto) {
        // 1. get user by name
        UserDomain userDomain = userService.readUserByName(userInDto.getUserName());
        // 1.1 if name is wrong, return 400
        if (Objects.isNull(userDomain)) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(
                            SkillUpResponse
                                    .builder()
                                    .result(null)
                                    .msg(String.format(SkillUpCommon.USER_NAME_WRONG, userInDto.getUserName()))
                                    .build()
                    );
        }
        // 2. check password match
        // 2.1 if password is wrong, return 400
        if (! userInDto.getPassword().equals(userDomain.getPassword())) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(
                            SkillUpResponse
                                    .builder()
                                    .result(null)
                                    .msg(SkillUpCommon.PASSWORD_NOT_MATCH)
                                    .build()
                    );

        }
        // 2.2 if password matches, return 200
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(
                        SkillUpResponse
                                .builder()
                                .result(toOutDto(userDomain))
                                .msg(null)
                                .build()
                );
    }

    @PutMapping("/password")
    public ResponseEntity<SkillUpResponse> updatePassword(@RequestBody UserPin userPin) {
        // 1. get user by name
        UserDomain userDomain = userService.readUserByName(userPin.getUserName());
        // 1.1 if name is wrong, return 400
        if (Objects.isNull(userDomain)) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(
                            SkillUpResponse
                                    .builder()
                                    .result(null)
                                    .msg(String.format(SkillUpCommon.USER_NAME_WRONG, userPin.getUserName()))
                                    .build()
                    );
        }
        // 2. check password
        // 2.1 if password is wrong, return 400
        if (! userPin.getOldPassword().equals(userDomain.getPassword())) {
            return ResponseEntity
                    .status(SkillUpCommon.BAD_REQUEST)
                    .body(
                            SkillUpResponse
                                    .builder()
                                    .result(null)
                                    .msg(SkillUpCommon.PASSWORD_NOT_MATCH)
                                    .build()
                    );

        }
        // 2.2 if password matches, update password and save, return 200
        userDomain.setPassword(userPin.getNewPassword());
        userService.updateUser(userDomain);
        return ResponseEntity
                .status(SkillUpCommon.SUCCESS)
                .body(
                        SkillUpResponse
                                .builder()
                                .result(toOutDto(userDomain))
                                .msg(null)
                                .build()
                );
    }







    public UserDomain toDomain(UserInDto userInDto) {
        return UserDomain
                .builder() // private Builder
                .userId(UUID.randomUUID().toString())
                .userName(userInDto.getUserName())
                .password(userInDto.getPassword())
                .build();
    }

    public UserOutDto toOutDto(UserDomain userDomain) {
        return UserOutDto
                .builder()
                .userId(userDomain.getUserId())
                .userName(userDomain.getUserName())
                .build();
    }

}