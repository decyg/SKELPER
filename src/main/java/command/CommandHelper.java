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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Created by Declan on 07/04/2016.
 */
public final class CommandHelper {

	public static void sM(IMessage source, String line) throws CommandException {

		List<String> messageSplit = new ArrayList<>();
		String tLine = line;
		int mlimit = 1500;

		while(tLine.length() >= mlimit){
			String part = tLine.substring(0, mlimit-1);
			tLine = tLine.substring(mlimit);
			messageSplit.add(part);
		}
		messageSplit.add(tLine);

		for(String partline : messageSplit) {
			RequestBuffer.request(() -> {

				try {

					if (source != null) {

						source.reply("\n" + partline);

					} else {

						MainExecutor.log.info("[skelper] " + partline);

					}

				} catch (DiscordException | MissingPermissionsException e) {

					MainExecutor.log.error("Could not send that message", e);

				}

				return null;

			});

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignored) {
			}
		}

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


		URL rssURL = new URL(url);
		URI rssURI = new URI(rssURL.getProtocol(), rssURL.getUserInfo(), rssURL.getHost(), rssURL.getPort(), rssURL.getPath(), rssURL.getQuery(), rssURL.getRef());

		SyndFeedInput input = new SyndFeedInput();

		return input.build(new XmlReader(new URL(rssURI.toASCIIString())));

	}

}
