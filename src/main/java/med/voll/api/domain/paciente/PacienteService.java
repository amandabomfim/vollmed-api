package med.voll.api.domain.paciente;

import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
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

    public Paciente alterar(DadosAtualizacaoPaciente dados) {
        Paciente paciente = pacienteRepository.findById(dados.id()).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        if (dados.nome() != null) {
            paciente.setNome(dados.nome());
        } else if (dados.telefone() != null) {
            paciente.setTelefone(dados.telefone());
        } else if (dados.endereco() != null) {
            paciente.setEndereco(new Endereco((dados.endereco())));
        }

        return paciente;
    }
}
