package kfu.coviddashboard.coviddashboardspring.repository;
import kfu.coviddashboard.coviddashboardspring.model.Day;
import kfu.coviddashboard.coviddashboardspring.model.Entry;
import kfu.coviddashboard.coviddashboardspring.model.KeyMetric;
import kfu.coviddashboard.coviddashboardspring.model.ZipcodeDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface KeyMetricRepository extends JpaRepository<KeyMetric,Integer>{

    @Transactional
    @Modifying
    @Query(value = "insert into keymetrictable (type, metric, timestamp) values (:type,:metric,:time)", nativeQuery = true)
    void postData(@Param("type") String type,@Param("metric") float metric,@Param("time") String time);

    @Query(value = "select * from keymetrictable where type = :type order by timestamp desc limit 1", nativeQuery = true)
    KeyMetric getRecent(@Param("type") String type);

    @Query(value = "select * from keymetrictable where type = :type order by timestamp desc limit :rows", nativeQuery = true)
    List<KeyMetric> getRecent(@Param("type") String type,@Param("rows") int rows);
}
