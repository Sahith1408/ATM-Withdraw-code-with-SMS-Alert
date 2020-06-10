package transaction;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
public class SendSMS {
  
	public static void sendSMS(float amt ,int acc_no)
	{
		try
		{
			String apiKey="apiKey="+"wXJBXq35vJc-wEFMKUm3xdBTdATdA8SipIUlGXJU8C";
			
			String message="&message="+URLEncoder.encode("Rs."+amt+" has been debit from your a/c no XXXX"+acc_no,"UTF-8");
			
			String numbers="&numbers="+"receivers phone number";
			String apiURL="https://api.textlocal.in/send/?" + apiKey + message + numbers;
			URL url=new URL(apiURL);
			URLConnection con=url.openConnection();
			con.setDoOutput(true);
			
			BufferedReader reader=new BufferedReader(new 
					InputStreamReader(con.getInputStream()));
			String line= "";
			StringBuilder sb=new StringBuilder();
			while((line = reader.readLine())!=null)
			{
				sb.append(line).append("\n");
			}
			System.out.println(sb.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
