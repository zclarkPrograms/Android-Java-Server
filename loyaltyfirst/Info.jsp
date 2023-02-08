<%@page import="java.sql.*"%>

<%
  DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
  String[] info = GetInfo.getInfo();
  Connection conn = DriverManager.getConnection(info[0], info[1], info[2]);
  Statement stmt = conn.createStatement();
  String cid = request.getParameter("cid");

  ResultSet rs = stmt.executeQuery("select cu.cname, pa.num_of_points from Customers cu inner join Point_Accounts pa on cu.cid=pa.cid where pa.cid="+cid);
  String output = "";
  
  while (rs.next()) {
    output += rs.getObject(1) + "," + rs.getObject(2) + "#";
  }
  
  conn.close();
  out.print(output);
%>
