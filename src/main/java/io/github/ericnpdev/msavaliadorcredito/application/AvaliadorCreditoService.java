package io.github.ericnpdev.msavaliadorcredito.application;

import feign.FeignException;
import io.github.ericnpdev.msavaliadorcredito.application.ex.DadosClientNotFoundException;
import io.github.ericnpdev.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import io.github.ericnpdev.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.ericnpdev.msavaliadorcredito.domain.model.DadosCliente;
import io.github.ericnpdev.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.ericnpdev.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.ericnpdev.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf)
            throws DadosClientNotFoundException, ErroComunicacaoMicroservicesException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClientNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
}