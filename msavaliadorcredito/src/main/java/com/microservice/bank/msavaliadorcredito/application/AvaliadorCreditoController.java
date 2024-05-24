package com.microservice.bank.msavaliadorcredito.application;

import com.microservice.bank.msavaliadorcredito.application.ex.DadosClientNotFoundException;
import com.microservice.bank.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import com.microservice.bank.msavaliadorcredito.application.ex.ErroSolicitacaoCartaoException;
import com.microservice.bank.msavaliadorcredito.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("avaliacoes-credito")
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "OK";
    }

    @GetMapping(value = "situacao-cliente",params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf){
       try {
           SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
           return ResponseEntity.ok(situacaoCliente);
       }catch (DadosClientNotFoundException e){
           return ResponseEntity.notFound().build();
       }catch (ErroComunicacaoMicroservicesException e){
           return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
       }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvalicao dados){
        try {
            RetornoAvalicaoCliente retornoAvalicaoCliente = avaliadorCreditoService.realizarAvalicao(dados.getCpf(), dados.getRenda());
            return ResponseEntity.ok(retornoAvalicaoCliente);
        }catch (DadosClientNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch (ErroComunicacaoMicroservicesException e){
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }


    @PostMapping("solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados){
        try {
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService
                    .solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        }catch (ErroSolicitacaoCartaoException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}
