package at.aau.ase.workouttrackeruserservice;

import java.util.Optional;

public interface UserDataService {

  UserData createUser(String username, String password, String email);

  boolean isValidUser(String username, String password);

  boolean userExistsByUsername(String username);

  Optional<UserData> findByUsername(String username);
}
