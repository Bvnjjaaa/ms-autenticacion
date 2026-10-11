package cl.novabank.autenticacion.service;

import cl.novabank.autenticacion.model.Usuario;
import cl.novabank.autenticacion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

    private static final Pattern PATRON_RUT = Pattern.compile("^(\\d{1,8})-([0-9K])$");
    private static final String ROLES_VALIDOS = "(?:CLIENTE|OPERADOR|AUDITOR)";

    private final UsuarioRepository usuarioRepository;

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La password es obligatoria");
        }
        if (usuario.getRol() == null || !usuario.getRol().matches(ROLES_VALIDOS)) {
            throw new IllegalArgumentException("Rol debe ser CLIENTE, OPERADOR o AUDITOR");
        }
        if (!validarRut(usuario.getRut())) {
            throw new IllegalArgumentException("RUT invalido");
        }
        usuario.setRut(normalizarRut(usuario.getRut()));
        if (usuarioRepository.existsByRut(usuario.getRut())) {
            throw new IllegalArgumentException("El RUT ya se encuentra registrado");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya se encuentra registrado");
        }
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }
        return usuarioRepository.save(usuario);
    }

    public String login(String rut, String password) {
        if (rut == null || password == null || !validarRut(rut)) {
            return null;
        }
        Optional<Usuario> usuarioOpt = usuarioRepository.findByRut(normalizarRut(rut));
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getPassword().equals(password)) {
            return null;
        }
        if (Boolean.FALSE.equals(usuarioOpt.get().getActivo())) {
            return null;
        }
        return "token-simulado-" + UUID.randomUUID();
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorRut(String rut) {
        if (rut == null || !validarRut(rut)) {
            return Optional.empty();
        }
        return usuarioRepository.findByRut(normalizarRut(rut));
    }

    public boolean eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        usuarioRepository.deleteById(id);
        return true;
    }

    private boolean validarRut(String rut) {
        if (rut == null || rut.isBlank()) {
            return false;
        }
        String rutNormalizado = normalizarRut(rut);
        Matcher matcher = PATRON_RUT.matcher(rutNormalizado);
        if (!matcher.matches()) {
            return false;
        }
        String cuerpo = matcher.group(1);
        char dvIngresado = matcher.group(2).charAt(0);

        int suma = 0;
        int multiplo = 2;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += (cuerpo.charAt(i) - '0') * multiplo;
            multiplo = multiplo == 7 ? 2 : multiplo + 1;
        }
        int resto = 11 - (suma % 11);
        char dvCalculado;
        if (resto == 11) {
            dvCalculado = '0';
        } else if (resto == 10) {
            dvCalculado = 'K';
        } else {
            dvCalculado = (char) ('0' + resto);
        }
        return dvCalculado == dvIngresado;
    }

    private String normalizarRut(String rut) {
        return rut.replace(".", "").trim().toUpperCase();
    }
}