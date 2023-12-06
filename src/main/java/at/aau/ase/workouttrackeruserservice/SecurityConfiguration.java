package at.aau.ase.workouttrackeruserservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration {

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//        .csrf(AbstractHttpConfigurer::disable)
//        .authorizeHttpRequests(authorize -> authorize
//            .requestMatchers("/login").permitAll()
//            .anyRequest().authenticated()
//        );
//
//    return http.build();
//  }
//
//  @Bean
//  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
//                                                     PasswordEncoder passwordEncoder) {
//    var authenticationProvider = new DaoAuthenticationProvider();
//
//    authenticationProvider.setUserDetailsService(userDetailsService);
//    authenticationProvider.setPasswordEncoder(passwordEncoder);
//
//    return new ProviderManager(authenticationProvider);
//  }
//
//  @Bean
//  UserDetailsManager users(DataSource dataSource, AuthenticationManager authenticationManager) {
//    JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//
//    users.setAuthenticationManager(authenticationManager);
//
//    return users;
//  }
//
//  @Bean
//  public UserDetailsService userDetailsService(UserDataRepository userDataRepository) {
//    return new UserDetailsServiceImpl(userDataRepository);
//  }
//
  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}
