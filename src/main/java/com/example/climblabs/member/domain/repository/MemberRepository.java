package com.example.climblabs.member.domain.repository;

import com.example.climblabs.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
