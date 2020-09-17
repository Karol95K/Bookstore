package pl.horyzont.praca.Entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
public class Author {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id_autor;

    @Pattern(regexp = "^[A-Ża-ż]{2,50}$", message = "Wykorzystaj litery z zakresu 'a-ż' oraz 'A-Ż'. Podaj od 2 do 50 liter.")
    private String imie;

    @Pattern(regexp = "^[A-Ża-ż]{2,100}$", message = "Wykorzystaj litery z zakresu 'a-ż' oraz 'A-Ż'. Podaj od 2 do 100 liter.")
    private String nazwisko;

    @Pattern(regexp = "^[1-9][\\d]{0,3}$", message = "Podaj liczbę max. czterocyfrową.")
    private String liczbaPublikacji;

    @Pattern(regexp = "^[\\d]{9,15}$", message = "Podaj numer składający się z 9-15 cyfr.")
    private String telefonAutora;

    @ManyToMany (cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable (
            name = "authors_books",
            joinColumns = @JoinColumn (name = "author_id"),
            inverseJoinColumns = @JoinColumn (name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    @Transient
    public Set <Integer> index_autor = new HashSet<>();

    public void addBook (Book book){
        this.books.add(book);
        book.getAuthors().add(this);
    }

    public void removeBook (Book book){
        this.books.remove(book);
        book.getAuthors().remove(this);
    }

    public Author() {
    }

    public Author(String imie, String nazwisko, String liczbaPublikacji, String telefonAutora) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.liczbaPublikacji = liczbaPublikacji;
        this.telefonAutora = telefonAutora;
    }

    public Author(Integer id_autor, String imie, String nazwisko, String liczbaPublikacji, String telefonAutora) {
        this.id_autor = id_autor;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.liczbaPublikacji = liczbaPublikacji;
        this.telefonAutora = telefonAutora;
    }

    public Set<Integer> getIndex_autor() {
        return index_autor;
    }

    public void setIndex_autor(Set<Integer> index_autor) {
        this.index_autor = index_autor;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Integer getId_autor() {
        return id_autor;
    }

    public void setId_autor(Integer id_autor) {
        this.id_autor = id_autor;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getLiczbaPublikacji() {
        return liczbaPublikacji;
    }

    public void setLiczbaPublikacji(String liczbaPublikacji) {
        this.liczbaPublikacji = liczbaPublikacji;
    }

    public String getTelefonAutora() {
        return telefonAutora;
    }

    public void setTelefonAutora(String telefonAutora) {
        this.telefonAutora = telefonAutora;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id_autor=" + id_autor +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", liczbaPublikacji=" + liczbaPublikacji +
                ", telefonAutora=" + telefonAutora +
                '}';
    }
}

