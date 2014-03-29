package fr.sewatech.mqttra.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * @author Alexis Hassler
 */
@WebServlet(urlPatterns = "/mqtt/*")
public class MqttServlet extends HttpServlet {

}
