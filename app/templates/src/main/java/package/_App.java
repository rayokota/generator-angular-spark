package <%= packageName %>;

import static spark.Spark.*;
<% if (entities.length > 0) { %>import <%= packageName %>.models.*;<% } %>
import <%= packageName %>.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.hibernate.Session;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;

public class App {

    public static void main(final String[] args) {
        setPort(8080);
        externalStaticFileLocation("public"); // Static files

        get("/", (request, response) -> {
            response.redirect("/index.html");
            return "";
        });
        /* ----- Generate REST Web Services -----*/
        <% _.each(services, function (service) { %>
        
            <% _.each(services.items, function (item){
                <%if(item.type == 'get'){%>
        get("<%= baseName %>/services/<%= service%>/<%= pluralize(item.name) %>", "application/json", (request, response) -> {
            List objs = <%= _.capitalize(service.name) %>.<%item%>();
            return objs;
        }, new JsonTransformer());


                <%} else if (item.type == 'post'){}

                <%} else if (item.type == 'update'){}
                
                <%} else if (item.type == 'delete'){}
                
                <%} else if (item.type == 'post'){}

                <%}%>
            <%})%>
        <%})%>

        /* ----- Generate Entity Web services -----*/
        <% _.each(entities, function (entity) { %>
        get("<%= baseName %>/<%= pluralize(entity.name) %>", "application/json", (request, response) -> {
            List objs = HibernateUtil.getSession().createCriteria(<%= _.capitalize(entity.name) %>.class).list();
            return objs;
        }, new JsonTransformer());

        get("<%= baseName %>/<%= pluralize(entity.name) %>/:id", "application/json", (request, response) -> {
            long id = Long.parseLong(request.params(":id"));
            <%= _.capitalize(entity.name) %> obj = (<%= _.capitalize(entity.name) %>)HibernateUtil.getSession().get(<%= _.capitalize(entity.name) %>.class, id);
            if (obj == null) halt(404);
            return obj;
        }, new JsonTransformer());

        post("<%= baseName %>/<%= pluralize(entity.name) %>", "application/json", (request, response) -> {
            <%= _.capitalize(entity.name) %> obj = JacksonUtil.readValue(request.body(), <%= _.capitalize(entity.name) %>.class);
            HibernateUtil.getSession().saveOrUpdate(obj);
            response.status(201);
            return obj;
        }, new JsonTransformer());

        put("<%= baseName %>/<%= pluralize(entity.name) %>/:id", "application/json", (request, response) -> {
            long id = Long.parseLong(request.params(":id"));
            <%= _.capitalize(entity.name) %> obj = (<%= _.capitalize(entity.name) %>)HibernateUtil.getSession().get(<%= _.capitalize(entity.name) %>.class, id);
            if (obj == null) halt(404);
            obj = JacksonUtil.readValue(request.body(), <%= _.capitalize(entity.name) %>.class);
            obj = (<%= _.capitalize(entity.name) %>)HibernateUtil.getSession().merge(obj);
            return obj;
        }, new JsonTransformer());

        delete("<%= baseName %>/<%= pluralize(entity.name) %>/:id", (request, response) -> {
            long id = Long.parseLong(request.params(":id"));
            <%= _.capitalize(entity.name) %> obj = (<%= _.capitalize(entity.name) %>)HibernateUtil.getSession().get(<%= _.capitalize(entity.name) %>.class, id);
            if (obj == null) halt(404);
            HibernateUtil.getSession().delete(obj);
            response.status(204);
            return "";
        });
        <% }); %>

        before((request, response) -> {
            HibernateUtil.getSession().beginTransaction();
        });

        after((request, response) -> {
            HibernateUtil.getSession().getTransaction().commit();
            HibernateUtil.closeSession();
        });

        exception(Exception.class, (e, request, response) -> {
            HibernateUtil.getSession().getTransaction().rollback();
            HibernateUtil.closeSession();
            response.status(500);
        });
    }
}
