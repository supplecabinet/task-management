import React, { Component} from 'react'
import TaskService from '../services/TaskService';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class CreateEmployeeComponent extends Component {
    
    constructor(props) {
        super(props)

        this.state = {
            // step 2
            id: this.props.match.params.id,
            description: '',
            taskTitle:'',
            priority:'Low',
            titleError:'',
            descriptionError:''
        }
        this.changeDescriptionHandler = this.changeDescriptionHandler.bind(this);
        this.saveOrUpdateTask = this.saveOrUpdateTask.bind(this);
       
    }
    

    // step 3
    componentDidMount(){
        
        // step 4
        if(this.state.id === '_add'){
            return
        }else{
            TaskService.getMyTaskById(this.state.id).then( (res) =>{
                let task = res;
                this.setState({description: task.description,
                               taskTitle: task.taskTitle,
                               priority: task.priority
                });
            });
        }        
    }
    saveOrUpdateTask = (e) => {
        e.preventDefault();
        if (this.state.taskTitle=="" || this.state.description=="") {
       
            if (this.state.description=="") {
                this.setState({descriptionError: "*Description is mandatory."});
            }
            if (this.state.taskTitle=="") {
                this.setState({titleError: "*Title is mandatory."});
            }
            
            return;
        } 
        let task = {description: this.state.description,
                    taskTitle: this.state.taskTitle,
                    priority: this.state.priority};
        console.log('task => ' + JSON.stringify(task));

        // step 5
        if(this.state.id === '_add'){
            TaskService.createMyTask(task).then(res =>{
                setTimeout(() => {
                    toast.success("Task Created Successfully!");
                }, 300);
                this.props.history.push('/');
            });
        }else{
            TaskService.updateMyTask(task, this.state.id).then( res => {
                this.props.history.push('/');
            });
        }
    }
    
    changeDescriptionHandler= (event) => {
        this.setState({descriptionError: ""});
        this.setState({description: event.target.value});
    }
    changeTitleHandler= (event) => {
        this.setState({titleError: ""});
        this.setState({taskTitle: event.target.value});
    }
    changePriorityHandler= (event) => {
        this.setState({priority: event.target.value});
    }

    cancel(){
        this.props.history.push('/tasks');
    }

    getTitle(){
        if(this.state.id === '_add'){
            return <h3 className="text-center">Create Task</h3>
        }else{
            return <h3 className="text-center">Update Task</h3>
        }
    }
    render() {
        return (
            <div>
                <br></br>
                   <div className = "container">
                        <div className = "row">
                        <div className = "card col-md-9 offset-md-3" style={{marginRight: 30,marginLeft:120,marginBottom:20}}>
                                {
                                    this.getTitle()
                                }
                                <div className = "card-body">
                                    <form>
                                        <div className = "form-group">
                                            <label> Task Title: </label>
                                            <input placeholder="Task Title" name="taskTitle" className="form-control" 
                                                value={this.state.taskTitle} onChange={this.changeTitleHandler}/>
                                                <label className="errorLabel">{this.state.titleError}</label>
                                        </div>
                                        <div className = "form-group">
                                            <label> Task Description: </label>
                                            <textarea  placeholder="Task Description" name="description" className="form-control" 
                                                value={this.state.description} onChange={this.changeDescriptionHandler}/>
                                                <label className="errorLabel">{this.state.descriptionError}</label>
                                        </div>
                                        <div className = "form-group">
                                            <label> Task Priority: </label>
                                            <select className="form-control" name="priority" onChange={this.changePriorityHandler}>
                                            <option value="Low">Low</option>
                                            <option value="Medium">Medium</option>
                                            <option value="High">High</option>
                                            </select>
                                        </div>

                                        <button className="btn btn-success" onClick={this.saveOrUpdateTask}>Save</button>
                                        <button className="btn btn-danger" onClick={this.cancel.bind(this)} style={{marginLeft: "10px"}}>Cancel</button>
                                    </form>
                                </div>
                            </div>
                        </div>

                   </div>
                   <div>
                    <ToastContainer position="bottom-right" />
                </div>
            </div>
        )
    }
}

export default CreateEmployeeComponent
