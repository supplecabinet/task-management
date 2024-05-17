import axios from 'axios';

const TASKS_BASE_URL = "http://localhost:8080/api/v1/auth/";

const authToken = {
    headers: {
      "X-AUTH-TOKEN": sessionStorage.getItem("X-AUTH-TOKEN"),
    }
  }
class HeaderService {

    getMyDetails(){
          
      return axios.get(TASKS_BASE_URL+"self",authToken);
        
    }
    
}

export default new HeaderService()