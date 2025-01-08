const taskSectionTitle = document.getElementById("task-section-title");
const spanCompleted = document.getElementById("task-section-subtitle-completed");
const spanTotal = document.getElementById("task-section-subtitle-total");
const taskCreateForm = document.getElementById("task-create-form");
const taskList = document.getElementById("task-list");

const projectSection = document.getElementById("project-section");
const projectListDefault = document.getElementById("project-list-default");
const projectList = document.getElementById("project-list");
const projectCreateForm = document.getElementById("project-create-form");

// Default project
let currentProject = 1;

getTasksFromProject(currentProject);
getProjects();


/*
-------------------------------------------
                   GET
-------------------------------------------
*/

function getTasksFromProject(projectId) {

    fetch(`http://localhost:8080/api/projects/${projectId}`)
        .then(res => res.json())
        .then(project => {

            taskSectionTitle.textContent = project.name;
            spanCompleted.textContent = project.completed;
            spanTotal.textContent = project.total;

        }).catch(error => {

            console.error("GET project.name failed: ", error)

    });

    fetch(`http://localhost:8080/api/projects/${projectId}/tasks`)
        .then(res => res.json())
        .then(data => processJsonTask(data))
        .catch(error => {

            console.error("GET request failed: ", error);

    });
};

function processJsonTask(data) {

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

function getProjects() {

    fetch("http://localhost:8080/api/projects")
        .then(res => res.json())
        .then(data => processJsonProject(data))
        .catch(error => {
            console.error("GET request failed: ", error);
        
    });
};

function processJsonProject(data) {

    data.forEach(jsonProject => {

        // New project
        const project = document.createElement("div");
        project.classList.add("project");
        project.setAttribute("id", `project-${jsonProject.id}`);
        
        // Fill DIV
        project.innerHTML = `<div class="project-normal-view-container" id="project-normal-view-container-${jsonProject.id}">

                                <p class="project-name" id="project-name-${jsonProject.id}">${jsonProject.name}</p>

                                <div class="project-button-container" id="project-button-container-${jsonProject.id}">

                                    <button type="button" class="project-button" id="project-button-edit-${jsonProject.id}" onclick='switchView(this, "update")'>RENAME</button>
                                    <button type="button" class="project-button" id="project-button-delete-${jsonProject.id}" onclick="deleteProject(this)">DELETE</button>

                                </div>

                            </div>
                    
                            <form action="submit" class="project-update-form" id="project-update-form-${jsonProject.id}">

                                <input id="project-update-form-input-${jsonProject.id}" type="text" name="name" placeholder="Rename project..." autocomplete="off" required>
        
                                    <div class="project-button-container" id="project-button-container-${jsonProject.id}-update">

                                        <button type="submit" class="project-button" id="project-button-submit-${jsonProject.id}">SAVE</button>
                                        <button type="button" class="project-button" id="project-button-cancel-${jsonProject.id}" onclick='switchView(this, "normal")'>CANCEL</button>

                                    </div>

                            </form>`;
        
        // Add to task list
        if (jsonProject.id === 1) {

            projectListDefault.appendChild(project);

            // Default project can't be modified
            document.getElementById("project-button-container-1").remove();
            document.getElementById("project-update-form-1").remove();
        }
        else {

            projectList.appendChild(project);
        }

        // Color selected (current) project
        if (jsonProject.id === currentProject) {

            project.style.background = "#def0ff";
        }
    });
};

// Move project selector
projectSection.addEventListener("click", event => {

    if (event.target.matches(".project-name")) {

        // Example: project-name-1
        projectId = Number(event.target.id.split('-')[2]);

        if (projectId != currentProject) currentProject = projectId;

        refreshContent();
    }
});


/*
-------------------------------------------
                  POST
-------------------------------------------
*/

taskCreateForm.addEventListener('submit', event => {

    event.preventDefault();

    const formData = new FormData(event.target);
    const formObject = Object.fromEntries(formData);

    if (currentProject != 1) {

        formObject.project = {

            id: currentProject
        }
    }

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

projectCreateForm.addEventListener('submit', event => {

    event.preventDefault();

    const formData = new FormData(event.target);
    const formObject = Object.fromEntries(formData);

    fetch("http://localhost:8080/api/projects", {

        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formObject)

    }).then(() => {

        refreshContent();
        projectCreateForm.reset();
        switchView(document.getElementById("project-button-cancel-create"), "normal");

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

    }).then(() => {

        refreshContent();

    }).catch(error => {

        console.error("PATCH request failed: ", error);

    });
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

projectList.addEventListener("submit", event => {

    event.preventDefault();

    // project-button-submit-1
    let projectId = Number(event.target.id.split('-')[3]);
    
    const formData = new FormData(event.target);
    const formObject = Object.fromEntries(formData);

    fetch(`http://localhost:8080/api/projects/${projectId}`, {

        method: 'PATCH',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "name": formObject.name }),

    }).then(res => res.json())
    .then(data => {

        const projectName = document.getElementById(`project-name-${projectId}`);
        projectName.innerText = data.name;

        switchView(document.getElementById(`project-button-cancel-${projectId}`, "normal"));
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

function deleteProject(button) {
    
    // Example: project-button-delete-1
    let projectId = Number(button.id.split('-')[3]);

    fetch(`http://localhost:8080/api/projects/${projectId}`, {

        method: 'DELETE'

    }).then(() => {

        // Reset view to default
        currentProject = 1;
        refreshContent();

    }).catch(error => {

        console.error("DELETE request failed: ", error);

    });
};


/*
-------------------------------------------
                UTILITIES
-------------------------------------------
*/

function refreshContent() {

    taskList.innerHTML = "";
    projectListDefault.innerHTML = "";
    projectList.innerHTML = "";

    getTasksFromProject(currentProject);
    getProjects();
}

function switchView(button, direction) {

    // Example: task-button-cancel-1
    let elementId = Number(button.id.split('-')[3]);
    let elementClass = String(button.id.split('-')[0]);

    // Set variables according to element class
    let normalView;
    let updateForm;
    let updateFormInput;
    let currentContent;

    let createFormContainer;

    // Create project button (special case)
    if (button.classList.contains("project-create-switch")) {

        normalView = document.getElementById("project-button-create");
        updateForm = document.getElementById("project-create-form");
        createFormContainer = document.getElementById("project-create-container");
    }

    else if (elementClass === "task") {

        normalView = document.getElementById(`task-normal-view-container-${elementId}`);
        updateForm = document.getElementById(`task-update-form-${elementId}`);
        currentContent = document.getElementById(`task-content-${elementId}`).textContent;
        updateFormInput = document.getElementById(`task-update-form-input-${elementId}`);
    }

    else if (elementClass === "project") {

        normalView = document.getElementById(`project-normal-view-container-${elementId}`);
        updateForm = document.getElementById(`project-update-form-${elementId}`);
        currentContent = document.getElementById(`project-name-${elementId}`).textContent;
        updateFormInput = document.getElementById(`project-update-form-input-${elementId}`);
    }

    // Check for direction of the switch
    if (direction == "update") {

        if (updateFormInput != undefined) updateFormInput.value = currentContent;
        normalView.style.display = "none";
        updateForm.style.display = "flex";

        if (createFormContainer != undefined) createFormContainer.style.display = "flex";

    } else {

        if (normalView.classList.contains("project-button")) {

            normalView.style.display = "block";
        }
        else normalView.style.display = "flex";

        updateForm.style.display = "none";
        updateForm.reset();

        if (createFormContainer != undefined) createFormContainer.style.display = "none";
    }
}