
import java.sql.*;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Willy
 */
public class EquipmentRow {

    public Integer id = null;
    public String name = null;
    public String unit = null;
    public float quantity = 0;

    public void setValues(Integer id, String name, String unit, float quantity) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

//    public ArrayList<equipmentRow> setValuesFromDB(ResultSet rs) {
//        ArrayList<equipmentRow> fullData = new ArrayList<equipmentRow>();
//        try {
//            while (rs.next()) {
//                this.id = rs.getInt("id");
//                this.name = rs.getString("name");
//                this.unit = rs.getString("unit");
//                this.quantity = rs.getFloat("quantity");
//                fullData.add(this);
//            }
//            return fullData;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }

}
