<%@page import="java.sql.*"%>

<%
  String path = "";
  DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
  Connection conn = DriverManager.getConnection("jdbc:sqlite:"+path);

  Statement stmt = conn.createStatement();
  String cid = request.getParameter("cid");

  ResultSet rs = stmt.executeQuery("select tref, t_date, t_points, amount from Transactions where cid='"+cid+"'");

  ResultSetMetaData metadata=rs.getMetaData();
  int cols=metadata.getColumnCount();

  String output = "";
  while (rs.next()) {
    String rowStr="";

    for(int i=1;i<=cols;i++){
        rowStr+=rs.getObject(i) + ",";
    }
    output += rowStr.replaceAll(",$", "") + "#";
  }
  conn.close();
  out.print(output);
%>
