package io.relationship.auth;
import io.relationship.common.exception.DuplicateResourceException;
import io.relationship.security.JwtTokenProvider;
import io.relationship.user.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UserRepository userRepo; @Mock PasswordEncoder pw;
    @Mock AuthenticationManager authManager; @Mock JwtTokenProvider jwt;
    @InjectMocks AuthService authService;

    @Test void register_success() {
        when(userRepo.existsByEmail(any())).thenReturn(false);
        when(userRepo.existsByUsername(any())).thenReturn(false);
        when(pw.encode(any())).thenReturn("hashed");
        UserEntity saved = UserEntity.builder().id(1L).username("pravin").email("pravin@test.com").passwordHash("hashed").build();
        when(userRepo.save(any())).thenReturn(saved);
        when(jwt.fromUsername(any())).thenReturn("access-token");
        when(jwt.refreshFromUsername(any())).thenReturn("refresh-token");
        var res = authService.register(new AuthDto.RegisterRequest("pravin","pravin@test.com","pass1234",null,null));
        assertThat(res.getAccessToken()).isEqualTo("access-token");
        assertThat(res.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(res.getTokenType()).isEqualTo("Bearer");
    }
    @Test void register_duplicateEmail_throws() {
        when(userRepo.existsByEmail("pravin@test.com")).thenReturn(true);
        assertThatThrownBy(()->authService.register(new AuthDto.RegisterRequest("pravin","pravin@test.com","pass1234",null,null)))
            .isInstanceOf(DuplicateResourceException.class).hasMessageContaining("Email already registered");
    }
    @Test void register_duplicateUsername_throws() {
        when(userRepo.existsByEmail(any())).thenReturn(false);
        when(userRepo.existsByUsername("pravin")).thenReturn(true);
        assertThatThrownBy(()->authService.register(new AuthDto.RegisterRequest("pravin","pravin@test.com","pass1234",null,null)))
            .isInstanceOf(DuplicateResourceException.class).hasMessageContaining("Username already taken");
    }
    @Test void refreshToken_invalidToken_throws() {
        when(jwt.validateRefreshToken("bad-token")).thenReturn(false);
        assertThatThrownBy(()->authService.refreshToken(new AuthDto.RefreshTokenRequest("bad-token")))
            .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Invalid or expired refresh token");
    }
}