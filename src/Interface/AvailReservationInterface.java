package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AvailReservationInterface {
    static Connection con = Conn.getConnection();
    static Statement st;
    private static String sql;

    static void showFlihgts(){
        try {
            st= con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql="select * from flights where flights.numAvail>0";
        try {
            ResultSet rs=st.executeQuery(sql);
            System.out.println("以下为可预约项目(空白即为没有可预约项目)：");
            while (rs.next())
            {
                System.out.println("航班号:"+rs.getString("flightNum")+
                        " 票价:"+rs.getString("price")+
                        " 总座位数:"+rs.getString("numSeats")+
                        " 可预定座位数:"+rs.getString("numAvail")+
                        " 出发地:"+rs.getString("FromCity")+
                        " 目的地:"+rs.getString("ArivCity"));
            }
            System.out.println();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void showBuses(){
        try {
            st= con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql="select * from bus where bus.numAvail>0";
        try {
            ResultSet rs= st.executeQuery(sql);
            System.out.println("以下为可预约项目(空白即为没有可预约项目)：");
            while(rs.next())
            {
                System.out.println("公交地点:"+rs.getString("location")+
                        " 价格:"+rs.getString("price")+
                        " 总公交数:"+rs.getString("numBus")+
                        " 可预定公交数:"+rs.getString("numAvail"));
            }
            System.out.println();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void showHotels(){
        try {
            st= con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql="select * from hotels where hotels.numAvail>0";
        try {
            ResultSet rs= st.executeQuery(sql);
            System.out.println("以下为可预约项目(空白即为没有可预约项目)：");
            while(rs.next())
            {
                System.out.println("地名:"+rs.getString("location")+
                        " 价格:"+rs.getString("price")+
                        " 总房间数:"+rs.getString("numRooms")+
                        " 可预定房间数:"+rs.getString("numAvail"));
            }
            System.out.println();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void availReservationInterface() {
        Scanner sc = new Scanner(System.in);
        boolean run = true;

        System.out.println("-------------------------------");
        System.out.println("欢迎来到可预约服务查询页面！");
        while(run) {
            System.out.println("-------------------------------");
            System.out.println("现有以下几种查询服务");
            System.out.println("1.查询飞机航班");
            System.out.println("2.查询公交车班次");
            System.out.println("3.查询旅店房间");
            System.out.println("4.退出返回");
            System.out.println("请选择您的操作：");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    showFlihgts();
                    break;
                case 2:
                    showBuses();
                    break;
                case 3:
                    showHotels();
                    break;
                case 4:
                    run = false;
                    break;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
}
