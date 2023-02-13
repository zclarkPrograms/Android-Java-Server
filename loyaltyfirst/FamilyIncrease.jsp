<%@page import="java.sql.*"%>

<%
  DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
  String[] info = GetInfo.getInfo();
  Connection conn = DriverManager.getConnection(info[0], info[1], info[2]);
  Statement stmt = conn.createStatement();
  String fid = request.getParameter("fid");
  String cid = request.getParameter("cid");
  String npoints = request.getParameter("npoints");

  int rows = stmt.executeUpdate("update point_accounts set num_of_points = num_of_points + " + npoints + " where family_id = " + fid + " and cid != " + cid);

  conn.close();

  if(rows>0){
    out.print("Point accounts for Family " + fid + " were updated successfully.");
  }
  else{
    out.print("Unable to update point accounts for Family " + fid);
  }
  
%>
