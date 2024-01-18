package at.aau.ase.workouttrackeruserservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import at.aau.ase.workouttrackeruserservice.model.UserData;

public interface UserDataRepository extends JpaRepository<UserData, Long>, JpaSpecificationExecutor<UserData> {

  Optional<UserData> findByUsername(String username);

  Boolean existsByUsernameOrEmail(String username, String email);

  Boolean existsByEmail(String email);

  Boolean existsByUsernameAndPassword(String username, String password);

}