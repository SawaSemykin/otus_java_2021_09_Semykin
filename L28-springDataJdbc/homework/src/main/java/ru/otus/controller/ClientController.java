package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.dto.ClientDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.service.ClientService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/client/list"})
    public String clientsListView(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clientList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        ClientDto dto = new ClientDto("", "", List.of("", "", ""));
        model.addAttribute("client", dto);

        return "clientCreate";
    }

// todo add addPhone/removePhone buttons to manage client's phones from view
//    @GetMapping("/client/addPhone")
//    public String addPhoneView(@ModelAttribute Client client) {
//        client.getPhones().add(new Phone());
//        return "clientCreate";
//    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute("client") ClientDto clientDto) {
        // todo validation
        Address address = new Address(null, clientDto.getAddress());
        Set<Phone> phones = clientDto.getPhones().stream()
                .filter(p -> !p.isEmpty())
                .map(Phone::new)
                .collect(Collectors.toSet());
        Client client = new Client(clientDto.getName(), address, phones);
        clientService.save(client);
        return new RedirectView("/client/list", true);
    }
}
