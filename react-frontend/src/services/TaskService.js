import axios from 'axios';

const TASKS_BASE_URL = "http://localhost:8080/api/v1/tasks";

const authToken = {
    headers: {
      "X-AUTH-TOKEN": sessionStorage.getItem("X-AUTH-TOKEN"),
    }
  }
class EmployeeService {

    getMyTasks(){
          
        return axios.get(TASKS_BASE_URL,authToken);
        
    }
    getMyFilteredTasks(filter){
          
        return axios.get(TASKS_BASE_URL+"/filter/"+filter,authToken);
        
    }

    createMyTask(task){
        return axios.post(TASKS_BASE_URL, task,authToken);
    }

    getMyTaskById(taskId){
        return axios.get(TASKS_BASE_URL + '/' + taskId,authToken);
    }

    updateMyTask(task, taskId){
        return axios.put(TASKS_BASE_URL + '/' + taskId, task,authToken);
    }

    deleteMyTask(taskId){
        return axios.delete(TASKS_BASE_URL + '/' + taskId,authToken);
    }
}

export default new EmployeeService()