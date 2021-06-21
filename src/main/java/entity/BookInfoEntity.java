package entity;

public class BookInfoEntity {
    private long id;
    private String authorName;
    private String authorSurname;
    private String authorPatronymic;
    private String edition;
    private String publishingHouse;
    private String publishingYear;
    private int pages;
    private String writtenYear;
    private double weight;

    public BookInfoEntity() {
    }

    public BookInfoEntity(long id, String authorName, String authorSurname, String authorPatronymic, String edition,
                          String publishingHouse, String publishingYear, int pages, String writtenYear, double weight) {
        this.id = id;
        this.authorName = authorName;
        this.authorSurname = authorSurname;
        this.authorPatronymic = authorPatronymic;
        this.edition = edition;
        this.publishingHouse = publishingHouse;
        this.publishingYear = publishingYear;
        this.pages = pages;
        this.writtenYear = writtenYear;
        this.weight = weight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public void setAuthorSurname(String authorSurname) {
        this.authorSurname = authorSurname;
    }

    public String getAuthorPatronymic() {
        return authorPatronymic;
    }

    public void setAuthorPatronymic(String authorPatronymic) {
        this.authorPatronymic = authorPatronymic;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public String getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(String publishingYear) {
        this.publishingYear = publishingYear;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getWrittenYear() {
        return writtenYear;
    }

    public void setWrittenYear(String writtenYear) {
        this.writtenYear = writtenYear;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
