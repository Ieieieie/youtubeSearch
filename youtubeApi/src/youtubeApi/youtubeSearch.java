package youtubeApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class youtubeSearch {
	static List<JsonNode> searchList = new ArrayList<>();
	static List<JsonNode> videoInfoList = new ArrayList<>();
	static List<JsonNode> channelInfoList = new ArrayList<>();
	final static String apiKey = "AIzaSyCC_hrh0uQdpQCH6QbFp-WVSWZODtX2sjw";
	static int limitCount = 0;

	public static void main(String[] args) throws Exception {
		System.out.println("検索キーワードを入力してください。");
		Scanner sc = new Scanner(System.in);
		String keyword = sc.nextLine();
		searchKeyword(keyword,null);
		csvGenerateRun();
		sc.close();
	}
	public static void searchKeyword(String searchKeyword,String next_page_token) {
		String baseUrl = "https://www.googleapis.com/youtube/v3/search?key="+apiKey+"&part=snippet&q="+searchKeyword+
				"&order=date&maxResults=50";
		if(next_page_token != null) {
			baseUrl = baseUrl + "&pageToken="+next_page_token;
		}
		int limit = 5;
		int count = 0;
		HttpURLConnection huc = null;
		InputStream is = null;
		BufferedReader bf  = null;
		System.out.println("キーワードサーチ");
		try {
			URL apiUrl = new URL(baseUrl);
			huc = (HttpURLConnection) apiUrl.openConnection();
			huc.setRequestMethod("GET");
			huc.connect();
			if(huc.getResponseCode() == HttpURLConnection.HTTP_OK){
				is = huc.getInputStream();
				bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(bf);
				for(JsonNode n : node.get("items")) {
					searchList.add(n);
					System.out.println(n);
					if(n.get("id").get("videoId") != null && n.get("snippet").get("channelId") != null) {
						searchVideoInfo(n.get("id").get("videoId").asText());
						searchChannelInfo(n.get("snippet").get("channelId").asText());
					}else {
						videoInfoList.add(null);
						channelInfoList.add(null);
					}

				}
				if(limitCount<limit) {
					next_page_token = node.get("nextPageToken").asText();
					limitCount++;
					searchKeyword(searchKeyword,next_page_token);
				}
			}else{
				System.out.println("HttpError: " + huc.getResponseCode());
			}
		}catch(Exception e){
			System.out.print("失敗しました");
			e.printStackTrace();
		}finally {
			try {
				if(bf != null) {
					bf.close();
				}
				if(huc != null) {
					huc.disconnect();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public static void searchVideoInfo(String videoId) {
		String baseUrl = "https://www.googleapis.com/youtube/v3/videos?key="+apiKey+"&id="+videoId+"&part=statistics&maxResults=50";

		HttpURLConnection huc = null;
		InputStream is = null;
		BufferedReader bf  = null;

		System.out.println("ビデオサーチ");
		try {
			URL apiUrl = new URL(baseUrl);
			huc = (HttpURLConnection) apiUrl.openConnection();
			huc.setRequestMethod("GET");
			huc.connect();
			if(huc.getResponseCode() == HttpURLConnection.HTTP_OK){
				is = huc.getInputStream();
				bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(bf);
				videoInfoList.add(node.get("items"));
				System.out.println(node.get("items").get(0).get("statistics").get("viewCount").asInt());
			}else{
				System.out.println("HttpError: " + huc.getResponseCode());
			}
		}catch(Exception e){
			System.out.print("video失敗しました");
			e.printStackTrace();
		}finally {
			try {
				if(bf != null) {
					bf.close();
				}
				if(huc != null) {
					huc.disconnect();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public static void searchChannelInfo(String channelId) {
		String baseUrl = "https://www.googleapis.com/youtube/v3/channels?key="+apiKey+"&id="+channelId+"&part=statistics&maxResults=50";

		HttpURLConnection huc = null;
		InputStream is = null;
		BufferedReader bf  = null;

		System.out.println("チャンネルサーチ");
		try {
			URL apiUrl = new URL(baseUrl);
			huc = (HttpURLConnection) apiUrl.openConnection();
			huc.setRequestMethod("GET");
			huc.connect();
			if(huc.getResponseCode() == HttpURLConnection.HTTP_OK){
				is = huc.getInputStream();
				bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(bf);
				channelInfoList.add(node.get("items"));
				System.out.println(node.get("items"));
				System.out.println(node.get("items").get(0).get("statistics").get("subscriberCount").asInt());
			}else{
				System.out.println("HttpError: " + huc.getResponseCode());
			}
		}catch(Exception e){
			System.out.print("channel失敗しました");
			e.printStackTrace();
		}finally {
			try {
				if(bf != null) {
					bf.close();
				}
				if(huc != null) {
					huc.disconnect();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public static void csvGenerateRun() {
		System.out.println("csvgenerate");
		CsvGenerate cg = new CsvGenerate();
		System.out.println(searchList.size());
		for(int i = 0; i<searchList.size(); i++) {
			if(videoInfoList.get(i) != null) {
				double partVideoCount = videoInfoList.get(i).get(0).get("statistics").get("viewCount").asDouble();
				double partChannelSubscribers = channelInfoList.get(i).get(0).get("statistics").get("subscriberCount").asDouble();
				double percentage = 0;
				if(partChannelSubscribers != 0) {
					percentage = (partVideoCount/partChannelSubscribers) * 100;
				}
				if(percentage>=60) {
					cg.csvItemStore(new csvItems(searchList.get(i).get("snippet").get("title").asText(),
							"https://www.youtube.com/watch?v="+videoInfoList.get(i).get(0).get("id").asText(),
							videoInfoList.get(i).get(0).get("statistics").get("viewCount").asInt(),
							channelInfoList.get(i).get(0).get("statistics").get("subscriberCount").asInt(),
							percentage,
							searchList.get(i).get("snippet").get("publishedAt").asText()));
				}
			}

		}
		cg.csvGenerater();
	}
}
