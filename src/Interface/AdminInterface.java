/*
Create Time: 2024/11/20
Author: 高涵宸 (CrazyApple)
studentID:22009200189
Description: 管理员的操作界面，包括查看所有用户的预定信息，查看所有用户信息，查看可预定信息，修改所有后台数据，查询路线等功能
*/

package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class AdminInterface {
    static Connection con= Conn.getConnection();
    static Statement st;
    private static String sql;

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

    //查询路线,和UserInterface中checkPath()逻辑相同，都需要获取Vector<String>的路径信息
    static void searchPath(String custName){
        if(UserInterface.checkPath(custName,true)){
            System.out.println("路径完整，下面是此用户的行程");
            System.out.println();
        }else{
            System.out.println("路径不完整,查询失败");
            System.out.println();
            return;
        }

        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql="select * from reservations where custName='"+custName+"'";
        try {
            ResultSet rs=st.executeQuery(sql);
            Vector<String> path=new Vector<>();
            while (rs.next()) {
                String[] it = rs.getString("resvKey").split("-");
                if (it[1].equals("1")) {
                    path.add(it[0]);
                }
            }
            rs.close();
            if(path.isEmpty()){
                System.out.println("您还没有预定任何行程");
                return;
            }
            Map<String, String> flightMap = new HashMap<>();
            for (String flightNum : path) {
                sql = "select * from flights where flightNum='" + flightNum + "'";
                ResultSet rs_flight = st.executeQuery(sql);
                if (rs_flight.next()) {
                    String fromCity = rs_flight.getString("FromCity");
                    String arivCity = rs_flight.getString("ArivCity");
                    flightMap.put(fromCity, arivCity);
                }
            }

            String startCity = null;
            for (String city : flightMap.keySet()) {
                if (!flightMap.containsValue(city)) {
                    startCity = city;
                    break;
                }
            }

            System.out.println("起点为："+startCity);
            System.out.println();

            String currentCity = startCity;
            Set<String> visitedCities = new HashSet<>();
            while (currentCity != null && !visitedCities.contains(currentCity)) {
                visitedCities.add(currentCity);
                currentCity = flightMap.get(currentCity);
                if(currentCity!=null) {
                    System.out.println("下一站为：" + currentCity);
                    sql = "select * from reservations where custName='" + custName + "' and (resvType=2 or resvType=3) and resvKey like '" + currentCity + "-%'";
                    ResultSet rs_resv = st.executeQuery(sql);
                    while (rs_resv.next()) {
                        String resvType = rs_resv.getString("resvType");
                        String resvKey = rs_resv.getString("resvKey");
                        if (resvType.equals("2")) {
                            System.out.println("酒店预定：" + resvKey);
                        } else if (resvType.equals("3")) {
                            System.out.println("巴士预定：" + resvKey);
                        }
                    }
                    System.out.println();
                    rs_resv.close();
                }
            }

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
            System.out.println("5.查询路线");
            System.out.println("6.退出返回");
            System.out.println("请选择您的操作：");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    showReservations();
                    break;
                case "2":
                    showCustomers();
                    break;
                case "3":
                    AvailReservationInterface.availReservationInterface();
                    break;
                case "4":
                    ModifyInterface.modifyInterface();
                    break;
                case "5":
                    System.out.println("请输入查询的用户名：");
                    String custName = sc.nextLine();
                    searchPath(custName);
                    break;
                case "6":
                    run = false;
                    break;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
}
