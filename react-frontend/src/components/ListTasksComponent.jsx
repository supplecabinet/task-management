import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import TaskService from '../services/TaskService';
import Login from '../LoginSignUp';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Select from 'react-select'

class ListEmployeeComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
                tasks: [],
                statuses: [
                    {
                        value: "All", 
                        label: "All"
                      },
                    {
                        value: "To Do", 
                        label: "To Do"
                      },
                      {
                        value: "In Progress",
                        label: "In Progress"
                      },
                      {
                        value: "Done",
                        label: "Done"
                      }
                ],
                filter:'All'
        }
        this.createMyTask = this.createMyTask.bind(this);
        this.logOut = this.logOut.bind(this);
        this.updateMyTask = this.updateMyTask.bind(this);
        this.deleteMyTask = this.deleteMyTask.bind(this);
        this.changeStatusHandler = this.changeStatusHandler.bind(this);
        this.filterTable = this.filterTable.bind(this);
    }

    deleteMyTask(id){
        TaskService.deleteMyTask(id).then( res => {
            this.setState({tasks: this.state.tasks.filter(task => task.id !== id)});
            toast.success("Task Deleted Successfully!");
        });
    }
    getMyTaskById(id){
        this.props.history.push(`/view-task/${id}`);
    }
    updateMyTask(id){
        this.props.history.push(`/update-task/${id}`);
    }

    componentDidMount(){

            if (!localStorage.getItem("refresh")) {
                TaskService.getMyTasks().then((res) => {
                    localStorage.setItem("refresh","true");
                    window.location.reload();
                 }); 
            }
            TaskService.getMyTasks().then((res) => { //Repeat only in case user not properly loaded in frontend (one time)
                this.setState({ tasks: res.data});
            }); 
          
    }
    changeStatusHandler(event){
        this.setState({filter: event.value});
    }
    createMyTask(){
        this.props.history.push('/add-task/_add');
    }
    filterTable(){
        if (this.state.filter == "All") {
            TaskService.getMyTasks().then((res) => { 
                this.setState({ tasks: res.data});
            }); 
        } else {
            TaskService.getMyFilteredTasks(this.state.filter).then((res) => { 
                this.setState({ tasks: res.data});
            }); 
        }
        
    }

    logOut(){

        sessionStorage.clear();
        localStorage.clear();
        this.props.history.push('/');
        ReactDOM.render(
            <React.StrictMode>
            <Login />
            </React.StrictMode>,
            document.getElementById('root')
        );
    }

    render() {
        return (
            
            <div>
                 <h2 className="text-center">Task List</h2>
                 <div className = "row">
                    <button className="btn btn-primary" style={{marginLeft: 20,marginRight:"auto"}} onClick={this.createMyTask}> Create Task</button>
                    <button className="btn btn-danger" style={{marginRight: 20,marginLeft:"auto"}} onClick={this.logOut}> Log Out</button>
                 </div>
                 <div  style={{marginLeft: "40%", width:"50%",padding: "10px"}} className = "row">
                    <Select style={{marginLeft: "10px"}} value={{value:this.state.filter,label:this.state.filter}} options={this.state.statuses} name="status" onChange={this.changeStatusHandler}/>
                    <button style={{marginLeft: "10px"}}  className="btn btn-secondary" onClick={this.filterTable}> Filter</button>
                    </div>
                 <br></br>
                 <div className = "row mycustom">
                        <table className = "table table-striped table-bordered" style={{marginRight: 30,marginLeft:30}}>

                            <thead>
                                <tr>
                                    <th> Task Title</th>
                                    <th> Status</th>
                                    <th> Priority</th>
                                    <th> Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.tasks.map(
                                        task => 
                                        <tr key = {task.id}>
                                             <td> {task.taskTitle} </td>   
                                             <td> {task.status} </td>
                                             <td> {task.priority} </td>
                                             <td>
                                                 <button onClick={ () => this.getMyTaskById(task.id)} className="btn btn-info">View </button>
                                                 <button style={{marginLeft: "10px"}} onClick={ () => this.updateMyTask(task.id)} className="btn btn-info">Update </button>
                                                 <button style={{marginLeft: "10px"}} onClick={ () => this.deleteMyTask(task.id)} className="btn btn-danger">Delete </button>
                                             </td>
                                        </tr>
                                    )
                                }
                            </tbody>
                        </table>

                 </div>
                 <div>
                    <ToastContainer position="bottom-right" />
                </div>
            </div>
        )
    }
}

export default ListEmployeeComponent
