package transaction;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class PinNumber
{
	String acc;
	Scanner scan=new Scanner(System.in);
	Connection con;
	PreparedStatement pstmt;
	ResultSet res;
	
   void generate() throws Exception
    {
	   int count=0;
	   //taking Connection to this place
    	con=Launch.connect();
    	
    	String check1=".com";
    	String check="";
   
    	System.out.println("Enter your Email");
    	String email=scan.nextLine();
    	if(email.length()<5)
    	{
    		System.out.println("Not Valid");
    		System.exit(0);
    	}
    	//validation for .com in entered mail-id
    	for(int i=email.length()-4;i<email.length();i++)
    	{
    		check=check+email.charAt(i);   
    	}
    	
    	if(!check.equals(check1))
    	{
    		System.out.println("Format incorrect");
    		System.exit(0);
    	}
    	//validating entered email present in Bank DB
    	pstmt=con.prepareStatement("SELECT * FROM CUSTOMER_DETAILS WHERE EMAIL=?");
    	pstmt.setString(1,email);
    	res=pstmt.executeQuery();  	
    	
    	boolean check3=res.next();
		if(check3==false)
		{
    		System.out.println("entered Email is not exit\nFor more detail contact your nearest Bank");
    		System.exit(0);
    	}
		
	   	System.out.println("Enter last 4 digit of ACC_No ");
    	String a1=scan.nextLine();
    	
    	 res.first();
       	 acc=res.getString("acc_no");
       	 String empt="";
       	 for(int i=acc.length()-4;i<acc.length();i++)
       	 {
       		empt=empt+acc.charAt(i);//last 4 digits from your DB acc_no
       	 }
    		
    	while(empt.equals(a1))
    	{
    		String c1=generateCaptchaString();
    		System.out.println(c1);
    		System.out.println("Enter the Capatch present above");
    		String res=scan.nextLine();
    		//validating whether he/she is robot or not
    		if(c1.equals(res))
    		{
                                                             
    			int pin=ThreadLocalRandom.current().nextInt(0000, 10000);
    	//generating pin number
    			System.out.println("Pin number generated for your account number ending with"
    					+ a1+" is "+pin);
    			pstmt=con.prepareStatement("INSERT INTO MAPPING(ACC_NO,PIN) VALUES(?,?)");
    			pstmt.setInt(1,Integer.parseInt(acc));
    			pstmt.setInt(2,pin);
    			pstmt.executeUpdate();
    			pstmt=con.prepareStatement("INSERT INTO BALANCE(PIN,BAL_AMT) VALUES(?,?)");
    			pstmt.setInt(1,pin);
    			pstmt.setFloat(2,500.00f);
    			pstmt.executeUpdate();
    			//Connecting to the blue print for withdraw of money
    			WithDraw w=new WithDraw();
    			w.login();
    		}
    		else
    		{
    			System.out.println("Entered Capatch is invalid");
    			count++;	
    		}
    		if(count>=2)
    		{
    			System.out.println("You have taken more attempt");
    			System.exit(0);
    		}
    	}
    	if(!empt.equals(a1))
    	{
    		System.out.println("Incorrect Account Number Sorry!!!");
    		System.exit(0);
    	}
    }
    public String generateCaptchaString() {
    	Random random=new Random();
    	int length = 7 + (Math.abs(random.nextInt()) % 3);

    	StringBuffer captchaStringBuffer = new StringBuffer();
    	for (int i = 0; i < length; i++) {
    		int baseCharNumber = Math.abs(random.nextInt()) % 62;
    		int charNumber = 0;
    		if (baseCharNumber < 26) {
    			charNumber = 65 + baseCharNumber;
    		}
    		else if (baseCharNumber < 52){
    			charNumber = 97 + (baseCharNumber - 26);
    		}
    		else {
    			charNumber = 48 + (baseCharNumber - 52);
    		}
    		captchaStringBuffer.append((char)charNumber);
    	}

    	return captchaStringBuffer.toString();
    }
}
