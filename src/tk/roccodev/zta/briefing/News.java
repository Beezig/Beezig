package tk.roccodev.zta.briefing;

public class News {

	private String title, content;
	private NewsPriority priority;
	private long postedAt;
	
	public News(String title, String content, int priority, long postedAt) {
		super();
		this.title = title;
		this.content = content;
		this.priority = NewsPriority.getByValue(priority);
		this.postedAt = postedAt;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public NewsPriority getPriority() {
		return priority;
	}

	public long getPostedAt() {
		return postedAt;
	}
	
	
	
	
}
