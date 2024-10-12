package com.csit321.NaviGo.Repository;
 
import com.csit321.NaviGo.Entity.VisitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
 
import java.util.List;
 
public interface VisitorRepository extends JpaRepository<VisitorEntity, Long> {
 
    // Find visitors by card number
    List<VisitorEntity> findByCardNo(int cardNo);
 
    // Custom query to find the maximum card number
    @Query("SELECT MAX(v.cardNo) FROM VisitorEntity v")
    Integer findMaxCardNo();
}
