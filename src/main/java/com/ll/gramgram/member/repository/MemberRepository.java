package com.ll.gramgram.member.repository;

import com.ll.gramgram.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
