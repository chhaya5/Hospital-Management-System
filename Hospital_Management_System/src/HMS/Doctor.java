package HMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection conn;

    public Doctor(Connection conn){
        this.conn = conn;
    }

    public void viewDoctors(){
        String query = "select * from doctors";
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors : ");
            System.out.println("+------------+---------------------+-------------------+");
            System.out.println("| Doctor id  | Name                | Specialization    |");
            System.out.println("+------------+---------------------+-------------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getNString("name");
                String specialization = resultSet.getNString("specializaton");
                System.out.printf("| %-10s | %-18s | %-16s |\n", id, name, specialization);
                System.out.println("+------------+---------------------+-------------------+");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorByID(int id){
        String query = "SELECT * FROM doctors WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return true;
            else return false;

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
