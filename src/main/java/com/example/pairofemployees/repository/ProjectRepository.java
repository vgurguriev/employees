package com.example.pairofemployees.repository;

import com.example.pairofemployees.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findFirstByOrderByTotalTimeDesc();

/*    TODO: create query to get team who worked together
       on common PROJECTS for the longest period of time
 */
}
