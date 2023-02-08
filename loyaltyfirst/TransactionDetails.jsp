<%@page import="java.sql.*"%>

<%
  DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
  String[] info = GetInfo.getInfo();
  Connection conn = DriverManager.getConnection(info[0], info[1], info[2]);
  Statement stmt = conn.createStatement();
  String tref = request.getParameter("tref");

  ResultSet rs = stmt.executeQuery("select t_date, t_points, prod_name, prod_points, quantity from Transactions t, Transactions_Products tp, Products p where t.tref = tp.tref and tp.prod_id=p.prod_id and t.tref='"+tref+"'");

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
