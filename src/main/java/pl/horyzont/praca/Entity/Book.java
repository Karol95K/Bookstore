package pl.horyzont.praca.Entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_ksiazka;
    private String tytul;
    private Integer rokWydania;
    private Long isbn;
    private Integer liczbaEgzemplarzy;
    private Integer cenaZaKsiazke;

    @ManyToOne
    @JoinColumn (name = "id_publisher")
    private Publisher publisher;



    @ManyToMany (mappedBy = "books")
    private Set<Author> authors = new HashSet<>();

//    @ElementCollection()
//    @CollectionTable(name="Map_books_authors", joinColumns=@JoinColumn(name="Map_id"))
//    @MapKeyColumn (name = "Key_author_Id")
//    @Column (name = "Value_book_Id")
    @Transient
    private  Map<Integer, Integer> book_map = new TreeMap<Integer, Integer>();

    public  void setBook_map(Integer klucz_idAutor, Integer wartosc_idKsiazka) {
        book_map.put(klucz_idAutor, wartosc_idKsiazka);
    }

    public  Map<Integer, Integer> getBook_map() {
        return book_map;
    }


    // Klucz - id_autor, wartosc - id_ksiazka
    public  Set<Integer> getKeysByValue( Integer value) {
        Set<Integer> keys = new HashSet<Integer>();
        for (Map.Entry<Integer, Integer> entry : book_map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    public Book() {
    }

    public Book(String tytul, Integer rokWydania, Long isbn, Integer liczbaEgzemplarzy, Integer cenaZaKsiazke, Set<Author> authors) {
        this.tytul = tytul;
        this.rokWydania = rokWydania;
        this.isbn = isbn;
        this.liczbaEgzemplarzy = liczbaEgzemplarzy;
        this.cenaZaKsiazke = cenaZaKsiazke;
        this.authors = authors;
    }

    public Book(String tytul, Integer rokWydania, Long isbn, Integer liczbaEgzemplarzy, Integer cenaZaKsiazke) {
        this.tytul = tytul;
        this.rokWydania = rokWydania;
        this.isbn = isbn;
        this.liczbaEgzemplarzy = liczbaEgzemplarzy;
        this.cenaZaKsiazke = cenaZaKsiazke;
    }

    public Book(Integer id_ksiazka, String tytul, Integer rokWydania, Long isbn, Integer liczbaEgzemplarzy, Integer cenaZaKsiazke) {
        this.id_ksiazka = id_ksiazka;
        this.tytul = tytul;
        this.rokWydania = rokWydania;
        this.isbn = isbn;
        this.liczbaEgzemplarzy = liczbaEgzemplarzy;
        this.cenaZaKsiazke = cenaZaKsiazke;
    }

    public Integer getPublisherId() {
        return publisher.getId_publisher();
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }


    public Integer getId_ksiazka() {
        return id_ksiazka;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public Integer getRokWydania() {
        return rokWydania;
    }

    public void setRokWydania(Integer rokWydania) {
        this.rokWydania = rokWydania;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public Integer getLiczbaEgzemplarzy() {
        return liczbaEgzemplarzy;
    }

    public void setLiczbaEgzemplarzy(Integer liczbaEgzemplarzy) {
        this.liczbaEgzemplarzy = liczbaEgzemplarzy;
    }

    public Integer getCenaZaKsiazke() {
        return cenaZaKsiazke;
    }

    public void setCenaZaKsiazke(Integer cenaZaKsiazke) {
        this.cenaZaKsiazke = cenaZaKsiazke;
    }


    @Override
    public String toString() {
        return "Book{" +
                "id_ksiazka=" + id_ksiazka +
                ", tytul='" + tytul + '\'' +
                ", rokWydania=" + rokWydania +
                ", isbn=" + isbn +
                ", liczbaEgzemplarzy=" + liczbaEgzemplarzy +
                ", cenaZaKsiazke=" + cenaZaKsiazke +
                ", publisher=" + publisher +
                ", authors=" + authors +
                ", book_map=" + book_map +
                '}';
    }
}
