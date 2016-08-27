/**
 * MIT License
 *
 * Copyright (c) 2016 Declan Neilson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

		List<String> bufferedMessage = new ArrayList<>();
		List<String> messageSplit = new ArrayList<>(Arrays.asList(line.split("\n")));

		int mlimit = 1500;
		String tempLine = "";

		while (messageSplit.size() > 0){

			String cString = messageSplit.remove(0);

			// add the first message to the buffer


			// if the total temp line length is less then that's fine, keep iterating
			// otherwise if it's just gone over, dump it into the bufferedMessage array, add the thing we just removed back
			// and clear the templine

			if ((tempLine + cString).length() > mlimit) { // not fine

				// output templine and clear it
				bufferedMessage.add(tempLine);
				tempLine = "";

				// then readd the thing we just popped off the list back on top
				messageSplit.add(0, cString);

			} else {
				tempLine += cString;
			}

		}

		if(bufferedMessage.size() == 0)
			bufferedMessage.add(tempLine);



		for(String partline : bufferedMessage) {
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

			});
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
