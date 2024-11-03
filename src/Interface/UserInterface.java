package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

public class UserInterface {
    static Connection con = Conn.getConnection();
    static Statement st;
    static Scanner sc = new Scanner(System.in);

    public static void userInterface(String account) {
        boolean run=true;

        System.out.println("-------------------------------");
        System.out.println(account+"，欢迎使用本程序！");
        System.out.println("您可以申请添加或删除属于您的预定信息，也可以查询现有的行程情况，设计属于自己的旅行路线");
        while(run) {
            System.out.println("-------------------------------");
            System.out.println("1.查询现可预订行程");
            System.out.println("2.申请预定");
            System.out.println("3.申请撤销");
            System.out.println("4.路线查询");
            System.out.println("5.退出返回");
            System.out.println("请选择您的操作：");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("查询行程");
                    break;
                case 2:
                    System.out.println("申请预定");
                    break;
                case 3:
                    System.out.println("申请撤销");
                    break;
                case 4:
                    System.out.println("设计路线");
                    break;
                case 5:
                    System.out.println("退出");
                    run = false;
                    break;
                default:
                    System.out.println("输入错误，请重新输入");
                    break;
            }
        }
    }
}
