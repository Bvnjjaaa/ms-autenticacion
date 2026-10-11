package cl.novabank.autenticacion.controller;

import cl.novabank.autenticacion.model.Usuario;
import cl.novabank.autenticacion.service.AutenticacionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacionController {

    private final AutenticacionService autenticacionService;

    public record LoginRequest(
            @NotBlank(message = "El RUT es obligatorio") String rut,
            @NotBlank(message = "La password es obligatoria") String password) {
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario nuevo = autenticacionService.registrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = autenticacionService.login(request.rut(), request.password());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(Map.of("token", token, "rut", request.rut()));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(autenticacionService.listarTodos());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = autenticacionService.obtenerPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        boolean eliminado = autenticacionService.eliminarUsuario(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}