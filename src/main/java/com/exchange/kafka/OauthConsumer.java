package com.exchange.kafka;

import com.exchange.Constants;
import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.service.MemberService;
import com.exchange.utils.AuthCheck;
import com.exchange.utils.JwtAndPassword;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class OauthConsumer {

    private final JwtAndPassword jwt;
    private final AuthCheck authCheck;

    // 토큰 발급 -> api로 처리
    /*@KafkaListener(topics = "token", groupId = "exchange")
    public void makeToken(String message) {
        log.debug("Consumed message : {}", message);

//        kafkaTemplate.send("topic", jwt.makeJwt(message));
    }*/

    public void normalProccess(String message, Constants.ROLE submitRole, String requestIdName, Constants.TOPIC topic) {
        Map<String, String> js = authCheck.checkMessage(message);
        if(null != js) {
            authCheck.sendResult(authCheck.checkRole(js.get("token"), Constants.ROLE.NORMAL), js.get(requestIdName), topic);
        }
    }

    // 입출금 기능 이용 시 토큰검증
    @KafkaListener(topics = "reqDW", groupId = "exchange")
    public void bankstatement(String message) {
        log.debug("reqDW message : {}", message);
        normalProccess(message, Constants.ROLE.NORMAL, "transactionId", Constants.TOPIC.submitDw);
    }

    // 거래
    @KafkaListener(topics = "trade", groupId = "exchange")
    public void trade(String message) {
        log.debug("Consumed message : {}", message);
        normalProccess(message, Constants.ROLE.NORMAL, "orderId", Constants.TOPIC.submitOrder);
    }


}
