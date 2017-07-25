package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealsDAO;
import ru.javawebinar.topjava.dao.MealsDAOImpl;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsServlet extends HttpServlet {

    private static final Logger log = getLogger(MealsServlet.class);
    private static String LIST = "meals.jsp";
    private static String INSERT_OR_EDIT = "save_meal.jsp";

    public MealsDAO mealsDAOImpl = new MealsDAOImpl();
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String forward = LIST;
        String action = request.getParameter("action");

        if (action == null || action.equalsIgnoreCase("list")) {
            log.debug("List action");
            request.setAttribute("meals", mealsDAOImpl.list());
        } else if (action.equalsIgnoreCase("delete")) {
            log.debug("Delete action");
            Long id = Long.parseLong(request.getParameter("id"));
            mealsDAOImpl.delete(id);
            response.sendRedirect("meals");
            return;
        } else if (action.equalsIgnoreCase("edit")) {
            log.debug("Edit action");
            Long id = Long.parseLong(request.getParameter("id"));
            Meal meal = mealsDAOImpl.read(id);
            request.setAttribute("meal", meal);
            forward = INSERT_OR_EDIT;
        } else if (action.equalsIgnoreCase("create")) {
            log.debug("Create action");
            forward = INSERT_OR_EDIT;
        }

        request.setAttribute("formatter", formatter);
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Create or update action");
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Long numericId = null;
        if (id != null && !id.isEmpty()) {
            numericId = Long.parseLong(id);
        }
        Meal meal = new Meal(
                numericId,
                LocalDateTime.parse(request.getParameter("dateTime"), formatter),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );
        if (numericId == null) {
            mealsDAOImpl.create(meal);
        } else {
            mealsDAOImpl.update(meal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST);
        request.setAttribute("meals", mealsDAOImpl.list());
        request.setAttribute("formatter", formatter);
        view.forward(request, response);
    }
}

//2017-07-28 14:00:00
//Супер калорийный обед
//3500