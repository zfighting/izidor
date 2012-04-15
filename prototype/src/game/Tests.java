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
		String command 		= null; 
		BufferedReader br	= null;
		Stage stage 		= null;
		Player izidor 		= null;
		Player mortimer		= null;
		boolean vanIzidor	= false;
		boolean vanMortimer	= false;
		
		
		
		try
		{			
			FileReader fr = new FileReader("res" + File.separator +args[0]); 
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
					stage = XMLReader.load("res\\" + tokens[1]);
				
				//stageinfo
				// Egyik fele a stage osztályon belül van megvalósítva, másik fele itt, itt hozunk létre playereket
				if(tokens[0].matches("stageinfo"))
				{
					FileWriter fw= new FileWriter("res"+ File.separator + /*System.getProperty("user.dir") + tokens[0] */ "output.txt" ); 	//ebbe írunk, ideiglenesen
					//System.out.println("mentés"); //consolra írás					
				
					fw.write(stage.toString());		//stage-en belüli kírás
					
					
					//Playerek kiírása, ha csak 1 van akkor a másik nem fut.
					//Ha második is jön akkor a második
					
					if(vanIzidor && !vanMortimer)
						fw.write("Player1 position	: " + (int)(izidor.position.x)+"  "+ (int)(izidor.position.y)+"\n")  ;
					else if(vanIzidor && vanMortimer)
					{
						fw.write("Player1 position	: " + (int)(izidor.position.x)+"  "+  (int)(izidor.position.y)+"\n");
						fw.write("Player2 position	: " + (int)(mortimer.position.x)+"  "+ (int)(mortimer.position.y)+"\n");
						
					}
						
					fw.close();					
				}
				
				
				//swap
				if(tokens[0].matches("swap"))
				{
					stage.swap(Direction.valueOf(tokens[1]));					
				}
				
				
				//TODO
				
				
				
				
				
				if(tokens[0].matches("setforce")){}
				
				if(tokens[0].matches("kill")){}
				
				
				if(tokens[0].matches("addplayer") ) 
				{
					if(!vanIzidor)
					{
						SpawnPoint sp = new SpawnPoint((byte) (Integer.parseInt(tokens[1])), new Vector2d(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
						izidor = new Player((byte) (Integer.parseInt(tokens[1])), sp.position, Color.BLACK, 10, 10);
						
					}
					if(vanIzidor)
					{
						SpawnPoint sp =new SpawnPoint((byte) (Integer.parseInt(tokens[1])), new Vector2d(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
						mortimer = new Player((byte) (Integer.parseInt(tokens[1])), sp.position, Color.BLACK, 10, 10);
						vanMortimer= true;
					}
					vanIzidor=true;
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