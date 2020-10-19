package youtubeApi;

public class csvItems {
	String title = null;
	String videoUrl = null;
	int views = 0;
	int subscribers = 0;
	double viewsDivideSubscribers = 0;
	String publishedAt = null;

	csvItems(String title, String videoUrl,int views,int subscribers,double viewsDivideSubscribers,String publishedAt){
		this.title = title;
		this.videoUrl = videoUrl;
		this.views = views;
		this.subscribers = subscribers;
		this.viewsDivideSubscribers = viewsDivideSubscribers;
		this.publishedAt = publishedAt;
	}

	String getTitle() {return title;}
	private void setTitle(String title) {this.title = title;}

	String getVideoUrl() {return videoUrl;}
	private void setVideoUrl(String videoUrl) {this.videoUrl = videoUrl;}

	int getViews() {return views;}
	private void setViews(int views) {this.views = views;}

	int getSubscribers() {return subscribers;}
	private void setSubscribers(int subscribers) {this.subscribers = subscribers;}

	double getviewsDivideSubscribers() {return viewsDivideSubscribers;}
	private void setviewsDivideSubscribers(double viewsDivideSubscribers) {this.viewsDivideSubscribers = viewsDivideSubscribers;}

	String getPublishedAt() {return publishedAt;}
	private void setPublishedAt(String publishedAt) {this.publishedAt = publishedAt;}
}
