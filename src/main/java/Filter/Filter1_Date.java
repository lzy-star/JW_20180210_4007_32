package Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@WebFilter(filterName = "Filter 1", urlPatterns = {"/*"})
public class Filter1_Date implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
   public void destroy() {}//释放占用资源

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("Filter 1 - date begins");
        //执行强制类型转换
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        //声明变量，获得发出请求字符串的客户端地址
        String path=request.getRequestURI();
        //获取一个Calendar的实例
        Calendar cal = Calendar.getInstance();
        //获取当前时间，.MONTH从0开始
       String time= cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH) + 1)+
               "月"+cal.get(Calendar.DATE)+"日"+cal.get(Calendar.HOUR_OF_DAY)+": "+cal.get(Calendar.MINUTE);
        //输出当前时间
        System.out.println(path+" @ "+ time);
        ////执行其他过滤器，如过滤器已经执行完毕，则执行原请求
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Filter 1 - date ends");
    }
}
