package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AdminInterface {
    static Connection con= Conn.getConnection();
    static Statement st;
    static String sql;

    static void showReservations(){
        try {
            st= con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql="select * from reservations";
        try {
            ResultSet rs =st.executeQuery(sql);
            System.out.println("所有用户的预定信息如下(空白即为没有):");
            while(rs.next()){
                System.out.println("用户昵称:"+rs.getString("custName")+
                        " 预约类型:"+rs.getString("resvType")+
                        " 预约标记:"+rs.getString("resvKey"));
            }
            System.out.println();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void showCustomers(){
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql="select * from customers";
        try {
            ResultSet rs=st.executeQuery(sql);
            System.out.println("以下为全部用户(空白即为没有):");
            while(rs.next()){
                System.out.println("用户昵称:"+rs.getString("custName")+
                        " 用户ID:"+rs.getString("custID"));
            }
            System.out.println();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static void adminInterface() {
        Scanner sc = new Scanner(System.in);
        boolean run = true;

        System.out.println("-------------------------------");
        System.out.println("管理员，欢迎使用本程序！");
        System.out.println("您可以查看所有用户的预定信息，也可以修改预定行程的信息，检查路径的完整性");
        while(run){
            System.out.println("-------------------------------");
            System.out.println("现有以下几种功能");
            System.out.println("1.查看所有用户的预定信息");
            System.out.println("2.查看所有的用户信息");
            System.out.println("3.查看可预定信息");
            System.out.println("4.修改所有后台数据");
            System.out.println("5.检查路径完整性");
            System.out.println("6.退出返回");
            System.out.println("请选择您的操作：");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    showReservations();
                    break;
                case 2:
                    showCustomers();
                    break;
                case 3:
                    AvailReservationInterface.availReservationInterface();
                    break;
                case 4:
                    System.out.println("修改所有后台数据");
                    break;
                case 5:
                    System.out.println("检查路径完整性");
                    break;
                case 6:
                    run = false;
                    break;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
}
