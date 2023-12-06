package at.aau.ase.workouttrackeruserservice;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Access(AccessType.FIELD)
@Table(name = "users")
public final class UserData {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotBlank
  @Size(max = 20)
  @Column(name = "username", nullable = false, unique = true, length = 20)
  private String username;

  @NotBlank
  @Size(max = 120)
  @Column(name = "password", length = 120, nullable = false)
  private String password;

  @NotBlank
  @Email
  @Size(max = 50)
  @Column(name = "email", nullable = false, unique = true, length = 50)
  private String email;

  public UserData(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

}