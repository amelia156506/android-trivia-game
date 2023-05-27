<%--
  Created by IntelliJ IDEA.
  Author: Amelia Lee <chichenl@andrew.cmu.edu>
  Date: 2022/11/17
  Time: 4:14 AM
--%>
<%@ page import="ds.Log" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Dashboard</title>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
        </style>
    </head>
    <body>
        <h3>The most frequent request category is <%= request.getAttribute("mostRequestCategory")%> with <%= request.getAttribute("mostRequestCategoryCount")%> times</h3>
        <h3>The most frequent request difficulty is <%= request.getAttribute("mostRequestDifficulty")%> with <%= request.getAttribute("mostRequestDifficultyCount")%> times</h3>
        <h3>The average latency (from receiving request till sending response in Android) is: <%= request.getAttribute("avgLatency")%></h3>
        <table style="width:100%">
            <tr>
                <th>Id</th>
                <th>Response_Code</th>
                <th>Category</th>
                <th>Difficulty</th>
                <th>Question</th>
                <th>Correct_Answer</th>
                <th>Wrong_Answer_1</th>
                <th>Wrong_Answer_2</th>
                <th>Wrong_Answer_3</th>
                <th>Receiving_Time</th>
                <th>Response_Time</th>
                <th>Latency</th>
            </tr>
            <% List<Log> logs = (List<Log>) request.getAttribute("logs"); %>
            <% for(Log log: logs) { %>
            <tr>
                <td><%= log.get_id() %></td>
                <td><%= log.getResponseCode() %></td>
                <td><%= log.getCategory() %></td>
                <td><%= log.getDifficulty() %></td>
                <td><%= log.getQuestion() %></td>
                <td><%= log.getCorrectAns() %></td>
                <td><%= log.getWrongAns1() %></td>
                <td><%= log.getWrongAns2() %></td>
                <td><%= log.getWrongAns3() %></td>
                <td><%= log.getReceivingTime() %></td>
                <td><%= log.getResponseTime() %></td>
                <td><%= log.getLatency() %></td>
            </tr>
            <% } %>
        </table>
    </body>
</html>
