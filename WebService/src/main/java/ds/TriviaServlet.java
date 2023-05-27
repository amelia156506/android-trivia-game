/**
 * Author: Amelia Lee (chichenl)
 * Last Modified: Nov 17, 2022
 * <p>
 * This program deals with getting the request and dealing with data according to the request
 */
package ds;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "triviaServlet", urlPatterns = {"/getQuestion", "/dashboard"})
public class TriviaServlet extends HttpServlet {
    GetQuestion question = null;
    MongoDB db = null;

    public void init() {
        question = new GetQuestion();
        db = new MongoDB();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // determine which function to call
        String path = request.getServletPath();
        if (path.equals("/getQuestion")) {
            // get the timestamp when receiving request from the Android application
            long receiveTime = System.currentTimeMillis();

            // category and difficulty input would be gotten from Android request
            int category = Integer.parseInt(request.getParameter("category"));
            String difficulty = request.getParameter("difficulty");

            // search for question according to the request inputs
            String responseData = question.search(category, difficulty);

            // get the timestamp when responding to the Android application
            long responseTime = System.currentTimeMillis();

            // sent response data back to Android
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(responseData);
            out.flush();

            // insert log into dashboard
            db.insert(responseData,  receiveTime, responseTime);
        } else if (path.equals("/dashboard")) {
            // to set logs table in jsp, sent in arraylist
            // cite: https://stackoverflow.com/questions/9130605/how-to-display-an-arraylist-in-tabular-format
            request.setAttribute("logs", db.retrieve());
            request.setAttribute("mostRequestCategory", db.mostRequestCategory);
            request.setAttribute("mostRequestCategoryCount", db.mostRequestCategoryCount);
            request.setAttribute("mostRequestDifficulty", db.mostRequestDifficulty);
            request.setAttribute("mostRequestDifficultyCount", db.mostRequestDifficultyCount);
            request.setAttribute("avgLatency", db.avgLatency);

            // forward view to the dashboard.jsp page
            RequestDispatcher view = request.getRequestDispatcher("dashboard.jsp");
            view.forward(request, response);
        }
    }
}