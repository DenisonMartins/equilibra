package br.com.mmtech.equilibra.backend.config.seguranca;

import br.com.mmtech.equilibra.backend.usuario.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    public static final ZoneId SAO_PAULO_ZONE_ID = ZoneId.of("America/Sao_Paulo");

    private final SecretKey secretKey;
    private final long expiracao;

    public JwtService(@Value("${app.jwt.secret}") String secretKey,
                      @Value("${app.jwt.expiration-ms}") long expiracao) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expiracao = expiracao;
    }

    public String gerarToken(Usuario usuario) {
        LocalDate agora = LocalDate.now(SAO_PAULO_ZONE_ID);
        LocalDate validade = agora.plus(expiracao, ChronoUnit.MILLIS);

        return Jwts.builder()
                .subject(usuario.getId().toString())
                .claim("email", usuario.getEmail())
                .claim("nome", usuario.getNome())
                .claim("perfil", usuario.getPerfil().name())
                .issuedAt(Date.from(agora.atStartOfDay(SAO_PAULO_ZONE_ID).toInstant()))
                .expiration(Date.from(validade.atStartOfDay(SAO_PAULO_ZONE_ID).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    public Long extrairUsuarioId(String token) {
        Claims claims = extrairClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public boolean isTokenValido(String token) {
        try {
            Claims claims = extrairClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
