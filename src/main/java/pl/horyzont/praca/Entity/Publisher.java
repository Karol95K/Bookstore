package pl.horyzont.praca.Entity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Publisher {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private Integer id_publisher;

    @Pattern(regexp = "^[A-Ża-ż\\s]{2,50}$", message = "Wykorzystaj litery z zakresu 'a-ż' oraz 'A-Ż'. Podaj od 2 do 50 liter.")
    private String namePublisher;

    @Pattern(regexp = "^[A-Ża-ż\\s]{3,50}$", message = "Wykorzystaj litery z zakresu 'a-ż' oraz 'A-Ż'. Podaj od 3 do 50 liter.")
    private String cityPublisher;

    @Pattern(regexp = "^[\\d]{9,15}$", message = "Podaj numer składający się z 9-15 cyfr.")
    private String phonePublisher;

    @OneToMany (mappedBy = "publisher", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List <Book> bookList= new ArrayList<>();

    public void insertBookToList (Book book){
        this.bookList.add(book);
    }

    public Publisher() {
    }



    public Publisher(String namePublisher, String city, String phonePublisher) {
        this.namePublisher = namePublisher;
        this.cityPublisher = city;
        this.phonePublisher = phonePublisher;
    }

    public Publisher(Integer id, String namePublisher, String cityPublisher, String phonePublisher) {
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

    public String getPhonePublisher() {
        return phonePublisher;
    }

    public void setPhonePublisher(String phonePublisher) {
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
