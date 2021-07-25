package com.exchange.utils;

import com.exchange.Constants;
import com.exchange.postgres.service.BankstatementService;
import com.exchange.postgres.service.MemberService;
import com.exchange.postgres.service.OrderService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class AuthCheck {

    private final MemberService memberService;
    private final OrderService orderService;
    private final BankstatementService bankstatementService;
    private final KafkaTemplate kafkaTemplate;

    public Map<String, String> checkMessage(String message){
        try {
            return new Gson().fromJson(message, Map.class);
        } catch (Exception e) {
            // 애러로 인한 토큰 검증 실패
            log.info("error kafka message : " + message);
            return null;
        }
    }

    //
    public boolean checkRole(String token, Constants.ROLE submitRole) {
        String role = null;

        if(null != token && !"".equals(token)) {
            role = memberService.getMemberRole(token);
        }

        if(null != role && !"".equals(role) && submitRole.toString().equals(role)) {
            return true;
        }

        return false;
    }

    //
    public void sendResult(boolean roleCheck, String requestId, Constants.TOPIC topic) {
        /* requestId
        topic이 submitDw 일 때는 trasactionId
        topic이 submitTrade 일 때는 orderId
        */

        if(roleCheck) {
            // 토큰 검증 및 회원 확인 성공 시
            try {
                kafkaTemplate.send(topic.toString(), requestId);
                updateStatus(topic, requestId, Constants.STATUS.TCKS);
            } catch(Exception e) {
                // kafka 서버 문제 발생 시 → batch 서버에서 해결
                log.info("kafka server error");
//                updateStatus(topic, requestId, Constants.STATUS.TCKS);
            }
        } else {
            // 토큰 검증 실패
            log.info("tokenCheck fail!! requestId : " + requestId);
            updateStatus(topic, requestId, Constants.STATUS.TCKF);
        }
    }

    // 토큰 검증 및 회원 확인 성공 시
    public void updateStatus(Constants.TOPIC topic, String requestId, Constants.STATUS status) {
        //
        if(Constants.TOPIC.submitDw.equals(topic)) {
             bankstatementService.updateStatus(requestId, status);
        }

        if(Constants.TOPIC.submitOrder.equals(topic)) {
            orderService.updateStatus(requestId, status);
        }
    }
}
