package at.aau.ase.workouttrackeruserservice;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDataServiceImpl implements UserDataService {

  private final PasswordEncoder passwordEncoder;
  private final UserDataRepository userDataRepository;

  public UserDataServiceImpl(PasswordEncoder passwordEncoder, UserDataRepository userDataRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userDataRepository = userDataRepository;
  }

  @Override
  public UserData createUser(String username, String password, String email) {
    String encodedPassword = passwordEncoder.encode(password);
    UserData userData = new UserData(username, encodedPassword, email);

    return userDataRepository.save(userData);
  }

  @Override
  public boolean isValidUser(String username, String password) {
    Optional<UserData> userData = userDataRepository.findByUsername(username);

    return userData.isPresent() && passwordEncoder.matches(password, userData.get().getPassword());
  }

  @Override
  public boolean userExistsByUsername(String username) {
    return userDataRepository.existsByUsername(username);
  }

  @Override
  public Optional<UserData> findByUsername(String username) {
    return userDataRepository.findByUsername(username);
  }

}
