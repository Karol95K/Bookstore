package pl.horyzont.praca.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.horyzont.praca.Entity.Book;
import pl.horyzont.praca.Entity.Publisher;
import pl.horyzont.praca.Repository.AuthorRepo;
import pl.horyzont.praca.Repository.BookRepo;
import pl.horyzont.praca.Repository.PublisherRepo;

@Controller
public class PublisherController {

    private BookRepo bookRepo;
    private AuthorRepo authorRepo;
    private PublisherRepo publisherRepo;

    @Autowired
    public PublisherController(BookRepo bookRepo, AuthorRepo authorRepo, PublisherRepo publisherRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.publisherRepo = publisherRepo;
    }

    @GetMapping("/wydawca")
    public String pokazWydawce(
            @RequestParam("id_ksiazka") Integer id_ksiazka,
            Model model) throws Exception {
        Book book = bookRepo.getOne(id_ksiazka);
        Integer id_publisher = book.getPublisherId();
        System.out.println("Nasz wydawca: "+ publisherRepo.getOne(id_publisher));

        model.addAttribute("publisher", publisherRepo.getOne(id_publisher));
        model.addAttribute("book", bookRepo.getOne(id_ksiazka));

        return "/publisher/Pokaz_wydawca";
    }

    @GetMapping ("/przekieruj_wydawca")
    public String przekierujWydawca (
            @RequestParam ("id_wydawca") Integer id_wydawca,
            @RequestParam ("id_ksiazka") Integer id_ksiazka,
            Model model)throws Exception{

        model.addAttribute("publisher", publisherRepo.getOne(id_wydawca));
        model.addAttribute("book", bookRepo.getOne(id_ksiazka));

        return "/publisher/Aktualizuj_wydawca";
    }

    @GetMapping ("/aktualizuj_wydawca")
    public String updatePublisher (
            @ModelAttribute Publisher publisher,
            @RequestParam ("id_ksiazka") Integer id_ksiazka,
        Model model) throws Exception{

        Book book =bookRepo.getOne(id_ksiazka);
        publisher.insertBookToList(book);
        publisherRepo.save(publisher);
        book.setPublisher(publisher);
        bookRepo.save(book);

        model.addAttribute("publisher",publisher);
        model.addAttribute("book", book);

        return "/publisher/Pokaz_wydawca";
    }
}
