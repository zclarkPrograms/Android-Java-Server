import java.sql.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.sql.DriverManager;


@WebServlet("/login")
public class Login extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String[] info = GetInfo.getInfo();

        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            
            Connection conn = DriverManager.getConnection(info[0], info[1], info[2]);
            
            Statement stmt = conn.createStatement();
            String uname = request.getParameter("user");
            String pswrd = request.getParameter("pass");
          
            String message = "";
            ResultSet rs = stmt.executeQuery("Select * from LOGIN where username='" + uname + "' AND passwd='" + pswrd +"'");
   
            while (rs.next()) {
                message += "Yes:" + rs.getString("cid");
            }
            if (message.equals("")) {
                message += "No";
            } 
            try {
               PrintWriter out = response.getWriter();
               out.println(message);
            } catch (IOException e) {}
            
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
