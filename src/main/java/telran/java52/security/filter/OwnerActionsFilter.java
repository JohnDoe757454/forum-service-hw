package telran.java52.security.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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
@Order(30)
public class OwnerActionsFilter implements Filter {
	final UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();

		if (path.matches("/account/user/[^\\/]+$")) {
			if (HttpMethod.PUT.matches(request.getMethod())
					&& isOwner(request.getUserPrincipal().getName(), path, false)) {
				chain.doFilter(request, response);
				return;
			} else if (HttpMethod.DELETE.matches(request.getMethod())
					&& isOwner(request.getUserPrincipal().getName(), path, true)) {

				chain.doFilter(request, response);
				return;
			} else if (HttpMethod.GET.matches(request.getMethod())) {
				chain.doFilter(request, response);
				return;
			}

			else {
				response.sendError(401);
				return;
			}
		}
		chain.doFilter(request, response);

	}

	private boolean isOwner(String name, String path, boolean isDelete) {
		String[] urlParts = path.split("/");
		if (urlParts[3].equals(name)) {
			return true;
		}
		if (!(urlParts[3].equals(name)) && isDelete) {
			UserAccount user = userRepository.findById(name).orElseThrow(UserNotFoundException::new);
			return user.getRoles().stream().anyMatch(r -> Role.ADMINISTRATOR.equals(r));
		}
		return false;
	}

}
