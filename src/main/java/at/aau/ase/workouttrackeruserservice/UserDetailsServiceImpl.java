package at.aau.ase.workouttrackeruserservice;

//import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl //implements UserDetailsService
{

  private final UserDataRepository userDataRepository;

  public UserDetailsServiceImpl(UserDataRepository userDataRepository) {
    this.userDataRepository = userDataRepository;
  }

//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    UserData userData = userDataRepository.findByUsername(username)
//        .orElseThrow(() -> new UsernameNotFoundException("UserData not found for username: " + username));
//
//    return new UserDataDetails(userData);
//  }

}
