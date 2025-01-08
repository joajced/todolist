const taskCreateForm = document.getElementById("task-create-form");
const taskList = document.getElementById("task-list");

// Load all tasks
// Needs to be replace with Project View
getTasks();


function refreshContent() {

    taskList.innerHTML = "";
    getTasks();
}

/*
-------------------------------------------
                   GET
-------------------------------------------
*/

function getTasks() {

    fetch("http://localhost:8080/api/tasks")
        .then(res => res.json())
        .then(data => processJsonData(data))
        .catch(error => {
            console.error("GET request failed: ", error);
        
    });
};

function processJsonData(data) {

    data.forEach(jsonTask => {

        // New task
        const task = document.createElement("div");
        task.classList.add("task");
        task.setAttribute("id", `task-${jsonTask.id}`);
        
        // Fill DIV
        task.innerHTML = `<div class="task-normal-view-container" id="task-normal-view-container-${jsonTask.id}">

                              <input type="checkbox" class="task-checkbox" id="task-checkbox-${jsonTask.id}" onclick="patchTask(this)">
                              <label for="task-checkbox-${jsonTask.id}" class="task-content" id="task-content-${jsonTask.id}">${jsonTask.content}</label>

                              <div class="task-button-container" id="task-button-container-${jsonTask.id}">

                                  <button type="button" class="task-button" id="task-button-edit-${jsonTask.id}" onclick='switchView(this, "update")'>EDIT</button>
                                  <button type="button" class="task-button" id="task-button-delete-${jsonTask.id}" onclick="deleteTask(this)">DELETE</button>

                              </div>

                          </div>
                              
                          <form action="submit" class="task-update-form" id="task-update-form-${jsonTask.id}">

                              <input id="task-update-form-input-${jsonTask.id}" type="text" name="content" placeholder="Edit task..." autocomplete="off" required>
                  
                              <div class="task-button-container" id="task-button-container-${jsonTask.id}-update">

                                  <button type="submit" class="task-button" id="task-button-submit-${jsonTask.id}">SAVE</button>
                                  <button type="button" class="task-button" id="task-button-cancel-${jsonTask.id}" onclick='switchView(this, "normal")'>CANCEL</button>

                              </div>

                          </form>`;
        
        // Add to task list
        taskList.appendChild(task);

        // Display checkmark correctly
        document.getElementById(`task-checkbox-${jsonTask.id}`).checked = jsonTask.done;
    });
};


/*
-------------------------------------------
                  POST
-------------------------------------------
*/

taskCreateForm.addEventListener('submit', event => {

    event.preventDefault();

    const formData = new FormData(event.target);
    const formObject = Object.fromEntries(formData);

    fetch("http://localhost:8080/api/tasks", {

        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formObject)

    }).then(() => {

        taskCreateForm.reset();
        refreshContent();

    }).catch(error => {

        console.error("POST request failed: ", error);

    });
});


/*
-------------------------------------------
                  PATCH
-------------------------------------------
*/

function patchTask(checkbox) {

    // Example: task-checkbox-1
    let taskId = Number(checkbox.id.split('-')[2]);
    const thisCheckbox = document.getElementById(`task-checkbox-${taskId}`);

    fetch(`http://localhost:8080/api/tasks/${taskId}`, {

        method: 'PATCH',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "done": thisCheckbox.checked }),

    }).catch(error => {

        console.error("PATCH request failed: ", error);

    });
}

function switchView(button, direction) {

    // Example: task-button-cancel-1
    let taskId = Number(button.id.split('-')[3]);

    const normalView = document.getElementById(`task-normal-view-container-${taskId}`);
    const updateForm = document.getElementById(`task-update-form-${taskId}`);

    if (direction == "update") {

        const currentContent = document.getElementById(`task-content-${taskId}`).textContent;
        const updateFormInput = document.getElementById(`task-update-form-input-${taskId}`);

        updateFormInput.value = currentContent;
        normalView.style.display = "none";
        updateForm.style.display = "flex";

    } else {

        normalView.style.display = "flex";
        updateForm.style.display = "none";
        updateForm.reset();
    }
}

taskList.addEventListener("submit", event => {

    event.preventDefault();

    // task-button-submit-1
    let taskId = Number(event.target.id.split('-')[3]);
    
    const formData = new FormData(event.target);
    const formObject = Object.fromEntries(formData);

    fetch(`http://localhost:8080/api/tasks/${taskId}`, {

        method: 'PATCH',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "content": formObject.content }),

    }).then(res => res.json())
    .then(data => {

        const taskContent = document.getElementById(`task-content-${taskId}`);
        taskContent.innerText = data.content;

        switchView(document.getElementById(`task-button-cancel-${taskId}`, "normal"));
        event.target.reset();

    }).catch(error => {

        console.error("PATCH request failed: ", error);

    });
});


/*
-------------------------------------------
                 DELETE
-------------------------------------------
*/

function deleteTask(button) {
    
    // Example: task-button-delete-1
    let taskId = Number(button.id.split('-')[3]);

    fetch(`http://localhost:8080/api/tasks/${taskId}`, {

        method: 'DELETE'

    }).then(() => {

        refreshContent();

    }).catch(error => {

        console.error("DELETE request failed: ", error);

    });
};