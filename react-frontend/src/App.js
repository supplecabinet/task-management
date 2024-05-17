import React from 'react';
import './App.css';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import ListTasksComponent from './components/ListTasksComponent';
import HeaderComponent from './components/HeaderComponent';
import FooterComponent from './components/FooterComponent';
import CreateTaskComponent from './components/CreateTaskComponent';
import UpdateTaskComponent from './components/UpdateTaskComponent';
import ViewTaskComponent from './components/ViewTaskComponent';

function App() {
  // <FooterComponent />
  return (
    <div>
        <Router>
              <HeaderComponent />
                <div className="container">
                    <Switch> 
                          <Route path = "/" exact component = {ListTasksComponent}></Route>
                          <Route path = "/tasks" component = {ListTasksComponent}></Route>
                          <Route path = "/add-task/:id" component = {CreateTaskComponent}></Route>
                          <Route path = "/view-task/:id" component = {ViewTaskComponent}></Route>
                          <Route path = "/update-task/:id" component = {UpdateTaskComponent}></Route> 
                    </Switch>
                </div>
             
        </Router>
    </div>
    
  );
}

export default App;
