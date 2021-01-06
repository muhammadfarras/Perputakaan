package org.example;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlDB {
    private static final String URL = "jdbc:mysql://localhost/";
    private static final String DATABASE_NAME = "fp";
    private static final String USER_NAME = "farras";
    private static final String PASS = "farrasmuhammad";
    private static final String TABLE_NAME = "users";
    private static final String USER_COLUMN = "userID";
    private static final String FP_COLUMN = "print1";
    private static final String P_NAME = "personName";
    private static final String P_NUMBER = "phoneNumber";
    private static final String P_EMAIL = "personEmail";

    private String preppedStmtInsert=null;

    private java.sql.Connection connection = null;
    private boolean isConnect = false;

    public class Record {
        String usertId;
        byte[] fmdBinary;
        String personName;
        String phoneNumber;
        String personEmail;

        public Record (String usertId, byte[]fmd,String personName,String phoneNumber,String personEmail){
            this.usertId = usertId;
            this.fmdBinary = fmd;
            this.personName = personName;
            this.phoneNumber = phoneNumber;
            this.personEmail = personEmail;
        }
    }


    public MysqlDB (){
        preppedStmtInsert="INSERT INTO " + TABLE_NAME + "(" + USER_COLUMN + "," + FP_COLUMN
                + "," + P_NAME +"," + P_NUMBER +"," + P_EMAIL+") VALUES(?,?,?,?,?)";
    }

    public boolean Open(){
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL+DATABASE_NAME,
                    USER_NAME,PASS);
            isConnect = true;
            System.out.println("Berhasil terhubung");
            return true;
        }
        catch (SQLException e){
//            e.printStackTrace();
            return false;
        }
    }

    public void Close() throws SQLException {
        connection.clearWarnings();
    }

    public void Insert (String userID, byte[] print1,String name, String phone, String email) throws SQLException, SQLException {
        Open();
        if (isConnect){
            System.out.println("Connection Berhasil");
            //prepered statment
            java.sql.PreparedStatement pst =connection.prepareStatement(preppedStmtInsert);
            pst.setString(1,userID);
            pst.setBytes(2,print1);
            pst.setString(3,escapeStringForMySQL(name));
            pst.setString(4,phone);
            pst.setString(5,escapeStringForMySQL(email));
            pst.execute();
        }
    }

    public List<Record> getAllFPData () throws SQLException {

        Open();

        List<Record> records = new ArrayList<>();
        String query = "SELECT * from "+TABLE_NAME;
        Statement statement =connection.createStatement();
        ResultSet resultSet =statement.executeQuery(query);

        while (resultSet.next()){
            if (resultSet.getBytes(FP_COLUMN) != null){
                records.add(new Record(resultSet.getString(USER_COLUMN)
                ,resultSet.getBytes(FP_COLUMN),resultSet.getString(P_NAME),
                        resultSet.getString(P_NUMBER),resultSet.getString(P_EMAIL)));
            }
        }

        return records;
    }

    private String escapeStringForMySQL(String s) {
        return s.replaceAll("\b","\\b")
                .replaceAll("\n","\\n")
                .replaceAll("\r", "\\r")
                .replaceAll("\t", "\\t")
                .replaceAll("\\x1A", "\\Z")
                .replaceAll("\\x00", "\\0")
                .replaceAll("'", "\\'")
                .replaceAll("\"", "\\\"");
    }

    private String escapeWildcardsForMySQL(String s) {
        return escapeStringForMySQL(s)
                .replaceAll("%", "\\%")
                .replaceAll("_","\\_");
    }
}