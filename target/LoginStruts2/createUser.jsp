<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create User</title>
</head>
<body>
    <h2>Create User</h2>
    <form action="createUser" method="post">
        <!-- Your form fields for creating a new user -->
        <label for="username">Username:</label>
        <input type="text" name="userBean.username" required>
        <br>
        <label for="password">Password:</label>
        <input type="password" name="userBean.password" required>
        <br>
        <input type="submit" value="Create User">
    </form>
</body>
</html>
