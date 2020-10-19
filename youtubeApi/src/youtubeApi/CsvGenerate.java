package youtubeApi;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvGenerate {
	  //カンマ
	  private static final String COMMA = "	";
	  //改行
	  private static final String NEW_LINE= "\r\n";
	  //格納用リスト
	  private static final List<csvItems> list = new ArrayList<>();

	  public  void csvItemStore(csvItems item) {
	      //格納
	      list.add(item);
	  }

	  public  void csvGenerater() {
		  FileWriter fileWriter = null;

		    try {
		    	fileWriter = new FileWriter("youtubeSearch.csv");

			    fileWriter.append("タイトル");
		        fileWriter.append(COMMA);
		        fileWriter.append("videoURL");
		        fileWriter.append(COMMA);
		        fileWriter.append("再生回数");
		        fileWriter.append(COMMA);
		        fileWriter.append("チャンネル登録者数");
		        fileWriter.append(COMMA);
		        fileWriter.append("再生回数/チャンネル登録者数[%]");
		        fileWriter.append(COMMA);
		        fileWriter.append("投稿時間");
		        fileWriter.append(NEW_LINE);


		      //リストの内容を順に処理
		      for (csvItems items : list) {

		        fileWriter.append(items.getTitle());
		        fileWriter.append(COMMA);
		        fileWriter.append(items.getVideoUrl());
		        fileWriter.append(COMMA);
		        fileWriter.append(String.valueOf(items.getViews()));
		        fileWriter.append(COMMA);
		        fileWriter.append(String.valueOf(items.getviewsDivideSubscribers()));
		        fileWriter.append(COMMA);
		        fileWriter.append(items.getPublishedAt());
		        fileWriter.append(NEW_LINE);
		      }

		      System.out.println("CSVファイル出力完了");

		    } catch (Exception e) {
		      e.printStackTrace();
		    } finally {

		      try {
		        fileWriter.flush();
		        fileWriter.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }

		    }
		  }

}
