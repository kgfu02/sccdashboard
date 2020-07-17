package kfu.coviddashboard.coviddashboardspring.repository;
import kfu.coviddashboard.coviddashboardspring.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface DayRepository extends JpaRepository<Day,Integer> {
    @Query(value = "select * from dashboardtable where city = ?1", nativeQuery = true)
    List<Day> findDaysByCity(String city);

    @Query(value = "select * from dashboardtable", nativeQuery = true)
    List<Day> findDaysAllCity();

    @Transactional
    @Modifying
    @Query(value = "insert into dashboardtable (city, count, timestamp) values (:city,:count,:time)", nativeQuery = true)
    void postDataCity(@Param("city") String city,@Param("count") Integer count,@Param("time") String time);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Query(value = "select * from zipcodetable where timestamp = ?1", nativeQuery = true)
    List<Day> findZipcodeDaysByDate(String time);

    @Query(value = "select * from zipcodetable", nativeQuery = true)
    List<Day> findZipcodeDaysAll();

    @Transactional
    @Modifying
    @Query(value = "insert into zipcodetable (zipcode, count, timestamp) values (:city,:count,:time)", nativeQuery = true)
    void postDataZipcode(@Param("city") String city,@Param("count") Integer count,@Param("time") String time);
}
