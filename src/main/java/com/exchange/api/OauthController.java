package com.exchange.api;

import com.exchange.postgres.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    private final MemberService memberService;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public String getToken(@RequestParam("memberId") String memberId,
                           @RequestParam("password") String password) {
        return memberService.getToken(memberId, password);
    }
}
