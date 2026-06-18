package med.voll.api.domain.consulta;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {
    @Autowired
    ConsultaRepository consultaRepository;

    @Autowired
    MedicoRepository medicoRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    public void agendar(DadosAgendamentoConsulta dados) {
//        Medico medico = null;
//
//        if (dados.idMedico() != null){
//             medico = medicoRepository.findById(dados.idMedico()).orElseThrow(()-> new RuntimeException("Médico não encontrado"));
//        }

        Paciente paciente = pacienteRepository.findById(dados.idPaciente()).orElseThrow(()-> new RuntimeException("Paciente não encontrado"));

        Consulta consulta = Consulta.builder()
                .medico(escolherMedico(dados))
                .paciente(paciente)
                .data(dados.data())
                .isCancelado(false)
                .especialidade(dados.especialidade())
                .build();

        consultaRepository.save(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados){

        if (dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if (dados.especialidade() == null){
            throw new RuntimeException("Especialidade é obrigatoria quando médico não for escolhido");
        }
        Page<Medico> medicos = medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data(), PageRequest.of(0, 1));

        return medicos.getContent().get(0);
    }

    public void cancelar(Long id, DadosCancelamentoConsulta dados){
        Consulta consultaEncontrada = consultaRepository.findById(id).orElseThrow(()->new RuntimeException("Consulta não encontrada"));

        if (consultaEncontrada.getIsCancelado() == true){
            throw new RuntimeException("Consulta já foi cancelada");
        }

        if (dados.motivo() == null){
            throw new RuntimeException("O motivo do cancelamento deve ser informado");
        }


        if (consultaEncontrada.getData().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new RuntimeException("A consulta deve ser cancelada com 24h de antecedência");
        }

        consultaEncontrada.setIsCancelado(true);
    }
}
