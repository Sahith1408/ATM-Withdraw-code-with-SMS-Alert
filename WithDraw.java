package transaction;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class WithDraw 
{
	String acc;
	Scanner scan=new Scanner(System.in);
	Connection con;
	PreparedStatement pstmt;
	ResultSet res;
	
	void login() throws Exception
	{
		int id =0 ;
		float balance_amt=0.0f;
		con=Launch.connect();
		System.out.println("please enter your pin number");
		int pin=scan.nextInt();
		
		pstmt=con.prepareStatement("SELECT * FROM BALANCE WHERE PIN=?");
		pstmt.setInt(1,pin);
		res=pstmt.executeQuery();
		
		boolean check=res.next();
		if(check==false)
		{
			System.out.println("Entered PIN number not exists");
			System.exit(0);
		}
		    res.first();
		    
		    balance_amt=res.getFloat("bal_amt");//balance amount in customer account
		    
			System.out.println("Enter the Amount");//end user entered amount
			float user_amt=scan.nextFloat();
			
			if(user_amt>balance_amt)
			{
				System.out.println("InSufficient Balance Sorry!!!");
				System.exit(0);
			}
			//transaction begin
			else
			{
				//without action take places no auto changes to be happen's in DB
			con.setAutoCommit(false);
			float rbalance=balance_amt-user_amt;
			//updating remaining balance to your bank DB
			pstmt=con.prepareStatement("update balance set bal_amt=? where pin=?");
			pstmt.setFloat(1,rbalance);
			pstmt.setInt(2,pin);
			pstmt.executeUpdate();
			
			pstmt=con.prepareStatement("SELECT * FROM MAPPING WHERE PIN=?");
			pstmt.setInt(1,pin);
			res=pstmt.executeQuery();
			res.first();
			int acc_no=res.getInt("acc_no");
			pstmt=con.prepareStatement("INSERT INTO DEBIT_HIS(ACC_NO,AMT) VALUE(?,?)");
			pstmt.setInt(1,acc_no);
			pstmt.setFloat(2,user_amt);
			pstmt.executeUpdate();
			
			boolean ac=action();
			if(ac==true)
			{
				con.commit();
				
				SendSMS.sendSMS(user_amt, acc_no);
				System.out.println("would you like to check balance!!!\nPress Enter");
				scan.nextLine();
				boolean d=scan.hasNextLine();
				 if(d==true)
				   {
					 pstmt=con.prepareStatement("select * from balance where pin=?");
					 pstmt.setInt(1,pin);
					 res=pstmt.executeQuery();
					 while(res.first())
					 {
						 System.out.println("Remaing Balance is "+res.getFloat("bal_amt"));
						 System.out.println("Your Transaction is Successfull");
						 System.exit(0);
					 }
				   }
				   else
				   {
					   System.out.println("Your Transaction is Successfull");
					   System.exit(0);
				   }
			}
			else
			{
				con.rollback();
				System.out.println("Something Went Wrong Unable to process Transaction");
				System.exit(0);
			}
	    }
	}
	 boolean action()
	{
		System.out.println("Press 1 to collect");
		char i=scan.next().charAt(0);
		if(i=='1')
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
