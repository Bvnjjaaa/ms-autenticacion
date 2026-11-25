package cl.novabank.autenticacion;

import static org.assertj.core.api.Assertions.assertThat;

import cl.novabank.autenticacion.model.Usuario;
import cl.novabank.autenticacion.repository.UsuarioRepository;
import cl.novabank.autenticacion.service.AutenticacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest(showSql = false)
@Import(AutenticacionService.class)
class AutenticacionServiceTest {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private AutenticacionService service;

    @Test
    void guardaUnHashYOmiteLaPasswordEnLaRespuesta() throws Exception {
        Usuario usuario = Usuario.builder()
                .rut("12345678-5").nombre("Demostración").rol("CLIENTE")
                .email("demo@example.test").password("clave-ficticia").build();

        Usuario registrado = service.registrarUsuario(usuario);
        Usuario persistido = repository.findByRut(registrado.getRut()).orElseThrow();

        assertThat(persistido.getPassword()).isNotBlank().isNotEqualTo("clave-ficticia");
        assertThat(new ObjectMapper().writeValueAsString(registrado)).doesNotContain("password");
        assertThat(service.login(registrado.getRut(), "clave-ficticia")).isNotBlank();
        assertThat(service.login(registrado.getRut(), "otra-clave")).isNull();
    }
}
