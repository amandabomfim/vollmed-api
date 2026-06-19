package med.voll.api.domain.paciente;

import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosListagemMedico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<DadosListagemPaciente> listar(Pageable pagina){
        return  pacienteRepository.findAllByAtivoTrue(pagina).map(DadosListagemPaciente::new);
    }
}
