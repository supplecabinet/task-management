import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import Login from './login';
import * as serviceWorker from './serviceWorker';
import 'bootstrap/dist/css/bootstrap.min.css';

if (window.sessionStorage.getItem("X-AUTH-TOKEN")) { //If already logged in, render main screen
  ReactDOM.render(
    <React.StrictMode>
      <App />
    </React.StrictMode>,
    document.getElementById('root')
  );
} else {
  ReactDOM.render(
    <React.StrictMode>
      <Login />
    </React.StrictMode>,
    document.getElementById('root')
  );
}


serviceWorker.unregister();
