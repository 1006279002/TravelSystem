/*
Create Time: 2024/11/3
Author: 高涵宸 (CrazyApple)
studentID:22009200189
Description: 程序的注册界面，用于注册账户，提交更新SQL
*/

package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class RegisterInterface {
    @SuppressWarnings("SqlSourceToSinkFlow")
    public static void registerInterface() {
        Connection con = Conn.getConnection();
        Scanner sc = new Scanner(System.in);
        Statement st;
        boolean run = true;

        System.out.println("-------------------------------");
        System.out.println("欢迎来到注册界面");
        while(run) {
            System.out.println("-------------------------------");
            System.out.println("请输入账户用户名:");
            String account = sc.nextLine();
            System.out.println("请输入账户密码:");
            String password = sc.nextLine();

            try {
                st = con.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String sql = "insert into customers values('" + account + "','" + password + "')";
            try {
                st.executeUpdate(sql);
                System.out.println("注册成功！");
                run=false;
            } catch (SQLException e) {
                System.out.println("注册失败！");
            }
        }
    }
}
