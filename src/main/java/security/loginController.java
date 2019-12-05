package security;

import com.alibaba.fastjson.JSONObject;
import domain.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login.ctl")
public class loginController extends HttpServlet{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //定义username，password对象
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //创建json对象 message，以便前端响应信息
        JSONObject message  = new JSONObject();
        try {
            //创建loggedUser对象，若用户名和密码匹配，则得到一个代表当前用户的User对象
            User loggedUser = UserService.getInstance().login(username,password);
            //如果loggedUser不为空
            if (loggedUser!=null){
                message.put("message", "登录成功");
                //返回与当前请求关联的Session对象
                HttpSession session = req.getSession();
                //十分钟没有操作，则使session失效
                session.setMaxInactiveInterval(10*60);
                //将loggedUser对象写入session的属性，名称为currentUser，以便在其它请求中使用
                session.setAttribute("currentUser",loggedUser);
                resp.getWriter().println(message);
                //此处应重定向到索引页
                return;
            }else {
                message.put("message","用户名或密码错误");
            }
        } catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }    catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }resp.getWriter().println(message);
    }
}



