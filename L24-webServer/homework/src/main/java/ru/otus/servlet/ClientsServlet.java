package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.service.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Aleksandr Semykin
 */
@MultipartConfig
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";
    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;
    private final Gson gson;

    public ClientsServlet(TemplateProcessor templateProcessor,
                          DBServiceClient dbServiceClient,
                          Gson gson) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String addressStreet = req.getParameter("address");
        String[] phoneNumbers = req.getParameter("phones").split(",");

        Address address = new Address(addressStreet);
        List<Phone> phoneList = Arrays.stream(phoneNumbers)
                .map(Phone::new)
                .collect(Collectors.toList());
        Client client = new Client(null, name, address, phoneList);

        dbServiceClient.saveClient(client);

        resp.sendRedirect("/clients");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        if (req.getPathInfo() == null) {
            Map<String, Object> paramsMap = new HashMap<>();
            List<Client> allClients = dbServiceClient.findAll();
            paramsMap.put(TEMPLATE_ATTR_CLIENTS, allClients);

            response.setContentType("text/html");
            response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
        } else {
            Client client = dbServiceClient.getClient(extractIdFromRequest(req)).orElse(null);

            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(client));
        }
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }
}
