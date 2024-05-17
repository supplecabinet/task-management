import React, { Component } from 'react'
import HeaderService from '../services/HeaderService';

class HeaderComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
               userId: ""
        }
    }
    componentDidMount(){
        HeaderService.getMyDetails().then((res) => {
            this.setState({ userId: "User : "+res.data});
        }); 
    }
    render() {
        return (
            <div>
                <header>
                    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                    <div className="navbar-brand">Task Management App</div>
                    <div className="navbar-brand" style={{marginRight:20, marginLeft:'auto'}}>{this.state.userId}</div>
                    </nav>
                </header>
            </div>
        )
    }
}

export default HeaderComponent
