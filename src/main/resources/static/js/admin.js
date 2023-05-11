let roleMap = new Map();
let newUserForm;
let modalForm;
let tableBody;
let modalId;
let modalFirstName;
let modalLastName;
let modalAge;
let modalEMail;
let modalPassword;
let modalRoleSelect;
let modalLabel;
let modalEditButton;
let modalDeleteButton;

const ACTIONS = {
    EDIT: "Edit",
    DELETE: "Delete"
}

async function getRoleMap() {
    let response = await fetch("/api/roles");
    if (response.ok) {
        fillRoleMap(await response.json())
    } else {
        alert(`Error, ${response.status}`)
    }
}

function fillRoleMap(roles) {
    roles.forEach(r => roleMap.set(r.id, r.name));
    fillRoleSelector(document.getElementById('newRoleSelect'));
    fillRoleSelector(document.getElementById('modalRoleSelect'));
}

async function getUserList() {
    let response = await fetch("/api/users");
    if (response.ok) {
        fillUserList(await response.json())
    } else {
        alert(`Error, ${response.status}`)
    }
}

function fillUserList(userList) {
    tableBody.replaceChildren();
    //console.log(userList);
    userList.forEach(u => {
        let userRoles = "";
        u.roles.forEach(r => userRoles += " " + r.name.replace("ROLE_", ""));
        tableBody.innerHTML +=
            `<tr>
                <td>${u.id}</td>
                <td>${u.firstName}</td>
                <td>${u.lastName}</td>
                <td>${u.age}</td>
                <td>${u.email}</td>
                <td>${userRoles}</td>
                <td>
                    <button type="button" class="btn btn-info btn-sm text-white" 
                        onclick="showModal(${u.id}, ACTIONS.EDIT)">
                        Edit
                    </button>
                </td>
                <td>
                    <button type="button" class="btn btn-danger btn-sm" 
                        onclick="showModal(${u.id}, ACTIONS.DELETE)">
                        Delete
                    </button>
                </td>
            </tr>`;
    })
}

async function saveUser(httpMethod, source, path) {
    let userRoles = [];
    for (let el of document.getElementById(source + 'RoleSelect').getElementsByTagName('option')) {
        if (el.selected) userRoles.push({id:el.value})
    }
    let request = {
        method: httpMethod,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify({
            firstName: document.getElementById(source + 'FirstName').value,
            lastName: document.getElementById(source + 'LastName').value,
            age: document.getElementById(source + 'Age').value,
            email: document.getElementById(source + 'EMail').value,
            password: document.getElementById(source + 'Password').value,
            roles: userRoles
        })
    }
    return await fetch('/api/users' + path, request)
}

async function deleteUser(path) {
    let request = {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
    }
    return await fetch('/api/users' + path, request)
}

function fillRoleSelector(selectElement) {
    let element;
    roleMap.forEach((value, key) => {
        element = document.createElement('option');
        element.value = key;
        element.text = value.replace("ROLE_", "");
        selectElement.append(element);
    });
}

function setupNewUserFormButton() {
    var triggerTabList = [].slice.call(document.querySelectorAll('#viewselector-tab a'));
    triggerTabList.forEach(function (triggerEl) {
        var tabTrigger = new bootstrap.Tab(triggerEl);
        triggerEl.addEventListener('click', function (event) {
            event.preventDefault();
            tabTrigger.show();
        })
    })
    var triggerEl = document.querySelector('#viewselector-tab a[href="#viewselector-userlist"]');
    bootstrap.Tab.getInstance(triggerEl).show();
}

function addNewUser(event) {
    event.preventDefault();
    saveUser("POST", 'new', "").then(() => {
        newUserForm.reset();
        getUserList();
        setupNewUserFormButton();
    })
}

async function showModal(userId, action) {
    function resetModal(disableInputs) {
        modalForm.reset();
        modalForm.disabled = disableInputs;
        modalFirstName.disabled = disableInputs;
        modalLastName.disabled = disableInputs;
        modalAge.disabled = disableInputs;
        modalEMail.disabled = disableInputs;
        modalPassword.disabled = disableInputs;
        modalRoleSelect.disabled = disableInputs;
        modalEditButton.hidden = true;
        modalDeleteButton.hidden = true;
    }
    function doShowModal(user) {
        var modal = bootstrap.Modal.getOrCreateInstance(document.getElementById("Modal"));
        resetModal(action === ACTIONS.DELETE)
        let button = document.getElementById("modal" + action + "Button");
        button.hidden = false;
        button.disabled = true;
        modalLabel.innerText = action + " " + "user";
        modalId.value = user.id;
        modalFirstName.value = user.firstName;
        modalLastName.value = user.lastName;
        modalAge.value = user.age;
        modalEMail.value = user.email;
        Array.from(modalRoleSelect.getElementsByTagName("option")).forEach(el => {
            user.roles.forEach(r => {
                if (r.id == el.value) { el.selected = true }
            })
        });
        button.disabled = false;
        modal.show();
    }
    let response = await fetch("/api/users/" + userId);
    if (response.ok) {
        await response.json().then(user => {
            doShowModal(user)
        })
    } else {
        alert(`Error, ${response.status}`)
    }
}

function processUser(action) {
    function hideModal() {
        modalForm.reset();
        getUserList();
        modal.hide();
    }
    var modal = bootstrap.Modal.getInstance(document.getElementById('Modal'))
    switch(action) {
        case ACTIONS.EDIT:
            saveUser("PUT", 'modal', `/${modalId.value}`).then(() => { hideModal() })
            break
        case ACTIONS.DELETE:
            deleteUser(`/${modalId.value}`).then(() => { hideModal() })
    }
}

document.addEventListener("DOMContentLoaded", () => {
    newUserForm = document.getElementById('addNewUserForm');
    modalForm = document.getElementById("modalForm");
    tableBody = document.getElementById('userListTableBody');
    modalId = document.getElementById("modalId");
    modalFirstName = document.getElementById("modalFirstName");
    modalLastName = document.getElementById("modalLastName");
    modalAge = document.getElementById("modalAge");
    modalEMail = document.getElementById("modalEMail");
    modalPassword = document.getElementById("modalPassword");
    modalRoleSelect = document.getElementById("modalRoleSelect");
    modalLabel = document.getElementById("modalLabel");
    modalEditButton = document.getElementById("modalEditButton");
    modalDeleteButton = document.getElementById("modalDeleteButton");
    modalEditButton.innerText = ACTIONS.EDIT;
    modalDeleteButton.innerText = ACTIONS.DELETE;
    newUserForm.addEventListener('submit', e => addNewUser(e));
    modalForm.addEventListener('submit', e => { e.preventDefault() });
    getRoleMap();
    getUserList();
});