package com.exchange.postgres.service;

import com.exchange.postgres.entity.Member;
import com.exchange.postgres.repository.BankStatementRepository;
import com.exchange.postgres.repository.MemberRepository;
import com.exchange.postgres.repository.WalletRepository;
import com.exchange.utils.JwtAndPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BankStatementRepository bankstatementRepository;
    private final WalletRepository walletRepository;
    private final JwtAndPassword jwtAndPassword;

    public String getToken(String memberId, String password) {
//        String tmpMemberId = memberRepository.identifyMember(memberId, jwtAndPassword.hashPassword(password));

        Member repoMember = memberRepository.findByMemberId(memberId);

        if(repoMember == null){
            return "없는 회원입니다.";
        }

        if(!jwtAndPassword.comparePassword(password, repoMember.getPassword())){
            return "비밀번호가 틀렸습니다.";
        }

        return jwtAndPassword.makeJwt(memberId);
    }

    public String getMemberRole(String token) {
        return memberRepository.findMemberRole(jwtAndPassword.checkJwt(token));
    }
}
