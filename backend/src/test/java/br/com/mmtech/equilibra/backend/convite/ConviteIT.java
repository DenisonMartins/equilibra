package br.com.mmtech.equilibra.backend.convite;

import br.com.mmtech.equilibra.backend.TestcontainersConfiguration;
import br.com.mmtech.equilibra.backend.convite.dto.AceiteConviteRequest;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteResponse;
import br.com.mmtech.equilibra.backend.convite.dto.ValidarTokenResponse;
import br.com.mmtech.equilibra.backend.convite.entity.Convite;
import br.com.mmtech.equilibra.backend.convite.service.ConviteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ConviteIT {

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

        ValidarTokenResponse validacao = conviteService.validarToken(convite.token());
        assertThat(validacao.valido()).isTrue();
        assertThat(validacao.email()).isEqualTo(email);

        AceiteConviteRequest novoUsuario = new AceiteConviteRequest(convite.token(), "Novo usuário teste", "SenhaSegura@123");
        conviteService.aceitarConvite(novoUsuario);

        ValidarTokenResponse validacaoAposUso = conviteService.validarToken(convite.token());
        assertThat(validacaoAposUso.valido()).isFalse();
    }
}
