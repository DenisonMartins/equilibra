package br.com.mmtech.equilibra.backend.convite;

import br.com.mmtech.equilibra.backend.AbstractIntegrationTest;
import br.com.mmtech.equilibra.backend.convite.domain.dto.AceiteConviteRequest;
import br.com.mmtech.equilibra.backend.convite.domain.dto.ConviteResponse;
import br.com.mmtech.equilibra.backend.convite.domain.dto.ValidarConviteResponse;
import br.com.mmtech.equilibra.backend.convite.service.ConviteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ConviteIT extends AbstractIntegrationTest {

    @Autowired
    ConviteService conviteService;

    @Test
    @DisplayName("Deve gerar convite, validar token e aceitar criando o usuário")
    void deveCompletarCicloDeVidaDoConvite() {
        String email = "teste@email.com";

        ConviteResponse convite = conviteService.criar(email);
        assertThat(convite.id()).isNotNull();
        assertThat(convite.token()).isNotBlank();
        assertThat(convite.utilizado()).isFalse();

        ValidarConviteResponse validacao = conviteService.validarHash(convite.token());
        assertThat(validacao.valido()).isTrue();
        assertThat(validacao.email()).isEqualTo(email);

        AceiteConviteRequest novoUsuario = new AceiteConviteRequest(convite.token(), "Novo usuário teste", "SenhaSegura@123");
        conviteService.aceitarConvite(novoUsuario);

        ValidarConviteResponse validacaoAposUso = conviteService.validarHash(convite.token());
        assertThat(validacaoAposUso.valido()).isFalse();
    }
}
