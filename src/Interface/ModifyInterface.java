/*
Create Time: 2024/11/22
Author: 高涵宸 (CrazyApple)
studentID:22009200189
Description: 修改数据库内容的界面，包括修改预约信息、用户信息，添加、修改和删除航班、酒店、公交信息等功能
*/

package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class ModifyInterface {
    static Connection con = Conn.getConnection();
    static Statement st;
    static String sql;

    //更新预约
    static void updateReservation(String custName) {
        //TODO
    }

    //更新用户信息
    static void updateCustomer(String custName) {
        //TODO
    }

    //添加航班
    static void addFlight() {
        //TODO
    }

    //修改航班
    static void updateFlight() {
        //TODO
    }

    //删除航班
    static void deleteFlight() {
        //TODO
    }

    //添加酒店
    static void addHotel() {
        //TODO
    }

    //修改酒店
    static void updateHotel() {
        //TODO
    }

    //删除酒店
    static void deleteHotel() {
        //TODO
    }

    //添加公交
    static void addBus() {
        //TODO
    }

    //修改公交
    static void updateBus() {
        //TODO
    }

    //删除公交
    static void deleteBus() {
        //TODO
    }

    static void modifyInterface() {
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
            System.out.println("12.退出返回信息");
            System.out.println("请选择您的操作：");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("请输入修改对象的用户名：");
                    custName = sc.nextLine();
                    updateReservation(custName);
                    break;
                case 2:
                    System.out.println("请输入修改对象的用户名：");
                    custName = sc.nextLine();
                    updateCustomer(custName);
                    break;
                case 3:
                    addFlight();
                    break;
                case 4:
                    updateFlight();
                    break;
                case 5:
                    deleteFlight();
                    break;
                case 6:
                    addHotel();
                    break;
                case 7:
                    updateHotel();
                    break;
                case 8:
                    deleteHotel();
                    break;
                case 9:
                    addBus();
                    break;
                case 10:
                    updateBus();
                    break;
                case 11:
                    deleteBus();
                    break;
                case 12:
                    run = false;
                    break;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
}
