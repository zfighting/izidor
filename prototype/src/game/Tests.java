package game;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import engine.Vector2d;



//A parancssori argumentumban kapott tesztfájlt töltjük be.
public class Tests 
{
	
	public void TestsRun(String[] args, Game game) throws ParserConfigurationException, SAXException, IOException, InvalidTileIDException
    { 
		String command 		= null; 
		BufferedReader br	= null;
		game.stage 		= null;
		ArrayList<Player> playerlist = new ArrayList();
		
		try
		{			
			FileReader fr = new FileReader(System.getProperty("user.dir") + File.separatorChar + args[0]); 
			br = new BufferedReader(fr); 
			
			while ((command = br.readLine())!= null) 
			{
				String[] tokens = new String[5];	//veszünk egy 5-s tömböt, annál nagyobb nem kell
				
				//tokenizerrel tördelünk
				StringTokenizer st 	= new StringTokenizer(command);
				
				//a token tömb feltöltése
				for(int i= 0;st.hasMoreElements();i++)				
					tokens[i]=st.nextToken();

				//HSZKs gépeken 1.6-os java van, így nincs benne stringes switch szerkezet, így IF-elés
				
				//loadstage
				if(tokens[0].matches("loadstage"))
					game.stage = XMLReader.load(File.separatorChar + "levels"  + File.separatorChar + tokens[1]);
				
				//stageinfo
				// Egyik fele a stage osztályon belül van megvalósítva, másik fele itt, itt hozunk létre playereket
				if(tokens[0].matches("stageinfo"))
				{
					String ps = new String();
					FileWriter fw = new FileWriter(System.getProperty("user.dir") + File.separatorChar + tokens[1]);
					//FileWriter fw= new FileWriter("res"+ File.separator + /*System.getProperty("user.dir") + tokens[0] */ "output.txt" ); 	//ebbe írunk, ideiglenesen
					//System.out.println("mentés"); //consolra írás					
				
					fw.write(game.stage.toString());		//stage-en belüli kírás
					System.out.println(game.stage.toString());
					
					
					ps += "Player position(s)\t: ";
					for (Player p : game.getPlayers())
					{
						ps += p.toString();
						ps += "\t";
					}
					
					fw.write(ps);
					System.out.println(ps);
					
					fw.close();					
				}
				
				
				//swap
				if(tokens[0].matches("swap"))
				{
					game.stage.swap(Direction.valueOf(tokens[1]));					
				}
		
				//TODO
			
				if(tokens[0].matches("setforce"))
				{
					game.getPlayers().get(Integer.parseInt(tokens[1])).applyForce(new Vector2d(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
					//game.stage.movePlayer(game.getPlayers().get(Integer.parseInt(tokens[1])));
				}
				
				if(tokens[0].matches("tick"))
				{
					for (int x = 0; x < 30; x++)
					{
						// force refresh
						for (Player p : game.getPlayers())
							p.applyForce(new Vector2d(0, 0));
						game.globalUpdate();
					}
				}
				
				if(tokens[0].matches("addplayer") ) 
				{
						game.stage.addSpawnPoint((byte) (Integer.parseInt(tokens[1])), new Vector2d(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
						game.addPlayer((byte) (Integer.parseInt(tokens[1])), new Vector2d(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
				}
				
				if(tokens[0].matches("adddoor") ) 
				{
						// igaziból maradhat ittis spawnpoint, csak a helyét használjuk...
						SpawnPoint sp = new SpawnPoint((byte) (Integer.parseInt(tokens[1])), new Vector2d(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
						game.stage.addDoor((byte) (Integer.parseInt(tokens[1])), sp.position);
				}

				if(tokens[0].matches("addkey") ) 
				{
					// igaziból maradhat ittis spawnpoint, csak a helyét használjuk...
						SpawnPoint sp = new SpawnPoint((byte) (Integer.parseInt(tokens[1])), new Vector2d(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
						game.stage.addKey((byte) (Integer.parseInt(tokens[1])), sp.position);
				}
				
				//int j=0;
				//while(tokens[j]!=null)
				//{
				//	System.out.println(tokens[j]); 		//debug consolra
				//	j++;
				//}			
				
			}                  
            
		}
		catch(IOException e) 	//ha nincs ilyen file
		{
			 e.printStackTrace();
					 
		}
		finally 
		{
			if (br != null)
					br.close();		//bezárás
				
		}
    }
}