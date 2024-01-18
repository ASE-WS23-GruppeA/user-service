package at.aau.ase.workouttrackeruserservice.service;

import java.util.Optional;

import at.aau.ase.workouttrackeruserservice.model.UserData;

public interface UserDataService {

  UserData createUser(String username, String password, String email);

  boolean isValidUser(String username, String password);

  boolean userExistsByUsernameOrEmail(String username, String email);

  Optional<UserData> findByUsername(String username);
}
