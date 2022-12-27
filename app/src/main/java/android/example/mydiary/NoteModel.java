package android.example.mydiary;

public class NoteModel {

    private String Title;
    private String Content;

    public NoteModel(){

    }

    public NoteModel(String Title, String Content){
        this.Title = Title;
        this.Content = Content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

}
