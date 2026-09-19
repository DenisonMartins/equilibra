package br.com.mmtech.equilibra.backend.lancamento;

import br.com.mmtech.equilibra.backend.AbstractIntegrationTest;
import br.com.mmtech.equilibra.backend.comum.exception.EntidadeNaoEncontradaException;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoRequest;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.Categoria;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.Lancamento;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.TipoLancamento;
import br.com.mmtech.equilibra.backend.lancamento.repository.CategoriaRepository;
import br.com.mmtech.equilibra.backend.lancamento.repository.LancamentoRepository;
import br.com.mmtech.equilibra.backend.support.WithMockCustomUser;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Perfil;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import br.com.mmtech.equilibra.backend.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LancamentoIT extends AbstractIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    private Usuario usuarioPrincipal;
    private Usuario usuarioOutro;
    private Categoria categoriaUsuario;

    @BeforeEach
    void setUp() {
        lancamentoRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();

        this.usuarioPrincipal = usuarioRepository.saveAndFlush(Usuario.builder()
                        .nome("Usuario Teste")
                        .email("usuario@equilibra.com.br")
                        .senha("hash_senha")
                        .ativo(true)
                        .perfil(Perfil.ROLE_USER)
                        .build());

        UsernamePasswordAuthenticationToken autencicacao = new UsernamePasswordAuthenticationToken(
                this.usuarioPrincipal,
                null,
                List.of(new SimpleGrantedAuthority(this.usuarioPrincipal.getPerfil().name()))
        );
        SecurityContextHolder.getContext().setAuthentication(autencicacao);

        usuarioOutro = usuarioRepository.findByEmail("outro@equilibra.com.br")
                .orElseGet(() -> usuarioRepository.saveAndFlush(Usuario.builder()
                .nome("Outro Usuario")
                .email("outro@equilibra.com.br")
                .senha("hash_senha")
                .ativo(true)
                .perfil(Perfil.ROLE_USER)
                .build()));

        categoriaUsuario = categoriaRepository.saveAndFlush(Categoria.builder()
                .nome("Alimentação")
                .cor("#ef4444")
                .usuario(usuarioPrincipal)
                .build());
    }

    @Test
    @DisplayName("Deve criar um lançamento de despesa com sucesso")
    @WithMockCustomUser
    void deveCriarLancamentoComSucesso() throws Exception {
        LancamentoRequest request = new LancamentoRequest(
                "Supermercado Mensal",
                new BigDecimal("350.50"),
                LocalDate.now(),
                TipoLancamento.DESPESA,
                categoriaUsuario.getId(),
                true
        );

        mockMvc.perform(post("/lancamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.descricao").value("Supermercado Mensal"))
                .andExpect(jsonPath("$.valor").value(350.50))
                .andExpect(jsonPath("$.tipo").value("DESPESA"))
                .andExpect(jsonPath("$.pago").value(true))
                .andExpect(jsonPath("$.categoria.id").value(categoriaUsuario.getId().toString()));
    }

    @Test
    @DisplayName("Deve calcular resumo financeiro do mês com receitas, despesas e saldo")
    @WithMockCustomUser
    void deveCalcularResumoFinanceiro() throws Exception {
        LocalDate hoje = LocalDate.now();

        // Receita: 5000.00
        lancamentoRepository.save(Lancamento.builder()
                .descricao("Salário")
                .valor(new BigDecimal("5000.00"))
                .data(hoje)
                .tipo(TipoLancamento.RECEITA)
                .pago(true)
                .categoria(categoriaUsuario)
                .usuario(usuarioPrincipal)
                .build());

        // Despesa: 1200.00
        lancamentoRepository.save(Lancamento.builder()
                .descricao("Aluguel")
                .valor(new BigDecimal("1200.00"))
                .data(hoje)
                .tipo(TipoLancamento.DESPESA)
                .pago(true)
                .categoria(categoriaUsuario)
                .usuario(usuarioPrincipal)
                .build());

        mockMvc.perform(get("/lancamentos/resumo")
                        .param("competencia", YearMonth.from(hoje).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReceitas").value(5000.00))
                .andExpect(jsonPath("$.totalDespesas").value(1200.00))
                .andExpect(jsonPath("$.saldo").value(3800.00));
    }

    @Test
    @DisplayName("Não deve listar lançamentos pertencentes a outro usuário")
    @WithMockCustomUser
    void naoDeveListarLancamentosDeOutroUsuario() throws Exception {
        // Categoria e lançamento criados para o "usuarioOutro"
        Categoria categoriaOutro = categoriaRepository.save(Categoria.builder()
                .nome("Investimentos")
                .usuario(usuarioOutro)
                .build());

        lancamentoRepository.save(Lancamento.builder()
                .descricao("Ações")
                .valor(new BigDecimal("1000.00"))
                .data(LocalDate.now())
                .tipo(TipoLancamento.DESPESA)
                .pago(true)
                .categoria(categoriaOutro)
                .usuario(usuarioOutro)
                .build());

        mockMvc.perform(get("/lancamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar excluir lançamento de outro usuário")
    @WithMockCustomUser
    void naoDeveExcluirLancamentoDeOutroUsuario() throws Exception {
        Categoria categoriaOutro = categoriaRepository.save(Categoria.builder()
                .nome("Saúde")
                .usuario(usuarioOutro)
                .build());

        Lancamento lancamentoOutro = lancamentoRepository.save(Lancamento.builder()
                .descricao("Farmácia")
                .valor(new BigDecimal("85.00"))
                .data(LocalDate.now())
                .tipo(TipoLancamento.DESPESA)
                .pago(true)
                .categoria(categoriaOutro)
                .usuario(usuarioOutro)
                .build());

        mockMvc.perform(delete("/lancamentos/{id}", lancamentoOutro.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Recurso Não Encontrado"))
                .andExpect(jsonPath("$.detail").value("Lançamento não encontrado"));

        // Confirma que não deletou do banco
        assertFalse(lancamentoRepository.findById(lancamentoOutro.getId()).isEmpty());
    }
}
