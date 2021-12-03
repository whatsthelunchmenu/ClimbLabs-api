package com.example.climblabs.common.factory;

import com.example.climblabs.member.domain.Member;

import java.time.LocalDateTime;

public class MemberFactory {
    private MemberFactory() {
    }

    public static Member createMember(String userId, String userName) {
        return Member.builder()
                .userId(userId)
                .name(userName)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
