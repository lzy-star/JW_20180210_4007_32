package Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//idea启动服务器的时候会请求一下tomcat的根目录。过滤器是针对所有的请求起作用，这个请求自然会被过滤器捕捉到
@WebFilter(filterName = "Filter 0", urlPatterns = {"/*"})

public class Filter0_Encoding implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("Filter 0 - encoding begins");
        //执行强制类型转换
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        //声明变量，获得发出请求字符串的客户端地址
        String path= request.getRequestURI();
        //获得客户端向服务器端传送数据的方法，有get、post、put，delete等类型
        String method=request.getMethod();
        //该方法是判断字符串中是否有子字符串
        if (path.contains("/login") || path.contains("/myapp")){
            System.out.println("未设置字符编码格式");
        }else {
            System.out.println(method);
            response.setContentType("text/html;charset=UTF-8");
            System.out.println("设置响应对象字符编码格式为utf8");
            if (method=="POST"||method=="PUT"){
                System.out.println(method);
                request.setCharacterEncoding("UTF-8");
                System.out.println("设置请求对象字符编码格式为utf8");
            }
        }
        //执行其他过滤器，如过滤器已经执行完毕，则执行原请求
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Filter 0 - encoding ends");
    }
}
