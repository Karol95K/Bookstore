package pl.horyzont.praca.Entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id_ksiazka;

    @Pattern(regexp = "^[\\w\\W]{2,300}$", message = "Podaj od 2 do 300 znaków.")
    private String tytul;

   // @Pattern(regexp = "^[1-9][0-9]{3}$", message = "Podaj czterocyfrowy rok wydania")
    @Min(value = 1000, message = "Podaj realny rok wydania") @Max(value = 2100, message = "Podaj realny rok wydania")
    private Integer rokWydania;

    @Pattern(regexp = "^[0-9]{13}$", message = "Podaj trzynastocyfrowy numer ISBN")
    private String isbn;

    @Pattern(regexp = "^[1-9][0-9]{1,3}$", message = "Podaj max. czterocyfrową liczbę")
    private String liczbaEgzemplarzy;

    @Pattern(regexp = "^[1-9][0-9]{0,3}\\.[0-9]{2}$", message = "Podaj cenę książki w formacie 'X.YY' np. 24.99 [zł]")
    private String cenaZaKsiazke;

    @ManyToOne
    @JoinColumn (name = "id_publisher")
    private Publisher publisher;

    @ManyToMany (mappedBy = "books")
    private Set<Author> authors = new HashSet<>();

    @ElementCollection()
    @CollectionTable(name="Map_books_authors", joinColumns=@JoinColumn(name="Map_id"))
    @MapKeyColumn (name = "Key_author_Id")
    @Column (name = "Value_book_Id")
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

    public Book(String tytul, Integer rokWydania, String isbn, String liczbaEgzemplarzy, String cenaZaKsiazke, Set<Author> authors) {
        this.tytul = tytul;
        this.rokWydania = rokWydania;
        this.isbn = isbn;
        this.liczbaEgzemplarzy = liczbaEgzemplarzy;
        this.cenaZaKsiazke = cenaZaKsiazke;
        this.authors = authors;
    }

    public Book(String tytul, Integer rokWydania, String isbn, String liczbaEgzemplarzy, String cenaZaKsiazke) {
        this.tytul = tytul;
        this.rokWydania = rokWydania;
        this.isbn = isbn;
        this.liczbaEgzemplarzy = liczbaEgzemplarzy;
        this.cenaZaKsiazke = cenaZaKsiazke;
    }

    public Book(Integer id_ksiazka, String tytul, Integer rokWydania, String isbn, String liczbaEgzemplarzy, String cenaZaKsiazke) {
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLiczbaEgzemplarzy() {
        return liczbaEgzemplarzy;
    }

    public void setLiczbaEgzemplarzy(String liczbaEgzemplarzy) {
        this.liczbaEgzemplarzy = liczbaEgzemplarzy;
    }

    public String getCenaZaKsiazke() {
        return cenaZaKsiazke;
    }

    public void setCenaZaKsiazke(String cenaZaKsiazke) {
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
