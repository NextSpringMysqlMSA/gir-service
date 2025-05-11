package com.nsmm.esg.griservice.repository;

import com.nsmm.esg.griservice.entity.GriDisclosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GriDisclosureRepository extends JpaRepository<GriDisclosure, Long> {
    Optional<GriDisclosure> findByMemberIdAndGriCode(Long memberId, String griCode);
    List<GriDisclosure> findAllByMemberId(Long memberId);
}
