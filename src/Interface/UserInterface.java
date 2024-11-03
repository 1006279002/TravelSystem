package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class UserInterface {
    static Connection con = Conn.getConnection();
    static Statement st;
    static String sql;

    static void updateReservation(String custName) {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("请输入预约类型(1为航班预约;2为酒店预约;3为公交预约):");
        String resvType = sc.nextLine();
        if(!resvType.equals("1") && !resvType.equals("2") && !resvType.equals("3")){
            System.out.println("输入错误，请重新输入");
            return;
        }

        System.out.println("请输入预约标记:");
        String label = sc.nextLine();

        switch(resvType){
            case "1":
                sql="select * from flights where flightNum='"+label+"'";
                break;
            case "2":
                sql="select * from hotels where location='"+label+"'";
                break;
            case "3":
                sql="select * from bus where location='"+label+"'";
                break;
        }

        try {
            st.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("可预定信息不存在，请重新输入");
            return;
        }

        String resvKey = label+"-"+resvType+"-"+custName;

        sql="insert into reservations(custName,resvType,resvKey) values('"+custName+"',"+resvType+",'"+resvKey+"')";
        try {
            st.executeUpdate(sql);
            String[] it = resvKey.split("-");

            switch(resvType){
                case "1":
                    sql="update flights set flights.numAvail=flights.numAvail-1 where flightNum='"+it[0]+"'";
                    break;
                case "2":
                    sql="update hotels set hotels.numAvail=hotels.numAvail-1 where location='"+it[0]+"'";
                    break;
                case "3":
                    sql="update bus set bus.numAvail=bus.numAvail-1 where location='"+it[0]+"'";
                    break;
            }

            st.executeUpdate(sql);

            System.out.println("预约成功！");
        } catch (SQLException e) {
            System.out.println("预约失败，请检查预约目标的可预约性！");
            throw new RuntimeException(e);
        }
    }




    public static void userInterface(String account) {
        Scanner sc = new Scanner(System.in);
        boolean run=true;

        System.out.println("-------------------------------");
        System.out.println(account+"，欢迎使用本程序！");
        System.out.println("您可以申请添加或删除属于您的预定信息，也可以查询现有的行程情况，设计属于自己的旅行路线");
        while(run) {
            System.out.println("-------------------------------");
            System.out.println("现有以下几种功能");
            System.out.println("1.查询现可预订行程");
            System.out.println("2.申请预定");
            System.out.println("3.申请撤销");
            System.out.println("4.路线查询");
            System.out.println("5.退出返回");
            System.out.println("请选择您的操作：");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    AvailReservationInterface.availReservationInterface();
                    break;
                case 2:
                    updateReservation(account);
                    break;
                case 3:
                    System.out.println("申请撤销");
                    break;
                case 4:
                    System.out.println("设计路线");
                    break;
                case 5:
                    run = false;
                    break;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
}
