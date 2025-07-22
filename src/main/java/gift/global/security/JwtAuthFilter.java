package gift.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return !path.startsWith("/api/wishes"); // "/api"로 시작하지 않으면 필터 적용하지 않음
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException
    {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter()
                .write("Missing or invalid Authorization header");
            return;
        }

        String token = header.substring(7);
        try {
            Long memberId = jwtProvider.getMemberId(token);
            // memberId를 request에 저장
            request.setAttribute("memberId", memberId);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter()
                .write("Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}


