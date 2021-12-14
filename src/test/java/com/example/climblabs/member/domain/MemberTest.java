package com.example.climblabs.member.domain;

import com.example.climblabs.member.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("회원정보를 저장한다.")
    public void addUser() {
        //given
        Member member = Member.builder()
                .userId("뉴맨")
                .name("홍길동")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        //when
        testEntityManager.persist(member);

        //then
        Assertions.assertThat(member).isEqualTo(testEntityManager.find(Member.class, member.getId()));
    }
}