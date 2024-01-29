<%@ taglib prefix="s" uri="/struts-tags" %>
    <%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="utf-8">
            <title>CZ3002 Login Website</title>
            <script src="https://code.jquery.com/jquery-3.4.1.min.js" integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous"></script>
            <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.0/dist/jquery.validate.min.js"></script>

            <style type="text/css">
                .errorMessage {
                    font-weight: bold;
                    color: #BE2239;
                    list-style: none;
                    padding: 0;
                }
                
                html,
                body {
                    height: 100%;
                    background-color: slategray;
                    color: white;
                    font-family: Arial, Helvetica, sans-serif;
                }
                
                html {
                    display: table;
                    margin: auto;
                }
                
                body {
                    display: table-cell;
                    vertical-align: middle;
                }
                
                .input-text {
                    padding: 15px;
                }
                
                .login-button {
                    background-color: #333;
                    ;
                    /* Green */
                    border: none;
                    color: white;
                    padding: 15px 32px;
                    text-align: center;
                    text-decoration: none;
                    display: inline-block;
                    font-size: 16px;
                    width: 100%;
                }
            </style>

        </head>

        <body>
            <div style="text-align: center">
                <h1>Struts 2 - Login</h1>
                <s:form action="loginSubmit" method="post" theme="css_xhtml">
                    <label for="username">Username:</label>
                    <s:textfield name="userBean.username" size="30" class="input-text" />
                    <br>
                    <label for="password">Password:</label>
                    <s:password name="userBean.password" size="30" class="input-text" />
                    <br>
                    <s:submit value="LOGIN" class="login-button" />
                </s:form>
                <br>
                <s:actionerror/>
            </div>
        </body>

        </html>