package com.ll.gramgram.member;

import com.ll.gramgram.member.controller.MemberController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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
                .andExpect(content().string(containString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())))
                .andExpect(content().string(containString("""
                        <input type="password" name="password"
                        """.stripIndent().trim())))
                .andExpect(content().string(containString("""
                        <input type="submit" value="회원가입"
                        """.stripIndent().trim())));
    }

    @Test
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
                .andExpect(status().is2xxSuccessful());
    }
}