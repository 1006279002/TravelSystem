/*
Create Time: 2024/11/3
Author: 高涵宸 (CrazyApple)
studentID:22009200189
Description: 程序的第一个人机交互界面，用于选择登录或者注册
*/

package Interface;

import java.util.Scanner;

public class MainInterface {
    public static void mainInterface() {
        boolean run = true;

        Scanner sc = new Scanner(System.in);

        System.out.println("-------------------------------");
        System.out.println("欢迎使用本旅行系统！");
        System.out.println("此系统版本为1.0.0");
        while(run){
            System.out.println("-------------------------------");
            System.out.println("请选择登录或者注册");
            System.out.println("1.登录");
            System.out.println("2.注册");
            System.out.println("3.退出");
            System.out.print("请输入您的选择：");
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    LoginInterface.loginInterface();
                    break;
                case 2:
                    RegisterInterface.registerInterface();
                    break;
                case 3:
                    run = false;
                    System.out.println("感谢使用本旅行系统，再见！");
                    break;
                default:
                    System.out.println("输入错误，请重新输入！");
                    break;
            }
        }
    }
}
