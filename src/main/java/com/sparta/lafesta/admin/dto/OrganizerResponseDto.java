package com.sparta.lafesta.admin.dto;

import com.sparta.lafesta.user.entity.User;
import com.sparta.lafesta.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class OrganizerResponseDto {
    private Long id;
    private String username;
    private String email;
    private UserRoleEnum role;
    private String nickname;
    private Boolean organizerRequest;

    public OrganizerResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.nickname = user.getNickname();
        this.organizerRequest = user.getOrganizerRequest();
    }
}
