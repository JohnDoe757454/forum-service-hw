package telran.java52.security.filter;

import java.io.IOException;
import java.security.Principal;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.java52.accounting.dao.UserRepository;
import telran.java52.accounting.exceptions.UserNotFoundException;
import telran.java52.accounting.model.Role;
import telran.java52.accounting.model.UserAccount;

@Component
@RequiredArgsConstructor
@Order(20)
public class AdminManagingRolesFilter implements Filter {

	final UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (request.getServletPath().matches("/account/user/[^\\/]+\\/role/[^\\/]+$")) {
			String login = request.getUserPrincipal().getName();
			UserAccount user = userRepository.findById(login).orElseThrow(UserNotFoundException::new);
			if (user.getRoles().stream().anyMatch(r -> Role.ADMINISTRATOR.equals(r))) {
				chain.doFilter(request, response);
				return;
			} else {
				response.sendError(401);
				return;
			}
		}
		chain.doFilter(request, response);

	}

}
