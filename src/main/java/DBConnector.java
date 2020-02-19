/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Willy
 */
import java.sql.*;
import java.util.ArrayList;

public class DBConnector {

    //public class MainClass {
    private final String driverName = "com.mysql.jdbc.Driver";
    private final String connectionString = "jdbc:mysql://localhost:3306/test";
    private String login = "";
    private String password = "";
    private Connection connection = null;
    private ResultSet rs = null;
//    private equipmentRow CurrentRow = new equipmentRow();

    public void createConnection(String login, String pwd) throws Exception {
        this.login = login;
        this.password = pwd;
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            throw new Exception("Can't get class. No driver found");
//            System.out.println("Can't get class. No driver found");
//            e.printStackTrace();
//            return;
        }
        try {
            connection = DriverManager.getConnection(connectionString, login, password);
        } catch (SQLException ex) {
            throw new Exception("Can't get connection. Incorrect URL" + "/" + login + "/" + password);
//            System.out.println("Can't get connection. Incorrect URL");
//            e.printStackTrace();
//            return;
        }
    }

    public void closeConnection() throws Exception {
        try {
            rs.getStatement().close();
            connection.close();
        } catch (SQLException ex) {
            throw new Exception("Can't close connection");
//            System.out.println("Can't close connection");
//            e.printStackTrace();
//            return;
        }
    }

    public ArrayList<EquipmentRow> getFullData() throws Exception {

        ArrayList<EquipmentRow> fullData = new ArrayList<EquipmentRow>();

        try {
            Statement stmt = connection.createStatement();
            String myQuery = "SELECT * FROM test.equipment";
            rs = stmt.executeQuery(myQuery);
            while (rs.next()) {
                EquipmentRow CurrentRow = new EquipmentRow();
                CurrentRow.id = rs.getInt("id");
                CurrentRow.name = rs.getString("name");
                CurrentRow.unit = rs.getString("unit");
                CurrentRow.quantity = rs.getFloat("quantity");
                fullData.add(CurrentRow);
            }
            return fullData;
        } catch (SQLException ex) {
            throw new Exception("Can't RUN QUERY");
        }
    }

//    public void InsertData(String query) throws Exception {
//        Statement stmt = connection.createStatement();
//        try {
//            int result = stmt.executeUpdate(query);
//        } catch (SQLException ex) {
//
//        }
//    }
    public void SaveChanges(ArrayList<EquipmentRow> Old, ArrayList<EquipmentRow> Curr) throws Exception {
        ArrayList<EquipmentRow> UpdateVal = new ArrayList<EquipmentRow>();
        ArrayList<EquipmentRow> InsertVal = new ArrayList<EquipmentRow>();
        ArrayList<EquipmentRow> DeleteVal = new ArrayList<EquipmentRow>();

        int i = 0;
        int j = 0;
        for (i = 0; i < Curr.size(); i++) {
            if (Curr.get(i).id > 0) {
                for (j = 0; j < Old.size(); j++) {
                    if (Curr.get(i).id == Old.get(j).id) {
                        break;
                    }
                }
                if ((Curr.get(i).name.equals(Old.get(j).name))
                        && (Curr.get(i).unit.equals(Old.get(j).unit))
                        && (Curr.get(i).quantity == Old.get(j).quantity)) {
                } else {
                    UpdateVal.add(Curr.get(i));
                }
            } else {
                InsertVal.add(Curr.get(i));
            }
        }

//У нас заполнены (или не заполнены) три коллекции - для Update, для Insert и для Delete команд SQL. 
//Откроем новое Statement и запустим соответствующие команды
        if ((UpdateVal.size() > 0) || (InsertVal.size() > 0) || (DeleteVal.size() > 0)) {
            try {
                Statement stms = connection.createStatement();
//                if (UpdateVal.size() > 0) { 
                for (i = 0; i < UpdateVal.size(); i++) { //UPDATE Table
//UPDATE test.equipment Set name = "AKСУ-74", unit = "АВТ", quantity = 7.8 WHERE id = 7
                    String UpdQuery = "UPDATE test.equipment SET name = \"" + UpdateVal.get(i).name + "\", "
                            + "unit = \"" + UpdateVal.get(i).unit + "\","
                            + "quantity = \"" + Float.toString(UpdateVal.get(i).quantity) + "\""
                            + "WHERE id = " + Integer.toString(UpdateVal.get(i).id);
                    stms.executeUpdate(UpdQuery);
                }
//                }

//                if (InsertVal.size() > 0) { 
                for (i = 0; i < InsertVal.size(); i++) { //INSERT new values
//INSERT into test.equipment VALUES(0,"Пластид", "КГ", 8.4)                        
                    String InsQuery = "INSERT INTO test.equipment VALUES(0, \"" + InsertVal.get(i).name + "\", \""
                            + InsertVal.get(i).unit + "\", "
                            + Float.toString(InsertVal.get(i).quantity) + ")";
                    boolean result = stms.execute(InsQuery);
                }
//                }
//                if (DeleteVal.size() > 0) {
                for ( i = 0; i < DeleteVal.size(); i++) { //Delete values
                    String DelQuery = "DELETE FROM test.equipment WHERE id = " + Integer.toString(DeleteVal.get(i).id);
                    boolean result = stms.execute(DelQuery);
                }
//                }

            } catch (SQLException ex) {
                throw new Exception(ex.getMessage());
            }
        } else {
            throw new Exception("Нечего менять");
        }

    }

}
