package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {

    /*@Query(value = "select member_id, reg_date from member where member_id=?1", nativeQuery = true)
    Object findMember(String memberId);

    @Query(value = "select * from member where member_id=?1", nativeQuery = true)
    Member findByMemberId(String memberId);*/

    @Query(value = "select role from member where member_id=?1 and use_yn = 'Y'", nativeQuery = true)
    String findMemberRole(String memberId);

    @Query(value = "select member_id from member where member_id=?1 and password=?2", nativeQuery = true)
    String identifyMember(String memberId, String password);
}
