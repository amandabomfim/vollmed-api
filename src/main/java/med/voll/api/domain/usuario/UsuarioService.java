package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;

    public void createUser(DadosAutenticacao dados){
        Usuario usuario = Usuario.builder()
                .login(dados.login())
                .senha(new BCryptPasswordEncoder().encode(dados.senha()))
                .build();

        repository.save(usuario);
    }
}
