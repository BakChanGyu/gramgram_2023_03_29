package com.ll.gramgram.boundedContext.member.service;

import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly라는 것을 명시
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    // readonly 붙인 이유. 보통 db 하나쓰는데, db 복제해서 여러개 쓰는경우 있음
    // 읽기만 가능한 db(슬레이브), rw 가능한 디비(마스터)
    // sql의 80%는 select, 20%가 update, insert이기 때문에, select는 읽기전용 db에 넣는게 좋음.
    @Transactional // 나중에 insert, update 일어날때는 이렇게 따로 메서드에 선언해주기..
    public Member join(String username, String password) {
        Member member = Member
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        return memberRepository.save(member);
    }
}
