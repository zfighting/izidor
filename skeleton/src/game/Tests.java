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
	
	public int lvl = 0;
	
	//konstrukor
	public Tests()
	{
	
	}
	
	//integer beolvas�sa �s tesztesetek elindit�s�ra szolg�l� f�ggv�ny
	public void TestsRun() throws InvalidTileIDException, IOException
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
		int exit = 0;
		
		for (int i = 0; i < lvl; i++) System.out.print("\t"); lvl++;
		Stage stage = new Stage("test");
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		Player p = new Player((byte) 1, new Vector2d(1, 10), Color.BLACK, 10, 10);
		
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		Key key = new Key((byte) 1, new Vector2d(10, 10), Color.BLACK, 10, 10);
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		Door door = new Door((byte) 1, new Vector2d(20, 10), Color.BLACK, 10, 10);
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		
		stage.tiles[1][0].addKey(key); System.out.print("\t");
		stage.tiles[1][0].addObject(door); System.out.print("\t");
		
		while (exit != 2)
		{
			p.moveTo(new Vector2d(10, 10)); System.out.print("\t");
			stage.tiles[1][0].pickKey(new Vector2d(10, 10)); System.out.print("\t");
			if (stage.tiles[1][0].getNumberOfKeys() == 0 && exit == 0)
			{
				System.out.print("\t");
				exit++;
			}	
			
			p.moveTo(new Vector2d(20, 10)); System.out.print("\t");
			if (exit == 1 && (p.position.x == door.position.x && p.position.y == door.position.y))
				exit++;
		}
	
		
		
		
	}
	//Tilitoli tesztj�nek f�ggv�nye
	public void Test2()
	{
		for (int i = 0; i < lvl; i++) System.out.print("\t"); lvl++;
		Stage stage = new Stage("test");
		
		// stage kiírása
		System.out.println(stage.print()[0]);
		System.out.println(stage.print()[1]);
		
		for (int i = 0; i < lvl; i++) System.out.print("\t"); lvl++;
		
		stage.swap(Direction.LEFT);
		
		// stage kiírás
		for (int i = 0; i < lvl; i++) System.out.print("\n\t"); lvl++;
		System.out.println(stage.print()[0]); System.out.print("\t");
		System.out.println(stage.print()[1]);
	}
	//K�t tile k�z�tti �thalad�s tesztj�nek a f�ggv�nye
	public void Test3() throws InvalidTileIDException
	{
		// egyelőre 540 px széles egy tile, ez még nincs implementálva pontosan
		int exit = 0;
		
		for (int i = 0; i < lvl; i++) System.out.print("\t"); lvl++;
		Stage stage = new Stage("test");
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		Player p = new Player((byte) 2, new Vector2d(1, 10), Color.BLACK, 10, 10);
		
		while (exit != 1)
		{
			p.position.x += 60;
			for (int i = 0; i < lvl; i++) System.out.println("\t tileID = "+p.tileID+", x = "+p.position.x);
			if (p.position.x > 540) exit++;
		}
		
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		p.moveTo((byte)3, new Vector2d(1,10)); for (int i = 0; i < lvl; i++) System.out.println("\t");
		for (int i = 0; i < lvl; i++) System.out.println("\t tileID = "+p.tileID+" x = "+p.position.x);
		
		
		
		
	}
	//Meghal�s teszj�nek a f�ggv�nye
	public void Test4()
	{
		int exit = 0;
		
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		Stage stage = new Stage("test");
		SpawnPoint sp = new SpawnPoint((byte) 2, new Vector2d(1, 100)); for (int i = 0; i < lvl; i++) System.out.print("\t"); lvl++;
		
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		Player p = new Player((byte) 2, sp.position, Color.BLACK, 10, 10);
		
		System.out.println("\t player position: "+sp.position);
		
		
		while (exit != 1)
		{
			p.position.y -= 20;
			for (int i = 0; i < lvl; i++) System.out.println("\t tileID = "+p.tileID+", y = "+p.position.y);
			if (p.position.y < 0) exit++;
		}
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		p.moveTo(sp.position);
		
		
	}
	//�tk�z�s tesztj�nek a megh�v�sa
	public void Test5() throws IOException
	{
		// egyelőre 540 px széles egy tile
		int exit = 0;
		String line;
		char c = 'a';
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		
		for (int i = 0; i < lvl; i++) System.out.print("\t"); lvl++;
		Stage stage = new Stage("test");
		
		for (int i = 0; i < lvl; i++) System.out.print("\t");
		Player p = new Player((byte) 2, new Vector2d(1, 10), Color.BLACK, 10, 10);
		
		while (exit != 1)
		{
			p.position.x += 60;
			for (int i = 0; i < lvl; i++) System.out.println("\t tileID = "+p.tileID+", x = "+p.position.x);
			if (p.position.x > 540) 
			{
				for (int i = 0; i < lvl; i++) System.out.println("\t");
				p.moveTo(Vector2d.subtract(p.position, new Vector2d(61, 0))); 
				for (int i = 0; i < lvl; i++) System.out.println("\t");
				
				System.out.println("Should we collide again? [y/n]");
				line = is.readLine();
				c = line.charAt(0);
				if (c == 'n') exit = 1;		
			}	
			
		}
		
	}
}
