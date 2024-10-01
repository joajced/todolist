const taskList = document.getElementById("task-list");
const createTaskForm = document.getElementById("create-task-form");

getTasks();

// GET request
function getTasks() {
    fetch("http://localhost:8080/api/tasks")
        .then((res) => res.json())
        .then((data) => processJsonData(data));
};

function processJsonData(jsonData) {
    jsonData.forEach(item => {
        const listItem = document.createElement("div");
        listItem.classList.add("task");
        listItem.setAttribute("id", `task-${item.id}`);
        
        listItem.innerHTML = `<input type="checkbox" class="task-check" id="checkbox-${item.id}">
                              <label for="checkbox-${item.id}" class="task-content">${item.content}</label>

                              <div class="button-container">
                                  <button type="button" class="task-button task-edit" id="edit-${item.id}">EDIT</button>
                                  <button type="button" class="task-button task-delete" id="delete-${item.id}" onclick="deleteTask(this)">DELETE</button>
                              </div>`;
        
        taskList.appendChild(listItem);
    });
};

// POST request
createTaskForm.addEventListener('submit', event => {
    event.preventDefault();

    const formData = new FormData(event.target);
    const formObject = Object.fromEntries(formData);

    fetch("http://localhost:8080/api/tasks", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formObject)
    }).then(res => res.json());

    createTaskForm.reset();
    refreshContent();
});

// DELETE request
function deleteTask(button) {
    let taskDeleteId = Number(button.id.split('-')[1]);

    fetch(`http://localhost:8080/api/tasks/${taskDeleteId}`, {
        method: 'DELETE'
    });
    
    refreshContent();
};

function refreshContent() {
    taskList.innerHTML = "";
    setTimeout(() => getTasks(), 100);
};