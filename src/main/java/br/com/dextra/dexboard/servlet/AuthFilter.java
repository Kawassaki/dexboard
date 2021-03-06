package br.com.dextra.dexboard.servlet;

import br.com.dextra.dexboard.utils.Config;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);

	private static final String ORG_DOMAIN = Context.isProductionEnvironment() ? Config.getProperty("dxb.domain", "dextra-sw.com") : "dextra-sw.com";
	private static final String SINGLE_LOGIN_EMAIL = Context.isProductionEnvironment() ? Config.getProperty("dxb.singleLoginEmail", null) : null;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		UserService service = UserServiceFactory.getUserService();

		String uri = request.getRequestURI();
		User user = service.getCurrentUser();

		if (isAdminPath(uri)) {
			chain.doFilter(request, response);
			return;
		}

		if (user == null) {
			if ("GET".equals(request.getMethod())) {
				goToLoginPage(response, service, uri);
				return;
			}

			unauthorizedRequest(response, service);
			return;
		}

		if (isLogoutPath(uri) || !isValidOrgUser(user)) {
			goToLogoutPage(response, service);
			return;
		}

		chain.doFilter(request, response);
	}

	private boolean isAdminPath(String uri) {
		return uri.startsWith("/_ah") || uri.startsWith("/cron/") || uri.startsWith("/_tools");
	}

	private void unauthorizedRequest(HttpServletResponse response, UserService service) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.append("<html><head><title>Murkfight</title></head><body>You need login <a href=\"");
		writer.append(service.createLoginURL("/"));
		writer.append("\">here</a></body></html>");
	}

	private void goToLoginPage(HttpServletResponse response, UserService service, String uri) throws IOException {
		response.sendRedirect(service.createLoginURL(uri));
	}

	private void goToLogoutPage(HttpServletResponse response, UserService service) throws IOException {
		response.sendRedirect(service.createLogoutURL("/"));
	}

	private boolean isLogoutPath(String uri) {
		return uri.equals("/logout");
	}

	private boolean isValidOrgUser(User user) {
		UserService service = UserServiceFactory.getUserService();
		if (service.isUserAdmin()) {
			return true;
		}
		LOGGER.info("Logging in; singleLoginEmail = " + SINGLE_LOGIN_EMAIL + "; email: " + user.getEmail() + "; domain: " + ORG_DOMAIN);
		if (user.getEmail().equals(SINGLE_LOGIN_EMAIL)) {
			return true;
		}
		if (ORG_DOMAIN == null) {
			return false;
		}
		return user.getEmail().endsWith("@" + ORG_DOMAIN);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}
}
