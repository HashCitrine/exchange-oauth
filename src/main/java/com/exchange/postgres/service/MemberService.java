package com.exchange.postgres.service;

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

    /*public Member memberInfo(Member member) {
        return memberRepository.findByMemberId(member.getMemberId());
    }

    public String register(Member member) {
        if (member.getMemberId().equals("") || member.getPassword().equals("")){
            return "아이디와 비밀번호는 반드시 입력해주세요.";
        }

        if (memberRepository.findByMemberId(member.getMemberId()) != null){
            return "이미 아이디가 존재합니다.";
        }

        Member newMember = Member.builder()
                .memberId(member.getMemberId())
                .password(jwtAndPassword.hashPassword(member.getPassword()))
                .role(member.getRole())
                .useYn(member.getUseYn())
                .regDate(LocalDateTime.now())
                .uptDate(LocalDateTime.now())
                .build();

        memberRepository.save(newMember);

        return "success register";
    }

    public String login(Member member) throws Exception {
        Member repoMember = memberRepository.findByMemberId(member.getMemberId());

        if(repoMember == null){
            return "없는 회원입니다.";
        }

        if(!jwtAndPassword.comparePassword(member.getPassword(), repoMember.getPassword())){
            return "비밀번호가 틀렸습니다.";
        }

//        repoMember.setToken(jwtAndPassword.makeJwt(member.getMemberId()));
        memberRepository.save(repoMember);

        return "success login";
    }

    public String logout(Member member){
        // 멤버를 가지고와서 set해야 그거(토큰)만 저장된다.
        Member repoMember = memberRepository.findByMemberId(member.getMemberId());

//        repoMember.setToken("");
        memberRepository.save(repoMember);

        return "success logout";
    }

    public String depositAndWithdraw(Bankstatement bankStatement){
        // 이유 없는(?) 단순 에러 발생시 예외처리
        saveBank(bankStatement);
        saveWallet(bankStatement);

        return "success deposit or withdraw";
    }

    private void saveBank(Bankstatement bankStatement) {
        // 이거 transactionDate 떄문에 빌더씀
        Bankstatement newBankStatement = Bankstatement.builder()
                .transactionDate(LocalDateTime.now())
                .memberId(bankStatement.getMemberId())
                .transactionType(bankStatement.getTransactionType())
                .krw(bankStatement.getKrw())
                .build();

        bankstatementRepository.save(newBankStatement);
    }

    private void saveWallet(Bankstatement bankStatement) {
        Wallet existWallet = walletRepository.findByMemberId(bankStatement.getMemberId());
        if (existWallet.getMemberId().equals(bankStatement.getMemberId())){
            log.info("[saveBank]: "+ "duplicated");

            existWallet.setQuantity(checkDepositAndWithdraw(
                    bankStatement.getKrw(), bankStatement.getTransactionType()) + existWallet.getQuantity());

            walletRepository.save(existWallet);
            return;
        }

        Wallet newWallet = Wallet.builder()
                .memberId(bankStatement.getMemberId())
                .currency("MONEY")
                .averagePrice(bankStatement.getKrw())
                .quantity(checkDepositAndWithdraw(bankStatement.getKrw(), bankStatement.getTransactionType()))
                .build();

        walletRepository.save(newWallet);
    }

    private Long checkDepositAndWithdraw(Long krw, Constants.TRANSACTION_TYPE type) {
        if (type == Constants.TRANSACTION_TYPE.WITHDRAW) {
            return -krw;
        }
        return krw;
    }

    public String authUser(Member member) throws Exception {
//        if(!jwtAndPassword.checkJwt(memberRepository
//                .findByMemberId(member.getMemberId())
//                .getToken())){
//            return "failed to user auth";
//        }

        return "success user auth";
    }*/

    public String getToken(String memberId, String password) {
        return jwtAndPassword.makeJwt(memberRepository.identifyMember(memberId, jwtAndPassword.hashPassword(password)));
    }

    public String getMemberRole(String token) {
        return memberRepository.findMemberRole(jwtAndPassword.checkJwt(token));
    }
}
