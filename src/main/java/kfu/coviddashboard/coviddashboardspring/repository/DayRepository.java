package kfu.coviddashboard.coviddashboardspring.repository;
import kfu.coviddashboard.coviddashboardspring.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DayRepository extends JpaRepository<Day,Integer> {
    @Query(value = "select * from dashboard where city = ?1", nativeQuery = true)
    List<Day> findDayByCity(String city);
}
