package command;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import main.MainExecutor;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.*;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Created by Declan on 07/04/2016.
 */
public final class CommandHelper {

	// string constants

	private static long lastMessage = System.currentTimeMillis();


	// helper functions

	public static boolean isValidStation(String code){
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(new File("data/station_codes.csv")));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] allstat = line.split(cvsSplitBy);

				code = code.toLowerCase();

				if(allstat[1].toLowerCase().equals(code)) {
					br.close();
					return true;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public static String getStationCode(String station){
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(new File("data/station_codes.csv")));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] allstat = line.split(cvsSplitBy);

				station = station.toLowerCase();

				if(allstat[0].toLowerCase().contains(station)) {
					br.close();
					return allstat[1].toLowerCase();
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}

	public static boolean v(Object o){return o != null;}

	public static void sM(IMessage source, String line) throws CommandException {

		RequestBuffer.request(() -> {

			try {

				if(source != null) {

					source.reply("\n" + line);

				} else {

					MainExecutor.log.info("[skelper] " + line);

				}

			} catch (DiscordException | MissingPermissionsException e) {

				MainExecutor.log.error("Could not send that message", e);

			}

			return null;

		});

	}

	public static String hashMe(String input){
		short s = (short) (input.hashCode() ^ (input.hashCode() >>> 16));

		return Integer.toHexString(s & 0xffff).toUpperCase();
	}

	public static String shortenMagnet(String magnet){
		try {
			URL u = new URL("http://mgnet.me/api/create?m=" + magnet + "&format=text");

			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = rd.readLine();
			if(line != null){
				return line;
			}

			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String getTinyURL(String LongURL) {
		String _result = "";
		if(LongURL.startsWith("http://")) {
			LongURL = LongURL.replace("http://", "");
		}

		if(LongURL.startsWith("https://")) {
			LongURL = LongURL.replace("https://", "");
		}

		if(LongURL.startsWith("ftp://")) {
			LongURL = LongURL.replace("ftp://", "");
		}

		try {
			URL e = new URL("http://tinyurl.com/create.php?url=" + LongURL);
			URLConnection openURL = e.openConnection();
			openURL.addRequestProperty("User-Agent", "Mozilla/4.76");
			BufferedReader in = new BufferedReader(new InputStreamReader(openURL.getInputStream()));

			String inputLine;
			while((inputLine = in.readLine()) != null) {
				if(inputLine.contains("<small>[")) {
					int smallStart = inputLine.indexOf("<small>");
					int smallEnd = inputLine.indexOf("</small>");
					_result = inputLine.substring(smallStart, smallEnd + 8);
					int hrefStart = _result.indexOf("href=\"");
					int hrefEnd = _result.indexOf("\"", hrefStart + 6);
					_result = _result.substring(hrefStart + 6, hrefEnd);
					break;
				}
			}

			in.close();
		} catch (IOException var10) {
			_result = "Check internet connection";
		} catch (Exception var11) {
			_result = var11.getMessage();
		}

		return _result;
	}

	public static SyndFeed rssFromURL(String url) throws IOException, URISyntaxException, FeedException {


		URL katU = new URL(url);
		URI katURI = new URI(katU.getProtocol(), katU.getUserInfo(), katU.getHost(), katU.getPort(), katU.getPath(), katU.getQuery(), katU.getRef());

		SyndFeedInput input = new SyndFeedInput();

		return input.build(new XmlReader(new URL(katURI.toASCIIString())));

	}

}
