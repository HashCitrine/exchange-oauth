package com.exchange.postgres.service;

import com.exchange.postgres.entity.Member;
import com.exchange.postgres.repository.BankStatementRepository;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.repository.WalletRepository;
import com.exchange.utils.JwtAndPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final WalletRepository walletRepository;
    private final JwtAndPassword jwtAndPassword;
    public String getToken(String memberId, String password) {

        Member repoMember = memberRepository.findByMemberId(memberId);

        if(repoMember == null || !jwtAndPassword.comparePassword(password, repoMember.getPassword())){
            return "incorrect id or password";
        }

        return jwtAndPassword.makeJwt(memberId);
    }

    public String getMemberRole(String token) {
        return memberRepository.findMemberRole(jwtAndPassword.checkJwt(token));
    }
}
