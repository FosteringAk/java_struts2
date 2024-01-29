<tr>
    <td>${user.username}</td>
    <td>${user.password}</td>
    <td>
        <button onclick="showEditFields('${user.username}')">Edit</button>
        <button onclick="deleteUser('${user.username}')">Delete</button>
        <!-- Edit fields -->
        <div id="editFields_${user.username}" style="display: none;">
            New Username: <input type="text" id="newUsername_${user.username}"><br>
            New Password: <input type="password" id="newPassword_${user.username}"><br>
            <button onclick="saveEdit('${user.username}')">Save</button>
        </div>
    </td>
</tr>
