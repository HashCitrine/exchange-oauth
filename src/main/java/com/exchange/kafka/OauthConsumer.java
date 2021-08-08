package com.exchange.kafka;

import com.exchange.Constants;
import com.exchange.utils.AuthCheck;
import com.exchange.utils.JwtAndPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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

    public void normalProcess(String message, Constants.ROLE submitRole, Constants.TOPIC topic) {
        Map<String, String> json = authCheck.checkMessage(message);
        log.debug("[normalProcess]: {}", json.toString());
        if(null != json) {
            authCheck.sendResult(authCheck.checkRole(json.get("token"), submitRole), json.get("requestId"), topic);
        }
    }

    // 입출금 기능 이용 시 토큰검증
    @KafkaListener(topics = "reqDw", groupId = "exchange")
    public void bankStatement(String message) {
        log.info("reqDw message : {}", message);
        normalProcess(message, Constants.ROLE.NORMAL, Constants.TOPIC.submitDw);
    }

    // 거래
    @KafkaListener(topics = "reqOrder", groupId = "exchange")
    public void trade(String message) {
        log.info("Consumed message : {}", message);
        normalProcess(message, Constants.ROLE.NORMAL, Constants.TOPIC.submitOrder);
    }


}
