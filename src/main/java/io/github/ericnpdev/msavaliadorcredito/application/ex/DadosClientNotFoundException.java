package io.github.ericnpdev.msavaliadorcredito.application.ex;

public class DadosClientNotFoundException extends Exception{
    public DadosClientNotFoundException() {
        super("Dados do clientes não encontrados para o cpf informado.");
    }
}
