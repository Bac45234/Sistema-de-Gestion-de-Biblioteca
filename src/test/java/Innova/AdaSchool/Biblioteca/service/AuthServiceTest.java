package Innova.AdaSchool.Biblioteca.service;

import Innova.AdaSchool.Biblioteca.dto.auth.AuthDto;
import Innova.AdaSchool.Biblioteca.dto.auth.LoginDto;
import Innova.AdaSchool.Biblioteca.dto.auth.RegisterDto;
import Innova.AdaSchool.Biblioteca.entity.UserEntity;
import Innova.AdaSchool.Biblioteca.exceptions.UserAlreadyExist;
import Innova.AdaSchool.Biblioteca.exceptions.UserDosentExist;
import Innova.AdaSchool.Biblioteca.repository.UserPostgreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserPostgreRepository userPostgreRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void login_UserExist(){
        // Configuración de mocks
        String email = "test@example.com";
        String password = "password";
        LoginDto loginDto = new LoginDto(email, password);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userPostgreRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("jwt-token");

        // Ejecución del método login
        AuthDto authDto = authService.login(loginDto);

        // Verificación
        assertNotNull(authDto);
        assertEquals("jwt-token", authDto.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).setEmail(email);
    }

    @Test
    public void login_UserNotExist(){
        // Configuración de mocks
        String email = "nonexistent@example.com";
        LoginDto loginDto = new LoginDto(email, "password");

        when(userPostgreRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Ejecución y verificación
        assertThrows(UserDosentExist.class, () -> authService.login(loginDto));
    }

    @Test
    public void register_UserExist() {
        // Configuración de mocks
        String email = "test@example.com";
        RegisterDto registerDto = new RegisterDto("Test", email, "password");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userPostgreRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // Ejecución y verificación
        assertThrows(UserAlreadyExist.class, () -> authService.register(registerDto));
    }

    @Test
    public void register_UserNotExist() {
        // Configuración de mocks
        String email = "newuser@example.com";
        RegisterDto registerDto = new RegisterDto("New User", email, "password");

        when(userPostgreRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("jwt-token");

        // Ejecución del método register
        AuthDto authDto = authService.register(registerDto);

        // Verificación
        assertNotNull(authDto);
        assertEquals("jwt-token", authDto.getToken());
        verify(userPostgreRepository).save(any(UserEntity.class));
        verify(userService).setEmail(email);
    }
}
