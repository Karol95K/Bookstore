package pl.horyzont.praca.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.horyzont.praca.Entity.Author;
import pl.horyzont.praca.Entity.Book;
import pl.horyzont.praca.Repository.AuthorRepo;
import pl.horyzont.praca.Repository.BookRepo;

import java.util.Set;

@Controller
public class AuthorController {
    AuthorRepo authorRepo;
    BookRepo bookRepo;

    @Autowired
    AuthorController(AuthorRepo authorRepo, BookRepo bookRepo) {
        this.authorRepo = authorRepo;
        this.bookRepo = bookRepo;
    }

    // Przejscie do formularza dodania autora
    @RequestMapping("/form_autor")
    public String formAutor(
            @RequestParam("id_ksiazka") Integer id_ksiazka, Model model) throws Exception {

        Book book = bookRepo.getOne(id_ksiazka);
        model.addAttribute("book", book);

        return "/author/form_author";
    }

    // Dodanie autora z formularza
    @RequestMapping(value = "/dodaj_autor")
    public String dodajAutor(
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

        return "/author/Pokaz_autor";
    }

    // Wyświetlenie autorów
    @RequestMapping("/autor")
    public String pokazAutor (@RequestParam("id_ksiazka") Integer id_ksiazka, Model model) throws Exception {
        Book book = bookRepo.getOne(id_ksiazka);

        Set<Integer> authors_id = book.getKeysByValue(id_ksiazka);
        System.out.println("\nTakie id maja autorzy " + authors_id + " książki o id "+ book.getId_ksiazka());

        System.out.println("Jakich autorow mamy " + authorRepo.findAllById(authors_id));
        model.addAttribute("author", authorRepo.findAllById(authors_id));
        model.addAttribute("book", bookRepo.getOne(id_ksiazka));

        return "/author/Pokaz_autor";
    }

    // Przekierowanie do aktualizacji danych autora
    @RequestMapping("/przekieruj_autor")
    public String przekierujAutor(
            @RequestParam("id_autor") Integer id_autor,
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            Model model) throws Exception {

        model.addAttribute("book",bookRepo.getOne(id_ksiazka));
        model.addAttribute("author", authorRepo.getOne(id_autor));

        return "/author/Aktualizuj_autor";
    }

    // Aktualizacja danych autora
    @RequestMapping("/aktualizuj_autor")
    public String updateAuthor(
            @RequestParam("id_autor") Integer id_autor,
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            @RequestParam("imie") String imie,
            @RequestParam("nazwisko") String nazwisko,
            @RequestParam("liczbaPublikacji") Integer liczbaPublikacji,
            @RequestParam("telefonAutora") Integer telefonAutora,
            Model model) throws Exception {
        Author author = new Author(id_autor, imie, nazwisko, liczbaPublikacji, telefonAutora);
        Book book= bookRepo.getOne(id_ksiazka);

        author.addBook(book);
        bookRepo.save(book);
        authorRepo.save(author);

        model.addAttribute("book", book);
        model.addAttribute("author", author);
        System.out.println("\n"+author);
        System.out.println(book);

        return "Widok";
    }

    // Kasowanie jednego autora
    @RequestMapping(value = "/kasuj_autor")
    public String kasuj_autor(
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            @RequestParam("id_autor") Integer id_autor,
            Model model) {

        Book book = bookRepo.getOne(id_ksiazka);
        System.out.println("\nUsuwamy autora o id " + id_autor +" jego dane "+authorRepo.getOne(id_autor));

        authorRepo.deleteById(id_autor);
        book.getBook_map().remove(id_autor);
        System.out.println("Sprawdzamy mape autor(key)-book(value): " + book.getBook_map());
        bookRepo.save(book);
        System.out.println("Zbiory ksiazki "+bookRepo.findAll());

        Set<Integer> authors_id = book.getKeysByValue(id_ksiazka);
        System.out.println("Takie id maja autorzy " + authors_id);
        System.out.println("Sprawdzmy " + bookRepo.getOne(id_ksiazka));

        model.addAttribute("book", bookRepo.getOne(id_ksiazka));
        model.addAttribute("author",authorRepo.findAllById(authors_id));  //
        return "/author/Pokaz_autor";
    }
}
