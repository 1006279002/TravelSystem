/*
Create Time: 2024/10/23
Author: 高涵宸 (CrazyApple)
studentID:22009200189
Description: 数据库连接类，用于连接数据库
*/
package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn{
    public Connection con;
    public static String user;
    public static String password;
    public Connection getConnection() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        user="root";
        password="1234";
        try{
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/travelsystem",user,password);
            System.out.println("Connection established");
        }catch(SQLException e){
            e.printStackTrace();
        }
        return con;
    }
}