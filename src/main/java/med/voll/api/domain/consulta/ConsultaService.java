package med.voll.api.domain.consulta;

import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        LocalTime horaIncio = LocalTime.of(7, 0);
        LocalTime horaFinal = LocalTime.of(17, 0);

        Paciente paciente = pacienteRepository.findById(dados.idPaciente())
                .orElseThrow(()->new RuntimeException("Paciente não encontrado"));


        if (dados.data().getDayOfWeek() == DayOfWeek.SATURDAY || dados.data().toLocalTime().isBefore(horaIncio) || dados.data().toLocalTime().isAfter(horaFinal)){
            throw new RuntimeException("Data e horario da consulta inválidas");
        }
        if (dados.data().isBefore(LocalDateTime.now().plusMinutes(30))){
            throw new RuntimeException("O agendamento deve ser feito com 30 minutos de antecedencia");
        }
        if (paciente.getAtivo() == false ){
            throw new RuntimeException("Registro inativo");
        }

        for (Consulta consulta : listarTodasConsultas()){
            if (dados.idPaciente().equals(consulta.getPaciente().getId())){
                if (dados.data().getDayOfMonth() == (consulta.getData().getDayOfMonth())){
                    throw new RuntimeException("Paciente já tem uma consulta agendada para esse dia");
                }
            }if(dados.idMedico().equals(consulta.getMedico().getId())){
                if (dados.data().equals(consulta.getData())){
                    throw new RuntimeException("Médico já tem uma consulta agendada para esse dia");
                }
            }
        }


        Consulta consulta = Consulta.builder()
                .medico(escolherMedico(dados))
                .paciente(paciente)
                .data(dados.data())
                .isCancelado(false)
                .especialidade(dados.especialidade())
                .build();

        consultaRepository.save(consulta);
    }

    private List<Consulta> listarTodasConsultas(){
        return consultaRepository.findAll();
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados){
        if (dados.idMedico() != null){
            Medico medico = medicoRepository.getReferenceById(dados.idMedico());
            if (medico.getAtivo() == false){
                throw new RuntimeException("Médico inativo");
            }
            return medico;
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
