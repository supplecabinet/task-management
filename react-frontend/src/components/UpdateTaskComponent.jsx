import React, { Component } from 'react'
import Select from 'react-select'
import TaskService from '../services/TaskService';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class UpdateEmployeeComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            description: '',
            priority: '',
            taskTitle: '',
            titleError:'',
            status:'',
            descriptionError:'',
            statuses: [
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
            priorities:[ {
                value: "Low", 
                label: "Low"
              },
              {
                value: "Medium",
                label: "Medium"
              },
              {
                value: "High",
                label: "High"
              }]
        }
        this.changeDescriptionHandler = this.changeDescriptionHandler.bind(this);
        this.updateMyTask = this.updateMyTask.bind(this);
    }

    componentDidMount(){
        TaskService.getMyTaskById(this.state.id).then( (res) =>{
            let task = res.data;
            this.setState({description: task.description,
                taskTitle: task.taskTitle,
                priority: task.priority,
                status: task.status
            });
        });
    }

    updateMyTask = (e) => {
        e.preventDefault();
        if (this.state.taskTitle === "" || this.state.description === "") {
       
            if (this.state.description === "") {
                this.setState({descriptionError: "*Description is mandatory."});
            }
            if (this.state.taskTitle === "") {
                this.setState({titleError: "*Title is mandatory."});
            }
            
            return;
        } 
        let task = {description: this.state.description,
            taskTitle: this.state.taskTitle,
            priority: this.state.priority,
            status: this.state.status};
        console.log('task => ' + JSON.stringify(task));
        console.log('id => ' + JSON.stringify(this.state.id));
        TaskService.updateMyTask(task, this.state.id).then( res => {
            setTimeout(() => {
                toast.success("Task Updated Successfully!");
            }, 300);
            this.props.history.push('/');
        });
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
        this.setState({priority: event.value});
    }
    changeStatusHandler= (event) => {
        this.setState({status: event.value});
    }

    cancel(){
        this.props.history.push('/');
    }

    render() {
        return (
            <div>
                <br></br>
                   <div className = "container">
                        <div className = "row">
                            <div className = "card col-md-6 offset-md-3 offset-md-3">
                                <h3 className="text-center">Update Task</h3>
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
                                            <textarea placeholder="Task Description" name="description" className="form-control" 
                                                value={this.state.description} onChange={this.changeDescriptionHandler}/>
                                                <label className="errorLabel">{this.state.descriptionError}</label>
                                        </div>
                                        <div className = "form-group">
                                            <label> Task Priority: </label>
                                            <Select className="" value={{value:this.state.priority,label:this.state.priority}} options={this.state.priorities} name="priority" onChange={this.changePriorityHandler}/>
                                            
                                        </div>
                                        <div className = "form-group">
                                            <label> Status: </label>
                                            <Select className="" value={{value:this.state.status,label:this.state.status}} options={this.state.statuses} name="priority" onChange={this.changeStatusHandler}/>
                                            
                                        </div>
                                        <button className="btn btn-success" onClick={this.updateMyTask}>Save</button>
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

export default UpdateEmployeeComponent
