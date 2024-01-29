<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
    <style>
        /* Hide the userList by default */
        #userList {
            display: none;
        }
    </style>

<script>
    function showUserList() {
        var userList = document.getElementById("userList");
        userList.style.display = "block";
    }

</script>

</head>

<body>
    <h2>Welcome to our application!</h2>

    <button onclick="showUserList()">Get All Users</button>

    <div id="userList">
        <c:if test="${not empty userList}">
            <h3>List of Users:</h3>
            <table border="1">
                <tr>
                    <th>Username</th>
                    <th>Password</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.password}</td>
                        <td>
                            <button onclick="editUser('${user.username}')">Edit</button>
                            <button onclick="deleteUser('${user.username}')">Delete</button>
                        </td>
                    </tr>
                </c:forEach>
                <c:forEach var="user" items="${userList}">
    <tr>
        <td>${user.username}</td>
        <td>${user.password}</td>
        <td>
            <button onclick="showEditFields('${user.username}', '${user.password}')">Edit</button>
            <button onclick="deleteUser('${user.username}')">Delete</button>
            <!-- Edit fields -->
            <div id="editFields_${user.username}" style="display: none;">
                New Username: <input type="text" id="newUsername_${user.username}"><br>
                New Password: <input type="password" id="newPassword_${user.username}"><br>
                <button onclick="saveEdit('${user.username}')">Save</button>
            </div>
        </td>
    </tr>
</c:forEach>

            </table>
        </c:if>
    </div>

 