package med.voll.api.domain.paciente;

import med.voll.api.domain.endereco.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {
    @Autowired
    PacienteRepository pacienteRepository;

    public Paciente criar(DadosCadastroPaciente dados){
        Paciente paciente = Paciente.builder()
                .nome(dados.nome())
                .email(dados.email())
                .telefone(dados.telefone())
                .cpf(dados.cpf())
                .endereco(new Endereco(dados.endereco()))
                .ativo(true)
                .build();

        pacienteRepository.save(paciente);
        return paciente;
    }
}
