package com.ll.gramgram.boundedContext.member.controller;

import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // 스프링부트 관련 컴포넌트 테스트할 때 붙여야함. IoC 컨테이너 작동시킴
@AutoConfigureMockMvc // http 요청, 응답 테스트를 가능케 해줌.
@Transactional // 실제로 테스트에서 발생한 DB 작업이 영구적으로 적용되지 않도록 해줌. (test+ transaction 조합은 자동롤백)
@ActiveProfiles("test") // application-test.yml 을 활성화 시킨다.
public class MemberControllerTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("회원가입 폼")
    void t001() throws Exception {
        //WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/join")) // 스프링이 내부적으로 브라우저 띄워서 테스트.
                .andDo(print()); // 크게 의미없고 그냥 확인용
        //THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showJoin"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="password" name="password"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="submit" value="회원가입"
                        """.stripIndent().trim())));
    }

    @Test
//    @Rollback(value = false) // DB에 흔적 남는다.
    @DisplayName("회원가입")
    void t002() throws Exception {
        //WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성 th:action 했을때 key가 생기는데 이때 키가없음안댐
                        .param("username", "user10")
                        .param("password", "1234")
                )
                .andDo(print());
        //THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is3xxRedirection());

        Member member = memberService.findByUsername("user10").orElse(null);

        assertThat(member).isNotNull();
    }


    @Test
    @DisplayName("회원가입시에 올바른 데이터를 넘기지 않으면 400")
    void t003() throws Exception {
        //WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성 th:action 했을때 key가 생기는데 이때 키가없음안댐
                        .param("username", "user10")
                )
                .andDo(print());
        //THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());

        //WHEN
        resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성 th:action 했을때 key가 생기는데 이때 키가없음안댐
                        .param("password", "1234")
                )
                .andDo(print());
        //THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());

        //WHEN
        resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성 th:action 했을때 key가 생기는데 이때 키가없음안댐
                        .param("username", "user10" + "a".repeat(30))
                        .param("password", "1234")
                )
                .andDo(print());
        //THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());

        //WHEN
        resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성 th:action 했을때 key가 생기는데 이때 키가없음안댐
                        .param("username", "user10")
                        .param("password", "1234" + "a".repeat(30))
                )
                .andDo(print());
        //THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인 폼")
    void t004() throws Exception {
        //WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/login")) // 스프링이 내부적으로 브라우저 띄워서 테스트.
                .andDo(print()); // 크게 의미없고 그냥 확인용
        //THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showLogin"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="password" name="password"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="submit" value="로그인"
                        """.stripIndent().trim())));
    }


    @Test
    // @Rollback(value = false) // DB에 흔적이 남는다.
    @DisplayName("로그인 처리")
    void t005() throws Exception {
        // 가상의 브라우저로 form 만들어서 처리한다.
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/login")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "user1")
                        .param("password", "1234")
                )
                .andDo(print());

        // 세션에 접근해서 값을 가져와서
        MvcResult mvcResult = resultActions.andReturn();
        // 원래 getSessoin을 하면, 만약에 없을 경우에 만들어서라도 준다.
        // 그러나 false로 하면, 없으면 안 만든다.
        HttpSession session = mvcResult.getRequest().getSession(false);
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();

        assertThat(user.getUsername()).isEqualTo("user1");

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/**"));
    }

    @Test
    // @Rollback(value = false) // DB에 흔적이 남는다.
    @DisplayName("로그인 후에 네비바에 로그인한 회원의 username")
    @WithUserDetails("user1") // user1로 로그인한 상태로 진행
    void t006() throws Exception {
        // 가상의 브라우저로 form 만들어서 처리한다.
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/me")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showMe"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        user1님 환영합니다!
                        """.stripIndent().trim())));

    }
}