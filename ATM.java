import java.sql.*;
import java.util.*;

class ATM
{
	static Connection con;
	static ResultSet rs;
	static PreparedStatement pst;
	static Scanner sc=new Scanner(System.in);
	
	static void getBalance()
	{
		try{
		
		pst=con.prepareStatement("select * from login where acnum=? and password=?");

		System.out.println("Enter account number");
		String acnum=sc.nextLine();
		System.out.println("Enter Pin");
		String pin=sc.nextLine();

		pst.setString(1,acnum);
		pst.setString(2,pin);
				
		rs=pst.executeQuery();
			
		if(rs.next())
			System.out.println("Your Balance is: "+rs.getString(3));

		/*if(i>0)
			System.out.println("Transaction successful");
		else
			System.out.println("Transaction unsuccessful");	*/
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}	 
	}

	

	static void transact()
	{
		try{

		int n_1000=0,n_500=0,n_100=0,bal=0,new_bal=0;

		pst=con.prepareStatement("select * from login where acnum=? and password=?");

		System.out.println("\nEnter account number");
		String acnum=sc.nextLine();
		System.out.println("\nEnter Pin");
		String pin=sc.nextLine();
		System.out.println("\nEnter amount");
		int amt=sc.nextInt();

		pst.setString(1,acnum);
		pst.setString(2,pin);
		
		rs=pst.executeQuery();

		if(rs.next())
			bal=Integer.parseInt(rs.getString(3));
		
		//int n_1000=0,n_500=0,n_100=0;
		
		if(amt>bal)
		{
			System.out.println("Low Account Balance"); 
		}
		else if((amt%100)!=0)
		{
			System.out.println("Please enter in multiple of 100");
		}
		else 
		{
			if(amt%1000==0)
			{	
				n_1000=(amt/1000)-1;
				n_500=1;
				n_100=5;
			}
			
			else if((amt%500==0) && (amt>1000))
			{
				n_1000=(amt/1000)-1;
				n_500=2;
				n_100=5;
			}

			else
			{
				n_1000=(amt/1000)-1;
				n_500=2;
				n_100=amt%1000;
			}
		

		System.out.println("\nPlease collect your cash");
		System.out.println("Number of Rs.1000 notes is: "+n_1000);
		System.out.println("Number of Rs.500 notes is: "+n_500);
		System.out.println("Number of Rs.100 notes is: "+n_100);

		new_bal=(Integer.parseInt(rs.getString(3)))-amt;
		String newBal=String.valueOf(new_bal);

		updateBalance(acnum, newBal);
		}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	static void updateBalance(String acnum, String newBal)
	{
		try
		{
			pst=con.prepareStatement("update login set balance=? where acnum=?");
			
			pst.setString(1,newBal);
			pst.setString(2,acnum);
			pst.executeUpdate();

			//System.out.println("Pin change successful");
		}
		catch(Exception ee)
		{
		}
	}	


	static void updatePin(String acnum, String password)
	{
		try
		{
			pst=con.prepareStatement("update login set password=? where acnum=?");
			//String acnum=sc.nextLine();
			//String password=sc.nextLine();
			pst.setString(1,password);
			pst.setString(2,acnum);
			pst.executeUpdate();

			System.out.println("Pin change successful");
		}
		catch(Exception ee)
		{
		}
	}

	
	static void pinChange()
	{
		try{		

		String c_pin="";		

		pst=con.prepareStatement("select * from login where acnum=?");

		System.out.println("Enter account number");
		String acnum=sc.nextLine();
		System.out.println("Enter Current Pin");
		String pin=sc.nextLine();
		System.out.println("Enter New pin");
		String new_pin=sc.nextLine();

		pst.setString(1,acnum);
		
		
		rs=pst.executeQuery();

		if(rs.next())
			c_pin=rs.getString(2);

		if(c_pin.equals(pin))
			updatePin(acnum, new_pin);

		}
		catch(Exception e)
		{}
		

	} 
	
	public static void main(String []ar)
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ATM","root","MySql");
			if(con!=null)
			{	
				System.out.println("Connection Ok\n");
				
				int choice=0;
				do{
				System.out.println("\nSelect your choice");
				System.out.println("1.Check Account Balance");
				System.out.println("2.Cash Withdrawal");
				System.out.println("3.Change Pin");
				System.out.println("Enter any other choice to exit");

				choice=Integer.parseInt(sc.nextLine());	

				switch(choice)
				{
					case 1:	getBalance();
					break;
					case 2: transact();
					break;
					case 3: pinChange();
					break;
					default: choice=0;
			
				}
				}while(choice!=0);
			}
			else
				System.out.println("Connection not OK");
		}
		catch(Exception e)
		{
		}
	}
}