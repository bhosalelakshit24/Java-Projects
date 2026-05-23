package com.example.gym.service;

import com.example.gym.entity.Member;
import com.example.gym.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id " + id));
    }

    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public long countAll() {
        return memberRepository.count();
    }

    public long countByStatus(String status) {
        return memberRepository.countByStatus(status);
    }
}
