package HMS;

import java.sql.*;
import java.util.Scanner;

public class Head_Class {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Chhaya@12";

    public static void main(String[] args) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in);
        try{
            Connection conn = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(conn, sc);
            Doctor doctor = new Doctor(conn);

            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter Your Choice : ");
                int choice = sc.nextInt();

                switch(choice){
                    case 1:
                        // add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //View Patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //View Doctor
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //Book Appointment
                        bookApp(patient, doctor, conn, sc);
                        System.out.println();
                        break;
                    case 5:
                        //Exit
                        return;

                    default:
                        System.out.println("Please Enter Valid Choice.");
                        break;
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookApp(Patient patient, Doctor doctor, Connection conn, Scanner sc){
        System.out.print("Enter Patient id : ");
        int patiId = sc.nextInt();
        System.out.print("Enter Doctor id : ");
        int docId = sc.nextInt();
        System.out.println();
        System.out.print("Enter Appointment date (YYYY-MM-DD)");
        String appDate = sc.next();

        if(patient.getPatientByID(patiId) && doctor.getDoctorByID(docId)){
            if(checkDoctorAvailability(docId, appDate, conn)) {
                String appQuery = "INSERT INTO appointments(pati_id, doc_id, app_date) VALUES(?, ? , ?)";

                try {
                    PreparedStatement preparedStatement = conn.prepareStatement(appQuery);
                    preparedStatement.setInt(1, patiId);
                    preparedStatement.setInt(2, docId);
                    preparedStatement.setString(3, appDate);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected > 0){
                        System.out.println("Appointment Booked");
                    }
                    else{
                        System.out.println("Failed");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Doctor is not available on this date.");
            }
        }
        else{
            System.out.println("Either Doctor or Patience does not exit.");
        }
    }

    public static boolean checkDoctorAvailability(int docId, String appDate, Connection conn){
        String query = "SELECT COUNT(*) FROM appointments WHERE doc_id = ? AND app_date = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, docId);
            preparedStatement.setString(2, appDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0) return true;
                else return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
