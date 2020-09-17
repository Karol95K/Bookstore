package pl.horyzont.praca.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.horyzont.praca.Entity.Author;
import pl.horyzont.praca.Entity.Book;
import pl.horyzont.praca.Entity.Publisher;
import pl.horyzont.praca.Repository.AuthorRepo;
import pl.horyzont.praca.Repository.BookRepo;
import pl.horyzont.praca.Repository.PublisherRepo;

import javax.validation.Valid;
import java.util.*;

//import static pl.horyzont.praca.Entity.Book.getKeysByValue;

@Controller
public class BookController {
    private BookRepo bookRepo;
    private AuthorRepo authorRepo;
    private PublisherRepo publisherRepo;

    @Autowired
    public BookController(BookRepo bookRepo, AuthorRepo authorRepo, PublisherRepo publisherRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.publisherRepo = publisherRepo;
    }


    // Dodaj książkę wraz z autorem z formularza głównego
    @RequestMapping(value = "/dodaj", method = RequestMethod.POST)
    public String dodajemyDane(
            @Valid Book book, BindingResult bindingResultBook,
            @Valid Author author, BindingResult bindingResultAuthor,
            @Valid Publisher publisher, BindingResult bindingResultPublisher,
            Model model) throws Exception {

        boolean error1 = bindingResultBook.hasErrors();
        boolean error2 = bindingResultAuthor.hasErrors();
        boolean error3 = bindingResultPublisher.hasErrors();
        System.out.println("/nBłąd book: " + error1 + " Błąd author " + error2 + " Błąd publisher " + error3);

        if (bindingResultBook.hasErrors() | bindingResultAuthor.hasErrors() | bindingResultPublisher.hasErrors()) {
            return "/book/Error";
        } else {
            publisher.insertBookToList(book);
            publisherRepo.save(publisher);
            book.setPublisher(publisher);
            //System.out.println("\nPublisher" + publisher);

            System.out.println("Name " + book.getPublisherId());


            // dodanie autora do książki i vice versa
            author.addBook(book);
            System.out.println("11111");
            authorRepo.save(author); //do usuniecia -> o to chodzi jednak
            bookRepo.save(book);
            // authorRepo.save(author); //do usuniecia raczej
            System.out.println("222222");


            // zestawienie id autora do id książki
            book.setBook_map(author.getId_autor(), book.getId_ksiazka());


            bookRepo.save(book);


            model.addAttribute("book", book);
            model.addAttribute("author", author);
            model.addAttribute("publisher", publisher);
            System.out.println("\nDane po wstawieniu z formularza głównego:");
            System.out.println(book);
            System.out.println(author);
            System.out.println("Zestawienie id_a do id_b: " + book.getBook_map());

            System.out.println("\nPublisher" + publisher); //do usuniecia
            System.out.println("\nLista book" + publisher.getBookList()); //do usuniecia
            System.out.println("\nPobranie ksiazki po id" + bookRepo.getOne(book.getId_ksiazka())); //do usuniecia

            model.addAttribute("book", bookRepo.findAll()); //gdy Widok return to usun
            //model.addAttribute("error", error1);
            return "book/Pokaz2";   ///book/Pokaz2  --tylko 1 wiersz POPRAW   Widok
        }
    }

    // Wyświetlenie zbioru ksiegarni
    @RequestMapping("/pokaz2")
    public String pokaz2(Model model) throws Exception {

        for (Book book : bookRepo.findAll()) {
            System.out.println(book);
        }

        model.addAttribute("book", bookRepo.findAll());
        model.addAttribute("author", authorRepo.findAll());
        model.addAttribute("publisher", publisherRepo.findAll());

        return "/book/Pokaz2";
    }

    // Przekierowanie do aktualizacji danych książki
    @RequestMapping(value = "/przekieruj")
    public String przekieruj(
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            Model model) throws Exception {

        System.out.println("Pirzekierowany" + bookRepo.getOne(id_ksiazka));

        model.addAttribute("book", bookRepo.getOne(id_ksiazka));  //findById(id_ksiazka)

        return "/book/Aktualizuj";
    }

    // Aktualizacja danych książki
    @RequestMapping(value="/aktualizuj")
    public String update(
            @Valid Book book1, BindingResult bindingResultBook,
            //  @ModelAttribute Book book,
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            @RequestParam("tytul") String tytul,
            @RequestParam("rokWydania") Integer rokWydania,
            @RequestParam("isbn") String isbn,
            @RequestParam("liczbaEgzemplarzy") String liczbaEgzemplarzy,
            @RequestParam("cenaZaKsiazke") String cenaZaKsiazke,
            Model model) throws Exception {

        System.out.println("\n Byle co check it ####");
        // Integer id_ksiazka = book1.getId_ksiazka();

        boolean error = bindingResultBook.hasErrors();
        System.out.println("\n Bookkk : "+ bookRepo.findById(id_ksiazka));
        System.out.println(" Błąd book: "+error);
        System.out.println(" Idik ksiazki: "+id_ksiazka);
        //System.out.println("/nId ksiązki: "+idik);

        Book book2 = bookRepo.getOne(id_ksiazka);

        if (bindingResultBook.hasErrors() ){
            //  bookRepo.save(book1);

            book2.setTytul(tytul);
            book2.setRokWydania(rokWydania);
            book2.setIsbn(isbn);
            book2.setLiczbaEgzemplarzy(liczbaEgzemplarzy);
            book2.setCenaZaKsiazke(cenaZaKsiazke);


            if (id_ksiazka == null){
                Integer tym = book1.getId_ksiazka();
                System.out.println("Sprawdzmy "+tym);
            }


            System.out.println("Booking"+ book2);

            //   bookRepo.save(book2);
            model.addAttribute("book", book2);
            return "/book/Aktualizuj";
        }
        else {
            //  Book bookk = bookRepo.getOne(idik);
            System.out.println("\nPobrana z bazy ksiazka " + book1);


            book2.setTytul(tytul);
            book2.setRokWydania(rokWydania);
            book2.setIsbn(isbn);
            book2.setLiczbaEgzemplarzy(liczbaEgzemplarzy);
            book2.setCenaZaKsiazke(cenaZaKsiazke);
            System.out.println("Pobrana z bazy ksiazka z updatem " + book2);

            bookRepo.save(book2);

            model.addAttribute("book", bookRepo.findAll());

            return "/book/Pokaz2";  //"Widok"
        }
    }

    // Kasowanie danej książki ze zbioru
    @RequestMapping(value = "/kasuj")
    public String kasuj(@RequestParam("id_ksiazka") Integer id_ksiazka, Model model) {
        //Author author=authorRepo.getOne(id_autor);
        Book book = bookRepo.getOne(id_ksiazka);
        Set<Integer> authors_id = book.getKeysByValue(id_ksiazka);
        System.out.println("\nTakie id maja autorzy" + authors_id);

        // wstawianie id_autorow do tablicy
        int set_size = authors_id.size();
        int[] tab = new int[set_size];
        System.out.println("Rozmiar tablicy: " + set_size);
        if (set_size != 0) {
            int index = 0;
            for (Integer i : authors_id) {
                tab[index++] = i;
            }

            // usuwanie wszystkich autorow z ksiazki
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


        Integer id_publisher = book.getPublisherId();

        //usuwanie ksiazki
        bookRepo.deleteById(id_ksiazka);
        // usuniecie wydawcy
        publisherRepo.deleteById(id_publisher);

        model.addAttribute("book", bookRepo.findAll());
        return "/book/Pokaz2";
    }

    // Wyszukaj książek wg. kryterium 'rokWydania'
    @RequestMapping("/wyszukaj")
    public String wyszukaj(@RequestParam("kryterium") Integer kryterium, Model model) {
       // int rok = Integer.parseInt(kryterium);
       // System.out.println("\n Look : " + rok);
        model.addAttribute("book", bookRepo.findAllByrokWydania(kryterium));
        return "/book/Pokaz2";
    }

    // Formularz dodaj książkę wraz z autorem
    @RequestMapping("/form")
    String formularz() {
        return "/book/Formularz";
    }

    @RequestMapping("/")
    String zbior() {
        return "/book/Pokaz2";
    }

    @RequestMapping("/onas")
    String oNas() {
        return "/ONas";
    }
}
