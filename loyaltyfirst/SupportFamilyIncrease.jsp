<%@page import="java.sql.*"%>

<%
  String path = "";
  DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
  Connection conn = DriverManager.getConnection("jdbc:sqlite:"+path);

  Statement stmt = conn.createStatement();
  String cid = request.getParameter("cid");
  String tref = request.getParameter("tref");

  ResultSet rs = stmt.executeQuery("select p.family_id, percent_added, t.t_points from point_accounts p, transactions t where p.cid = t.cid and p.cid = " + cid + " and t.tref = " + tref);

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
