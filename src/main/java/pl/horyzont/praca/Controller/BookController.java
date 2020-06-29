package pl.horyzont.praca.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.horyzont.praca.Entity.Author;
import pl.horyzont.praca.Entity.Book;
import pl.horyzont.praca.Repository.AuthorRepo;
import pl.horyzont.praca.Repository.BookRepo;

import java.util.*;

//import static pl.horyzont.praca.Entity.Book.getKeysByValue;

@Controller
public class BookController {
    private BookRepo bookRepo;
    private AuthorRepo authorRepo;

    @Autowired
    public BookController(BookRepo bookRepo, AuthorRepo authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }


    // Dodaj książkę wraz z autorem z formularza głównego
    @RequestMapping("/dodaj")
    public String dodajemyDane(
            @RequestParam("tytul") String tytul,
            @RequestParam("rokWydania") Integer rokWydania,
            @RequestParam("isbn") Long isbn,
            @RequestParam("liczbaEgzemplarzy") Integer liczbaEgzemplarzy,
            @RequestParam("cenaZaKsiazke") Integer cenaZaKsiazke,
            @RequestParam("imie") String imie,
            @RequestParam("nazwisko") String nazwisko,
            @RequestParam("liczbaPublikacji") Integer liczbaPublikacji,
            @RequestParam("telefonAutora") Integer telefonAutora,
            Model model) throws Exception {
        Book book = new Book(tytul, rokWydania, isbn, liczbaEgzemplarzy, cenaZaKsiazke);
        Author author = new Author(imie, nazwisko, liczbaPublikacji, telefonAutora);

        // dodanie autora do książki i vice versa
        author.addBook(book);

        bookRepo.save(book);
        authorRepo.save(author);

        // zestawienie id autora do id książki
        book.setBook_map(author.getId_autor(), book.getId_ksiazka());
        bookRepo.save(book);

        model.addAttribute("book", book);
        model.addAttribute("author", author);
        System.out.println("\nDane po wstawieniu z formularza głównego:");
        System.out.println(book);
        System.out.println(author);
        System.out.println("Zestawienie id_a do id_b: " + book.getBook_map());

        return "Widok";
    }

    // Formularz dodania autora
    @RequestMapping("/form_autor")
    public String formAutor(
            @RequestParam("id_ksiazka") Integer id_ksiazka, Model model) throws Exception {

        Book book = bookRepo.getOne(id_ksiazka);
        model.addAttribute("book", book);

        return "form_author";
    }

    // Dodanie autora z formularza
    @RequestMapping("/dodaj_autor")
    public String dodajemyDane(
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            @RequestParam("imie") String imie,
            @RequestParam("nazwisko") String nazwisko,
            @RequestParam("liczbaPublikacji") Integer liczbaPublikacji,
            @RequestParam("telefonAutora") Integer telefonAutora,
            Model model) throws Exception {

        Book book = bookRepo.getOne(id_ksiazka);
        Author author = new Author(imie, nazwisko, liczbaPublikacji, telefonAutora);

        author.addBook(book);
        authorRepo.save(author);
        bookRepo.save(book);

        book.setBook_map(author.getId_autor(), book.getId_ksiazka());
        bookRepo.save(book);

        System.out.println("\nDane po wstawieniu autora z formularza:");
        System.out.println(book);
        System.out.println(author);
        System.out.println("Zestawienie id_a do id_b: " + book.getBook_map());

        Set<Integer> authors_id = book.getKeysByValue(id_ksiazka);

        model.addAttribute("book", book);
        model.addAttribute("author", authorRepo.findAllById(authors_id));

        return "Pokaz_autor";
    }

    // Wyświetlenie autorów
    @RequestMapping("/autor")
    public String dodajemydane_2(@RequestParam("id_ksiazka") Integer id_ksiazka, Model model) throws Exception {
        Book book = bookRepo.getOne(id_ksiazka);

        Set<Integer> authors_id = book.getKeysByValue(id_ksiazka);
        System.out.println("\nTakie id maja autorzy " + authors_id + " książki o id "+ book.getId_ksiazka());

        System.out.println("Jakich autorow mamy " + authorRepo.findAllById(authors_id));
        model.addAttribute("author", authorRepo.findAllById(authors_id));
        model.addAttribute("book", bookRepo.getOne(id_ksiazka));

        return "Pokaz_autor";
    }

    // Wyświetlenie zbioru ksiegarni
    @RequestMapping("/pokaz2")
    public String pokaz2(Model model) throws Exception {
        int i = 0;
        for (Book book : bookRepo.findAll()) {
            System.out.println(book);
        }

        model.addAttribute("book", bookRepo.findAll());
        model.addAttribute("author", authorRepo.findAll());

        return "Pokaz2";
    }

    // Kasowanie danej książki ze zbioru
    @RequestMapping(value = "/kasuj")
    public String kasuj(@RequestParam("id_ksiazka") Integer id_ksiazka, Model model) {
        //Author author=authorRepo.getOne(id_autor);
        Book book = bookRepo.getOne(id_ksiazka);

        Set<Integer> authors_id = book.getKeysByValue(id_ksiazka);
        System.out.println("\nTakie id maja autorzy" + authors_id);

        int set_size = authors_id.size();
        int[] tab = new int[set_size];
        System.out.println("Rozmiar tablicy: " + set_size);
        if(set_size !=0) {
            int index = 0;
            for (Integer i : authors_id) {
                tab[index++] = i;
            }

            int a = 0;
            for (Integer j : tab) {
                Author author = authorRepo.getOne(tab[a]);
                System.out.println("Usuwamy autora o id " + tab[a]);//a++
                author.removeBook(book);
                book.getBook_map().remove(tab[a]);
                authorRepo.deleteById(tab[a++]);
                System.out.println("Sprawdzamy mape autor(key)-book(value): " + book.getBook_map());
            }
        }

        bookRepo.deleteById(id_ksiazka);

        model.addAttribute("book", bookRepo.findAll());
        return "Pokaz2";
    }

    // Kasowanie jednego autora
    @RequestMapping(value = "/kasuj_autor")
    public String kasuj_autor(
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            @RequestParam("id_autor") Integer id_autor,
            Model model) {

        Author author=authorRepo.getOne(id_autor);
        Book book = bookRepo.getOne(id_ksiazka);

         System.out.println("\nUsuwamy autora o id " + id_autor +"jego dane"+authorRepo.getOne(id_autor));
           // author.removeBook(book);


         authorRepo.deleteById(id_autor);
        book.getBook_map().remove(id_autor);
        System.out.println("Sprawdzamy mape autor(key)-book(value): " + book.getBook_map());
        bookRepo.save(book);
        /////authorRepo.save(author);

        System.out.println("Zbiory ksiazki"+bookRepo.findAll());
       // System.out.println("Usuwamy autora o id " + id_autor +"jego dane"+authorRepo.getOne(id_autor));

        Set<Integer> authors_id = book.getKeysByValue(id_ksiazka);
        System.out.println("Takie id maja autorzy" + authors_id);
        System.out.println("Sprawdzmy" + bookRepo.getOne(id_ksiazka));

        model.addAttribute("book", bookRepo.getOne(id_ksiazka));
        model.addAttribute("author",authorRepo.findAllById(authors_id));  //
        return "Pokaz_autor";
    }

    // Przekierowanie do aktualizacji danych książki
    @RequestMapping(value="/przekieruj")
    public String przekieruj(
            @RequestParam("id_ksiazka") Integer id_ksiazka, Model model
    )
            throws Exception {
        //System.out.println(bookRepo.findById(id_ksiazka));
        model.addAttribute("book", bookRepo.getOne(id_ksiazka));  //findById(id_ksiazka)
      ////  model.addAttribute("author", authorRepo.getOne(id_ksiazka));
        //System.out.println(authorRepo);

        return "Aktualizuj";
    }

    // Przekierowanie do aktualizacji danych autora
    @RequestMapping("/przekieruj_autor")
    public String przekieruj2(
            @RequestParam("id_autor") Integer id_autor,
            @RequestParam("id_ksiazka") Integer id_ksiazka, Model model
    )
            throws Exception {
        //System.out.println("SDASDASD"+bookRepo.findById(id_ksiazka).get());
        model.addAttribute("book",bookRepo.getOne(id_ksiazka));
        model.addAttribute("author", authorRepo.getOne(id_autor));
        //System.out.println(authorRepo);

        return "Aktualizuj_autor";
    }

    // Aktualizacja danych książki
    @RequestMapping(value="/aktualizuj")
    public String update(
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            @RequestParam("tytul") String tytul,
            @RequestParam("rokWydania") Integer rokWydania,
            @RequestParam("isbn") Long isbn,
            @RequestParam("liczbaEgzemplarzy") Integer liczbaEgzemplarzy,
            @RequestParam("cenaZaKsiazke") Integer cenaZaKsiazke,
            Model model) throws Exception {
       // lepsza krotsza wersja
       Book book1 = bookRepo.getOne(id_ksiazka);
        System.out.println("\nPobrana z bazy ksiazka"+book1);
        book1.setTytul(tytul);
        book1.setRokWydania(rokWydania);
        book1.setIsbn(isbn);
        book1.setLiczbaEgzemplarzy(liczbaEgzemplarzy);
        book1.setCenaZaKsiazke(cenaZaKsiazke);
        System.out.println("Pobrana z bazy ksiazka z updatem"+book1);
        bookRepo.save(book1);
        model.addAttribute("book", book1);

/*

        Book book = new Book(id_ksiazka, tytul, rokWydania, isbn, liczbaEgzemplarzy, cenaZaKsiazke);
        //Author author = new Author(id_ksiazka, imie, nazwisko, liczbaPublikacji, telefonAutora);

        Set <Integer> authors_id = bookRepo.getOne(id_ksiazka).getKeysByValue(id_ksiazka);
        System.out.println("Takie id maja autorzy" + authors_id);

        List<Integer> mainList = new ArrayList<Integer>();
        mainList.addAll(authors_id);

        System.out.println("Jakich autorow mamy" + authorRepo.findAllById(authors_id));
        Set<Author> foo = new HashSet<Author>(authorRepo.findAllById(mainList));
        book.setAuthors(foo);
        System.out.println("Book"+book);

        int set_size = authors_id.size();
        int[] tab = new int[set_size];
        System.out.println("Rozmiar tablicy: " + set_size);
            int index = 0;
            for (Integer i : authors_id) {
                tab[index++] = i;
            }

            int index2 =0;
            for (int a:tab)
            {        book.setBook_map(tab[index2++],id_ksiazka);
            }

        System.out.println("Book222"+book);


        //author.addBook(book);
        bookRepo.save(book);
        //authorRepo.save(author);

        model.addAttribute("book", book);
        //model.addAttribute("author", author);
        // dodajemydane_2(author, model);
       // System.out.println(author);
        System.out.println(book);

 */

        return "Widok";
    }

    // Aktualizacja danych autora
    @RequestMapping("/aktualizuj_autor")
    public String update_author(
            @RequestParam("id_autor") Integer id_autor,
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            @RequestParam("imie") String imie,
            @RequestParam("nazwisko") String nazwisko,
            @RequestParam("liczbaPublikacji") Integer liczbaPublikacji,
            @RequestParam("telefonAutora") Integer telefonAutora,
            Model model) throws Exception {
        Author author = new Author(id_autor, imie, nazwisko, liczbaPublikacji, telefonAutora);
        Book book= bookRepo.getOne(id_ksiazka);
        //book.getAuthors();
        //book.ge
        //System.out.println("Dodanie autorow");
        //book.getBook_map();

        author.addBook(book);
        bookRepo.save(book);
        authorRepo.save(author);

        model.addAttribute("book", book);
        model.addAttribute("author", author);
        // dodajemydane_2(author, model);
        System.out.println("\n"+author);
        System.out.println(book);

        return "Widok";
    }

    // Wyszukaj książek wg. kryterium 'rokWydania'
    @RequestMapping("/wyszukaj")
    public String wyszukaj(@RequestParam("kryterium") Integer kryterium, Model model) {
        model.addAttribute("book", bookRepo.findAllByrokWydania(kryterium));
        return "pokaz2";
    }

    // Stara wersja (nieaktulana) do wyswietlania zbiorów księgarni
    @RequestMapping("/pokaz")
    public String pokaz(Model model) throws Exception {
        int i = 0;
        for (Book book : bookRepo.findAll()) {
            System.out.println("\n"+book);
        }
        model.addAttribute("book", bookRepo.findAll());
        return "Pokaz";
    }

    // Formularz dodaj książkę wraz z autorem
    @RequestMapping("/form")
    String formularz() {
        return "Formularz";
    }

    @RequestMapping("/")
    String zbior() {
        return "Pokaz2";
    }
}
