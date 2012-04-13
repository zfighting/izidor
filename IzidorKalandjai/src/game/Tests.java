package game;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import engine.Vector2d;



//A parancssori argumentumban kapott tesztfájlt töltjük be.
public class Tests 
{
	
	public void TestsRun(String[] args) throws ParserConfigurationException, SAXException, IOException, InvalidTileIDException
    { 
		String command = null; 
		BufferedReader br= null;
		
		
		try
		{			
			FileReader fr = new FileReader("res" + File.separator +args[0]); 
			br = new BufferedReader(fr); 
			
			while ((command = br.readLine()) != null) 
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
					XMLReader.load("res\\" + tokens[1]);
				
				//stageinfo
				if(tokens[0].matches("stageinfo"))
				{
					FileWriter fw= new FileWriter("res"+ File.separator + /*System.getProperty("user.dir") + tokens[0] */ "output.txt" ); 	//ebbe írunk, ideiglenesen
					System.out.println("mentés");
					
					//TODO stageinfo kiíratás
					//for (int i=0; i < 0; i ++)
					fw.write("Spawnpoint    	: 0, 0, 0\n" +
							 "Door postion   	: 0, 0, 0\n" +
							 "Key position      : 0, 0, 0\n" +
							 "TileID_1 position : x1, y1\n" +
							 "TileID_2 position : x2, y2\n" +
							 "TileID_3 position : x3, y3\n" +
							 "TileID_4 position : x4, y4\n" +
							 "Remaining keys    : 0\n" +
							 "Player1 position  : 50-x*, y\n");
					
					fw.close();					
				}
				
				
				//swap
				if(tokens[0].matches("swap"))
				{
					//.swap(Direction.valueOf(tokens[1]));					
				}
				
				
				if(tokens[0].matches("setforce")){}
			
				if(tokens[0].matches("swap")){}
				
				if(tokens[0].matches("kill")){}
				
				if(tokens[0].matches("addplayer")) {}

				
				int j=0;
				while(tokens[j]!=null)
				{
				//	System.out.println(tokens[j]); 		//debug consolra
					j++;
				}
					
				

				
				
				
			}                  
            
		}
		catch(IOException e) 	//ha nincs ilyen file
		{
			 e.printStackTrace();
					 
		}
		finally 
		{
			if (br != null)
					br.close();
				
		}
    }
}