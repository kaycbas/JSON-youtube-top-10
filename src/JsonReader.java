import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonReader {

	public JsonReader() {
		try {
			URL website = new URL("http://gdata.youtube.com/feeds/api/standardfeeds/most_popular?v=2&alt=json");
			ReadableByteChannel rbc;
			rbc = Channels.newChannel(website.openStream());		
			FileOutputStream fos = new FileOutputStream("youtubeInfo");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			
			File file = new File("youtubeInfo.txt");
			String str = readFile(file.toString(), StandardCharsets.UTF_8);
			
			JSONObject json = new JSONObject(str);
			JSONObject feed = (JSONObject)json.get("feed");
			JSONObject title = (JSONObject)feed.get("title");
			JSONArray info = (JSONArray)feed.get("entry");
			System.out.println(title.get("$t"));
			
			for (int i=0; i<10; i++) {
				JSONObject obj = (JSONObject)info.get(i);
				JSONObject tit = (JSONObject)obj.get("title");
				JSONObject stats = (JSONObject)obj.get("yt$statistics");
				System.out.print(tit.get("$t"));
				System.out.print(" -- ");
				System.out.println(stats.get("viewCount"));					
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
	public static void main(String[] args) {
		JsonReader jr = new JsonReader();	
	}	
}
