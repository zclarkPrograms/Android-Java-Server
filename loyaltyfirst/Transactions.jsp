<%@page import="java.sql.*"%>

<%
  DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
  String[] info = GetInfo.getInfo();
  Connection conn = DriverManager.getConnection(info[0], info[1], info[2]);
  Statement stmt = conn.createStatement();
  String cid = request.getParameter("cid");

  ResultSet rs = stmt.executeQuery("select tref, t_date, t_points, amount from Transactions where cid='"+cid+"'");

  ResultSetMetaData metadata=rs.getMetaData();
  int cols=metadata.getColumnCount();

  String output = "";
  while (rs.next()) {
    output += rs.getObject(1) + "," + rs.getDate(2) + "," + rs.getObject(3) + "," + rs.getObject(4) + "#";
  }
  
  conn.close();
  out.print(output);
%>
