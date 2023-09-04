package com.sparta.lafesta.user.entity;

import java.util.Arrays;

public enum UserRoleEnum {
    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN),  // 관리자 권한
    ORGANIZER(Authority.ORGANIZER);  // 주최사 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static UserRoleEnum valueOfRole (String roleStr) {
        return Arrays.stream(values())
                .filter(role -> role.getAuthority().equals("ROLE_"+roleStr))
                .findAny()
                .orElse(null);
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String ORGANIZER = "ROLE_ORGANIZER";
    }
}