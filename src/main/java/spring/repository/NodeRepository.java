package spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.entities.NodeEntity;

import java.util.Collection;

public interface NodeRepository extends JpaRepository<NodeEntity, Long> {

    @Query(
            value = "SELECT * FROM nodes " +
                    "WHERE earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.latitude, nodes.longitude)) < ?3 " +
                    "ORDER BY earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.latitude, nodes.longitude))" +
                    " ASC",
            nativeQuery = true
    )
    Collection<NodeEntity> getNodesInRadius(Double latitude, Double longitude, Double radius);

}