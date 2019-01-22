package eu.beezig.core.briefing;

public class News {

    private String title, content;
    private long postedAt;

    public News(String title, String content, long postedAt) {
        super();
        this.title = title;
        this.content = content;
        this.postedAt = postedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getPostedAt() {
        return postedAt;
    }


}
