package game;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import engine.Vector2d;

//teszteseteket tartalmaz� oszt�ly, 
//5 k�l�nb�z� teszteset(kulcsfelv�tele majd p�lya elhagy�sa,tilitoli,k�t tile k�z�tti �thalad�s, meghal�s, �tk�z�s)
public class Tests
{
	//konstrukor
	public Tests()
	{
	
	}
	
	//integer beolvas�sa �s tesztesetek elindit�s�ra szolg�l� f�ggv�ny
	public void TestsRun()
	{	
			//tesztesetekhez egy integer beolvas�sa
			int val = 0;  //ebben a v�lzozoban t�roljuk a bek�rt sz�mot
			
			while(val<1 || val>5){  //addig k�rj�k a sz�mot am�g nem megfelel�
			String line = null;  
		    System.out.println("Choose between the test cases [1-5]:");
		    System.out.println("*************************************");
		    System.out.println("[1] - Picking up a key, then exiting");
		    System.out.println("[2] - Sliding puzzle test");
		    System.out.println("[3] - Transition between two tiles");
		    System.out.println("[4] - Death");
		    System.out.println("[5] - Collision with a tile");
		    System.out.println("*************************************");
		    System.out.print("Choice: ");
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
		      val = Integer.parseInt(line);  //ha nem integert �rtak be
		    } catch (NumberFormatException ex) {
		      System.err.println("Not a valid number: " + line);
		    }
		    if(val<1 || val>5) // ha nem megfelel� m�ret� a sz�m
		    {
		    	System.out.println("Not a valid number, it must be between 1 and 5.");
		    }
			}
		    //Tests objektum l�trehoz�sa a tesztel�shez
		    
		    
		    switch (val)
		    {
		    //els� teszteset megh�v�sa(kulcs felv�tele, majd p�lya elhagy�s�nak teszje)
		    case 1: Test1(); break;
		    
		    //m�sodik teszteset megh�v�sa(Tilitoli tesztje)
		    case 2: Test2(); break;

		    //harmadik teszteset megh�v�sa(K�t tile k�z�tti �thalad�s tesztje)
		    case 3: Test3(); break;
		    //negyedik teszteset megh�v�sa(Meghal�s tesztje)
		    case 4: Test4(); break;
		    //�t�dik teszteset megh�v�sa(�tk�z�s tesztje)
		    case 5: Test5(); break;
		    
		    default: break;
		    
		    }
}
	
	//Kulcsfelv�tele, majd a p�lya elhagy�s�nak tesztj�nek f�ggv�nye
	public void Test1()
	{
		Stage stage = new Stage("test");
		
		Key key = new Key((byte) 1, new Vector2d(10, 10), Color.BLACK, 10, 10);
		Door door = new Door((byte) 1, new Vector2d(20, 10), Color.BLACK, 10, 10);
		
		stage.tiles[1][0].addKey(key);
		stage.tiles[1][0].addObject(door);
		
	}
	//Tilitoli tesztj�nek f�ggv�nye
	public void Test2()
	{
		Stage stage = new Stage("test");
		stage.swap(Direction.LEFT);
	}
	//K�t tile k�z�tti �thalad�s tesztj�nek a f�ggv�nye
	public void Test3()
	{
		System.out.println("\nI am the third test case");
	}
	//Meghal�s teszj�nek a f�ggv�nye
	public void Test4()
	{
		System.out.println("\nI am the fourth test case");
	}
	//�tk�z�s tesztj�nek a megh�v�sa
	public void Test5()
	{
		System.out.println("\n5I am the fifth test case");
	}
}
