package Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter 20",urlPatterns = {"/*"})
public class Filter2_LoginSession implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 20 - LoginSessionFilter begin!");
        //执行强制类型转换
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        // (false:没有session也不自动创建)
        HttpSession session = request.getSession(false);
        //声明变量，获得发出请求字符串的客户端地址
        String path = request.getRequestURI();
        if (path.contains("/login.ctl") || path.contains("/logout.ctl")){//let login and logout go
            chain.doFilter(req, resp);
            System.out.println("Filter 20 - LoginSessionFilter ends!");
        }else  if (session != null && session.getAttribute("currentUser") != null){//if login ,then go
            chain.doFilter(req,resp);
            System.out.println("Filter 20 - LoginSessionFilter ends!");
        }else {//u have to login firstly
            response.getWriter().println("您没有登录，请登录");
        }


    }

    public void init(FilterConfig config) throws ServletException {

    }

}
