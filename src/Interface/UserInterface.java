/*
Create Time: 2024/11/20
Author: 高涵宸 (CrazyApple)
studentID:22009200189
Description: 用户的操作界面，包括查询现有行程，申请预定，申请撤销，设计路线等功能
*/

package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class UserInterface {
    static Connection con = Conn.getConnection();
    static Statement st;
    private static String sql;

    //更新预约
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
            ResultSet rs=st.executeQuery(sql);
            if(!rs.next()) {
                System.out.println("可预定信息不存在，请重新输入");
                return;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接产生错误");
            return;
        }

        String resvKey = label+"-"+resvType+"-"+custName;

        try {
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

            sql="insert into reservations(custName,resvType,resvKey) values('"+custName+"',"+resvType+",'"+resvKey+"')";
            st.executeUpdate(sql);

            System.out.println("预约成功！");
        } catch (SQLException e) {
            System.out.println("预约失败，请检查预约目标的可预约性！");
        }
    }

    //撤销预约
    static void undoReservation(String custName){
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("请输入要撤销的预约类型(1为航班预约;2为酒店预约;3为公交预约):");
        String resvType = sc.nextLine();
        if(!resvType.equals("1") && !resvType.equals("2") && !resvType.equals("3")){
            System.out.println("输入错误，请重新输入");
            return;
        }

        System.out.println("请输入预约标记:");
        String label = sc.nextLine();

        String resvKey = label+"-"+resvType+"-"+custName;

        try {
            String[] it = resvKey.split("-");

            sql="delete from reservations where custName='"+custName+"' and resvType="+resvType+" and resvKey='"+resvKey+"'";
            st.executeUpdate(sql);

            switch(resvType){
                case "1":
                    sql="update flights set flights.numAvail=flights.numAvail+1 where flightNum='"+it[0]+"'";
                    break;
                case "2":
                    sql="update hotels set hotels.numAvail=hotels.numAvail+1 where location='"+it[0]+"'";
                    break;
                case "3":
                    sql="update bus set bus.numAvail=bus.numAvail+1 where location='"+it[0]+"'";
                    break;
            }
            st.executeUpdate(sql);

            System.out.println("撤销成功！");
        } catch (SQLException e) {
            System.out.println("撤销失败，请检查撤销目标是否存在！");
        }
    }

    //路线检查完整性，检查航班是否相接,不可以是回环
    //由于简化了巴士和酒店的关系，所以检查酒店和巴士的地名是否超出了航班的地名
    public static boolean checkPath(String custName,boolean check) {
        ResultSet rs, rs_flight;

        try {
            st = con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sql = "select * from reservations where custName='" + custName + "'";
        try {
            rs = st.executeQuery(sql);
            Vector<String> path = new Vector<>();
            while (rs.next()) {
                String[] it = rs.getString("resvKey").split("-");
                if (it[1].equals("1")) {
                    path.add(it[0]);
                }
            }
            rs.close();
            if (path.isEmpty()) {
                if(!check)
                    System.out.println("您还没有预定航班，请先预定航班！");
                return false;
            }

            // 建立一个映射表来存放航班的起点和终点
            Map<String, String> flightMap = new HashMap<>();
            for (String flightNum : path) {
                sql = "select * from flights where flightNum='" + flightNum + "'";
                rs_flight = st.executeQuery(sql);
                if (rs_flight.next()) {
                    String fromCity = rs_flight.getString("FromCity");
                    String arivCity = rs_flight.getString("ArivCity");
                    flightMap.put(fromCity, arivCity);
                }
            }

            // 检查路径是否完整
            String startCity = null;
            for (String city : flightMap.keySet()) {
                if (!flightMap.containsValue(city)) {
                    startCity = city;
                    break;
                }
            }

            if (startCity == null) {
                if(!check)
                    System.out.println("您的路径不完整，请重新预定！");
                return false;
            }

            String currentCity = startCity;
            Set<String> visitedCities = new HashSet<>();
            while (currentCity != null && !visitedCities.contains(currentCity)) {
                visitedCities.add(currentCity);
                currentCity = flightMap.get(currentCity);
            }

            if ((visitedCities.size() - 1) != flightMap.size() || currentCity != null) {
                if(!check)
                    System.out.println("您的路径不完整，请重新预定！");
                return false;
            } else {
                sql = "select * from reservations where custName='" + custName + "'";
                rs=st.executeQuery(sql);
                boolean OutOfBounds = false;
                while(rs.next()){
                    String[] it = rs.getString("resvKey").split("-");
                    if(it[1].equals("2") || it[1].equals("3")){
                        if(!flightMap.containsKey(it[0]) && !flightMap.containsValue(it[0])){
                            OutOfBounds = true;
                            break;
                        }
                    }
                }

                if (OutOfBounds) {
                    if(!check)
                        System.out.println("您的酒店或巴士地名超出了航班的地名，请重新预定！");
                    return false;
                } else {
                    if(!check)
                        System.out.println("您的路径完整！");
                    return true;
                }
            }
        } catch (SQLException e) {
            if(!check)
                System.out.println("检查路径失败，请检查您的预定信息！");
            return false;
        }
    }


    //用户界面
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
            System.out.println("4.路线检查");
            System.out.println("5.退出返回");
            System.out.println("请选择您的操作：");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    AvailReservationInterface.availReservationInterface();
                    break;
                case "2":
                    updateReservation(account);
                    break;
                case "3":
                    undoReservation(account);
                    break;
                case "4":
                    checkPath(account,false);
                    break;
                case "5":
                    run = false;
                    break;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
}
