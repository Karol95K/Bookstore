package pl.horyzont.praca.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Publisher {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id_publisher;
    private String namePublisher;
    private String cityPublisher;
    private Integer phonePublisher;

    @OneToMany (mappedBy = "publisher", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List <Book> bookList= new ArrayList<>();

    public void insertBookToList (Book book){
        this.bookList.add(book);
    }

    public Publisher() {
    }



    public Publisher(String namePublisher, String city, Integer phonePublisher) {
        this.namePublisher = namePublisher;
        this.cityPublisher = city;
        this.phonePublisher = phonePublisher;
    }

    public Publisher(Integer id, String namePublisher, String cityPublisher, Integer phonePublisher) {
        this.id_publisher = id;
        this.namePublisher = namePublisher;
        this.cityPublisher = cityPublisher;
        this.phonePublisher = phonePublisher;
    }

    public String getNamePublisher() {
        return namePublisher;
    }

    public void setNamePublisher(String namePublisher) {
        this.namePublisher = namePublisher;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Integer getId_publisher() {
        return id_publisher;
    }

    public void setId_publisher(Integer id_publisher) {
        this.id_publisher = id_publisher;
    }

    public String getCityPublisher() {
        return cityPublisher;
    }

    public void setCityPublisher(String cityPublisher) {
        this.cityPublisher = cityPublisher;
    }

    public Integer getPhonePublisher() {
        return phonePublisher;
    }

    public void setPhonePublisher(Integer phonePublisher) {
        this.phonePublisher = phonePublisher;
    }


    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id_publisher +
                ", namePublisher='" + namePublisher + '\'' +
                ", cityPublisher='" + cityPublisher + '\'' +
                ", phonePublisher=" + phonePublisher +
             //   ", bookList=" + bookList +
                '}';
    }
}
