package transaction;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class Launch {

	public static void main(String[] args) throws Exception 
	{
		System.out.println("Press->1 to create pin for your acc_no\npress->2 withdraw money");
		Scanner scan=new Scanner(System.in);
		int i=scan.nextInt();
		if(i==1)
		{
       PinNumber p=new PinNumber();
       p.generate();
		}
		else if(i==2)
		{
			WithDraw w=new WithDraw();
			w.login();
		}
		else
		{
			System.out.println("Sorry!!! something Went Wrong");
			System.exit(0);
		}
		
	}
	public static Connection connect()throws Exception
	{
		Connection con1;
		String path1="E:\\newWorkspace\\JDBC\\src\\transaction\\connect.properties";
		FileInputStream fis1=new FileInputStream(path1);
		
		Properties p1=new Properties();
		p1.load(fis1);
		
		String url=p1.getProperty("url");
		String un=p1.getProperty("un");
		String password=p1.getProperty("password");
		con1=DriverManager.getConnection(url, un, password);
		System.out.println("Connection Established to Student DB");
		return con1;
	}

}
