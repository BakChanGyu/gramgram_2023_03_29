package com.ll.gramgram.boundedContext.member.controller;


import com.ll.gramgram.boundedContext.insta.controller.InstaMemberController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static javax.swing.UIManager.get;
import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 스프링부트 관련 컴포넌트 테스트할 때 붙여야함. IoC 컨테이너 작동시킴
@AutoConfigureMockMvc // http 요청, 응답 테스트를 가능케 해줌.
@Transactional // 실제로 테스트에서 발생한 DB 작업이 영구적으로 적용되지 않도록 해줌. (test+ transaction 조합은 자동롤백)
@ActiveProfiles("test") // application-test.yml 을 활성화 시킨다.
public class InstaMemberControllerTests {

    @Test
    @DisplayName("인스타회원 정보입력 폼")
    @WithUserDetails("user1")
    void t001() throws Exception {
        //WHEN
        ResultActions resultActions = mvc
                .perform(get("/instaMember/connect"))
                .andDo(print());

        //THEN
        resultActions
                .andExpect(handler().handlerType(InstaMemberController.class))
                .andExpect(handler().methodName("showConnect"))
                .andExpect(status().is2xxSuccessful())
                .
    }
}
