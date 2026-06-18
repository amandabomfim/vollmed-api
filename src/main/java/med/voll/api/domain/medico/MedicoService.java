package med.voll.api.domain.medico;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {
    @Autowired
    MedicoRepository medicoRepository;

    public Medico cadastrar(DadosCadastroMedico dados){
        Medico medico = Medico.builder()
                .nome(dados.nome())
                .email(dados.email())
                .telefone(dados.telefone())
                .crm(dados.crm())
                .especialidade(dados.especialidade())
                .endereco(new Endereco(dados.endereco()))
                .ativo(true)
                .build();


        medicoRepository.save(medico);
        return medico;

    }
}