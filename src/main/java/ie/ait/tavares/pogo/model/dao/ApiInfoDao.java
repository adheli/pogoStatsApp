package ie.ait.tavares.pogo.model.dao;

import ie.ait.tavares.pogo.model.entity.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiInfoDao extends JpaRepository<ApiInfo, Integer> {
}
