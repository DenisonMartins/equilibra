package br.com.mmtech.equilibra.backend.support;

import br.com.mmtech.equilibra.backend.usuario.domain.model.Perfil;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import br.com.mmtech.equilibra.backend.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private final UsuarioRepository usuarioRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Usuario usuario = Usuario.builder()
                .email(customUser.email())
                .perfil(Perfil.valueOf(customUser.perfil()))
                .build();

        Authentication autenticacao = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                List.of(new SimpleGrantedAuthority(usuario.getPerfil().name())));

        context.setAuthentication(autenticacao);
        return context;
    }
}
