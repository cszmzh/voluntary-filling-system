package com.banana.volunteer.repository;

import com.banana.volunteer.entity.view.ReportView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportViewRepository extends JpaRepository<ReportView, Integer> {

    /**
     * 通过所属组织名称查找组织所有志愿信息
     */
    @Query("select a from ReportView a where a.orgFirst=:orgName or " +
            "a.orgSecond=:orgName")
    Page<ReportView> findByOrgName(@Param("orgName") String orgName, Pageable pageable);

    @Query(nativeQuery = true, value = "select distinct org_first from report_view")
    List<String> findDistinctByOrgFirst();

    @Query(nativeQuery = true, value = "select distinct org_second from report_view where org_second is not null")
    List<String> findDistinctByOrgSecond();

    long countDistinctByOrgFirst(String orgFirst);

    long countDistinctByOrgSecond(String orgSecond);
}
