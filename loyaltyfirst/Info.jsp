<%@page import="java.sql.*"%>

<%
  String path = "";
  DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
  Connection conn = DriverManager.getConnection("jdbc:sqlite:"+path);

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
