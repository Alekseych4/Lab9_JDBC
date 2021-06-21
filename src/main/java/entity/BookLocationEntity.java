package entity;

public class BookLocationEntity {
    private long id;
    private int floor;
    private int bookcase;
    private int shelf;

    public BookLocationEntity() {
    }

    public BookLocationEntity(long id, int floor, int bookcase, int shelf) {
        this.id = id;
        this.floor = floor;
        this.bookcase = bookcase;
        this.shelf = shelf;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getBookcase() {
        return bookcase;
    }

    public void setBookcase(int bookcase) {
        this.bookcase = bookcase;
    }

    public int getShelf() {
        return shelf;
    }

    public void setShelf(int shelf) {
        this.shelf = shelf;
    }
}
