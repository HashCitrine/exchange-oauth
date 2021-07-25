package com.exchange.api;

import com.exchange.Constants;
import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Member;
import com.exchange.postgres.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OauthController {

    private final MemberService memberService;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public String getToken(@RequestParam("memberId") String memberId, @RequestParam("password") String password) {
        return memberService.getToken(memberId, password);
    }

    /*@PostMapping("/token/check")
    @ResponseStatus(HttpStatus.OK)
    public String tokenCheck(@RequestParam("token") String token){
        String result = memberService.getMemberRole(token);
        log.info("Role : " + result);
        return result;
    }*/


}
