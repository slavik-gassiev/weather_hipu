package com.slava.repositories;

import com.slava.entities.Location;
import com.slava.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> getLocationByUser(User user);
}
