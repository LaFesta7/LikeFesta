package com.sparta.lafesta.festivalRequest.repository;

import com.sparta.lafesta.festivalRequest.entity.FestivalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalRequestRepository extends JpaRepository<FestivalRequest, Long> {
    List<FestivalRequest> findAllByOrderByCreatedAtDesc();

    Page<FestivalRequest> findAllByAdminApproval(Boolean adminApproval , Pageable pageable);
}
