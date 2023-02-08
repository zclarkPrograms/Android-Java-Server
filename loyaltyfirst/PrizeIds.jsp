<%@page import="java.sql.*"%>

<%
  DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
  String[] info = GetInfo.getInfo();
  Connection conn = DriverManager.getConnection(info[0], info[1], info[2]);
  Statement stmt = conn.createStatement();
  String cid = request.getParameter("cid");

  ResultSet rs = stmt.executeQuery("select distinct r.prize_id from customers c, redemption_history r where c.cid = r.cid and c.cid = "+cid);

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
