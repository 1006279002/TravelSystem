/*
Create Time: 2024/11/3
Author: 高涵宸 (CrazyApple)
studentID:22009200189
Description: 程序的登录界面，用于进入系统，区分用户权限
*/

package Interface;

import SQL.Conn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LoginInterface {
    public static void loginInterface() {
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        Connection con = Conn.getConnection();
        Statement st;

        System.out.println("-------------------------------");
        System.out.println("欢迎来到登录界面");
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

            if (account.equals("admin") && password.equals("admin")) {
                System.out.println("管理员登录成功！");
                AdminInterface.adminInterface();
                run = false;
            } else {
                String sql = "select * from customers where custName = '" + account + "' and custID = '" + password + "'";
                try {
                    ResultSet rs = st.executeQuery(sql);
                    if (rs.next()) {
                        System.out.println("登录成功！");
                        UserInterface.userInterface(account);
                        run = false;
                    } else {
                        System.out.println("账号不存在！");
                    }
                } catch (SQLException e) {
                    System.out.println("数据库连接异常！");
                }
            }
        }
    }
}
