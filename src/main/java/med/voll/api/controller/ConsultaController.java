package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.consulta.ConsultaService;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    @Autowired
    ConsultaService service;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados){
        service.agendar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoConsulta(null,null,null,null));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity cancelar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCancelamentoConsulta dados)
    {
        service.cancelar(id,dados);
        return ResponseEntity.noContent().build();
    }


//    @DeleteMapping("/{id}")
//    @Transactional
//    public ResponseEntity excluir(@PathVariable Long id) {
//        var medico = repository.getReferenceById(id);
//        medico.excluir();
//
//        return ResponseEntity.noContent().build();
//    }
}
