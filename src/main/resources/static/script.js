const taskList = document.getElementById("task-list");
const createTaskForm = document.getElementById("create-task-form");

getTasks();

// GET request
function getTasks() {
    fetch("/api/tasks")
        .then(res => res.json())
        .then(data => processJsonData(data))
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
    });
};

function processJsonData(jsonData) {
    jsonData.forEach(item => {
        const listItem = document.createElement("div");
        listItem.classList.add("task");
        listItem.setAttribute("id", `task-${item.id}`);
        
        listItem.innerHTML = `<input type="checkbox" class="task-check" id="checkbox-${item.id}" onclick="patchTask(this)">
                              <label for="checkbox-${item.id}" class="task-content" id="content-${item.id}">${item.content}</label>

                              <div class="button-container" id="task-${item.id}-button-container-normal">
                                  <button type="button" class="task-button task-edit" id="edit-${item.id}" onclick="showRename(this)">EDIT</button>
                                  <button type="button" class="task-button task-delete" id="delete-${item.id}" onclick="deleteTask(this)">DELETE</button>
                              </div>
                              
                              <form action="submit" class="update-task-form hide" id="task-${item.id}-update-form">
                                  <input type="text" name="content" placeholder="Edit task..." autocomplete="off">
                  
                                  <div class="button-container" id="task-${item.id}-button-container-edit">
                                      <button type="submit" class="task-button task-submit" id="submit-${item.id}">SAVE</button>
                                      <button type="button" class="task-button task-cancel" id="cancel-${item.id}" onclick="hideRename(this)">CANCEL</button>
                                  </div>
                              </form>`;

        taskList.appendChild(listItem);

        if (item.done === true) {
            document.getElementById(`checkbox-${item.id}`).checked = true;
        }
    });
};

// POST request
createTaskForm.addEventListener('submit', event => {
    event.preventDefault();

    const formData = new FormData(event.target);
    const formObject = Object.fromEntries(formData);

    fetch("/api/tasks", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formObject)
    }).then(() => {
        createTaskForm.reset();
        refreshContent();
    }).catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
});

// PATCH request (done)
function patchTask(checkbox) {
    let taskPatchId = Number(checkbox.id.split('-')[1]);
    const checkboxPatch = document.getElementById(`checkbox-${taskPatchId}`);

    fetch(`/api/tasks/${taskPatchId}`, {
        method: 'PATCH',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "done": checkboxPatch.checked }),
    }).catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
}

// PATCH request (content)
function showRename(button) {
    let taskRenameId = Number(button.id.split('-')[1]);
    const updateForm = document.getElementById(`task-${taskRenameId}-update-form`);
    const checkbox = document.getElementById(`checkbox-${taskRenameId}`)
    const label = document.getElementById(`content-${taskRenameId}`);
    const buttonContainer = document.getElementById(`task-${taskRenameId}-button-container-normal`)

    updateForm.classList.remove("hide");
    
    checkbox.classList.add("hide");
    label.classList.add("hide");
    buttonContainer.classList.add("hide");
}

function hideRename(button) {
    let taskCancelId = Number(button.id.split('-')[1]);
    const updateForm = document.getElementById(`task-${taskCancelId}-update-form`);
    const checkbox = document.getElementById(`checkbox-${taskCancelId}`)
    const label = document.getElementById(`content-${taskCancelId}`);
    const buttonContainer = document.getElementById(`task-${taskCancelId}-button-container-normal`)

    checkbox.classList.remove("hide");
    label.classList.remove("hide");
    buttonContainer.classList.remove("hide");

    updateForm.classList.add("hide");
    updateForm.reset();
}

taskList.addEventListener("submit", event => {
    event.preventDefault();

    let taskRenameId = Number(event.target.id.split('-')[1]);
    
    const formData = new FormData(event.target);
    const formObject = Object.fromEntries(formData);

    fetch(`/api/tasks/${taskRenameId}`, {
        method: 'PATCH',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "content": formObject.content }),
    }).then(res => res.json())
    .then(data => {
        const updatedTask = document.getElementById(`content-${taskRenameId}`);
        updatedTask.innerText = data.content;
        hideRename(document.getElementById(`cancel-${taskRenameId}`));
        event.target.reset();
    }).catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
});

// DELETE request
function deleteTask(button) {
    let taskDeleteId = Number(button.id.split('-')[1]);

    fetch(`/api/tasks/${taskDeleteId}`, {
        method: 'DELETE'
    }).then(() => {
        refreshContent();
    }).catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
};

function refreshContent() {
    taskList.innerHTML = "";
    getTasks();
}