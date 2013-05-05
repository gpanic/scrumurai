package scrumurai.cors;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {

    public CORSFilter() {
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers", "Content-Type");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
        chain.doFilter(request, response);
    }
}
