package com.ll.gramgram.boundedContext.insta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/instaMember")
public class InstaMemberController {

    @GetMapping("/connect")
    @PreAuthorize("isAuthenticated()")
    public String showConnect() {

    }
}
