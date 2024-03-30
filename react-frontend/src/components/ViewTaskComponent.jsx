import React, { Component } from 'react'
import TaskService from '../services/TaskService'

class ViewEmployeeComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            task: {}
        }
    }

    cancel(){
        this.props.history.push('/tasks');
    }
    componentDidMount(){
        TaskService.getMyTaskById(this.state.id).then( res => {
            this.setState({task: res.data});
        })
    }

    render() {
        return (
            <div>
                <br></br>
                <div className = "card col-md-6 offset-md-3">
                    <h3 className = "text-center"> Task Details</h3>
                    <div className = "card-body">
                        <form>
                        <div className = "row">
                            <label> Title: </label>
                            <div style={{marginLeft: "10px"}}> { this.state.task.taskTitle }</div>
                        </div>
                        <div className = "row">
                            <label> Description: </label>
                            <div id="customDesc" style={{marginLeft: "10px"}} > { this.state.task.description }</div>
                        </div>
                        <div className = "row">
                            <label> Priority: </label>
                            <div style={{marginLeft: "10px"}}> { this.state.task.priority }</div>
                        </div>
                        <div className = "row">
                            <label> Status: </label>
                            <div style={{marginLeft: "10px"}}> { this.state.task.status }</div>
                        </div>
                        <div className = "row">
                            <label> Add Date: </label>
                            <div style={{marginLeft: "10px"}}> { this.state.task.addDate }</div>
                        </div>
                        <div className = "row">
                            <label> Last Update: </label>
                            <div style={{marginLeft: "10px"}}> { this.state.task.modDate }</div>
                        </div>
                        <button className="btn btn-danger" onClick={this.cancel.bind(this)} style={{marginLeft: "200px"}}>Back</button>
                        </form>
                    </div>
                </div>
                
            </div>
        )
    }
}

export default ViewEmployeeComponent
