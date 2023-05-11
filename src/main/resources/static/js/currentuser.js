async function getCurrentUserData(url) {
    let response = await fetch(url);
    if (response.ok) {
        showCurrentUserData(await response.json())
    } else {
        alert(`Error, ${response.status}`)
    }
}

function showCurrentUserData(user) {
    document.getElementById('currentUserEMail').innerText = user.email;
    let userRoles = "";
    user.roles.forEach(r => userRoles += " " + r.name.replace("ROLE_", ""));
    document.getElementById('currentUserRoles').innerText = userRoles;
    let row = document.getElementById('currentUserInfoRow');
    row.cells[0].innerText = user.id;
    row.cells[1].innerText = user.firstName;
    row.cells[2].innerText = user.lastName;
    row.cells[3].innerText = user.age;
    row.cells[4].innerText = user.email;
    row.cells[5].innerText = userRoles;
}

document.addEventListener("DOMContentLoaded", () => {
    getCurrentUserData('/api/users/current')
});

