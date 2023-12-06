package at.aau.ase.workouttrackeruserservice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDataRepository extends JpaRepository<UserData, Long>, JpaSpecificationExecutor<UserData> {

  Optional<UserData> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Boolean existsByUsernameAndPassword(String username, String password);

}