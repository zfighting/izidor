package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//teszteseteket tartalmazó osztály, 
//5 különbözõ teszteset(kulcsfelvétele majd pálya elhagyása,tilitoli,két tile közötti áthaladás, meghalás, ütközés)
public class Tests
{
	//konstrukor
	public Tests(){}
	
	//integer beolvasása és tesztesetek elinditására szolgáló függvény
	public void TestsRun()
	{	
			//tesztesetekhez egy integer beolvasása
			int val = 0;  //ebben a válzozoban tároljuk a bekért számot
			
			while(val<1 || val>5){  //addig kérjük a számot amíg nem megfelelõ
			String line = null;  
		    System.out.println("Choose between the test cases [1-5]:");
		    System.out.println("[1] - Picking up a key, then exiting");
		    System.out.println("[2] - Sliding puzzle test");
		    System.out.println("[3] - Transition between two tiles");
		    System.out.println("[4] - Death");
		    System.out.println("[5] - Collision with a tile");
		    try {
		      BufferedReader is = new BufferedReader(
		        new InputStreamReader(System.in));
		      try
			{
				line = is.readLine();
			}
			catch (IOException e) //ha nem tudunk beolvasni
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      val = Integer.parseInt(line);  //ha nem integert írtak be
		    } catch (NumberFormatException ex) {
		      System.err.println("Not a valid number: " + line);
		    }
		    if(val<1 || val>5) // ha nem megfelelõ méretû a szám
		    {
		    	System.out.println("Not a valid number, it must be between 1 and 5.");
		    }
			}
		    //Tests objektum létrehozása a teszteléshez
		    
		    
		    switch (val)
		    {
		    //elsõ teszteset meghívása(kulcs felvétele, majd pálya elhagyásának teszje)
		    case 1: Test1(); break;
		    //második teszteset meghívása(Tilitoli tesztje)
		    case 2: Test2(); break;
		    //harmadik teszteset meghívása(Két tile közötti áthaladás tesztje)
		    case 3: Test3(); break;
		    //negyedik teszteset meghívása(Meghalás tesztje)
		    case 4: Test4(); break;
		    //ötödik teszteset meghívása(Ütközés tesztje)
		    case 5: Test5(); break;
		    
		    default: break;
		    
		    }
}
	
	//Kulcsfelvétele, majd a pálya elhagyásának tesztjének függvénye
	public void Test1()
	{
		System.out.println("I am the first test case");
	}
	//Tilitoli tesztjének függvénye
	public void Test2()
	{
		System.out.println("I am the second test case");
	}
	//Két tile közötti áthaladás tesztjének a függvénye
	public void Test3()
	{
		System.out.println("I am the third test case");
	}
	//Meghalás teszjének a függvénye
	public void Test4()
	{
		System.out.println("I am the fourth test case");
	}
	//Ütközés tesztjének a meghívása
	public void Test5()
	{
		System.out.println("I am the fifth test case");
	}
}
