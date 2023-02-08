<%@page import="java.sql.*"%>

<%
  DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
  String[] info = GetInfo.getInfo();
  Connection conn = DriverManager.getConnection(info[0], info[1], info[2]);
  Statement stmt = conn.createStatement();
  String prizeid = request.getParameter("prizeid");
  String cid = request.getParameter("cid");

  ResultSet rs = stmt.executeQuery("select p_description, points_needed, r_date, center_name from redemption_history r, prizes p, exchgcenters e where r.prize_id = p.prize_id and r.center_id = e.center_id and r.prize_id = " + prizeid + "and r.cid = " + cid);

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
