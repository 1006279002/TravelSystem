/*
Create Time: 2024/11/22
Author: 高涵宸 (CrazyApple)
studentID:22009200189
Description: 修改数据库内容的界面，包括修改预约信息、用户信息，添加、修改和删除航班、酒店、公交信息等功能
*/

package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Vector;

public class ModifyInterface {
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

        sql="select * from reservations where custName='"+custName+"'";
        try {
            ResultSet rs=st.executeQuery(sql);
            if(!rs.next()) {
                System.out.println("用户不存在，请重新输入");
                return;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接产生错误");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("请输入修改目标的resvKey:");
        String resvKey = sc.nextLine();

        System.out.println("请输入修改后的方案(1为航班预约;2为酒店预约;3为公交预约)：");
        String resvType = sc.nextLine();
        if (!resvType.equals("1") && !resvType.equals("2") && !resvType.equals("3")) {
            System.out.println("输入错误，请重新输入");
            return;
        }
        System.out.println("请输入修改的标记：");
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

        String resvKeyNew = label+"-"+resvType+"-"+custName;

        try{
            String[] it1=resvKey.split("-");
            String[] it2=resvKeyNew.split("-");
            switch (it2[1]){
                case "1":
                    sql="update flights set flights.numAvail=flights.numAvail-1 where flightNum='"+it2[0]+"'";
                    break;
                case "2":
                    sql="update hotels set hotels.numAvail=hotels.numAvail-1 where location='"+it2[0]+"'";
                    break;
                case "3":
                    sql="update bus set bus.numAvail=bus.numAvail-1 where location='"+it2[0]+"'";
                    break;
            }
            st.executeUpdate(sql);

            sql="update reservations set resvKey='"+resvKeyNew+"',resvType='"+resvType+"' where resvKey='"+resvKey+"'";
            st.executeUpdate(sql);

            switch (it1[1]){
                case "1":
                    sql="update flights set flights.numAvail=flights.numAvail+1 where flightNum='"+it1[0]+"'";
                    break;
                case "2":
                    sql="update hotels set hotels.numAvail=hotels.numAvail+1 where location='"+it1[0]+"'";
                    break;
                case "3":
                    sql="update bus set bus.numAvail=bus.numAvail+1 where location='"+it1[0]+"'";
                    break;
            }
            st.executeUpdate(sql);

            System.out.println("更新成功！");
        }catch(SQLException e){
            System.out.println("更新失败,请检查数据的可更新性，并检查数据库是否产生问题");
        }
    }

    //更新用户信息
    static void updateCustomer(String custName) {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入新的用户名：");
        String custNameNew = sc.nextLine();
        System.out.println("请输入新的用户ID：");
        String custID = sc.nextLine();
        sql="select * from customers where custName='"+custName+"'";

        try {
            ResultSet rs=st.executeQuery(sql);
            if(!rs.next()) {
                System.out.println("用户不存在，请重新输入");
                return;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接产生错误");
            return;
        }

        sql="select * from reservations where custName='"+custName+"'";
        Vector<String> resvKeys=new Vector<>();
        try{
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()) {
                resvKeys.add(rs.getString("resvKey"));
            }
        }catch (SQLException e){
            System.out.println("数据库连接产生错误");
            return;
        }
        sql="update customers set custName='"+custNameNew+"',custID='"+custID+"' where custName='"+custName+"'";
        try {
            st.executeUpdate(sql);

            for(String resvKey:resvKeys){
                String[] it=resvKey.split("-");
                sql="update reservations set custName='"+custNameNew+"',resvKey='"+it[0]+"-"+it[1]+"-"+custNameNew+"' where resvKey='"+resvKey+"'";
                st.executeUpdate(sql);
            }

            System.out.println("更新成功！");
        } catch (SQLException e) {
            System.out.println("更新失败，请检查数据的可更新性，并检查数据库是否产生问题");
        }
    }

    //添加航班
    static void addFlight() {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入航班号：");
        String flightNum = sc.nextLine();
        System.out.println("请输入票价：");
        String price = sc.nextLine();
        System.out.println("请输入座位数：");
        String numSeats = sc.nextLine();
        System.out.println("请输入可用座位数：");
        String numAvail = sc.nextLine();
        System.out.println("请输入出发地：");
        String fromCity = sc.nextLine();
        System.out.println("请输入目的地：");
        String toCity = sc.nextLine();
        sql="insert into flights values('"+flightNum+"','"+price+"','"+numSeats+"','"+numAvail+"','"+fromCity+"','"+toCity+"')";
        try {
            st.executeUpdate(sql);
            System.out.println("添加成功！");
        } catch (SQLException e) {
            System.out.println("添加失败，请检查数据的可添加性，并检查数据库是否产生问题");
        }
    }

    //修改航班
    static void updateFlight() {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入修改对象的航班号：");
        String flightNum = sc.nextLine();

        sql="select * from flights where flightNum='"+flightNum+"'";
        try {
            ResultSet rs=st.executeQuery(sql);
            if(!rs.next()) {
                System.out.println("航班不存在，请重新输入");
                return;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接产生错误");
            return;
        }

        System.out.println("请输入修改后的票价：");
        String price = sc.nextLine();
        System.out.println("请输入修改后的座位数：");
        String numSeats = sc.nextLine();
        System.out.println("请输入修改后的可用座位数：");
        String numAvail = sc.nextLine();
        System.out.println("请输入修改后的出发地：");
        String fromCity = sc.nextLine();
        System.out.println("请输入修改后的目的地：");
        String ArivCity = sc.nextLine();

        Vector<String> resvKeys=new Vector<>();
        sql="select * from reservations where resvType='1'";
        try {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                resvKeys.add(rs.getString("resvKey"));
            }
        }catch (SQLException e){
            System.out.println("数据库连接产生错误");
            return;
        }

        for(String resvKey:resvKeys){
            String[] it=resvKey.split("-");
            if(it[0].equals(flightNum)){
                System.out.println("该航班已被预约，无法修改");
                return;
            }
        }

        sql="update flights set price='"+price+"',numSeats='"+numSeats+"',numAvail='"+numAvail+"',fromCity='"+fromCity+"',ArivCity='"+ArivCity+"' where flightNum='"+flightNum+"'";
        try {
            st.executeUpdate(sql);
            System.out.println("修改成功！");
        } catch (SQLException e) {
            System.out.println("修改失败，请检查数据的可修改性，并检查数据库是否产生问题");
        }
    }

    //删除航班
    static void deleteFlight() {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入删除对象的航班号：");
        String flightNum = sc.nextLine();
        Vector<String> resvKeys=new Vector<>();
        sql="select * from reservations where resvType='1'";
        try {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                resvKeys.add(rs.getString("resvKey"));
            }
        }catch (SQLException e){
            System.out.println("数据库连接产生错误");
            return;
        }

        for(String resvKey:resvKeys){
            String[] it=resvKey.split("-");
            if(it[0].equals(flightNum)){
                System.out.println("该航班已被预约，无法删除");
                return;
            }
        }

        sql="delete from flights where flightNum='"+flightNum+"'";
        try {
            st.executeUpdate(sql);
            System.out.println("删除成功！");
        } catch (SQLException e) {
            System.out.println("删除失败，请检查数据的可删除性，并检查数据库是否产生问题");
        }
    }

    //添加酒店
    static void addHotel() {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入地点：");
        String location = sc.nextLine();
        System.out.println("请输入价格：");
        String price = sc.nextLine();
        System.out.println("请输入房间数：");
        String numRooms = sc.nextLine();
        System.out.println("请输入可用房间数：");
        String numAvail = sc.nextLine();
        sql="insert into hotels values('"+location+"','"+price+"','"+numRooms+"','"+numAvail+"')";
        try {
            st.executeUpdate(sql);
            System.out.println("添加成功！");
        } catch (SQLException e) {
            System.out.println("添加失败，请检查数据的可添加性，并检查数据库是否产生问题");
        }
    }

    //修改酒店
    static void updateHotel() {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入修改对象的地点：");
        String location = sc.nextLine();

        sql="select * from hotels where location='"+location+"'";
        try {
            ResultSet rs=st.executeQuery(sql);
            if(!rs.next()) {
                System.out.println("酒店不存在，请重新输入");
                return;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接产生错误");
            return;
        }

        System.out.println("请输入修改后的价格：");
        String price = sc.nextLine();
        System.out.println("请输入修改后的房间数：");
        String numRooms = sc.nextLine();
        System.out.println("请输入修改后的可用房间数：");
        String numAvail = sc.nextLine();

        Vector<String> resvKeys=new Vector<>();
        sql="select * from reservations where resvType='2'";
        try {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                resvKeys.add(rs.getString("resvKey"));
            }
        }catch (SQLException e){
            System.out.println("数据库连接产生错误");
            return;
        }

        for(String resvKey:resvKeys){
            String[] it=resvKey.split("-");
            if(it[0].equals(location)){
                System.out.println("该酒店已被预约，无法修改");
                return;
            }
        }

        sql="update hotels set price='"+price+"',numRooms='"+numRooms+"',numAvail='"+numAvail+"' where location='"+location+"'";
        try {
            st.executeUpdate(sql);
            System.out.println("修改成功！");
        } catch (SQLException e) {
            System.out.println("修改失败，请检查数据的可修改性，并检查数据库是否产生问题");
        }
    }

    //删除酒店
    static void deleteHotel() {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入删除对象的地点：");
        String location = sc.nextLine();
        Vector<String> resvKeys=new Vector<>();
        sql="select * from reservations where resvType='2'";
        try {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                resvKeys.add(rs.getString("resvKey"));
            }
        }catch (SQLException e){
            System.out.println("数据库连接产生错误");
            return;
        }

        for(String resvKey:resvKeys){
            String[] it=resvKey.split("-");
            if(it[0].equals(location)){
                System.out.println("该酒店已被预约，无法删除");
                return;
            }
        }

        sql="delete from hotels where location='"+location+"'";
        try {
            st.executeUpdate(sql);
            System.out.println("删除成功！");
        } catch (SQLException e) {
            System.out.println("删除失败，请检查数据的可删除性，并检查数据库是否产生问题");
        }
    }

    //添加公交
    static void addBus() {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入地点：");
        String location = sc.nextLine();
        System.out.println("请输入价格：");
        String price = sc.nextLine();
        System.out.println("请输入公交数：");
        String numBus = sc.nextLine();
        System.out.println("请输入可用座位数：");
        String numAvail = sc.nextLine();
        sql="insert into bus values('"+location+"','"+price+"','"+ numBus +"','"+numAvail+"')";
        try {
            st.executeUpdate(sql);
            System.out.println("添加成功！");
        } catch (SQLException e) {
            System.out.println("添加失败，请检查数据的可添加性，并检查数据库是否产生问题");
        }
    }

    //修改公交
    static void updateBus() {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入修改对象的地点：");
        String location = sc.nextLine();

        sql="select * from bus where location='"+location+"'";
        try {
            ResultSet rs=st.executeQuery(sql);
            if(!rs.next()) {
                System.out.println("公交不存在，请重新输入");
                return;
            }
        } catch (SQLException e) {
            System.out.println("数据库连接产生错误");
            return;
        }

        System.out.println("请输入修改后的价格：");
        String price = sc.nextLine();
        System.out.println("请输入修改后的公交数：");
        String numBus = sc.nextLine();
        System.out.println("请输入修改后的可用座位数：");
        String numAvail = sc.nextLine();

        Vector<String> resvKeys=new Vector<>();
        sql="select * from reservations where resvType='3'";
        try {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                resvKeys.add(rs.getString("resvKey"));
            }
        }catch (SQLException e){
            System.out.println("数据库连接产生错误");
            return;
        }

        for(String resvKey:resvKeys){
            String[] it=resvKey.split("-");
            if(it[0].equals(location)){
                System.out.println("该公交已被预约，无法修改");
                return;
            }
        }

        sql="update bus set price='"+price+"',numBus='"+numBus+"',numAvail='"+numAvail+"' where location='"+location+"'";
        try {
            st.executeUpdate(sql);
            System.out.println("修改成功！");
        } catch (SQLException e) {
            System.out.println("修改失败，请检查数据的可修改性，并检查数据库是否产生问题");
        }
    }

    //删除公交
    static void deleteBus() {
        try {
            st=con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入删除对象的地点：");
        String location = sc.nextLine();
        Vector<String> resvKeys=new Vector<>();
        sql="select * from reservations where resvType='3'";
        try {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                resvKeys.add(rs.getString("resvKey"));
            }
        }catch (SQLException e){
            System.out.println("数据库连接产生错误");
            return;
        }

        for(String resvKey:resvKeys){
            String[] it=resvKey.split("-");
            if(it[0].equals(location)){
                System.out.println("该公交已被预约，无法删除");
                return;
            }
        }

        sql="delete from bus where location='"+location+"'";
        try {
            st.executeUpdate(sql);
            System.out.println("删除成功！");
        } catch (SQLException e) {
            System.out.println("删除失败，请检查数据的可删除性，并检查数据库是否产生问题");
        }
    }

    public static void modifyInterface() {
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        String custName;

        System.out.println("-------------------------------");
        System.out.println("欢迎来到数据修改界面！");
        while (run) {
            System.out.println("-------------------------------");
            System.out.println("现有以下几种功能");
            System.out.println("1.更新预约");
            System.out.println("2.更新用户信息");
            System.out.println("3.添加航班信息");
            System.out.println("4.修改航班信息");
            System.out.println("5.删除航班信息");
            System.out.println("6.添加酒店信息");
            System.out.println("7.修改酒店信息");
            System.out.println("8.删除酒店信息");
            System.out.println("9.添加公交信息");
            System.out.println("10.修改公交信息");
            System.out.println("11.删除公交信息");
            System.out.println("12.退出返回");
            System.out.println("请选择您的操作：");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("请输入修改对象的用户名：");
                    custName = sc.nextLine();
                    updateReservation(custName);
                    break;
                case "2":
                    System.out.println("请输入修改对象的用户名：");
                    custName = sc.nextLine();
                    updateCustomer(custName);
                    break;
                case "3":
                    addFlight();
                    break;
                case "4":
                    updateFlight();
                    break;
                case "5":
                    deleteFlight();
                    break;
                case "6":
                    addHotel();
                    break;
                case "7":
                    updateHotel();
                    break;
                case "8":
                    deleteHotel();
                    break;
                case "9":
                    addBus();
                    break;
                case "10":
                    updateBus();
                    break;
                case "11":
                    deleteBus();
                    break;
                case "12":
                    run = false;
                    break;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
}
